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


public class User {
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
        if(!User.validToken(Token)){
            return"{\"error\": \"You don't appear to be logged in.\"}";
        }
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

    public static boolean validToken(String Token){
        try{
            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID FROM Users WHERE Token = ?");
            ps.setString(1, Token);
            ResultSet logoutResults = ps.executeQuery();
            return logoutResults.next();
        }catch(Exception exception){
            System.out.println("Database error during /users/logout: " + exception.getMessage());
            return false;
        }
    }



}
