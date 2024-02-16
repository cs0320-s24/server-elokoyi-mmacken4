package edu.brown.cs.student.main.server;

import static spark.Spark.after;

import edu.brown.cs.student.main.data.ACS.ACS;
import edu.brown.cs.student.main.data.ACS.ACSAPIUtilities;
import edu.brown.cs.student.main.endpoints.LoadCSVHandler;
import edu.brown.cs.student.main.endpoints.SearchCSVHandler;
import edu.brown.cs.student.main.endpoints.ViewCSVHandler;
import java.util.ArrayList;
import java.util.List;
import spark.Spark;

// main class
public class Server {
  public static void main(String[] args) {
    int port = 3232;
    Spark.port(port);

    after(
            (request, response) -> {
              response.header("Access-Control-Allow-Origin", "*");
              response.header("Access-Control-Allow-Methods", "*");
            });

    // Sets up data needed for the OrderHandler. You will likely not read from local
    // JSON in this sprint.
    String menuAsJson = ACSAPIUtilities.readInJson("data/menu.json");
    List<ACS> menu = new ArrayList<>();
    try {
      menu = ACSAPIUtilities.deserializeMenu(menuAsJson);
    } catch (Exception e) {
      // See note in ActivityHandler about this broad Exception catch... Unsatisfactory, but gets
      // the job done in the gearup where it is not the focus.
      e.printStackTrace();
      System.err.println("Errored while deserializing the menu");
    }

    // Setting up the handler for the GET /order and /activity endpoints
    Spark.get("load", new LoadCSVHandler());
    Spark.get("search", new SearchCSVHandler());
    Spark.get("view", new ViewCSVHandler());
    Spark.init();
    Spark.awaitInitialization();

    // Notice this link alone leads to a 404... Why is that?
    System.out.println("Server started at http://localhost:" + port);
  }
}


