package proj4DurstQuanYokotaZhang.Controllers;

import javafx.fxml.FXML;
import proj4DurstQuanYokotaZhang.Common.AlertBox;
import proj4DurstQuanYokotaZhang.Common.Messages;

public class FileMenuController {
    //@FXML TabPaneController tabPaneController;
    MainController mainController;

    @FXML protected void handleAboutAction(){
        AlertBox.aboutUs();
    }

    @FXML protected void handleNewAction(){
        mainController.handleFileMenuToTabPane(Messages.TabPaneMessage.NEW);
    }

    @FXML protected void handleOpenAction(){
        mainController.handleFileMenuToTabPane(Messages.TabPaneMessage.OPEN);
    }

    @FXML protected void handleSaveAsAction(){
        mainController.handleFileMenuToTabPane(Messages.TabPaneMessage.SAVEAS);
    }

    @FXML protected void handleSaveAction(){
        mainController.handleFileMenuToTabPane(Messages.TabPaneMessage.SAVE);
    }

    @FXML protected void handleCloseAction(){
        mainController.handleFileMenuToTabPane(Messages.TabPaneMessage.CLOSE);
    }

    @FXML protected void handleExitAction(){
        mainController.handleFileMenuToTabPane(Messages.TabPaneMessage.EXIT);
    }

    public void injectMainController(MainController mainController) {
        this.mainController = mainController;
    }
}