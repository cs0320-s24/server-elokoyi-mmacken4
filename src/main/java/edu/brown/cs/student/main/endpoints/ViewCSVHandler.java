package edu.brown.cs.student.main.endpoints;

//

import edu.brown.cs.student.main.csv.CSVParser;
import edu.brown.cs.student.main.csv.Creator;
import edu.brown.cs.student.main.csv.FactoryFailureException;
import edu.brown.cs.student.main.endpoints.LoadCSVHandler.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class ViewCSVHandler implements Route {
  private LoadCSVHandler csvHandler;

  public ViewCSVHandler(LoadCSVHandler csvHandler) {
    this.csvHandler = csvHandler;
  }

  // private String loadedCSVFilePath;

  public Object handle(Request request, Response response) {
    // Check if a CSV file is loaded

    if (this.csvHandler.loadedCSVFilePath == null) {
      response.status(400); // Bad Request status code
      return "No CSV file loaded";
    }

    try {
      // Perform your logic to read the CSV file and obtain its contents
      // For simplicity, let's assume you have a method readCSVFile that returns the data
      List<List<String>> csvData = readCSVFile(this.csvHandler.loadedCSVFilePath);

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
  private List<List<String>> readCSVFile(String filePath)
      throws FactoryFailureException, FileNotFoundException {
    // Implement your logic to read the CSV file using the provided
    // For demonstration purposes, return a dummy data

    // Create a CSVParser instance with FileReader
    Creator creator1 = new Creator();
    CSVParser csvParser = new CSVParser<>((new FileReader(filePath)), creator1, true);

    try {
      // Parse the CSV file and return the data
      return csvParser.parse();
    } catch (FactoryFailureException e) {
      // Log or handle the exception as needed
      throw e;
    }
  }
}
