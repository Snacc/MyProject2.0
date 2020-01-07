package Controllers;
import Server.Main;


import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("physics")
public class PhysicsController {
    //Lists the records in the physics table
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public String ListPhysics(){
        System.out.println("physics/list");
        JSONArray list = new JSONArray();
        try{
            PreparedStatement ps = Main.db.prepareStatement("SELECT QuestionID, Subtopic, Question, AnswerA, AnswerB, AnswerC, AnswerD FROM Physics");
            ResultSet results = ps.executeQuery();
            while(results.next()){    //Method keeps getting data until it reaches the end of the database.
                JSONObject item = new JSONObject();
                item.put("QuestionID", results.getInt(1));
                item.put("Subtopic", results.getString(2));
                item.put("Question", results.getString(3));
                item.put("AnswerA", results.getString(4));
                item.put("AnswerB", results.getString(5));
                item.put("AnswerC", results.getString(6));
                item.put("AnswerD", results.getString(7));
                list.add(item);

            }
            return list.toString();
        }catch (Exception exception){
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }

    @GET
    @Path("get/{QuestionID}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getThing(@PathParam("QuestionID") Integer QuestionID) throws Exception {
        if(QuestionID==null){
            throw new Exception("Thing's 'id' is missing in the HTTP request's URL.");

        }
        System.out.println("physics/get" + QuestionID);
        JSONObject item = new JSONObject();
        try{
            PreparedStatement ps = Main.db.prepareStatement("Select Subtopic, Question, AnswerA, AnswerB, AnswerC, AnswerD FROM Physics WHERE QuestionID =?");
            ps.setInt(1,QuestionID);
            ResultSet results = ps.executeQuery();
            if (results.next()){
                item.put("QuestionID", QuestionID);
                item.put("Subtopic", results.getString(1));
                item.put("Question",results.getString(2));
                item.put("AnswerA", results.getString(3));
                item.put("AnswerB", results.getString(4));
                item.put("AnswerC", results.getString(5));
                item.put("AnswerD", results.getString(6));

            }
            return item.toString();

        }catch (Exception exception){
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to get item, please see server console for more info.\"}";
        }
    }

    //Inserts a record into the physics table
    @POST
    @Path("new")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String InsertIntoPhysics(@FormDataParam("QuestionID") Integer QuestionID, @FormDataParam("Subtopic") String Subtopic, @FormDataParam("Question") String Question, @FormDataParam("AnswerA") String AnswerA, @FormDataParam("AnswerB") String AnswerB, @FormDataParam("AnswerC") String AnswerC, @FormDataParam("AnswerD") String AnswerD, @CookieParam("Token") String Token){
        if (!User.validToken(Token)){
            return "{\"error\": \"You don't appear to be logged in.\"}";

        }
        try{
            if (QuestionID == null || Subtopic == null || Question == null || AnswerA == null || AnswerB == null || AnswerC ==null || AnswerD == null){
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("physics/newid=" + QuestionID);
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Physics(QuestionID, Subtopic, Question, AnswerA, AnswerB, AnswerC, AnswerD) VALUES (?,?,?,?,?,?,?)");

            ps.setInt(1, QuestionID);
            ps.setString(2, Subtopic);
            ps.setString(3, Question);
            ps.setString(4, AnswerA);
            ps.setString(5, AnswerB);
            ps.setString(6, AnswerC);
            ps.setString(7, AnswerD);
            ps.execute();
            return "{\"status\": \"OK\"}";

        }catch (Exception exception){
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to create new item, please see server console for more info.\"}";
        }
    }

    //Updates a record in the physics table
    @POST
    @Path("update")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String UpdatePhysics(@FormDataParam("QuestionID") Integer QuestionID, @FormDataParam("Subtopic") String Subtopic, @FormDataParam("Question") String Question, @FormDataParam("AnswerA") String AnswerA,@FormDataParam("AnswerB") String AnswerB,@FormDataParam("AnswerC") String AnswerC, @FormDataParam("AnswerD") String AnswerD, @CookieParam("Token") String Token){
        if (!User.validToken(Token)){
            return "{\"error\": \"You don't appear to be logged in.\"}";

        }
        try{
            if (QuestionID == null || Subtopic == null || Question == null || AnswerA ==null || AnswerB == null || AnswerC == null || AnswerD == null){
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("physics/update QuestionID=" + QuestionID);
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Physics SET Subtopic = ?, Question = ?, AnswerA = ?, AnswerB = ?, AnswerC = ?, AnswerD = ? WHERE QuestionID = ?");
            ps.setString(1,Subtopic);
            ps.setString(2,Question);
            ps.setString(3,AnswerA);
            ps.setString(4,AnswerB);
            ps.setString(5,AnswerC);
            ps.setString(6,AnswerD);
            ps.setInt(7,QuestionID);
            ps.execute();
            return "{\"status\": \"OK\"}";

        }catch(Exception exception){
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to update item please see server console for more info.\"}";

        }
    }

    //Deletes a record in the Physics table
    @POST
    @Path("delete")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String DeletePhysics(@FormDataParam("QuestionID") Integer QuestionID, @CookieParam("Token") String Token){
        if (!User.validToken(Token)){
            return "{\"error\": \"You don't appear to be logged in.\"}";

        }
        try{
            if(QuestionID==null){
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("physics/delete QuestionID=" + QuestionID);
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Physics WHERE QuestionID = ?");
            ps.setInt(1,QuestionID);
            ps.execute();
            return"{\"status\": \"OK\"}";


        }catch (Exception exception){
            System.out.println("Database error:" + exception.getMessage());
            return "{\"error\": \"Unable to delete item, please see server console for more info.\"}";
        }
    }


}
