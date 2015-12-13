package mainProgram;

/**
 * JJ Small
 *
 * All of your books belong to some sort of collection, so having a class dedicated
 * to managing the collection makes sense.  This class contains a list of all of the
 * books that are in the system and provides ways of modifying them
 */

import java.io.*;
import java.util.*;

public class ShelfCollection {
    // We'll need to keep track of all of the books that are in the system
    // without having to open and read from the data file a million time.
    private static ArrayList<Shelf> shelfCollection = new ArrayList<>();
    private static int totalShelves = 0;
    private static String fileName = "shelf_data.txt";

    /**
     * At program startup, get all of the book data from the file and store it.
     */
    public static void gatherBookData() {
        File f = new File(fileName);

        if(f.exists()) {
            try {
                FileInputStream input = new FileInputStream(fileName);
                ObjectInputStream ois = new ObjectInputStream(input);
                shelfCollection = (ArrayList) ois.readObject();
                ois.close();
                input.close();

                totalShelves = shelfCollection.size();
                System.out.println(totalShelves);
            } catch (Exception e) {
                System.out.println("Do this later");
            }
        }
    }

    /**
     * Once the user quits the menu, we need to do some cleanup and then store any
     * new information into our book file.
     */
    public static void finalizeBookData() {
            try {
                FileOutputStream output = new FileOutputStream(fileName);
                ObjectOutputStream oos = new ObjectOutputStream(output);
                oos.writeObject(shelfCollection);
                oos.close();
                output.close();
            } catch (Exception e) {
                System.out.println("do this later");
            }
    }

    /**
     * Adding a book is fairly simple.  Once we get the title, we can then go out
     * and do a web scrape to get the remaining data.  Then we add it to our collection
     * of all the books that we have and we're good to go.
     */
    public static void addBook() {
        // If there are no shelves, which means there are no books yet, create the default
        // shelf where all books are stored and add it to our shelf collection.
        if(totalShelves == 0) {
            ArrayList<Book> books = new ArrayList<>();
            shelfCollection.add(new Shelf(0, "homeShelf", books));
            totalShelves++;
        }

        System.out.print("\tEnter the title of the book: ");
        String title = getInput();
        System.out.print("\tEnter the authors first name: ");
        String first = getInput();
        System.out.print("\tEnter the authors last name: ");
        String last = getInput();

        // Now we can add the book to the collection
        int numBooks = shelfCollection.get(0).books.size();
        shelfCollection.get(0).books.add(new Book(numBooks++, title, 186950193, first, last, "", ""));
        System.out.println("Book added to collection");
    }

    /**
     * Given the title of a book, scan through the list of books and remove it if possible
     */
    public static void removeBook() {
        System.out.print("\tEnter the title of the book: ");
        String title = getInput();
        boolean found = false;

        if(totalShelves > 0) {
            Shelf temp = shelfCollection.get(0);
            int numBooks = temp.books.size();

            for (int i = 0; i < numBooks; i++) {
                if (temp.books.get(i).title.equals(title)) {
                    temp.books.remove(i);
                    System.out.println("Book removed from system");
                    found = true;
                }
            }
            if (!found) {
                System.out.println("Book not in system.  Cannot remove");
            }
        }
    }

    public static void createNewShelf() {

    }

    public static void viewShelfBooks() {
        if(totalShelves > 0) {
            // List the shelf names
            System.out.println("\nShelf names:");
            for(int i = 0; i < totalShelves; i++) {
                System.out.println("\t" + shelfCollection.get(i).name);
            }

            // Prompt for the name of a shelf
            System.out.print("\n\t4Enter shelf name: ");
            String name = getInput();

            // Now we can print out all of the books in the shelf.
            for(Shelf s: shelfCollection) {
                if(s.name.equals(name)) {
                    for(Book b : s.books) {
                        System.out.println("\t" + b.toString());
                    }
                    break;
                }
            }
        }
    }

    public static void viewBookInfo() {
        System.out.print("\tEnter the title of the book ");
        String title = getInput();
        boolean found = false;

        if(totalShelves > 0) {
            Shelf temp = shelfCollection.get(0);
            int numBooks = temp.books.size();

            for (int i = 0; i < numBooks; i++) {
                Book b = temp.books.get(i);
                if (b.title.equals(title)) {
                    System.out.println(b.ID + " " + b.title + " " + b.ISBN);
                    found = true;
                }
            }
            if (found == false) {
                System.out.println("\tThe book is not in the system.");
            }
        }
    }

    /**
     * Helper function to grab a string from the user
     */
    private static String getInput() {
        Scanner in = new Scanner(System.in);
        String title = in.nextLine();

        return title;
    }
}
