package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

@Path("user/")
public class User {
    //login method - produces a token for user logged in
    @POST
    @Path("login")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    // Takes Username and Password as a login
    public String userLogin(@FormDataParam("Username") String Username, @FormDataParam("Password") String Password){
        try{
            System.out.println("user/login");
            PreparedStatement ps1= Main.db.prepareStatement("SELECT Password FROM Users WHERE Username = ?");
            ps1.setString(1, Username);
            ResultSet loginResults = ps1.executeQuery();
            if (loginResults.next()){
                String correctPassword = loginResults.getString(1);
                if (Password.equals(correctPassword)){
                    String Token = UUID.randomUUID().toString();
                    PreparedStatement ps2 = Main.db.prepareStatement("UPDATE Users SET Token = ? WHERE Username = ?");
                    ps2.setString(1,Token);
                    ps2.setString(2,Username);
                    ps2.executeUpdate();

                    JSONObject userDetails = new JSONObject();
                    userDetails.put("Username", Username);
                    userDetails.put("Token", Token);
                    return userDetails.toString();


                } else{
                    return "{\"error\": Incorrect Password!\"}";
                }

            }else {
                return "{\"error\": \"Unknown User!\"}";
            }
        } catch (Exception exception){
            System.out.println("Database error during /user/login: " +exception.getMessage());
            return "{\"error\": \"Server side error!\"}";
        }
    }


    @POST
    @Path("logout")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String userLogout(@CookieParam("Token") String Token) {//takes corresponding user's token
        try {
            System.out.println("user/logout");
            PreparedStatement ps1 = Main.db.prepareStatement("SELECT UserID FROM Users WHERE Token = ?");
            ps1.setString(1, Token);
            ResultSet logoutResults = ps1.executeQuery();
            if (logoutResults.next()) {
                int id = logoutResults.getInt(1);
                PreparedStatement ps2 = Main.db.prepareStatement("UPDATE Users SET Token = NULL WHERE UserID = ?");
                ps2.setInt(1, id);
                ps2.executeUpdate();

                return "{\"status\": \"OK\"}";
            } else {
                return "{\"error\": \"Invalid token!\"}";
            }
        } catch (Exception exception) {
            System.out.println("Database error during /user/logout: " + exception.getMessage());
            return "{\"error\": \"Server side error!\"}";
        }
    }

    public static boolean validToken(String Token){
        try{
            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID FROM Users WHERE Token = ?");
            ps.setString(1,Token);
            ResultSet logoutResults = ps.executeQuery();
            return logoutResults.next();

        }catch (Exception exception){
            System.out.println("Database error during /users/logout: " + exception.getMessage());
            return false;
        }
    }

    @POST
    @Path("register")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String InsertIntoUsers(@FormDataParam("Username") String Username, @FormDataParam("Password") String Password, @FormDataParam("FirstName") String FirstName, @FormDataParam("Surname") String Surname){
        try{
            if (Username == null || Password == null || FirstName == null || Surname == null){ //If inputs are null, throws exeception.
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Users(Username, Password, FirstName, Surname) VALUES (?,?,?,?)");
            //sets parameter data types

            ps.setString(1, Username);
            ps.setString(2, Password);
            ps.setString(3, FirstName);
            ps.setString(4, Surname);
            ps.execute();
            return "{\"status\": \"OK\"}"; //returns a status token if it has been executed successfully.

        }catch (Exception exception){
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to create new item, please see server console for more info.\"}";
        }
    }






}
