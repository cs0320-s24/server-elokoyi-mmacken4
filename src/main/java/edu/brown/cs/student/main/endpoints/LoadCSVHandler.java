package edu.brown.cs.student.main.endpoints;

import edu.brown.cs.student.main.csv.CSVParser;
import spark.Route;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import spark.Request;
import spark.Response;
import spark.Route;


public class LoadCSVHandler implements Route {

    private

    public void loadcsv(Request request, Response response) {
        String filepath = request.queryParams("filepath");
        // System.out.println(filepath);
        if (filepath == null || filepath.trim().isEmpty()) {
            response.status(400);
            System.out.println("Error: No filepath given. Please give a valid filepath");
        }
        try {
            CSVParser parser
        }
    }
    /*
    public Object handle(Request request, Response response) {
    // If you are interested in how parameters are received, try commenting out and
    // printing these lines! Notice that requesting a specific parameter requires that parameter
    // to be fulfilled.
    // If you specify a queryParam, you can access it by appending ?parameterName=name to the
    // endpoint
    // ex. http://localhost:3232/activity?participants=num
    Set<String> params = request.queryParams();
    //     System.out.println(params);
    String participants = request.queryParams("participants");
    //    System.out.println(participants);

    // Creates a hashmap to store the results of the request
    Map<String, Object> responseMap = new HashMap<>();
    try {
      // Sends a request to the API and receives JSON back
      String activityJson = this.sendRequest(Integer.parseInt(participants));
      // Deserializes JSON into an Activity
      Activity activity = ActivityAPIUtilities.deserializeActivity(activityJson);
      // Adds results to the responseMap
      responseMap.put("result", "success");
      responseMap.put("activity", activity);
      return responseMap;
    } catch (Exception e) {
      e.printStackTrace();
      // This is a relatively unhelpful exception message. An important part of this sprint will be
      // in learning to debug correctly by creating your own informative error messages where Spark
      // falls short.
      responseMap.put("result", "Exception");
    }
    return responseMap;
  }

     */
}
