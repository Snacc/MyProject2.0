package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;

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
            PreparedStatement ps = Main.db.prepareStatement("SELECT Password FROM Users WHERE Username = ?"); //checks against database
            ps.setString(1,Username); //sets parameter as string
            ResultSet loginResults = ps.executeQuery(); //stores result of SQL query
            if(loginResults.next()){
                String correctPassword=loginResults.getString(1);//verifies password is correct on from loginResults
                if(Password.equals(correctPassword)){ //if password is correct
                    String Token = UUID.randomUUID().toString();//token created
                    PreparedStatement ps2 = Main.db.prepareStatement("UPDATE Users SET Token = ? WHERE Username = ?");
                    ps2.setString(1,Token); //sets prepared statement parameter as string Token (above)
                    ps2.setString(2,Username); //sets prepared statement parameter as inputted Username (so query can locate record)
                    ps2.executeUpdate();

                    return "(\"Token\": \"" + Token + "\"}"; //returns generated token


                }else{
                    return "{\"error\": \"Incorrect Password!\"}"; //if password is incorrect, error returned
                }
            }else{
                return "{\"error\": \"Incorrect Password!\"}"; //if

            }
        }catch (Exception exception){
            System.out.println("Database error during /user/login: " + exception.getMessage());
            return "{\"error\": \"Server side error!\"}";
        }
    }

    @POST
    @Path("logout")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String userLogout(@CookieParam("Token") String Token){//takes corresponding user's token
        try{
            System.out.println("/users/logout");
            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID FROM Users WHERE Token = ?");
            ps.setString(1,Token);
            ResultSet logoutResults = ps.executeQuery();
            if (logoutResults.next()){
                int ID = logoutResults.getInt(1);
                PreparedStatement ps2 = Main.db.prepareStatement("UPDATE Users SET Token = NULL WHERE UserID = ?");
                ps2.setInt(1,ID);
                ps2.executeUpdate();
                return "{\"status\": \"OK\"}";
            } else{
                return "{\"error\": \"Invalid token\")";

            }
        } catch (Exception exception){
            System.out.println("Database error during /users/logout - could not update" + exception.getMessage());//prints error in console
            return "{\"error\": \"Server side error :(\"}";
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






}
