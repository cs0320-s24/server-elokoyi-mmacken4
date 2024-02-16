package edu.brown.cs.student.main.server;

import static spark.Spark.after;

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
    /*
       Setting CORS headers to allow cross-origin requests from the client; this is necessary for the client to
       be able to make requests to the server.

       By setting the Access-Control-Allow-Origin header to "*", we allow requests from any origin.
       This is not a good idea in real-world applications, since it opens up your server to cross-origin requests
       from any website. Instead, you should set this header to the origin of your client, or a list of origins
       that you trust.

       By setting the Access-Control-Allow-Methods header to "*", we allow requests with any HTTP method.
       Again, it's generally better to be more specific here and only allow the methods you need, but for
       this demo we'll allow all methods.

       We recommend you learn more about CORS with these resources:
           - https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS
           - https://portswigger.net/web-security/cors
    */
    after(
            (request, response) -> {
              response.header("Access-Control-Allow-Origin", "*");
              response.header("Access-Control-Allow-Methods", "*");
            });

    // Sets up data needed for the OrderHandler. You will likely not read from local
    // JSON in this sprint.
    String menuAsJson = SoupAPIUtilities.readInJson("data/menu.json");
    List<Soup> menu = new ArrayList<>();
    try {
      menu = SoupAPIUtilities.deserializeMenu(menuAsJson);
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

}
