/*
 * File: Main.java
 * Names: (Proj 1-2) Liwei Jiang, Yi Feng, Jackie Hang, Paige Hanssen
 *        (Proj 3) Yi Feng, Melody Mao, Danqing Zhao, Robert Durst
 * F18 CS361 Project 3
 * This file creates a window that contains Hello and Goodbye buttons that have features
 * of creating an "input a number" dialog, changing the text of the button to "Yah, sure!",
 * and displaying a tabbed pane of text areas containing "Sample text"
 * Date: 09/25/2018
 */

package proj4DurstQuanYokotaZhang;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 * This class creates a window that contains Hello and Goodbye buttons,
 * a File menu for About/New/Open/Save/Save As/Close/Exit,
 * a Edit menu for Undo/Redo/Cut/Copy/Paste/Select,
 * and a tab pane that can contain any number of tabs for text areas
 *
 * @author  Robert Durst, Yi Feng, Melogy Mao, Danqing Zhao
 */
public class Main extends Application {

    /**
     * Create a window and load content from fxml and css
     *
     * @param stage The stage that contains the content
     */
    @Override public void start(Stage stage) throws Exception{
        // load the fxml file and get the root
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
        Parent root = loader.load();


        // initialize a scene and the css file
        Scene scene = new Scene(root, 300, 250);
        scene.getStylesheets().add("java-keywords.css");

        // configure the stage
        stage.setTitle("DurstFengMaoZhao's Project ");
        stage.sizeToScene();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Main function of Main class
     *
     * @param args command line argument
     */
    public static void main(String[] args) {
        launch(args);
    }
}