package proj4DurstQuanYokotaZhang;

import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

public class AlertBox {

    /**
     * fileNotFound displays an alert message warning the
     * user that the file they selected was not found.
     */
    static void fileNotFound() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("ERROR!");
        alert.setHeaderText("File not found, please select another one or try again.");
       
        alert.showAndWait();
    }

    /**
     * aboutUs displays an alert message informing the
     * user about who is responsible for this elegant work of
     * art.
     */
    static void aboutUs() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About us");
        alert.setHeaderText("Some information about us...");
        alert.setContentText("Authors:\nRobert Durst, Yi Feng,Melody Mao, Danqing Zhao\njoyful programmers who code happily together everyday :)");

        alert.showAndWait();
    }

    /**
     * closeTab displays an alert message presenting the user
     * with three options for dealing with an open tab.
     */
    static ButtonType closeTab() {
        Alert alert = new Alert(
            Alert.AlertType.CONFIRMATION,
            "Want to save before exit?",
            ButtonType.YES,
            ButtonType.NO,
            ButtonType.CANCEL
        );
        alert.setTitle("Alert");

        // display alert
        alert.showAndWait();

        return alert.getResult();
    }

}