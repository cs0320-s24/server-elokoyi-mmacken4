package edu.brown.cs.student.main.endpoints;

import edu.brown.cs.student.main.csv.CSVParser;
import edu.brown.cs.student.main.csv.Creator;
import edu.brown.cs.student.main.data.ACS.ACS;
import edu.brown.cs.student.main.data.ACS.ACSAPIUtilities;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoadCSVHandler implements Route {

  private static String loadedCSVFilePath = null;
  //
  //    public void loadcsv(Request request, Response response) {
  //        String filepath = request.queryParams("filepath");
  //        // System.out.println(filepath);
  //        if (filepath == null || filepath.trim().isEmpty()) {
  //            response.status(400);
  //            System.out.println("Error: No filepath given. Please give a valid filepath");
  //        }
  //        try {
  //            CSVParser parser
  //        }
  //    }
  //

  public Object handle(Request request, Response response) {
    // If you are interested in how parameters are received, try commenting out and
    // printing these lines! Notice that requesting a specific parameter requires that parameter
    // to be fulfilled.
    // If you specify a queryParam, you can access it by appending ?parameterName=name to the
    // endpoint
    // ex. http://localhost:3232/activity?participants=num
    Set<String> params = request.queryParams();
    //     System.out.println(params);
    String filepath = request.queryParams("filepath");
    System.out.println(filepath);

    // Creates a hashmap to store the results of the request
    Map<String, Object> responseMap = new HashMap<>();

    try {
      // Sends a request to the API and receives JSON back

      // Deserializes JSON into an Activity

      // Sends a request to the API and receives JSON back
      String acsJson = this.sendRequest(Integer.parseInt(filepath));
      //  String activityJson = this.sendRequest(num);
      // Deserializes JSON into an Activity
      ACS acs = ACSAPIUtilities.deserializeACS(acsJson);
      // Adds results to the responseMap
      responseMap.put("result", "success");
      responseMap.put("ACS", acs);
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

//  private static Map<String, Object> successResponse(Object... params) {
//    Map<String, Object> responseMap = new HashMap<>();
//    responseMap.put("result", "success");
//
//    for (int i = 0; i < params.length; i += 2) {
//      responseMap.put((String) params[i], params[i + 1]);
//    }
//
//    return responseMap;
//  }
//
//  private static Map<String, Object> errorResponse(String errorCode, String details) {
//    Map<String, Object> responseMap = new HashMap<>();
//    responseMap.put("result", errorCode);
//    responseMap.put("details", details);
//
//    return responseMap;
//  }

//  public Route handleLoadCSV(Request request, Response response) {
//        String filePath = request.queryParams("filepath");
//
//        // Check if the provided file path is not null and not empty
//        if (filePath == null || filePath.isEmpty()) {
//          return errorResponse("error_bad_request", "Invalid file path");
//        }
//
//        // Attempt to load the CSV file using your preferred CSV parsing logic
//        // For simplicity, let's assume you have a method loadCSVFile that returns the data
//        Creator<String> creator = new Creator<>();
//        CSVParser<String> csvParser = new CSVParser<>(new FileReader(filePath), creator, true);
//        List<List<String>> csvData = csvParser.parse();
//
//        // Check if the CSV file loading was successful
//        if (csvData == null) {
//          return errorResponse("error_datasource", "Unable to load CSV file");
//        }
//
//        System.out.println("success");
//        // Update the loadedCSVFilePath variable to keep track of the loaded file
//        loadedCSVFilePath = filePath;
//
//        // Return success response with the loaded file path
//        return successResponse("filepath", loadedCSVFilePath);
//      };

  private String sendRequest(int stateNum)
      throws URISyntaxException, IOException, InterruptedException {
    // Build a request to this BoredAPI. Try out this link in your browser, what do you see?
    // TODO 1: Looking at the documentation, how can we add to the URI to query based
    // on participant number?
    HttpRequest buildBoredApiRequest =
        HttpRequest.newBuilder()
            .uri(new URI("https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:" + stateNum))
            .GET()
            .build();

    // Send that API request then store the response in this variable. Note the generic type.
    HttpResponse<String> sentBoredApiResponse =
        HttpClient.newBuilder()
            .build()
            .send(buildBoredApiRequest, HttpResponse.BodyHandlers.ofString());

    // What's the difference between these two lines? Why do we return the body? What is useful from
    // the raw response (hint: how can we use the status of response)?
    System.out.println(sentBoredApiResponse);
    System.out.println(sentBoredApiResponse.body());

    return sentBoredApiResponse.body();
  }

}
