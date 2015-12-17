package MainProgram;

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

import java.util.*;
import java.sql.*;

public class BookcaseOptions {
    // Create a connection to the database which will persist throughout the lifetime
    // of the program
    private static Connection con = null;

    /**
     * At program startup, get all of the book data from the file and store it.
     */
    public static void establishConnection() {
        con = DatabaseManagement.makeConnection();
    }

    /**
     * Once the user quits the menu, we need to do some cleanup and then store any
     * new information into our book file.
     */
    public static void closeConnection() {
        try {
            con.close();
        } catch(SQLException e) {
            System.out.println(e.getMessage());
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
        System.out.print("Enter the number of the shelf you want to add to: ");
        int id = Integer.parseInt(getInput());

        if(!shelfExists(id)) {
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
            Book rs = queryGoogleBooks(query);

            // First add the new book into the default shelf
            try {
                Statement stmt = con.createStatement();
                String s1 = "insert into books (title, author, isbn, publisher, date_published, shelf_id) ";
                String s2 = String.format("values (\"%s\", '%s', %d, '%s', '%s', 1);",
                                        rs.title, rs.authName, rs.ISBN, rs.publisher, rs.datePublished);
                String s3 = s1 + s2;
                stmt.executeUpdate(s3);
                stmt.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            // Add to another shelf if need be
            if(id != 1) {
                // First add the new book into the default shelf
                try {
                    Statement stmt = con.createStatement();
                    String s1 = "insert into books (title, author, isbn, publisher, date_published, shelf_id) ";
                    String s2 = String.format("values (\"%s\", '%s', %d, '%s', '%s', '%d');",
                            rs.title, rs.authName, rs.ISBN, rs.publisher, rs.datePublished, id);
                    String s3 = s1 + s2;
                    stmt.executeUpdate(s3);
                    stmt.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
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
        // Fist get which shelf they want to add to
        listShelfNames();
        System.out.print("Enter the number of the shelf you want to add to: ");
        int id = Integer.parseInt(getInput());

        if(!shelfExists(id)) {
            System.out.println("Shelf not found.");
            return;
        }

        // Now we can print out all of the books in the shelf.
        try {
            Statement stmt = con.createStatement();
            String s = String.format("select * from books where shelf_id = '%d';", id);
            ResultSet rs = stmt.executeQuery(s);

            while(rs.next()) {
                System.out.println("Title: " + rs.getString("title") +
                        " Author: " + rs.getString("author") +
                        " ISBN: " + rs.getInt("isbn") +
                        " Publisher: " + rs.getString("publisher") +
                        " Date Published: " + rs.getString("date_published") +
                        " Shelf: " + rs.getInt("shelf_id"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // Now get the book information
        System.out.print("\tEnter the title of the book: ");
        String title = getInput();
        try {
            Statement stmt = con.createStatement();
            String s = String.format("delete from books where title = \"%s\";", title);
            stmt.executeUpdate(s);
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Given a string as user input, create a new Shelf object and add it the
     * shelf table in the db
     */
    public static void createNewShelf() {
        System.out.print("Enter the new shelf name: ");
        String name = getInput();

        try {
            Statement stmt = con.createStatement();
            String s1 = String.format("insert into shelf (name) values ('%s');", name);
            stmt.executeUpdate(s1);
            stmt.close();
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * We also want to be able to remove a shelf
     */
    public static void removeShelf() {
        // List the shelf names
        listShelfNames();
        System.out.print("Enter the number of the shelf you want to remove: ");
        int id = Integer.parseInt(getInput());

        try {
            Statement stmt = con.createStatement();
            String s1 = String.format("delete from shelf where shelf_id = '%d';", id);
            stmt.executeUpdate(s1);
            stmt.close();
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This function will first list all of the shelves in the system,
     * and then output all of that shelf's book data
     */
    public static void viewShelfBooks() {
        // List the shelf names
        listShelfNames();
        System.out.print("Enter the number of the shelf you want to view: ");
        int id = Integer.parseInt(getInput());

        if(!shelfExists(id)) {
            System.out.println("Shelf not found.");
            return;
        }

        // Now we can print out all of the books in the shelf.
        try {
            Statement stmt = con.createStatement();
            String s = String.format("select * from books where shelf_id = '%d';", id);
            ResultSet rs = stmt.executeQuery(s);

            while(rs.next()) {
                System.out.println("Title: " + rs.getString("title") +
                                    " Author: " + rs.getString("author") +
                                    " ISBN: " + rs.getInt("isbn") +
                                    " Publisher: " + rs.getString("publisher") +
                                    " Date Published: " + rs.getString("date_published") +
                                    " Shelf: " + rs.getInt("shelf_id"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Output the data of a specific book.
     */
    public static void viewBookInfo() {
        System.out.print("\tEnter the title of the book ");
        String title = getInput();

        try {
            Statement stmt = con.createStatement();
            String s = String.format("select * from books where title = '%s';", title);
            ResultSet rs = stmt.executeQuery(s);

            while(rs.next()) {
                System.out.println("Title: " + rs.getString("title") +
                        " Author: " + rs.getString("author") +
                        " ISBN: " + rs.getInt("isbn") +
                        " Publisher: " + rs.getString("publisher") +
                        " Date Published: " + rs.getString("date_published") +
                        " Shelf: " + rs.getInt("shelf_id"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
    private static boolean shelfExists(int id) {
        boolean found = false;
        try {
            Statement stmt = con.createStatement();
            String s = String.format("select * from shelf where shelf_id = '%d';", id);
            ResultSet rs = stmt.executeQuery(s);
            if(rs.next()) {
               found = true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return found;
    }

    /**
     * Simply prints out the names of all shelves
     */
    private static void listShelfNames() {
        try {
            Statement stmt = con.createStatement();
            String s = "select * from shelf;";
            ResultSet rs = stmt.executeQuery(s);

            while(rs.next()) {
                System.out.println("\t" + rs.getInt("shelf_id") + ": " + rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
            System.out.print(" Date Published: " + volumeInfo.getPublisher() + "\n");
        }

        System.out.print("\tWhich book looks the most correct? (1,2,3): ");
        int choice = Integer.parseInt(getInput()) - 1;

        // Now that we have the correct volume, grab the data and create a new book object.
        Volume v = volumes.getItems().get(choice);
        Volume.VolumeInfo info = v.getVolumeInfo();

        return new Book(info.getTitle(),
                            Long.parseLong(info.getIndustryIdentifiers().get(0).getIdentifier()),
                            info.getAuthors().get(0), info.getPublisher(),
                            info.getPublishedDate());
    }
}
