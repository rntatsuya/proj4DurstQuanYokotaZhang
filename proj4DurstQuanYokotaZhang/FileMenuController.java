package proj4DurstQuanYokotaZhang;

import javafx.fxml.FXML;

public class FileMenuController {
    //@FXML TabPaneController tabPaneController;


    @FXML protected void handleAboutAction(){
        AlertBox.aboutUs();
    }

    @FXML protected void handleNewAction(){
        System.out.println("tabPaneController.makeNewTab()");
    }

    @FXML protected void handleOpenAction(){
        System.out.println("handleOpenAction");
    }

    @FXML protected void handleSaveAsAction(){
        System.out.println("handleSaveAsAction");
    }

    @FXML protected void handleSaveAction(){
        System.out.println("handleSaveAction");
    }

    @FXML protected void handleCloseAction(){
        System.out.println("handleCloseAction");
    }

    @FXML protected void handleExitAction(){
        System.out.println("handleExitAction");
    }


}
