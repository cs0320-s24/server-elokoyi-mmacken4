package edu.brown.cs.student.main.endpoints;

import edu.brown.cs.student.main.csv.CSVParser;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class ViewCSVHandler implements Route {

  @Override
  public Object handle(Request request, Response response) throws Exception {
    return null;
  }

 // private static CSVParser csvParser = new CSVParser();
  private static List<List<String>> loadedCSVData = null;

//  @Override
//  public Object handle(Request request, Response response) throws Exception {
//    return null;
//  }

  private static Map<String, Object> successResponse(Object... params) {
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put("result", "success");

    for (int i = 0; i < params.length; i += 2) {
      responseMap.put((String) params[i], params[i + 1]);
    }

    return responseMap;
  }

  private static Map<String, Object> errorResponse(String errorCode, String details) {
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put("result", errorCode);
    responseMap.put("details", details);

    return responseMap;
  }
  public static Route handleViewCSV = (Request request, Response response) -> {

    if (loadedCSVData == null) {
      return errorResponse("error_bad_request", "No CSV file loaded");
    }

    // The loadedCSVData variable contains the CSV data
    // You may want to handle pagination or limit the number of rows sent in the response
    return successResponse("data", loadedCSVData);
  };
}

