package mainProgram;

import java.util.ArrayList;

/**
 * JJ Small
 *
 * A shelf is just a container for a certain amount of books.  Each shelf has
 * a name and a collection of the boks that are in the shelf.
 */

public class Shelf implements java.io.Serializable {
    private static final long serialVersionUID = 1035072804522451545L;

    public int numBooks;
    public String name;
    public ArrayList<Book> books;

    /**
     * A shelf must have all three of these in order to be instantiated
     */
    public Shelf(int numBooks, String name, ArrayList<Book> books) {
        this.numBooks = numBooks;
        this.name = name;
        this.books = books;
    }
}
