package Controllers;
import Server.Main;


import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("leaderboard")
public class LeaderboardController {

    //This method lists all scoreboard records
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public String ListScoreboard(){
        System.out.println("leaderboard/list");
        JSONArray list = new JSONArray();
        try{
            PreparedStatement ps = Main.db.prepareStatement("SELECT LeaderboardID, Username, Score FROM Leaderboard");
            ResultSet results = ps.executeQuery();
            while(results.next()){    //Method keeps getting data until it reaches the end of the database.
                JSONObject item = new JSONObject();
                item.put("LeaderboardID", results.getInt(1));
                item.put("Username", results.getString(2));
                item.put("Score", results.getString(3));
                list.add(item);

            }
            return list.toString();
        }catch (Exception exception){
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }

    @GET
    @Path("get/{LeaderboardID}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getThing(@PathParam("LeaderboardID") Integer LeaderboardID) throws Exception {
        if(LeaderboardID==null){
            throw new Exception("Thing's 'id' is missing in the HTTP request's URL.");

        }
        System.out.println("leaderboard/get/" + LeaderboardID);
        JSONObject item = new JSONObject();
        try{
            PreparedStatement ps = Main.db.prepareStatement("Select Username, Score FROM Leaderboard WHERE LeaderboardID =?");
            ps.setInt(1,LeaderboardID);
            ResultSet results = ps.executeQuery();
            if (results.next()){
                item.put("LeaderboardID", LeaderboardID);
                item.put("Username", results.getString(1));
                item.put("Score",results.getString(2));
            }
            return item.toString();

        }catch (Exception exception){
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to get item, please see server console for more info.\"}";
        }
    }

    // This method will insert a new record into the leaderboard table
    @POST
    @Path("new")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String InsertIntoLeaderboard(@FormDataParam("LeaderboardID") Integer LeaderboardID, @FormDataParam("Username") String Username, @FormDataParam("Score") String Score){
        try{
            if (LeaderboardID == null || Username == null){
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("leaderboard/newid=" + LeaderboardID);
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Leaderboard(LeaderboardID, Username, Score) VALUES (?,?,?)");

            ps.setInt(1, LeaderboardID);
            ps.setString(2, Username);
            ps.setString(3, Score);
            ps.execute();
            return "{\"status\": \"OK\"}";

        }catch (Exception exception){
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to create new item, please see server console for more info.\"}";
        }
    }


    // This method updates a single record in the leaderboard table
    @POST
    @Path("update")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String UpdateLeaderboard(@FormDataParam("LeaderboardID") Integer LeaderboardID, @FormDataParam("Username") String Username, @FormDataParam("Score") String Score){
        try{
            if (LeaderboardID == null || Username == null || Score == null){
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("leaderboard/update LeaderboardID=" + LeaderboardID);
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Leaderboard SET Username = ?, Score = ? WHERE LeaderboardID = ?");
            ps.setString(1,Username);
            ps.setString(2,Score);
            ps.setInt(3,LeaderboardID);
            ps.execute();
            return "{\"status\": \"OK\"}";

        }catch(Exception exception){
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to update item please see server console for more info.\"}";

        }
    }

    //This method deletes a record in the leaderboard table
    @POST
    @Path("delete")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String DeleteLeaderboard(@FormDataParam("LeaderboardID") Integer LeaderboardID){
        try{
            if(LeaderboardID==null){
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("leaderboard/delete LeaderboardID=" + LeaderboardID);
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Leaderboard WHERE LeaderboardID = ?");
            ps.setInt(1,LeaderboardID);
            ps.execute();
            return"{\"status\": \"OK\"}";


        }catch (Exception exception){
            System.out.println("Database error:" + exception.getMessage());
            return "{\"error\": \"Unable to delete item, please see server console for more info.\"}";
        }
    }


}
