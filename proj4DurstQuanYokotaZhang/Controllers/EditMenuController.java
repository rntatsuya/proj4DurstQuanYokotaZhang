package proj4DurstQuanYokotaZhang.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import proj4DurstQuanYokotaZhang.Common.Messages;

public class EditMenuController {
    MainController mainController;
    @FXML MenuItem undoMenuButton;
    @FXML MenuItem redoMenuButton;
    @FXML MenuItem cutMenuButton;
    @FXML MenuItem copyMenuButton;
    @FXML MenuItem pasteMenuButton;
    @FXML MenuItem selectAllMenuButton;

    @FXML void handleUndoAction(ActionEvent event){
        mainController.handleFileMenuToTabPane(Messages.TabPaneMessage.UNDO);
    }

    @FXML void handleRedoAction(ActionEvent event){
        mainController.handleFileMenuToTabPane(Messages.TabPaneMessage.REDO);
    }

    @FXML void handleCutAction(ActionEvent event){
        mainController.handleFileMenuToTabPane(Messages.TabPaneMessage.CUT);
    }

    @FXML void handleCopyAction(ActionEvent event){
        mainController.handleFileMenuToTabPane(Messages.TabPaneMessage.COPY);
    }

    @FXML void handlePasteAction(ActionEvent event){
        mainController.handleFileMenuToTabPane(Messages.TabPaneMessage.PASTE);
    }

    @FXML void handleSelectAllAction(ActionEvent event){
        mainController.handleFileMenuToTabPane(Messages.TabPaneMessage.SELECTALL);
    }

    @FXML protected void handleEditMenuShown() {
        mainController.handleDisableEditMenuItems();
    }

    public void injectMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void disableMenuItems(boolean isTabEmpty, boolean isNumUndosZero) {
        if (!isTabEmpty)
            redoMenuButton.setDisable(isNumUndosZero);
        else
            redoMenuButton.setDisable(true);

        undoMenuButton.setDisable(isTabEmpty);
        cutMenuButton.setDisable(isTabEmpty);
        copyMenuButton.setDisable(isTabEmpty);
        pasteMenuButton.setDisable(isTabEmpty);
        selectAllMenuButton.setDisable(isTabEmpty);


    }
}
