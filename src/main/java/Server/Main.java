package Server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.sqlite.SQLiteConfig;

import java.sql.*;

public class Main {
    public static Connection db = null;

    //This method opens the database, so the SQL algorithms I have written can access it.
    private static void openDatabase(){
        try{
            Class.forName("org.sqlite.JDBC");
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            db = DriverManager.getConnection("jdbc:sqlite:resources/" + "Project Database.db", config.toProperties());
            System.out.println("Database connection successfully established.");
        } catch (Exception exception) {
            System.out.println("Database connection error: " + exception.getMessage());
        }
    }

    //Once the database has been opened and its purpose fulfilled, the database must be closed, this method closes it.
    private static void closeDatabase(){
        try{
            db.close();
            System.out.println("Disconnected from database.");
        } catch (Exception exception){
            System.out.println("Database disconnection error:" + exception.getMessage());
        }
    }

    //Main
    public static void main(String[] args) throws SQLException {
        openDatabase();
        {
            ResourceConfig config = new ResourceConfig();
            config.packages("Controllers");
            config.register(MultiPartFeature.class);
            ServletHolder servlet = new ServletHolder(new ServletContainer(config));

            Server server = new Server(8081);
            ServletContextHandler context = new ServletContextHandler(server,"/");
            context.addServlet(servlet,"/*");

            try{
                server.start();
                System.out.println("Server successfully started.");
                server.join();

            } catch (Exception e){
                e.printStackTrace();
            }



        }
        closeDatabase();


        // code using database goes here.

    }
}
