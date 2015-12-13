package mainProgram;

/**
 * JJ Small
 *
 * This class will handle the database creation and tables and stuff
 */

import java.io.File;
import java.sql.*;

public class dbManagment {
    /* Let's make a function to return a connection to the database */
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

    /* The first time running the program, create the database and make the books table */
    public static Connection createDatabase() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:books.db");

            Statement stmt = con.createStatement();
            String sql = "CREATE TABLE BOOKS " +
                        "(ISBN TEXT PRIMARY KEY," +
                        " TITLE TEXT, " +
                        " AUTH_FIRST TEXT, " +
                        " AUTH_LAST TEXT, " +
                        " PUBLISHER TEXT, " +
                        " DATE_PUBLISHED TEXT, " +
                        " COVER BLOW)";

            stmt.executeUpdate(sql);
            stmt.close();

            System.out.println("Table created successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return con;
    }
}
