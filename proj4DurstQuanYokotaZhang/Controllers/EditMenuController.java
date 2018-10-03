package proj4DurstQuanYokotaZhang.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import proj4DurstQuanYokotaZhang.Common.Messages;

public class EditMenuController {
    MainController mainController;

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

    public void injectMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
