package Controllers;

import Server.Main;
import com.sun.jersey.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public class User {
    //login method - produces a token for user logged in
    @POST
    @Path("login")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String userLogout(@FormDataParam("Username") String Username, @FormDataParam("Password") String Password){
        try{
            PreparedStatement ps = Main.db.prepareStatement("SELECT Password FROM Users WHERE Username = ?");
            ps.setString(1,Username);
            ResultSet loginResults = ps.executeQuery();
            if(loginResults.next()){
                String correctPassword=loginResults.getString(1);
                if(Password.equals(correctPassword)){
                    String Token = UUID.randomUUID().toString();
                    PreparedStatement ps2 = Main.db.prepareStatement("UPDATE Users SET Token = ? WHERE Username = ?");
                    ps2.setString(1,Token);
                    ps2.setString(2,Username);
                    ps2.executeUpdate();

                    return "(\"Token\": \"" + Token + "\"}";


                }else{
                    return "{\"error\": \"Incorrect Password!\"}";
                }
            }else{
                return "{\"error\": \"Incorrect Password!\"}";

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
    public String userLogout(@CookieParam("Token") String Token){
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
            System.out.println("Database error during /users/logout - could not update" + exception.getMessage());
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
