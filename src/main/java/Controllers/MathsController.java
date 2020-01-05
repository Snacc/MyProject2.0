package Controllers;
import Server.Main;


import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("maths")
public class MathsController {
    //Lists records in the maths table
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public String ListMaths(){
        System.out.println("maths/list");
        JSONArray list = new JSONArray();
        try{
            PreparedStatement ps = Main.db.prepareStatement("SELECT QuestionID, Subtopic, Question, AnswerA, AnswerB, AnswerC, AnswerD FROM Maths");
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
        System.out.println("maths/get/" + QuestionID);
        JSONObject item = new JSONObject();
        try{
        PreparedStatement ps = Main.db.prepareStatement("Select Subtopic, Question, AnswerA, AnswerB, AnswerC, AnswerD FROM Maths WHERE QuestionID =?");
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

    //Inserts a record into the maths table
    @POST
    @Path("new")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String InsertIntoMaths(@FormDataParam("QuestionID") Integer QuestionID, @FormDataParam("Subtopic") String Subtopic, @FormDataParam("Question") String Question, @FormDataParam("AnswerA") String AnswerA, @FormDataParam("AnswerB") String AnswerB, @FormDataParam("AnswerC") String AnswerC, @FormDataParam("AnswerD") String AnswerD, @CookieParam("Token") String Token){
        if (!User.validToken(Token)){
            return "{\"error\": \"You don't appear to be logged in.\"}";

        }
        try{
            if (QuestionID == null || Subtopic == null || Question == null || AnswerA == null || AnswerB == null || AnswerC ==null || AnswerD == null){
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("maths/newid=" + QuestionID);
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Maths(QuestionID, Subtopic, Question, AnswerA, AnswerB, AnswerC, AnswerD) VALUES (?,?,?,?,?,?,?)");

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

    //Updates a record in the maths table
    @POST
    @Path("update")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String UpdateMaths(@FormDataParam("QuestionID") Integer QuestionID, @FormDataParam("Subtopic") String Subtopic, @FormDataParam("Question") String Question, @FormDataParam("AnswerA") String AnswerA,@FormDataParam("AnswerB") String AnswerB,@FormDataParam("AnswerC") String AnswerC, @FormDataParam("AnswerD") String AnswerD, @CookieParam("Token") String Token){
        if (!User.validToken(Token)){
            return "{\"error\": \"You don't appear to be logged in.\"}";

        }
        try{
            if (QuestionID == null || Subtopic == null || Question == null || AnswerA ==null || AnswerB == null || AnswerC == null || AnswerD == null){
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("maths/update QuestionID=" + QuestionID);
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Maths SET Subtopic = ?, Question = ?, AnswerA = ?, AnswerB = ?, AnswerC = ?, AnswerD = ? WHERE QuestionID = ?");
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

    //Deletes a record in the maths table
    @POST
    @Path("delete")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String DeleteMaths(@FormDataParam("QuestionID") Integer QuestionID, @CookieParam("Token") String Token){
        if (!User.validToken(Token)){
            return "{\"error\": \"You don't appear to be logged in.\"}";

        }
        try{
            if(QuestionID==null){
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("maths/delete QuestionID=" + QuestionID);
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Maths WHERE QuestionID = ?");
            ps.setInt(1,QuestionID);
            ps.execute();
            return"{\"status\": \"OK\"}";


        }catch (Exception exception){
            System.out.println("Database error:" + exception.getMessage());
            return "{\"error\": \"Unable to delete item, please see server console for more info.\"}";
        }
    }


}
