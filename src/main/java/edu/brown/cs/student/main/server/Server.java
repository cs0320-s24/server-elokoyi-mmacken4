package edu.brown.cs.student.main.server;

import static spark.Spark.after;

import edu.brown.cs.student.main.endpoints.BroadbandHandler;
import edu.brown.cs.student.main.endpoints.LoadCSVHandler;
import edu.brown.cs.student.main.endpoints.SearchCSVHandler;
import edu.brown.cs.student.main.endpoints.ViewCSVHandler;
import java.io.FileNotFoundException;
import spark.Spark;

public class Server {
  public static void main(String[] args) throws FileNotFoundException {
    int port = 3232;
    Spark.port(port);

    Server server = new Server();

    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });

    LoadCSVHandler loadCSVHandler = new LoadCSVHandler();
    ViewCSVHandler viewCSVHandler = new ViewCSVHandler(loadCSVHandler);
    SearchCSVHandler searchCSVHandler = new SearchCSVHandler(loadCSVHandler);

    // Setting up the handler for the GET /load, GET /view, GET /search and /broadband endpoints
    Spark.get("load", loadCSVHandler);
    Spark.get("search", searchCSVHandler);
    Spark.get("view", viewCSVHandler);
    Spark.get("broadband", new BroadbandHandler());
    Spark.init();
    Spark.awaitInitialization();
    System.out.println(
        "Server Started. Please click on the local host link and in the URL bar, add "
            + "to the end of the link: /load?filepath=, then the name of the your csv filepath");
    System.out.println("Server started at http://localhost:" + port);
  }
}
