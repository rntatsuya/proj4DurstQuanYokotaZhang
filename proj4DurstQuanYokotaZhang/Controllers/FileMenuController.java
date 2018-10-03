package proj4DurstQuanYokotaZhang.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import proj4DurstQuanYokotaZhang.Common.AlertBox;
import proj4DurstQuanYokotaZhang.Common.Messages;

public class FileMenuController {
    //@FXML TabPaneController tabPaneController;
    MainController mainController;

    @FXML MenuItem closeMenuButton;
    @FXML MenuItem saveMenuButton;
    @FXML MenuItem saveAsMenuButton;


    public void disableMenuItems(boolean isTabEmpty, boolean isTabChanged) {
        closeMenuButton.setDisable(isTabEmpty);
        saveMenuButton.setDisable(!isTabChanged);
        saveAsMenuButton.setDisable(!isTabChanged);
    }

    @FXML protected void handleFileMenuShown() {
        mainController.handleDisableFileMenuItems();
    }

    /**
     * Handles the About button action in File menu.
     * Creates a dialog window that displays the authors' names.
     */
    @FXML protected void handleAboutAction(){
        AlertBox.aboutUs();
    }

    /**
     * Handles the New button action in File menu.
     * Opens a code area embedded in a new tab.
     */
    @FXML protected void handleNewAction(){
        mainController.handleFileMenuToTabPane(Messages.TabPaneMessage.NEW);
    }

    /**
     * Handles the Open button action in File menu.
     * Opens a dialog in which the user can select a file to open.
     * If the user chooses a valid file, a new tab is created and the file is loaded into the code area.
     * If the user cancels, the dialog disappears without doing anything.
     */
    @FXML protected void handleOpenAction(){
        mainController.handleFileMenuToTabPane(Messages.TabPaneMessage.OPEN);
    }

    /**
     * Handles the Save As button action in File menu.
     * Shows a dialog in which the user is asked for the name of the file into
     * which the contents of the current code area are to be saved.
     * If the user enters any legal name for a file and presses the OK button in the dialog,
     * then creates a new text file by that name and write to that file all the current
     * contents of the text area so that those contents can later be reloaded.
     * If the user presses the Cancel button in the dialog, then the dialog closes and no saving occurs.
     */
    @FXML protected void handleSaveAsAction(){
        mainController.handleFileMenuToTabPane(Messages.TabPaneMessage.SAVEAS);
    }

    /**
     * Handles the Save button action in File menu.
     * If a text area was not loaded from a file nor ever saved to a file,
     * behaves the same as the save as button.
     * If the current text area was loaded from a file or previously saved
     * to a file, then the text area is saved to that file.
     */
    @FXML protected void handleSaveAction(){
        mainController.handleFileMenuToTabPane(Messages.TabPaneMessage.SAVE);
    }

    /**
     * Handles the Close button action in File menu.
     * If the current code area has already been saved to a file, then the current tab is closed.
     * If the current code area has been changed since it was last saved to a file, a dialog
     * appears asking whether you want to save the text before closing it.
     */
    @FXML protected void handleCloseAction(){
        mainController.handleFileMenuToTabPane(Messages.TabPaneMessage.CLOSE);
    }

    /**
     * Handles the Exit button action in File menu.
     * Exits the program when the Exit button is clicked.
     */
    @FXML protected void handleExitAction(){
        mainController.handleFileMenuToTabPane(Messages.TabPaneMessage.EXIT);
    }


    public void injectMainController(MainController mainController) {
        this.mainController = mainController;
    }
}