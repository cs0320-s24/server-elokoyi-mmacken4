package edu.brown.cs.student.main.endpoints;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class BroadbandHandler implements Route {

  @Override
  public Object handle(Request request, Response response) {
    // Extract target state and county names from the request
    String stateName = request.queryParams("state");
    String countyName = request.queryParams("county");

    // Create a hashmap to store the results of the request
    Map<String, Object> responseMap = new HashMap<>();

    try {
      // Fetch state and county codes from the ACS API
      String stateCode = fetchStateCode(stateName);
      String countyCode = fetchCountyCode(stateCode, countyName);

      // Send a request to the ACS API and receive JSON back
      String broadbandData = fetchDataFromACS(stateCode, countyCode);

      // Process the broadband data as needed
      // (You'll need to parse the ACS API response and extract the relevant information)

      // Include the date and time of the data retrieval in the response
      LocalDateTime currentTime = LocalDateTime.now();
      responseMap.put("result", "success");
      responseMap.put("datetime", currentTime.toString());
      responseMap.put("state", stateName);
      responseMap.put("county", countyName);

      // Include any other relevant data in the response map

      return responseMap;
    } catch (Exception e) {
      e.printStackTrace();
      responseMap.put("result", "error");
      responseMap.put("details", "Failed to retrieve broadband data");
      return responseMap;
    }
  }

  private String fetchStateCode(String stateName) throws URISyntaxException {
    // Build a request to the ACS API to get state codes based on the state name
    HttpRequest stateCodeRequest =
        HttpRequest.newBuilder()
            .uri(new URI("YOUR_ACS_API_ENDPOINT_FOR_STATE_CODES_HERE" + stateName))
            .GET()
            .build();

    // Send the state code request and store the response
    try {
      HttpResponse<String> stateCodeResponse =
          HttpClient.newHttpClient().send(stateCodeRequest, HttpResponse.BodyHandlers.ofString());

      // Extract and return the state code from the ACS API response
      return extractStateCodeFromResponse(stateCodeResponse.body());
    } catch (Exception e) {
      e.printStackTrace();
      // Handle exceptions or rethrow if necessary
      throw new RuntimeException("Failed to fetch state code from ACS API", e);
    }
  }

  private String fetchCountyCode(String stateCode, String countyName) throws URISyntaxException {
    // Build a request to the ACS API to get county codes based on the state code and county name
    HttpRequest countyCodeRequest =
        HttpRequest.newBuilder()
            .uri(
                new URI(
                    "YOUR_ACS_API_ENDPOINT_FOR_COUNTY_CODES_HERE"
                        + stateCode
                        + "&county="
                        + countyName))
            .GET()
            .build();

    // Send the county code request and store the response
    try {
      HttpResponse<String> countyCodeResponse =
          HttpClient.newHttpClient().send(countyCodeRequest, HttpResponse.BodyHandlers.ofString());

      // Extract and return the county code from the ACS API response
      return extractCountyCodeFromResponse(countyCodeResponse.body());
    } catch (Exception e) {
      e.printStackTrace();
      // Handle exceptions or rethrow if necessary
      throw new RuntimeException("Failed to fetch county code from ACS API", e);
    }
  }

  private String fetchDataFromACS(String stateCode, String countyCode) throws URISyntaxException {
    // Build a request to the ACS API based on the target state and county codes
    HttpRequest acsApiRequest =
        HttpRequest.newBuilder()
            .uri(
                new URI(
                    "YOUR_ACS_API_ENDPOINT_FOR_BROADBAND_DATA_HERE"
                        + stateCode
                        + "&county="
                        + countyCode))
            .GET()
            .build();

    // Send the ACS API request and store the response
    try {
      HttpResponse<String> acsApiResponse =
          HttpClient.newHttpClient().send(acsApiRequest, HttpResponse.BodyHandlers.ofString());

      // Return the ACS API response body
      return acsApiResponse.body();
    } catch (Exception e) {
      e.printStackTrace();
      // Handle exceptions or rethrow if necessary
      throw new RuntimeException("Failed to fetch data from ACS API", e);
    }
  }

  private String extractStateCodeFromResponse(String response) {
    //    try {
    //      // Parse the ACS API response as a JSON array of arrays
    //      JsonArray jsonArray = JsonArray.readFrom(response);
    //
    //      // Check if the array is not empty
    //      if (!jsonArray.isEmpty()) {
    //        // Assuming the state code is at index 2 in the first array
    //        return jsonArray.get(0).get(2).asString();
    //      } else {
    //        throw new RuntimeException("Empty response from ACS API");
    //      }
    //    } catch (Exception e) {
    //      e.printStackTrace();
    //      // Handle exceptions or rethrow if necessary
    //      throw new RuntimeException("Failed to extract state code from ACS API response", e);
    //    }
    return response;
  }

  private String extractCountyCodeFromResponse(String response) {
    //    try {
    //      // Parse the ACS API response as a JSON array of arrays
    //      JsonArray jsonArray = JsonArray.readFrom(response);
    //
    //      // Check if the array is not empty
    //      if (!jsonArray.isEmpty()) {
    //        // Assuming the county code is at index 3 in the first array
    //        return jsonArray.get(0).get(3).asString();
    //      } else {
    //        throw new RuntimeException("Empty response from ACS API");
    //      }
    //    } catch (Exception e) {
    //      e.printStackTrace();
    //      // Handle exceptions or rethrow if necessary
    //      throw new RuntimeException("Failed to extract county code from ACS API response", e);

    return response;
  }
}
