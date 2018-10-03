package proj4DurstQuanYokotaZhang.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import proj4DurstQuanYokotaZhang.Common.AlertBox;
import proj4DurstQuanYokotaZhang.Common.Messages;
import javafx.stage.FileChooser;
import org.fxmisc.richtext.CodeArea;
import java.io.File;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import org.fxmisc.flowless.VirtualizedScrollPane;
import java.io.BufferedWriter;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.io.FileWriter;


public class TabPaneController {
    // The tabpane
    // tab pane containing text areas for open files, specified in Main.fxml
    @FXML TabPane tabPane;
    private boolean editedByUndo;
    MainController mainController;

    public void distributeMessage(Messages.TabPaneMessage tabPaneMessage) {
        Tab thisTab;
        CodeArea codeArea;
        switch (tabPaneMessage) {
            case NEW:
                addTab();
                break;
            case OPEN:
                openFileTab();
                break;
            case SAVEAS:
                handleSaveAsAction();
                break;
            case SAVE:
                handleSaveAction();
                break;
            case CLOSE:
                closeTab();
                break;
            case EXIT:
                handleExitAction();
                break;
            case UNDO:
                codeArea = getSelectedCodeArea();
                mainController.savedCache.incrementNumUndos(codeArea);
                editedByUndo = true;
                handleUndoAction();
                break;
            case REDO:
                codeArea = getSelectedCodeArea();
                mainController.savedCache.decrementNumUndos(codeArea);
                handleRedoAction();
                break;
            case PASTE:
                handlePasteAction();
                break;
            case CUT:
                handleCutAction();
                break;
            case COPY:
                handleCopyAction();
                break;
            case SELECTALL:
                handleSelectAll();
                break;
        }
    }

    public boolean checkIfChanged() {
        CodeArea codeArea = getSelectedCodeArea();

        if (codeArea == null)
            return false;

        return mainController.savedCache.hasChanged(codeArea, codeArea.getText());
    }

    public boolean checkIfEmpty() {
        return (tabPane.getTabs().size() == 0 ? true : false);
    }

    public boolean checkIfNumUndosZero() {
        CodeArea codeArea = getSelectedCodeArea();

        if (codeArea == null)
            return true;

        return (mainController.savedCache.getNumUndos(codeArea) == 0);
    }

    public void addTab() {
        Tab tab = new Tab();
        tab.setText("New file");

        tab.setOnCloseRequest(event -> closeTab());

        // add to tabPane
        tabPane.getTabs().add(tab);

        // set the new tab as the focus
        tabPane.getSelectionModel().select(tab);

        // instantiate the CodeArea
        CodeArea codeArea = new CodeArea();
        VirtualizedScrollPane scrollPane = new VirtualizedScrollPane<>(codeArea);
        // TODO: Move this listener somewhere else, doesn't make much sense to have this here
        codeArea.textProperty().addListener((obs, oldText, newText) -> {
            if (!editedByUndo) {
                mainController.savedCache.resetNumUndos(codeArea);
                editedByUndo = false;
            }

            mainController.handleDisableFileMenuItems();
            mainController.handleDisableEditMenuItems();
            codeArea.setStyleSpans(0, proj4DurstQuanYokotaZhang.RichText.computeHighlighting(newText));
        });

        // add scrollPane to tab
        tab.setContent(scrollPane);

        // add to savedCache -- while this file technically hasn't been saved
        // yet, this guarantees that the user won't be prompted to save an
        // empty file without any text upon closing.
        mainController.savedCache.add(codeArea, "", null);
    }

    public void closeTab() {
        Tab thisTab = tabPane.getSelectionModel().getSelectedItem();
        handleClose(thisTab);
    }

    private CodeArea getCodeArea(Tab tab){
        if (tab == null)
            return null;

        VirtualizedScrollPane pane = (VirtualizedScrollPane) tab.getContent();
        return (CodeArea) pane.getContent();
    }

    /**
     * Takes in a file, attempts to read the text from the file
     * and return it as a String.
     *
     * Modified and borrowed code from:
     * https://stackoverflow.com/questions/326390/how-do-i-create-a-java-string-from-the-contents-of-a-file
     *
     * @param  file    File object
     * @return String  contents of the file
     */
    String getFileContentString(File file) throws IOException {
        return new String(Files.readAllBytes(Paths.get(file.toURI())));
    }

