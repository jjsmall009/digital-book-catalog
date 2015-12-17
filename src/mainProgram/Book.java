package MainProgram;

/**
 * JJ Small
 *
 * The Book class simply holds all of the data the describes a book.  As of now,
 * the amount of data stored will be pretty basic and minimal.
 */
public class Book implements java.io.Serializable {
    private static final long serialVersionUID = 1035072804522451545L;
    // Everything is public because I don't want a bunch of getter/setters getting in the way
    public String title;
    public long ISBN;
    public String authName;
    public String publisher;
    public String datePublished;

    public Book(String title, long ISBN, String authName, String publisher, String datePublished) {
        this.title = title;
        this.ISBN = ISBN;
        this.authName = authName;
        this.publisher = publisher;
        this.datePublished = datePublished;
    }

    @Override
    public String toString() {
        String s = "Title: " + this.title +
                " , ISBN: " + this.ISBN +
                " , Author: " + this.authName +
                " , Publisher: " + this.publisher +
                " , Date Published: " + this.datePublished;

        return s;
    }
}
