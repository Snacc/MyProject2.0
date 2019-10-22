package Controllers;
import Server.Main;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CSController {
    public static void ListCS(){
        try{
            PreparedStatement ps = Main.db.prepareStatement("SELECT QuestionID, Subtopic, Question, AnswerA, AnswerB, AnswerC, AnswerD FROM CS");
            ResultSet results = ps.executeQuery();
            while(results.next()){    //Method keeps getting data until it reaches the end of the database.
                int QuestionID = results.getInt(1);
                String Subtopic = results.getString(2);
                String Question = results.getString(3);
                String AnswerA = results.getString(4);
                String AnswerB = results.getString(5);
                String AnswerC = results.getString(6);
                String AnswerD = results.getString(7);
                System.out.println(QuestionID + " " + Subtopic + " " + Question + " " + AnswerA + " " + AnswerB + " " + AnswerC + " " + AnswerD);

            }
        }catch (Exception exception){
            System.out.println("Database error: " + exception.getMessage());
        }
    }

    public static void InsertIntoCS(int QuestionID, String Question, String Subtopic, String AnswerA, String AnswerB, String AnswerC, String AnswerD){
        try{
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO CS(QuestionID, Subtopic, Question, AnswerA, AnswerB, AnswerC, AnswerD) VALUES (?,?,?,?,?,?,?)");
            ps.setInt(1, QuestionID);
            ps.setString(2, Subtopic);
            ps.setString(3, Question);
            ps.setString(4, AnswerA);
            ps.setString(5, AnswerB);
            ps.setString(6, AnswerC);
            ps.setString(7, AnswerD);
            ps.executeUpdate();
            System.out.println("Record added to Computer Science Table");

        }catch (Exception exception){
            System.out.println(exception.getMessage());
            System.out.println("Error: Something has gone wrong");
        }
    }

    public static void UpdateCS (String Subtopic, String Question, String AnswerA, String AnswerB, String AnswerC, String AnswerD){
        try{
            PreparedStatement ps = Main.db.prepareStatement("UPDATE CS SET Subtopic = ?, Question = ?, AnswerA = ?, AnswerB = ?, AnswerC = ?, AnswerD = ? WHERE UserID = ?");
            ps.setString(1,Subtopic);
            ps.setString(2,Question);
            ps.setString(3,AnswerA);
            ps.setString(4,AnswerB);
            ps.setString(5,AnswerC);
            ps.setString(6,AnswerD);
            ps.executeUpdate();

        }catch(Exception exception){
            System.out.println(exception.getMessage());

        }
    }

    public static void DeleteCS(int QuestionID){
        try{
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM CS WHERE QuestionID = ?");
            ps.setInt(1,QuestionID);
            ps.executeUpdate();


        }catch (Exception exception){
            System.out.println(exception.getMessage());
        }
    }


}