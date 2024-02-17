package edu.brown.cs.student.main.endpoints;

import edu.brown.cs.student.main.csv.CSVParser;
import edu.brown.cs.student.main.csv.Creator;
import edu.brown.cs.student.main.csv.CreatorFromRow;
import edu.brown.cs.student.main.csv.FactoryFailureException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoadCSVHandler<T> implements Route {

  private BufferedReader reader;

  private CreatorFromRow<T> creator;

  List<String> headers;
  private Boolean hasHeaders;

  private String line;

  public String loadedCSVFilePath;

  public LoadCSVHandler() {
    // this.reader = new BufferedReader(reader);
    Creator<T> creator = new Creator<>();
    this.creator = creator;
    this.hasHeaders = hasHeaders;
    this.headers = new ArrayList<>();
    this.loadedCSVFilePath = loadedCSVFilePath;
  }

  public String getLoadedCSVFilePath() {
    return this.loadedCSVFilePath;
  }

  @Override
  public Object handle(Request request, Response response) throws FactoryFailureException {
    Set<String> params = request.queryParams();
    String filePath = request.queryParams("filepath");

    // Creates a hashmap to store the results of the request
    Map<String, Object> responseMap = new HashMap<>();

    try {
      if (filePath != null && !filePath.isEmpty()) {
        // Attempt to load the CSV file using your preferred CSV parsing logic
        List<List<String>> csvData = loadLocalCSVFile(filePath);
        // loadedCSVFilePath = filePath;
        // Check if the CSV file loading was successful
        if (csvData != null) {
          // Update the loadedCSVFilePath variable to keep track of the loaded file
          loadedCSVFilePath = filePath;

          // Return success response with the loaded file path
          responseMap.put("result", "success");
          responseMap.put("filepath", loadedCSVFilePath);
          //        } else {
          //          responseMap.put("result", "error_datasource");
          //          responseMap.put("details", "Unable to load CSV file");
          //        }
          //        return responseMap;
          //      } else {
          //        responseMap.put("result", "error_bad_request");
          //        responseMap.put("details", "Invalid file path");
          //        return responseMap;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      responseMap.put("result", "Exception");

      return responseMap;
    }
    return responseMap;
  }

  //  private List<List<String>> loadLocalCSVFile(String filePath)
  //      throws FactoryFailureException, FileNotFoundException {
  //
  //    CSVParser csvParser = new CSVParser<>(new FileReader(filePath), creator, true);
  //    System.out.println(filePath);
  //    return csvParser.parse();
  //  }

  private List<List<String>> loadLocalCSVFile(String filePath)
      throws FactoryFailureException, FileNotFoundException {
    // Create a CSVParser instance with FileReader
    Creator creator1 = new Creator();
    CSVParser<String> csvParser = new CSVParser<>((new FileReader(filePath)), creator1, true);

    try {
      // Parse the CSV file and return the data
      return csvParser.parse();
    } catch (FactoryFailureException e) {
      // Log or handle the exception as needed
      throw e;
    }
  }

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
;
