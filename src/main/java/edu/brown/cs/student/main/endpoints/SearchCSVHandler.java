package edu.brown.cs.student.main.endpoints;

import edu.brown.cs.student.main.csv.CSVParser;
import edu.brown.cs.student.main.csv.Search;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class SearchCSVHandler implements Route {
  private CSVParser parser;
  private LoadCSVHandler loadCSVHandler;
  private Search search;

  public SearchCSVHandler(LoadCSVHandler loadCSVHandler) {
    this.loadCSVHandler = loadCSVHandler;
  }

  public void intializeComponents() throws FileNotFoundException {
    if (loadCSVHandler.getLoadedCSVFilePath() != null && (parser == null || search == null)) {
      this.parser =
          new CSVParser(
              new FileReader(loadCSVHandler.getLoadedCSVFilePath()), loadCSVHandler.creator, true);
      this.search = new Search(this.parser, loadCSVHandler.creator);
    }
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    Map<String, Object> responseMap = new HashMap<>();

    if (this.loadCSVHandler.loadedCSVFilePath == null) {
      response.status(400);
      System.out.println("Error no csv file loaded");
      return responseMap;
    }

    String searchQuery = request.queryParams("query");
    String columnID = request.queryParams("columnID");

    if (searchQuery == null || searchQuery.trim().isEmpty()) {
      response.status(400);
      System.out.println("Query parameter is missing");
      return responseMap;
    }

    try {
      intializeComponents();
      this.search.search(this.loadCSVHandler.getLoadedCSVFilePath(), searchQuery, columnID);
      List<List<String>> results = this.search.searchResults;

      System.out.println(results);

      Map<String, String> parametersMap = new HashMap<>();
      parametersMap.put("searchQuery", searchQuery);
      parametersMap.put("columnID", columnID);

      responseMap.put("result", "success");
      responseMap.put("data", results);
      responseMap.put("parameters", parametersMap);
      response.status(200);
      response.type("application/json");

      return responseMap;
    } catch (Exception e) {
      response.status(400);
      responseMap.put("result", "Exception: " + e.getMessage());
      return responseMap;
    }
  }
}
