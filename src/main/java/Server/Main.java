package Server;

import org.sqlite.SQLiteConfig;

import java.sql.*;

public class Main {
    public static Connection db = null;

    private static void openDatabase(){
        try{
            Class.forName("org.sqlite.JDBC");
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            db = DriverManager.getConnection("jdbc:sqlite:resources/" + "Project Database.db", config.toProperties());
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
        openDatabase();
        {
            Controllers.UserController.ListUsers();
            Controllers.LeaderboardController.ListScoreboard();
            Controllers.MathsController.ListMaths();
            Controllers.PhysicsController.ListPhysics();
            Controllers.CSController.ListCS();


        }
        closeDatabase();


        // code using database goes here.

    }
}
