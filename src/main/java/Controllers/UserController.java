package Controllers;
import Server.Main;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class UserController {
    // This method lists users from the database, including their firstname, surname, etc. For test purposes the password is not encrypted, or censored in anyway.
    public static void ListUsers(){
        try{
            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID, Username, Password, FirstName, Surname FROM UserDetails");
            ResultSet results = ps.executeQuery();
            while(results.next()){    //Method keeps getting data until it reaches the end of the database.
                int UserID = results.getInt(1);
                String Username = results.getString(2);
                String Password = results.getString(3);
                String FirstName = results.getString(4);
                String Surname = results.getString(5);
                System.out.println(UserID + " " + Username + " " + Password + " " + FirstName + " " + Surname);

            }
        }catch (Exception exception){
            System.out.println("Database error: " + exception.getMessage());
        }
    }

    // This method inserts a new user record into the user details table
    public static void InsertIntoUsers(int UserID, String Username, String Password, String FirstName, String Surname){
        try{
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO UserDetails(UserID, Username, Password, FirstName, Surname) VALUES (?,?,?,?,?)");
            ps.setInt(1, UserID);
            ps.setString(2, Username);
            ps.setString(3, Password);
            ps.setString(4, FirstName);
            ps.setString(5, Surname);
            ps.executeUpdate();
            System.out.println("Record added to UserDetails Table");

        }catch (Exception exception){
            System.out.println(exception.getMessage());
            System.out.println("Error: Something has gone wrong");
        }
    }


    //This method updates any records in the database table, it goes through each record parameter.
    public static void UpdateUserDetails(String Username, String Password, String FirstName, String Surname, int UserID){
        try{
            PreparedStatement ps = Main.db.prepareStatement("UPDATE UserDetails SET Username = ?, Password = ?, FirstName = ?, Surname = ? WHERE UserID = ?");
            ps.setString(1,Username);
            ps.setString(2,Password);
            ps.setString(3,FirstName);
            ps.setString(4,Surname);
            ps.setInt(5,UserID);
            ps.executeUpdate();

        }catch(Exception exception){
            System.out.println(exception.getMessage());

        }
    }

    // This method lets you delete any records from the database, by UserID
    public static void DeleteUserDetails(int UserID){
        try{
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM UserDetails WHERE UserID = ?");
            ps.setInt(1,UserID);
            ps.executeUpdate();


        }catch (Exception exception){
            System.out.println(exception.getMessage());
        }
    }
}
