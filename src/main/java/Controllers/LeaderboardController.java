package Controllers;
import Server.Main;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LeaderboardController {

    //This method lists all scoreboard records
    public static void ListScoreboard(){
        try{
            PreparedStatement ps = Main.db.prepareStatement("SELECT LeaderboardID, Username, Score FROM Leaderboard");
            ResultSet results = ps.executeQuery();
            while(results.next()){    //Method keeps getting data until it reaches the end of the database.
                int LeaderboardID = results.getInt(1);
                String Username = results.getString(2);
                int Score = results.getInt(3);
                System.out.println(LeaderboardID + " " + Username + " " + Score);

            }
        }catch (Exception exception){
            System.out.println("Database error: " + exception.getMessage());
        }
    }

    // This method will insert a new record into the leaderboard table
    public static void InsertIntoLeaderboard(int LeaderboardID, String Username, int Score){

        try{
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Leaderboard(LeaderboardID, Username, Score) VALUES (?,?,?)");
            ps.setInt(1, LeaderboardID);
            ps.setString(2, Username);
            ps.setInt(3, Score);
            ps.executeUpdate();
            System.out.println("Record added to Leaderboard Table");

        }catch (Exception exception){
            System.out.println(exception.getMessage());
            System.out.println("Error: Something has gone wrong");
        }


    }


    // This method updates a single record in the leaderboard table
    public static void UpdateLeaderboard(String Username, int Score){
        try{
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Leaderboard SET Username = ?, Score = ? WHERE LeaderboardID = ?");
            ps.setString(1,Username);
            ps.setInt(2,Score);
            ps.executeUpdate();

        }catch(Exception exception){
            System.out.println(exception.getMessage());

        }
    }

    //This method deletes a record in the leaderboard table
    public static void DeleteLeaderboard(int LeaderboardID){
        try{
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Leaderboard WHERE LeaderboardID = ?");
            ps.setInt(1,LeaderboardID);
            ps.executeUpdate();


        }catch (Exception exception){
            System.out.println(exception.getMessage());
        }
    }


}
