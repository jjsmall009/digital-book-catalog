package mainProgram;

/**
 * JJ Small
 *
 * All of your books belong to some sort of collection, so having a class dedicated
 * to managing the collection makes sense.  This class contains a list of all of the
 * books that are in the system and provides ways of modifying them
 */

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.books.Books;
import com.google.api.services.books.model.Volumes;
import com.google.api.services.books.model.Volume;

import java.io.*;
import java.util.*;
import java.sql.*;

public class ShelfCollection {
    // We'll need to keep track of all of the books that are in the system
    // without having to open and read from the data file a million time.
    private static ArrayList<Shelf> shelfCollection = new ArrayList<>();
    private static int totalShelves = 0;
    private static String fileName = "shelf_data.txt";

    /**
     * At program startup, get all of the book data from the file and store it.
     */
    public static void gatherShelfData() {
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

        // If there are no shelves, which means there are no books yet, create the default
        // shelf where all books are stored and add it to our shelf collection.
        if(totalShelves == 0) {
            ArrayList<Book> books = new ArrayList<>();
            shelfCollection.add(new Shelf(0, "homeShelf", books));
            totalShelves++;
        }
    }

    /**
     * Once the user quits the menu, we need to do some cleanup and then store any
     * new information into our book file.
     */
    public static void finalizeShelfData() {
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
        // Fist get which shelf they want to add to
        listShelfNames();
        System.out.print("Which shelf do you want to add to?: ");
        String shelf = getInput();

        if(!shelfExists(shelf)) {
            System.out.println("Shelf not found.");
            return;
        }

        // Now get the book information
        System.out.print("\tEnter the title of the book: ");
        String title = getInput();
        System.out.print("\tEnter the authors name: ");
        String authName = getInput();

        // Using this information, make a query to Google Books to get the remaining info
        String query = title + " " + authName;
        try {
            Book resultBook = queryGoogleBooks(query);

            // All books get added to the home shelf
            shelfCollection.get(0).books.add(resultBook);
            // Now add to the specified shelf
            if(!shelf.equals("homeShelf")) {
                Shelf s = findShelf(shelf);
                s.books.add(resultBook);
            }
            System.out.println("Book added to collection");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Given the title of a book, scan through the list of books and remove it if possible
     */
    public static void removeBook() {
        // Fist get which shelf they want to remove from
        listShelfNames();
        System.out.print("Which shelf do you want to remove a book from?: ");
        String shelf = getInput();

        if(!shelfExists(shelf)) {
            System.out.println("Shelf not found.");
            return;
        }

        boolean found = false;
        // Now get the book information
        System.out.print("\tEnter the title of the book: ");
        String title = getInput();
        if(totalShelves > 0) {
            Shelf temp = findShelf(shelf);
            int numBooks = temp.books.size();

            // Search through the book collection to find a match
            for (int i = 0; i < numBooks; i++) {
                if (temp.books.get(i).title.equals(title)) {
                    temp.books.remove(i);
                    System.out.println("Book removed from system");
                    temp.numBooks--;
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("Book not in system.  Cannot remove");
            }
        }
    }

    /**
     * Given a string as user input, create a new Shelf object and add it the
     * shelf arraylist
     */
    public static void createNewShelf() {
        System.out.print("Enter the new shelf name: ");
        String name = getInput();

        ArrayList<Book> books = new ArrayList<>();
        shelfCollection.add(new Shelf(0, name, books));
        totalShelves++;
    }

    /**
     * This function will first list all of the shelves in the system,
     * and then output all of that shelf's book data
     */
    public static void viewShelfBooks() {
        if(totalShelves > 0) {
            // List the shelf names
            listShelfNames();
            // Prompt for the name of a shelf
            System.out.print("\n\t4Enter shelf name: ");
            String name = getInput();

            if(!shelfExists(name)) {
                System.out.println("Shelf not found.");
                return;
            }

            // Now we can print out all of the books in the shelf.
            Shelf s = findShelf(name);
            for(Book b : s.books) {
                System.out.println("\t" + b.toString());
            }
        }
    }

    /**
     * Output the data of a specific book.
     */
    public static void viewBookInfo() {
        System.out.print("\tEnter the title of the book ");
        String title = getInput();
        boolean found = false;

        if(totalShelves > 0) {
            Shelf temp = shelfCollection.get(0);
            int numBooks = temp.books.size();

            // Scan through the book until we find the title we want
            for (int i = 0; i < numBooks; i++) {
                Book b = temp.books.get(i);
                if (b.title.equals(title)) {
                    System.out.println(b.toString());
                    found = true;
                }
            }
            if (!found) {
                System.out.println("\tThe book is not in the system.");
            }
        }
    }

    /**
     * Helper function to grab a string from the user
     */
    private static String getInput() {
        Scanner in = new Scanner(System.in);
        return in.nextLine();
    }

    /**
     * Helper function to check if a shelf exists or not
     */
    private static boolean shelfExists(String name) {
        boolean found = false;
        for(Shelf s: shelfCollection) {
            if(s.name.equals(name))
                found = true;
        }
        return found;
    }

    /**
     * Helper function to find and return a shelf
     */
    private static Shelf findShelf(String name) {
        Shelf temp = null;
        for(int i = 0; i < totalShelves; i++) {
            if(shelfCollection.get(i).name.equals(name)) {
                temp = shelfCollection.get(i);
                break;
            }
        }
        return temp;
    }

    /**
     * Simply prints out the names of all shelves
     */
    private static void listShelfNames() {
        // List the shelf names
        System.out.println("\nShelf names:");
        for(int i = 0; i < totalShelves; i++) {
            System.out.println("\t" + shelfCollection.get(i).name);
        }
    }

    /**
     * This large function will grab data from Google Books.  Instead of having
     * the user type out every piece of information about a book, by simply giving
     * the title and author name we can query google books and grab the rest of the
     * data.
     */
    private static Book queryGoogleBooks(String query) throws Exception {
        final String APP_NAME = "Digital Book Catalog";
        final long MAX_RESULTS = 3;

        // The JsonFactory will store the Book Builder stiff which in turn will store
        // the volume information from books returned by the query
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        // Set up Books client to store volume information
        final Books books = new Books.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, null)
                .setApplicationName(APP_NAME)
                .build();

        // Create a list of the books returned by the specified query, maximum 3
        Books.Volumes.List volumesList = books.volumes().list(query).setMaxResults(MAX_RESULTS);

        // Execute the query
        Volumes volumes = volumesList.execute();
        if (volumes.getTotalItems() == 0 || volumes.getItems() == null) {
            System.out.println("No matches found.");
            return new Book("", 1, "", "", "");
        }

        // Display info about the 3 books and prompt the user to indicate the most accurate one
        System.out.println("\tResults: ");
        for(Volume volume : volumes.getItems()) {
            Volume.VolumeInfo volumeInfo = volume.getVolumeInfo();
            // Title
            System.out.print("\tTitle: " + volumeInfo.getTitle() + " ");
            // Author(s).
            java.util.List<String> authors = volumeInfo.getAuthors();
            if (authors != null && !authors.isEmpty()) {
                System.out.print("Author(s): ");
                for (int i = 0; i < authors.size(); ++i) {
                    System.out.print(authors.get(i));
                    if (i < authors.size() - 1) {
                        System.out.print(", ");
                    }
                }
            }
            System.out.print(" Date Published: " + volumeInfo.getPublishedDate() + "\n");
        }

        System.out.print("\tWhich book looks the most correct? (1,2,3): ");
        int choice = Integer.parseInt(getInput());

        // Now that we have the correct volume, grab the data and create a new book object.
        Volume v = volumes.getItems().get(choice);
        Volume.VolumeInfo info = v.getVolumeInfo();

        Book b = new Book(info.getTitle(),
                            Long.parseLong(info.getIndustryIdentifiers().get(0).getIdentifier()),
                            info.getAuthors().get(0), info.getPublisher(),
                            info.getPublishedDate());

        return b;
    }
}
