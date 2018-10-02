package proj4DurstQuanYokotaZhang.Controllers;

import javafx.fxml.FXML;
import proj4DurstQuanYokotaZhang.SavedCache;

public class MainController {
    @FXML private EditMenuController editMenuController;
    @FXML private FileMenuController fileMenuController;
    @FXML private TabPaneController tabPaneController;
    @FXML private ToolBarController toolBarController;

    // CACHE
    SavedCache savedCache = new SavedCache();

}
