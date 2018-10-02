/*
 * File: Controller.java
 * Names: (Proj 1-2) Liwei Jiang, Yi Feng, Jackie Hang, Paige Hanssen
 *        (Proj 3) Yi Feng, Melody Mao, Danqing Zhao, Robert Durst
 * F18 CS361 Project 3
 * This file contains the controller methods that define the functionality
 * for the window elements in Main.fxml.
 * Date: 09/25/2018
 */

package proj4DurstQuanYokotaZhang;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser.ExtensionFilter;
import java.util.UUID;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This class contains event handlers for the buttons and the menu items.
 * Buttons: Hello, Goodbye
 * Menu items: Exit, New, About, Save As, Save, Close, Edit, Open
 *
 * @author  Robert Durst, Yi Feng, Melogy Mao, Danqing Zhao
 */
public class Controller{
    // hello button specified in Main.fxml
    @FXML Button helloButton;
    // goodbye button specified in Main.fxml
    @FXML Button goodbyeButton;
    // tab pane containing text areas for open files, specified in Main.fxml
    @FXML TabPane tabPane;

    // CACHE
    SavedCache savedCache = new SavedCache();


    private static final String[] KEYWORDS = new String[] {
            "abstract", "assert", "boolean", "break", "byte",
            "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else",
            "enum", "extends", "final", "finally", "float",
            "for", "goto", "if", "implements", "import",
            "instanceof", "int", "interface", "long", "native",
            "new", "package", "private", "protected", "public",
            "return", "short", "static", "strictfp", "super",
            "switch", "synchronized", "this", "throw", "throws",
            "transient", "try", "void", "volatile", "while", "var"
    };

    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";
    private static final String INT_PATTERN = "(?<![\\w])(?<![\\d.])[0-9]+(?![\\d.])(?![\\w])";

    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                    + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
                    + "|(?<INT>" + INT_PATTERN + ")"
    );






    /**
     * Create a dialog that takes in an integer between 0 and 255,
     * and set the hello button text to the entered number
     *
     * @param event ActionEvent object
     */
    @FXML void handleHelloButtonAction(ActionEvent event) {
        // set up the number input dialog
        TextInputDialog dialog = new TextInputDialog("60");
        dialog.setTitle("Give me a number");
        dialog.setHeaderText("Give me an integer from 0 to 255:");

        final Optional<String> enterValue = dialog.showAndWait();
        if (enterValue.isPresent()) {
            this.helloButton.setText(enterValue.get());
        }
    }

    /**
     * Sets the text of goodbye button to "Yah, sure!"
     *
     * @param event ActionEvent object
     */
    @FXML void handleGoodbyeButtonAction(ActionEvent event) {
        goodbyeButton.setText("Yah, sure!");
    }

    /**
     * Exit the program. Calls a handleClose method for all
     * tabs in tab pane to ask if it needs to be saved. Goes
     * through all tabs from right to left.
     *
     * @param event ActionEvent object
     */
    @FXML void handleExitAction(ActionEvent event) {
        for (int i =  tabPane.getTabs().size() - 1; i != -1; i--){
            // make sure the currently selected tab, by index and by focus,
            // are in sync
            tabPane.getSelectionModel().select(tabPane.getTabs().get(i));
            boolean shouldBreak = handleClose(tabPane.getTabs().get(i), event);

            // if cancel is selected, break
            if (shouldBreak) {
                break;
            }
        }

        if(tabPane.getTabs().size() == 0){
            System.exit(0);
        }
    }

    /**
     * Create a new tab named 'New file' with a text area.
     *
     * @param event ActionEvent object
     */
    @FXML void handleNewAction(ActionEvent event) {
        // instantiate a new Tab
        Tab tab = new Tab();
        tab.setText("New file");

        // add to tabPane
        tabPane.getTabs().add(tab);

        // set the new tab as the focus
        tabPane.getSelectionModel().select(tab);

        // instantiate the CodeArea
        CodeArea codeArea = new CodeArea();
        VirtualizedScrollPane scrollPane = new VirtualizedScrollPane<>(codeArea);
        codeArea.textProperty().addListener((obs, oldText, newText) -> {
            codeArea.setStyleSpans(0, computeHighlighting(newText));
        });

        // add scrollPane to tab
        tab.setContent(scrollPane);



        // Set a unique Id for the CodeArea
        String id = UUID.randomUUID().toString();
        codeArea.setId(id);
    }

    /**
     * Create and oepn a dialog that displays information about
     * the program.
     *
     * @param event ActionEvent object
     */
    @FXML void handleAboutAction(ActionEvent event) {
        AlertBox.aboutUs();
    }

    /**
     * Opens a save dialog for the user to specify a filename and
     * directory, then writes the new file
     *
     * @param event ActionEvent object
     * @return boolean returns true if the file saved, false otherwise
     */
    @FXML boolean handleSaveAsAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("save the file as...");
        File file = fileChooser.showSaveDialog(null);

        if (file != null){
            try{
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                Tab tab = tabPane.getSelectionModel().getSelectedItem();
                CodeArea codeArea = getCodeArea(tab);
                writer.write(codeArea.getText());
                writer.close();
                tab.setUserData(file.getAbsolutePath());
                tab.setText(file.getName());

                // add CodeArea to hashmap
                savedCache.append(codeArea.getId(),  codeArea.getText());
                return true;
            }
            catch(IOException e){
                System.out.println(e.getMessage());
                return false;
            }
        } else {
            return false;
        }
    }


    /**
     * Saves updates to pre-existing file, or if not previously
     * saved, opens a save dialog for the user to specify a filename
     * and directory, then writes the new file
     * @param event ActionEvent object
     * @return boolean returns true if the file saved, false otherwise
     */
    @FXML boolean handleSaveAction(ActionEvent event) {
        Tab tab = tabPane.getSelectionModel().getSelectedItem();
        String fileName = (String)tab.getUserData();

        //handle as unsaved file
        if (fileName == null) {
            return handleSaveAsAction(event);
        } else {
            //handle as previously saved file
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                CodeArea codeArea = getCodeArea(tab);
                writer.write(codeArea.getText());
                writer.close();

                // add CodeArea to hashmap
                savedCache.append(codeArea.getId(),  codeArea.getText());

                return true;

            } catch (IOException e) {
                System.out.println(e.getMessage());
                return false;
            }
        }
    }

    /**
     * Executes when close menu button selected.
     *
     * @param event ActionEvent object
     */
    @FXML void handleCloseAction(ActionEvent event) {
        Tab thisTab = tabPane.getSelectionModel().getSelectedItem();
        handleClose(thisTab, event);
    }

    /**
     * Checks against the tabContentCache if:
     *      a) the current tab has been saved yet
     *      b) the current tab has changed since last save
     *
     * This method utilizes the tabContentCache for quick
     * and efficient lookups.
     *
     * If the tab hasn't been saved, or has changed since last
     * save, then it asks the user if he/she wants to save the
     * tab's CodeArea's contents before closing it.
     *
     * @param event    ActionEvent object
     * @param tab  Tab object
     * @return boolean returns true if user hits cancel and the
     *                 exit method should halt
     */
    boolean handleClose(Tab tab, ActionEvent event){
        CodeArea codeArea = getCodeArea(tab);

        // check if (a) and (b)
        if (savedCache.tabHasChanged(codeArea.getId(), codeArea.getText())) {
            tabPane.getTabs().remove(tab);
            savedCache.remove(codeArea.getId());
            return false;
        }

        ButtonType res = AlertBox.closeTab();

        // handle user response
        if(res == ButtonType.YES){
            boolean didSave = handleSaveAction(event);

            // only need to remove tab if it has been saved
            if (didSave) {
                // remove tab from cache since saving it adds it
                // to the cache
                savedCache.remove(codeArea.getId());
                tabPane.getTabs().remove(tab);
            }
        } else if(res == ButtonType.NO){
            tabPane.getTabs().remove(tab);
        } else if (res == ButtonType.CANCEL) {
            return true;
        }

        return false;
    }

    /**
     * Responds to user clicks for each menu item under the edit
     * MenuBar drop down. Utilizes id from fxml to determine which
     * action to execute.
     * @param event ActionEvent object
     */
    @FXML void handleEditAction(ActionEvent event){
        // capture data relevent to the triggered action
        Tab tab = tabPane.getSelectionModel().getSelectedItem();
        CodeArea codeArea = getCodeArea(tab);
        MenuItem clickedItem = (MenuItem) event.getTarget();

        switch(clickedItem.getId()) {
            case "undoMenuButton":
                codeArea.undo();
                break;
            case "redoMenuButton":
                codeArea.redo();
                break;
            case "cutMenuButton":
                codeArea.cut();
                break;
            case "copyMenuButton":
                codeArea.copy();
                break;
            case "pasteMenuButton":
                codeArea.paste();
                break;
            case "selectAllMenuButton":
                codeArea.selectAll();
                break;
            default:
                System.out.println("Unexpected event!");
        }
    }

    /**
     * Presents user with option to open a (txt) file from their
     * local directory and then displays the text of the file in
     * a new tab.
     *
     * @param event ActionEvent object
     */
    @FXML void handleOpenAction(ActionEvent event){
        // instantiate and define a filechooser
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Java files (*.java)", "*.java");
        fileChooser.getExtensionFilters().add(filter);

        //Show save file dialog
        File file = fileChooser.showOpenDialog(null);
        if(file != null){

            //Got rid of some duplicate code here
            handleNewAction(event);
            Tab tab = tabPane.getSelectionModel().getSelectedItem();
            CodeArea codeArea = getCodeArea(tab);

            try {
                String fileText = getFileContentString(file);
                codeArea.replaceText(0, codeArea.getLength(), fileText);
                tab.setUserData(file.getAbsolutePath());
                tab.setText(file.getName());

                // Set a unique Id for the thing
                String id = UUID.randomUUID().toString();
                codeArea.setId(id);

                // add CodeArea to hashmap
                savedCache.append(codeArea.getId(), codeArea.getText());
            } catch (IOException e) {
                AlertBox.fileNotFound();
            }

        }
    }


    private CodeArea getCodeArea(Tab tab){
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



    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while(matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                            matcher.group("PAREN") != null ? "paren" :
                                    matcher.group("BRACE") != null ? "brace" :
                                            matcher.group("BRACKET") != null ? "bracket" :
                                                    matcher.group("SEMICOLON") != null ? "semicolon" :
                                                            matcher.group("STRING") != null ? "string" :
                                                                    matcher.group("COMMENT") != null ? "comment" :
                                                                            matcher.group("INT") != null ? "int" :
                                                                                    null; /* never happens */ assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }
}

