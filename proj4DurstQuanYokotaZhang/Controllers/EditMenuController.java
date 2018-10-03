package proj4DurstQuanYokotaZhang.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import proj4DurstQuanYokotaZhang.Common.Messages;


/**
 * EditMenuController handles the actions of buttons in Edit menu.
 *
 * @author Robert Durst
 * @author Tatsuya Yokota
 * @author Tracy Quan
 * @author Tia Zhang
 */
public class EditMenuController {
    MainController mainController;

    /**
     * Handles the Undo button action in Edit Menu.
     * Undo the actions in the cdoe area.
     */
    @FXML void handleUndoAction(ActionEvent event){
        mainController.handleFileMenuToTabPane(Messages.TabPaneMessage.UNDO);
    }

    /**
     * Handles the Redo button action in Edit Menu.
     * Redo the actions in the code area.
     */
    @FXML void handleRedoAction(ActionEvent event){
        mainController.handleFileMenuToTabPane(Messages.TabPaneMessage.REDO);
    }

    /**
     * Handles the Cut button action in Edit Menu.
     * Cuts the selected text.
     */
    @FXML void handleCutAction(ActionEvent event){
        mainController.handleFileMenuToTabPane(Messages.TabPaneMessage.CUT);
    }

    /**
     * Handles the Copy button action in Edit Menu.
     * Copies the selected text.
     */
    @FXML void handleCopyAction(ActionEvent event){
        mainController.handleFileMenuToTabPane(Messages.TabPaneMessage.COPY);
    }

    /**
     * Handles the Paste button action in Edit Menu.
     * Pastes the copied/cut text.
     */
    @FXML void handlePasteAction(ActionEvent event){
        mainController.handleFileMenuToTabPane(Messages.TabPaneMessage.PASTE);
    }

    /**
     * Handles the SelectAll button action in Edit Menu.
     * Selects all texts in the code area.
     */
    @FXML void handleSelectAllAction(ActionEvent event){
        mainController.handleFileMenuToTabPane(Messages.TabPaneMessage.SELECTALL);
    }


    public void injectMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
