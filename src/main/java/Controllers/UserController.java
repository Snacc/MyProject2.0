package Controllers;
import Server.Main;


import com.sun.jersey.core.header.Token;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

@Path("users")
public class UserController {
    // This method lists users from the database, including their firstname, surname, etc. For test purposes the password is not encrypted, or censored in anyway.
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public String ListUsers(){
        System.out.println("users/list");
        JSONArray list = new JSONArray(); //creates a JSON array which will list the data in Git Bash
        try{
            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID, Username, Password, FirstName, Surname FROM Users");
            ResultSet results = ps.executeQuery();
            while(results.next()){    //Method keeps getting data until it reaches the end of the database.
                JSONObject item = new JSONObject();
                item.put("UserID", results.getInt(1));
                item.put("Username", results.getString(2));
                item.put("Password", results.getString(3));
                item.put("FirstName", results.getString(4));
                item.put("Surname", results.getString(5));
                list.add(item);
            }
            return list.toString();
        }catch (Exception exception){
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }

    // Gets a single item from the table
    @GET
    @Path("get/{UserID}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getThing(@PathParam("UserID") Integer UserID) throws Exception {
        if(UserID==null){
            throw new Exception("Thing's 'id' is missing in the HTTP request's URL."); //If the userID input is null, or incorrect, then it throws an exception

        }
        System.out.println("users/get/" + UserID); //Prints out this in the server console.
        JSONObject item = new JSONObject(); //This takes the data from database and creates a JSONObject, which is outputted into GitBash.
        try{
            PreparedStatement ps = Main.db.prepareStatement("Select Username, Password, FirstName, Surname FROM Users WHERE UserID =?");
            ps.setInt(1,UserID);
            ResultSet results = ps.executeQuery();
            if (results.next()){
                item.put("UserID", UserID);
                item.put("Username", results.getString(1));
                item.put("Password",results.getString(2));
                item.put("FirstName", results.getString(3));
                item.put("Surname", results.getString(4));
            }
            return item.toString();

        }catch (Exception exception){
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to get item, please see server console for more info.\"}";
        }
    }

    // This method inserts a new user record into the user details table

    @POST
    @Path("new")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String InsertIntoUsers(@FormDataParam("UserID") Integer UserID, @FormDataParam("Username") String Username, @FormDataParam("Password") String Password, @FormDataParam("FirstName") String FirstName, @FormDataParam("Surname") String Surname, @CookieParam("Token") String Token){
        if(!UserID.validToken(Token)){
            return "{\"error\": \"You don't appear to be logged in\"}";
        }
        try{
            if (UserID == null || Username == null || Password == null || FirstName == null || Surname == null){ //If inputs are null, throws exeception.
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("users/newid=" +UserID); // Outputs in console log
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Users(UserID, Username, Password, FirstName, Surname) VALUES (?,?,?,?,?)");

            ps.setInt(1, UserID);
            ps.setString(2, Username);
            ps.setString(3, Password);
            ps.setString(4, FirstName);
            ps.setString(5, Surname);
            ps.execute();
            return "{\"status\": \"OK\"}"; //returns a status token if it has been executed successfully.

        }catch (Exception exception){
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to create new item, please see server console for more info.\"}";
        }
    }


    //This method updates any records in the database table, it goes through each record parameter.
    @POST
    @Path("update")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String UpdateUsers(@FormDataParam("UserID") Integer UserID, @FormDataParam("Username") String Username, @FormDataParam("Password") String Password, @FormDataParam("FirstName") String FirstName, @FormDataParam("Surname") String Surname, @CookieParam("Token") String Token){
        try{
            if (UserID == null || Username == null || Password == null || FirstName == null || Surname == null){
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("users/update UserID=" + UserID);
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Users SET Username = ?, Password = ?, Firstname = ?, Surname = ? WHERE UserID = ?");
            ps.setString(1,Username);
            ps.setString(2,Password);
            ps.setString(3,FirstName);
            ps.setString(4,Surname);
            ps.setInt(5,UserID);
            ps.execute();
            return "{\"status\": \"OK\"}";

        }catch(Exception exception){
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to update item please see server console for more info.\"}";

        }
    }

    // This method lets you delete any records from the database, by UserID
    @POST
    @Path("delete")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String DeleteUsers(@FormDataParam("UserID") Integer UserID, @CookieParam("Token") String Token){
        try{
            if(UserID==null){ //If inputted value is empty/incorrect, throw an exception.
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("users/delete UserID=" + UserID);
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Users WHERE UserID = ?");
            ps.setInt(1,UserID);
            ps.execute();
            return"{\"status\": \"OK\"}";


        }catch (Exception exception){
            System.out.println("Database error:" + exception.getMessage());
            return "{\"error\": \"Unable to delete item, please see server console for more info.\"}";
        }
    }

    //login method - produces a token for logged in user
    @POST
    @Path("login")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String userLogin(@FormDataParam("Username") String Username,@FormDataParam("Password") String Password, @CookieParam("Token") String Token){
        try{
            PreparedStatement ps = Main.db.prepareStatement("SELECT Password FROM Users WHERE Username = ?");
            ps.setString(1, Username);
            ResultSet loginResults = ps.executeQuery();
            if(loginResults.next()){
                String correctPassword=loginResults.getString(1);//verifies password is correct on database
                if(Password.equals(correctPassword)){
                    Token = UUID.randomUUID().toString();
                    PreparedStatement ps2 = Main.db.prepareStatement("UPDATE Users SET Token = ? WHERE Username = ?");
                    ps2.setString(1, Token);
                    ps2.setString(2, Username);
                    ps2.executeUpdate();

                    return "{\"Token\": \"" + Token + "\"}"; //returns the generated token




                } else {
                    return "{\"error\": \"Incorrect password!\"}"; //if password is incorrect
                }
            } else{
                return "{\"error\": \"Incorrect password!\"}"; //if password is incorrect

            }
        } catch (Exception exception){
            System.out.println("Database error during /user/login: " + exception.getMessage());
            return "{\"error\": \"Server side error :(\"}";
        }
    }

    //logout method
    @POST
    @Path("logout")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String userLogout(@CookieParam("Token") String Token){ //takes corresponding user's token
        try{
            System.out.println("/users/logout");
            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID FROM Users WHERE Token =?");
            ps.setString(1,Token);
            ResultSet logoutResults = ps.executeQuery();
            if (logoutResults.next()){
                int ID = logoutResults.getInt(1);
                PreparedStatement ps2 = Main.db.prepareStatement("UPDATE Users SET Token = NULL Where UserID = ?");
                ps2.setInt(1,ID);
                ps2.executeUpdate();
                return "{\"status\": \"OK\"}";

            } else{
                return "{\"error\": \"Invalid token\"}";
            }

        } catch (Exception exception){
            System.out.println("Database error during /users/logout - could not update" +exception.getMessage()); //prints error in console
            return "{\"error\": \"Server side error :(\"}"; //prints error for user
        }

    }







}
