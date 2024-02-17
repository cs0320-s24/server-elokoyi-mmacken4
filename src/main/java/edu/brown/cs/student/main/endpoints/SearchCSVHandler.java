package edu.brown.cs.student.main.endpoints;

import edu.brown.cs.student.main.csv.CSVParser;
import edu.brown.cs.student.main.csv.CreatorFromRow;
import edu.brown.cs.student.main.csv.FactoryFailureException;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class SearchCSVHandler implements Route {

  ViewCSVHandler viewHandler;

  @Override
  public Object handle(Request request, Response response) throws Exception {
    // check if viewed needed here too?
    Map<String, Object> responseMap = new HashMap<>();

    try {
      if (this.viewHandler.csvHandler.loadedCSVFilePath == null) {

        response.status(400);
        System.out.println("Error no csv file loaded");
      }

      String searchQuery = request.queryParams("query");
      String columnID = request.queryParams("columnID");

      List<List<String>> searchResults = searchCSV(searchQuery, columnID);

      Map<String, String> parametersMap = new HashMap<>();
      parametersMap.put("searchQuery", searchQuery);
      parametersMap.put("columnID", columnID);

      responseMap.put("result", "success");
      responseMap.put("data", searchResults);
      responseMap.put("parameters", parametersMap);
      response.status(200);
      response.type("application/json");

      return responseMap;
    } catch (Exception e) {
      responseMap.put("result", "Exception");
      return responseMap;
    }
  }

  private List<List<String>> searchCSV(String searchQuery, String columnID)
      throws FactoryFailureException, IOException {
    List<List<String>> searchResults = new ArrayList<>();
    Creator creator1 = new Creator();
    try {
      CSVParser<String> parser =
          new CSVParser<>(new FileReader(this.csvHandler.loadedCSVFilePath), creator1, true);

      Search<List<String>> search = new Search<>(parser, creator1);
      search.search(this.csvHandler.loadedCSVFilePath, searchQuery, columnID);

      // return searchResults;
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }
    return searchResults;
  }

}
