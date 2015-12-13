package mainProgram;

/**
 * JJ Small
 *
 * The Book class simply holds all of the data the describes a book.  As of now,
 * the amount of data stored will be pretty basic and minimal.
 */
public class Book implements java.io.Serializable {
    private static final long serialVersionUID = 1035072804522451545L;
    // Everything is public because I don't want a bunch of getter/setters getting in the way
    public int ID;
    public String title;
    public int ISBN;
    public String authFirst;
    public String authLast;
    public String publisher;
    public String datePublished;

    public Book(int ID, String title, int ISBN, String authFirst, String authLast, String publisher, String datePublished) {
        this.ID = ID;
        this.title = title;
        this.ISBN = ISBN;
        this.authFirst = authFirst;
        this.authLast = authLast;
        this.publisher = publisher;
        this.datePublished = datePublished;
    }

    @Override
    public String toString() {
        String s = "Title: " + this.title +
                " , ISBN: " + this.ISBN +
                " , Author: " + this.authFirst + " " + this.authLast +
                " , Publisher: " + this.publisher +
                " , Date Published: " + this.datePublished;

        return s;
    }
}
