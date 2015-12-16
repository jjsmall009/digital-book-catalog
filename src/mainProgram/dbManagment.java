package mainProgram;

/**
 * JJ Small
 *
 * This class will handle the database creation and tables and stuff
 */

import java.io.File;
import java.sql.*;

public class dbManagment {
    /**
     * Try and connect to the database if possible
     */
    public static Connection makeConnection(){
        File f = new File("books.db");
        Connection con = null;

        if(f.exists()) {
            try {
                Class.forName("org.sqlite.JDBC");
                con = DriverManager.getConnection("jdbc:sqlite:books.db");
                System.out.println("connected to already created db");
            } catch (Exception e) {
                System.err.println("error connecting to database books.db");
            }
        } else {
            con = createDatabase();
        }

        return con;
    }

    /**
     * If there is not database, this will create it and the two tables
     */
    public static Connection createDatabase() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:books.db");

            Statement stmt1 = con.createStatement();
            Statement stmt2 = con.createStatement();

            String sql1 = "CREATE TABLE shelf(" +
                            "shelf_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            " name TEXT not NULL);";

            stmt1.executeUpdate(sql1);
            stmt1.close();

            String sql2 = "CREATE TABLE BOOKS(" +
                        "book_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        " title TEXT not NULL, " +
                        " author TEXT not NULL, " +
                        " isbn INTEGER, " +
                        " publisher TEXT, " +
                        " date_published TEXT, " +
                        " shelf_id INTEGER, " +
                        " FOREIGN KEY (shelf_id) REFERENCES shelf(shelf_id));";

            stmt2.executeUpdate(sql2);
            stmt2.close();

            System.out.println("Database created successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return con;
    }
}
