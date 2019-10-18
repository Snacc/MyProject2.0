import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PhysicsController {
    public static void ListPhysics(){
        try{
            PreparedStatement ps = db.prepareStatement("SELECT QuestionID, Subtopic, Question, Answer A, Answer B, Answer C, Answer D FROM Physics");
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

    public static void InsertIntoPhysics(int QuestionID, String Question, String Subtopic, String AnswerA, String AnswerB, String AnswerC, String AnswerD){
        try{
            PreparedStatement ps = db.prepareStatement("INSERT INTO Physics(QuestionID, Subtopic, Question, Answer A, Answer B, Answer C, AnswerD) VALUES (?,?,?,?,?,?)");
            ps.setInt(1, QuestionID);
            ps.setString(2, Subtopic);
            ps.setString(3, Question);
            ps.setString(4, AnswerA);
            ps.setString(5, AnswerB);
            ps.setString(6, AnswerC);
            ps.setString(7,AnswerD);
            ps.executeUpdate();
            System.out.println("Record added to Physics Table");

        }catch (Exception exception){
            System.out.println(exception.getMessage());
            System.out.println("Error: Something has gone wrong");
        }
    }

    public static void UpdatePhysics (String Subtopic, String Question, String AnswerA, String AnswerB, String AnswerC, String AnswerD){
        try{
            PreparedStatement ps = db.prepareStatement("UPDATE Physics SET Subtopic = ?, Question = ?, AnswerA = ?, AnswerB = ?, AnswerC = ?, AnswerD = ? WHERE UserID = ?");
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

    public static void DeletePhysics(int QuestionID){
        try{
            PreparedStatement ps = db.prepareStatement("DELETE FROM Physics WHERE QuestionID = ?");
            ps.setInt(1,QuestionID);
            ps.executeUpdate();


        }catch (Exception exception){
            System.out.println(exception.getMessage());
        }
    }


}
