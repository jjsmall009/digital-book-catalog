package MainProgram;
/**
 * JJ Small
 *
 * This is the main class for the program.
 */

//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//import java.sql.*;

public class Main {
    /**
     * As of now I am working on a console only version of the model for this program.
     * As such, all of the view and controller stuff will be put aside until I get everything
     * else figured out.
     */
//    @Override
//    public void start(Stage primaryStage) throws Exception{

//    }

    /**
     * Main function.
     */
    public static void main(String[] args) {
        //launch(args);
        // Now let's create the menu and get everything running
        Menu mainMenu = Menu.createMenu();
        mainMenu.menuLoop();
    }
}
