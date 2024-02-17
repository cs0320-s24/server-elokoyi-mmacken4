package edu.brown.cs.student.main.server;

import static spark.Spark.after;

import edu.brown.cs.student.main.endpoints.LoadCSVHandler;
import edu.brown.cs.student.main.endpoints.ViewCSVHandler;
import spark.Spark;

public class Server {
  public static void main(String[] args) {
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

    // Setting up the handler for the GET /load, GET /view, GET /search and /broadband endpoints
    Spark.get("load", loadCSVHandler);
    // Spark.get("search", new SearchCSVHandler());
    Spark.get("view", viewCSVHandler);
    // Spark.get("broadband", new BroadbandHandler());
    Spark.init();
    Spark.awaitInitialization();
    System.out.println("Server started at http://localhost:" + port);
  }
}
