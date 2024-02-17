package edu.brown.cs.student.main.endpoints;

import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchCSVHandler implements Route {

  @Override
  public Object handle(Request request, Response response) throws Exception {
    //check if viewed needed here too?
    Map<String, Object> responseMap = new HashMap<>();

    try {
      if (LoadCSVHandler.loadedCSVFilePath == null) {
        response.status(400);
        return "Error no csv file loaded";
      }

      String searchQuery = request.queryParams("query");

      List<List<String>> searchResults = searchCSV(searchQuery);

      Map<String, String> parametersMap = new HashMap<>();
      parametersMap.put("searchQuery", searchQuery);

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

  private List<List<String>> searchCSV(String query) {
    //implement
  }
}
