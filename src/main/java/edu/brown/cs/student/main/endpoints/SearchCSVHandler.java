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
    //check if viewed needed here too?
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

  private List<List<String>> searchCSV(String searchQuery, String columnID) throws FactoryFailureException, IOException {
    try {
      CSVParser<String> parser = new CSVParser<>(
              new FileReader(this.viewHandler.csvHandler.loadedCSVFilePath),
              new CreatorFromRow<String>() {
                @Override
                public List<String> create(List<String> parsedRow) {
                  // Simply return the list of strings representing the row
                  return parsedRow;}
              },
              true);
      List<List<String>> searchResults = new ArrayList<>();

      // Parse the entire CSV to get data
      List<List<String>> parsedData = parser.parse();
      int columnIndex = parser.headers.indexOf(columnID);

      // If columnID is not found in headers, search may not proceed or you need a different logic
      if (columnIndex == -1) {
        throw new IllegalArgumentException("Column " + columnID + " not found.");
      }

      // Filter data based on searchQuery and columnID
      for (List<String> row : parsedData) {
        if (columnIndex < row.size() && row.get(columnIndex).contains(searchQuery)) {
          searchResults.add(row);
        }
      }
      return searchResults;
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

}
