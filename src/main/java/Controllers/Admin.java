package Controllers;

import Server.Main;


import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

@Path("admin")
public class Admin {
    // This method lists all the admin records from the admin table on the database.
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public String ListAdmins(){
        System.out.println("admin/list");
        JSONArray list = new JSONArray();
        try{
            PreparedStatement ps = Main.db.prepareStatement("SELECT AdminID, Username, Password FROM Admin");
            ResultSet results = ps.executeQuery();
            while(results.next()){    //Method keeps getting data until it reaches the end of the database.
                JSONObject item = new JSONObject();
                item.put("AdminID", results.getInt(1));
                item.put("Username", results.getString(2));
                item.put("Password", results.getString(3));
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
    @Path("get/{AdminID}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getThing(@PathParam("AdminID") Integer AdminID) throws Exception {
        if(AdminID==null){
            throw new Exception("Thing's 'id' is missing in the HTTP request's URL.");

        }
        System.out.println("admin/get/" + AdminID);
        JSONObject item = new JSONObject();
        try{
            PreparedStatement ps = Main.db.prepareStatement("Select Username, Password FROM Admin WHERE AdminID =?");
            ps.setInt(1,AdminID);
            ResultSet results = ps.executeQuery();
            if (results.next()){
                item.put("AdminID", AdminID);
                item.put("Username", results.getString(1));
                item.put("Password", results.getString(2));
            }
            return item.toString();

        }catch (Exception exception){
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to get item, please see server console for more info.\"}";
        }
    }

    // This method inserts a new admin record into the admin table

    @POST
    @Path("new")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String InsertIntoAdmin(@FormDataParam("AdminID") Integer AdminID, @FormDataParam("Username") String Username, @FormDataParam("Password") String Password){
        try{
            if (AdminID == null || Username == null || Password == null){
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("admin/newid=" +AdminID);
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Admin(AdminID, Username, Password) VALUES (?,?,?)");

            ps.setInt(1, AdminID);
            ps.setString(2, Username);
            ps.setString(3, Password);
            ps.execute();
            return "{\"status\": \"OK\"}";

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
    public String UpdateAdmin(@FormDataParam("AdminID") Integer AdminID, @FormDataParam("Username") String Username, @FormDataParam("Password") String Password, @CookieParam("Token") String Token){
        try{
            if (AdminID == null || Username == null || Password == null){
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("admin/update AdminID=" + AdminID);
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Admin SET Username = ?, Password = ? WHERE AdminID = ?");
            ps.setString(1,Username);
            ps.setString(2,Password);
            ps.setInt(3,AdminID);
            ps.execute();
            return "{\"status\": \"OK\"}";

        }catch(Exception exception){
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to update item please see server console for more info.\"}";

        }
    }

    // This method lets you delete any records from the database, by AdminID
    @POST
    @Path("delete")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String DeleteAdmin(@FormDataParam("AdminID") Integer AdminID, @CookieParam("Token") String Token){
        try{
            if(AdminID==null){
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("admin/delete AdminID=" + AdminID);
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Admin WHERE AdminID = ?");
            ps.setInt(1,AdminID);
            ps.execute();
            return"{\"status\": \"OK\"}";


        }catch (Exception exception){
            System.out.println("Database error:" + exception.getMessage());
            return "{\"error\": \"Unable to delete item, please see server console for more info.\"}";
        }
    }


    @POST
    @Path("login")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String adminLogin(@FormDataParam("Username") String Username,@FormDataParam("Password") String Password){
        try{
            PreparedStatement ps = Main.db.prepareStatement("SELECT Password FROM Admin WHERE Username = ?");
            ps.setString(1, Username);
            ResultSet loginResults = ps.executeQuery();
            if(loginResults.next()){
                String correctPassword=loginResults.getString(1);//verifies password is correct on database
                if(Password.equals(correctPassword)){
                    String Token = UUID.randomUUID().toString(); //token is created
                    PreparedStatement ps2 = Main.db.prepareStatement("UPDATE Admin SET Token = ? WHERE Username = ?");
                    ps2.setString(1, Token);
                    ps2.setString(2, Username);
                    ps2.executeUpdate();

                    return "{\"Token\": \"" + Token + "\"}"; //returns the generated token




                } else {
                    return "{\"error\": \"Incorrect password!\"}";
                }
            } else{
                return "{\"error\": \"Incorrect password!\"}";

            }
        } catch (Exception exception){
            System.out.println("Database error during /admin/login: " + exception.getMessage());
            return "{\"error\": \"Server side error!\"}";
        }
    }
}
