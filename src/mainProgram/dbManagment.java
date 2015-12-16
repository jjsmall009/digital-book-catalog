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
     * At program start up, we'll want to create a connection to the database for all
     * of the operations, so connect if possible.
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
     * When the program is run for the first time, there won't be a database to connect
     * to so simply create it and set up the books table.
     */
    public static Connection createDatabase() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:books.db");

            Statement stmt = con.createStatement();
            String sql = "CREATE TABLE BOOKS " +
                        "(ISBN TEXT PRIMARY KEY," +
                        " TITLE TEXT, " +
                        " AUTH_NAME, " +
                        " PUBLISHER TEXT, " +
                        " DATE_PUBLISHED TEXT)";

            stmt.executeUpdate(sql);
            stmt.close();

            System.out.println("Table created successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return con;
    }
}
