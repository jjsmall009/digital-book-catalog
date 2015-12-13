package mainProgram;

import java.util.Scanner;

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