    public void openFileTab() {
        // instantiate and define a filechooser
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Java files (*.java)", "*.java");
        fileChooser.getExtensionFilters().add(filter);

        // gets the current stage
        Stage parentWindow = (Stage)tabPane.getScene().getWindow();

        //Show save file dialog
        File file = fileChooser.showOpenDialog(parentWindow);
        if(file != null){

            //Got rid of some duplicate code here
            addTab();
            Tab curTab = tabPane.getSelectionModel().getSelectedItem();
            CodeArea codeArea = getCodeArea(curTab);

            try {
                String fileText = getFileContentString(file);
                codeArea.replaceText(0, codeArea.getLength(), fileText);
                curTab.setText(file.getName());

                // add CodeArea to hashmap
                mainController.savedCache.add(codeArea, codeArea.getText(), file.getAbsolutePath());
            } catch (IOException e) {
                AlertBox.fileNotFound();
            }

        }
    }

    public void injectMainController(MainController mainController) {
        this.mainController = mainController;
    }

    boolean handleClose(Tab tab){
        CodeArea codeArea = getCodeArea(tab);

        // check if (a) and (b)
        if (!mainController.savedCache.hasChanged(codeArea, codeArea.getText())) {
            tabPane.getTabs().remove(tab);
            mainController.savedCache.remove(codeArea);
            return false;
        }

        ButtonType res = AlertBox.closeTab();

        // handle user response
        if(res == ButtonType.YES){
            boolean didSave = handleSaveAction();

            // only need to remove tab if it has been saved
            if (didSave) {
                // remove tab from cache since saving it adds it
                // to the cache
                mainController.savedCache.remove(codeArea);
                tabPane.getTabs().remove(tab);
            }
        } else if(res == ButtonType.NO){
            tabPane.getTabs().remove(tab);
        } else if (res == ButtonType.CANCEL) {
            return true;
        }

        return false;
    }


    boolean handleSaveAction() {
        CodeArea codeArea = getSelectedCodeArea();

        String fileName = mainController.savedCache.getFileName(codeArea);

        //handle as unsaved file
        if (fileName == null) {
            return handleSaveAsAction();
        } else {
            //handle as previously saved file
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                writer.write(codeArea.getText());
                writer.close();

                // add CodeArea to hashmap
                mainController.savedCache.updateContent(codeArea,  codeArea.getText());

                return true;

            } catch (IOException e) {
                AlertBox.fileNotFound();
                return false;
            }
        }
    }

    boolean handleSaveAsAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("save the file as...");

        // gets the current stage
        Stage parentWindow = (Stage)tabPane.getScene().getWindow();

        File file = fileChooser.showSaveDialog(parentWindow);
        if (file != null ){
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                Tab curTab = tabPane.getSelectionModel().getSelectedItem();
                CodeArea codeArea = getCodeArea(curTab);
                writer.write(codeArea.getText());
                writer.close();
                curTab.setText(file.getName());

                // add CodeArea to hashmap
                mainController.savedCache.add(codeArea, codeArea.getText(), file.getAbsolutePath());
                return true;
            }
            catch(IOException e) {
                AlertBox.saveFailure();
                return false;
            }
        } else {
            return false;
        }
    }


    @FXML void handleExitAction() {
        for (int i =  tabPane.getTabs().size() - 1; i != -1; i--){
            // make sure the currently selected tab, by index and by focus,
            // are in sync
            tabPane.getSelectionModel().select(tabPane.getTabs().get(i));
            boolean shouldBreak = handleClose(tabPane.getTabs().get(i));

            // if cancel is selected, break
            if (shouldBreak) {
                break;
            }
        }

        if(tabPane.getTabs().size() == 0){
            System.exit(0);
        }
    }

    void handleUndoAction() {
        CodeArea codeArea = getSelectedCodeArea();
        codeArea.undo();
    }

    void handleRedoAction() {
        CodeArea codeArea = getSelectedCodeArea();
        codeArea.redo();
    }

    void handleCutAction() {
        CodeArea codeArea = getSelectedCodeArea();
        codeArea.cut();
    }

    void handleCopyAction() {
        CodeArea codeArea = getSelectedCodeArea();
        codeArea.copy();
    }

    void handlePasteAction() {
        CodeArea codeArea = getSelectedCodeArea();
        codeArea.paste();
    }

    void handleSelectAll() {
        CodeArea codeArea = getSelectedCodeArea();
        codeArea.selectAll();
    }

    private CodeArea getSelectedCodeArea() {
        Tab curTab = tabPane.getSelectionModel().getSelectedItem();
        return getCodeArea(curTab);
    }
}
