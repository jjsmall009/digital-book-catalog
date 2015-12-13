package mainProgram;

import java.lang.management.MemoryUsage;
import java.sql.Connection;

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
  import java.util.Scanner;


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
     * Main function.  Creates a connection to the database and then starts the menu loop
     */
    public static void main(String[] args) {
        //launch(args);
        // Now we can enter our menu loop to see what the user wants to do
        menuLoop();
    }

    /**
     * The menuLoop function uses a simple while loop that will repeatedly prompt the user
     * to enter a choice.  Once a choice is selected, control gets passed off to the logic
     * class that will do all of the heavy lifting.
     */
    public static void menuLoop() {
        int choice = 0;
        // Display the program header
        printHeader();

        Scanner input = new Scanner(System.in);
        while(choice != -1) {
            // Display the menu choices
            printChoices();

            System.out.print("\tChoice: ");
            choice = input.nextInt();

            // Now we switch over the users choice
            switch (choice) {
                case 1: {
                    MenuOptions.addBook();
                }case 2: {
                    MenuOptions.removeBook();
                }case 3: {
                    MenuOptions.createNewShelf();
                }case 4: {
                    MenuOptions.viewShelfContent();
                }case 5: {
                    MenuOptions.viewBookInfo();
                }
            }
        }
    }

    private static void printHeader() {
        System.out.println("===============================\n" +
                "///////////////////////////////\n" +
                "     DIGITAL BOOK CATALOG      \n" +
                "///////////////////////////////\n" +
                "===============================");
    }
    private static void printChoices() {
        System.out.println("Menu\n" +
                        "\t1.  Add book to collection\n" +
                        "\t2.  Remove book from collection\n" +
                        "\t3.  Crate new shelf\n" +
                        "\t4.  View books in a shelf\n" +
                        "\t5.  View book information\n");
    }
}
