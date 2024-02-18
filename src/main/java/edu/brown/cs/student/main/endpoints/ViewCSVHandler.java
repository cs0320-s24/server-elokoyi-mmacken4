package edu.brown.cs.student.main.endpoints;


import edu.brown.cs.student.main.csv.CSVParser;
import edu.brown.cs.student.main.csv.Creator;
import edu.brown.cs.student.main.csv.FactoryFailureException;
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

  public Object handle(Request request, Response response) {
    // Check if a CSV file is loaded

    if (this.csvHandler.loadedCSVFilePath == null) {
      response.status(400); // Bad Request code
      return "No CSV file loaded";
    }

    try {
      List<List<String>> csvData = readCSVFile(this.csvHandler.loadedCSVFilePath);

      // Create the response map
      Map<String, Object> responseMap = new HashMap<>();
      responseMap.put("result", "success");
      responseMap.put("data", csvData);

      // properties of the response
      response.status(200);
      response.type("application/json");

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
