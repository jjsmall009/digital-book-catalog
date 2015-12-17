package mainProgram;

import java.util.*;

/**
 * JJ Small
 *
 * This is the menu class for the first iteration of the program.  It uses the singleton pattern
 * as we only want a single instance of the menu to be instantiated.  Once the menu has been created
 * we'll enter the loop.
 */

public class Menu {
    /**
     * Here we will create our single instance of the menu class and have the only
     * constructor be private.
     */
    private static Menu instance = new Menu();
    private Menu() {
        // Block other classes from creating an instance of this class
    }

    /**
     * The only publicly available method return the instance
     */
    public static Menu createMenu() {
        return instance;
    }

    /**
     * The menuLoop function uses a simple while loop that will repeatedly prompt the user
     * to enter a choice.  Once a choice is selected, control gets passed off to the logic
     * class that will do all of the heavy lifting.
     */
    protected void menuLoop() {
        // We first need to get the book data from our file
        BookcaseOptions.establishConnection();

        // Display the program header
        printHeader();

        Scanner input = new Scanner(System.in);
        int choice = 0;
        while(choice != -1) {
            // Display the menu choices
            printChoices();

            System.out.print("\tChoice: ");
            try {
                choice = input.nextInt();
            } catch (InputMismatchException ex) {
                input = new Scanner(System.in);
                System.out.println("Please enter an integer number.\t");
            }

            // Now we switch over the users choice
            switch (choice) {
                case 1: {
                    BookcaseOptions.addBook();
                    break;
                }case 2: {
                    BookcaseOptions.removeBook();
                    break;
                }case 3: {
                    BookcaseOptions.createNewShelf();
                    break;
                }case 4: {
                    BookcaseOptions.removeShelf();
                    break;
                }case 5: {
                    BookcaseOptions.viewShelfBooks();
                    break;
                }case 6: {
                    BookcaseOptions.viewBookInfo();
                    break;
                }
            }
        }
        // Finish up some stuff before we quit
        BookcaseOptions.closeConnection();
    }
    private void printHeader() {
        System.out.println("===============================\n" +
                "///////////////////////////////\n" +
                "     DIGITAL BOOK CATALOG      \n" +
                "///////////////////////////////\n" +
                "===============================");
    }
    private void printChoices() {
        System.out.println("Menu\n" +
                "\t1.  Add a book\n" +
                "\t2.  Remove a book\n" +
                "\t3.  Create a new shelf\n" +
                "\t4.  Remove a shelf\n" +
                "\t5.  View books in a shelf\n" +
                "\t6.  View a specific book\n");
    }
}
