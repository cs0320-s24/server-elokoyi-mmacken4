package edu.brown.cs.student.main.endpoints;

import edu.brown.cs.student.main.csv.CSVParser;
import edu.brown.cs.student.main.csv.Creator;
import edu.brown.cs.student.main.csv.FactoryFailureException;
import edu.brown.cs.student.main.csv.Search;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class SearchCSVHandler implements Route {

  private LoadCSVHandler csvHandler;
  private Search search;

  public SearchCSVHandler(LoadCSVHandler csvHandler) {
    this.csvHandler = csvHandler;
    // this.search = new Search();
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    // check if viewed needed here too?
    Map<String, Object> responseMap = new HashMap<>();

    try {
      if (csvHandler.loadedCSVFilePath == null) {
        response.status(400);
        return "Error no csv file loaded";
      }

      String searchQuery = request.queryParams("query");
      String columnID = request.queryParams("columnID");

      List<List<String>> results = searchCSV(searchQuery, columnID);

      System.out.println(results);

      Map<String, String> parametersMap = new HashMap<>();
      parametersMap.put("searchQuery", searchQuery);

      responseMap.put("result", "success");
      responseMap.put("data", results);
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
    // List<List<String>> searchResults = new ArrayList<>();
    Creator creator1 = new Creator();
    try {
      CSVParser<String> parser =
          new CSVParser<>(new FileReader(this.csvHandler.loadedCSVFilePath), creator1, true);

      Search<String> search = new Search<>(parser, creator1);
      search.search(this.csvHandler.loadedCSVFilePath, searchQuery, columnID);

      // return search.searchResults;
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }
    return search.searchResults;
  }
}
