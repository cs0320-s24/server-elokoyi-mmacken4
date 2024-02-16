package edu.brown.cs.student.main.endpoints;

import static edu.brown.cs.student.main.endpoints.LoadCSVHandler.loadedCSVFilePath;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class ViewCSVHandler implements Route {

  public Object handle(Request request, Response response) {
    // Check if a CSV file is loaded
    if (loadedCSVFilePath == null) {
      response.status(400); // Bad Request status code
      return "No CSV file loaded";
    }

    try {
      // Perform your logic to read the CSV file and obtain its contents
      // For simplicity, let's assume you have a method readCSVFile that returns the data
      List<List<String>> csvData = readCSVFile(loadedCSVFilePath);

      // Create the response map
      Map<String, Object> responseMap = new HashMap<>();
      responseMap.put("result", "success");
      responseMap.put("data", csvData);

      // Set properties of the response
      response.status(200); // OK status code
      response.type("application/json"); // Set response type

      // Return the response map as JSON
      return responseMap;
    } catch (Exception e) {
      e.printStackTrace();
      response.status(500); // Internal Server Error status code
      return "Error while processing the CSV file";
    }
  }

  // Method to read the CSV file and return its contents
  private List<List<String>> readCSVFile(String filePath) {
    // Implement your logic to read the CSV file using the provided
    // For demonstration purposes, return a dummy data
    return List.of(
        List.of("Header1", "Header2", "Header3"),
        List.of("Data1", "Data2", "Data3"),
        List.of("Data4", "Data5", "Data6"));
  }
}
