import org.sqlite.SQLiteConfig;

import java.sql.*;

public class Main {
    public static Connection db = null;

    private static void openDatabase(String dbFile){
        try{
            Class.forName("org.sqlite.JDBC");
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            db = DriverManager.getConnection("jdbc:sqlite:resources/" + dbFile, config.toProperties());
            System.out.println("Database connection successfully established.");
        } catch (Exception exception) {
            System.out.println("Database connection error: " + exception.getMessage());
        }
    }


    private static void closeDatabase(){
        try{
            db.close();
            System.out.println("Disconnected from database.");
        } catch (Exception exception){
            System.out.println("Database disconnection error:" + exception.getMessage());
        }
    }

    public static void main(String[] args) throws SQLException {
        openDatabase("Project Database.db");
        PreparedStatement ps = db.prepareStatement("SELECT UserID, Username, Password, FirstName, Surname, Score FROM UserDetails");
        ResultSet results = ps.executeQuery();
        while(results.next()){
            int UserID = results.getInt(1);
            String Username = results.getString(2);
            String Password = results.getString(3);
            String FirstName = results.getString(4);
            String Surname = results.getString(5);
            int Score = results.getInt(6);
            System.out.println(UserID + " " + Username + " " + Password + " " + FirstName + " " + Surname + " " + Score);

        }


        // code using database goes here.

        closeDatabase();
    }

}
// oh god o _ o