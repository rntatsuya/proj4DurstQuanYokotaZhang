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
import javafx.scene.control.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import javafx.stage.FileChooser.ExtensionFilter;
import java.util.UUID;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import java.nio.file.Paths;
import java.nio.file.Files;



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
     * through all tabs from left to right, looping around
     * when past index 0.
     * 
     * @param event ActionEvent object
     */
    @FXML void handleExitAction(ActionEvent event) {
        int startSize = tabPane.getTabs().size();
        // starts from focused on tab 
        int startIndex = tabPane.getSelectionModel().getSelectedIndex();
        // secondaryIndex is the starting point for the second loop.
        // i.e. for tabs [0][1][2][3]
        //      startIndex     = 1
        //      secondaryIndex = 1
        //
        //      since after we close all the tabs going left from 1,
        //      we will have [2][3] left over. Thus our secondary
        //      index will put us at [3].
        int secondaryIndex = startSize - (startIndex + 2);
        int counter = 0;

        for (int i =  0; i < startSize; i++){
            // check if we need to wrap around for second loop
            if (startIndex < counter){
                startIndex = secondaryIndex;
                counter = 0; 
            }
            
            int curIndex = startIndex - counter;

            // make sure the currently selected tab, by index and by focus,
            // are in sync
            tabPane.getSelectionModel().select(tabPane.getTabs().get(curIndex));
            Boolean shouldBreak = handleClose(tabPane.getTabs().get(curIndex), event);

            // if cancel is selected, break
            if (shouldBreak) {
                break;
            }

            counter += 1;
        }

        if(tabPane.getTabs().size() == 0){
            System.exit(0);
        }
    }

    /** 
     * Create a new tab named 'New file' with a text area.
     * 
     * @param even ActionEvent object
     */
    @FXML void handleNewAction(ActionEvent event) {
        // instantiate a new Tab
        Tab tab = new Tab();
        tab.setText("New file");

        // add to tabPane
        tabPane.getTabs().add(tab);

        // set the new tab as the focus
        tabPane.getSelectionModel().select(tab);

        // instantiate the TextArea
        TextArea textArea = new TextArea();
        textArea.setText("Sample text");

        // add TextArea to tab
        tab.setContent(textArea);
        
        // Set a unique Id for the TextArea
        String id = UUID.randomUUID().toString();
        textArea.setId(id);
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
     */
    @FXML void handleSaveAsAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("save the file as...");
        File file = fileChooser.showSaveDialog(null);

        if (file != null){
            try{
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                Tab tab = tabPane.getSelectionModel().getSelectedItem();
                TextArea textArea = (TextArea) tab.getContent();
                writer.write(textArea.getText());
                writer.close();
                tab.setUserData(file.getAbsolutePath());
                tab.setText(file.getName());

                 // add TextArea to hashmap
                 savedCache.append(textArea.getId(),  textArea.getText());
            }
            catch(IOException e){
                System.out.println(e.getMessage());
            }
        }
    }

    
    /**
     * Saves updates to pre-existing file, or if not previously
     * saved, opens a save dialog for the user to specify a filename
     * and directory, then writes the new file
     * @param event ActionEvent object
     */
    @FXML void handleSaveAction(ActionEvent event) {
        Tab tab = tabPane.getSelectionModel().getSelectedItem();
        String fileName = (String)tab.getUserData();
        
        //handle as unsaved file
        if (fileName == null) {
            handleSaveAsAction(event);
        } else {
            //handle as previously saved file
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                TextArea textArea = (TextArea) tab.getContent();
                writer.write(textArea.getText());
                writer.close();

                // add TextArea to hashmap
                savedCache.append(textArea.getId(),  textArea.getText());

            } catch (IOException e) {
                System.out.println(e.getMessage());
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
     * tab's TextArea's contents before closing it.
     * 
     * @param event    ActionEvent object
     * @param thisTab  Tab object
     * @return Boolean should break for handleExitAction
    */
    Boolean handleClose(Tab tab, ActionEvent event){
        TextArea textArea = (TextArea) tab.getContent();

        // check if (a) and (b)
        if (savedCache.tabHasChanged(textArea.getId(), textArea.getText())) {
            tabPane.getTabs().remove(tab);
            savedCache.remove(textArea.getId());
            return false;
        } 

        ButtonType res = AlertBox.closeTab();

        // handle user response
        if(res == ButtonType.YES){
            handleSaveAction(event);
            // remove tab from cache since saving it adds it
            // to the cache
            savedCache.remove(textArea.getId());
            tabPane.getTabs().remove(tab);
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
        TextArea textArea = (TextArea) tab.getContent();
        MenuItem clickedItem = (MenuItem) event.getTarget();
        
        switch(clickedItem.getId()) {
            case "undoMenuButton":  
                textArea.undo();
                break;
            case "redoMenuButton":  
                textArea.redo();
                break;
            case "cutMenuButton":  
                textArea.cut();
                break;
            case "copyMenuButton":  
                textArea.copy();
                break;
            case "pasteMenuButton":  
                textArea.paste();
                break;
            case "selectAllMenuButton":  
                textArea.selectAll();
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
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(filter);
        
        //Show save file dialog
        File file = fileChooser.showOpenDialog(null);
        if(file != null){
            // create a new tab
            Tab tab = new Tab();

            // add tab to tab pane
            tabPane.getTabs().add(tab); 

            // set new tab as the focused on tab
            tabPane.getSelectionModel().select(tab);

            // define new textarea and add to new tab
            TextArea textArea = new TextArea();
            tab.setContent(textArea);

            try {
                String fileText = getFileContentString(file);
                textArea.setText(fileText);
                tab.setUserData(file.getAbsolutePath());
                tab.setText(file.getName());
                
                // Set a unique Id for the thing
                String id = UUID.randomUUID().toString();
                textArea.setId(id);

                // add TextArea to hashmap
                savedCache.append(textArea.getId(), textArea.getText());
            } catch (IOException e) {
                AlertBox.fileNotFound();
            }
            
        }
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
}