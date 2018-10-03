package proj4DurstQuanYokotaZhang.Controllers;

import javafx.fxml.FXML;
import proj4DurstQuanYokotaZhang.Common.Messages;
import proj4DurstQuanYokotaZhang.SavedCache;

public class MainController {
    @FXML private EditMenuController editMenuController;
    @FXML private FileMenuController fileMenuController;
    @FXML private TabPaneController tabPaneController;

    // CACHE
    public SavedCache savedCache = new SavedCache();

    @FXML private void initialize() {
        System.out.println("This is initialized");
        tabPaneController.injectMainController(this);
        fileMenuController.injectMainController(this);
        editMenuController.injectMainController(this);
    }

    public void handleFileMenuToTabPane(Messages.TabPaneMessage tabPaneMessage) {
        tabPaneController.distributeMessage(tabPaneMessage);
    }
}
