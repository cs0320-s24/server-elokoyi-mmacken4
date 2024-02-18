package edu.brown.cs.student.main.endpoints;

import com.squareup.moshi.Moshi;
import com.squareup.moshi.JsonAdapter;
import edu.brown.cs.student.main.datasource.ACS.ACS;
import edu.brown.cs.student.main.datasource.ACS.ACSResponse;
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

      //date and time of the retrieval
      LocalDateTime currTime = LocalDateTime.now();
      responseMap.put("result", "success");
      responseMap.put("datetime", currTime.toString());
      responseMap.put("state", stateName);
      responseMap.put("county", countyName);

      return responseMap;

    } catch (Exception e) {
      e.printStackTrace();
      responseMap.put("result", "error");
      responseMap.put("details", "Failed to get broadband data");
      return responseMap;
    }
  }

  private String fetchStateCode(String stateCode) throws URISyntaxException {
    // Build a request to get state name based on the state code
    HttpRequest stateCodeRequest =
        HttpRequest.newBuilder()
            .uri(new URI("https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:" + stateCode))
            .GET()
            .build();

    try {
      HttpResponse<String> stateCodeResponse =
          HttpClient.newHttpClient().send(stateCodeRequest, HttpResponse.BodyHandlers.ofString());

      return extractStateCodeFromResponse(stateCodeResponse.body());
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Failed to fetch state code from ACS API", e);
    }
  }

  private String fetchCountyCode(String stateCode, String countyName) throws URISyntaxException {
    // Build a request to the ACS API to get county codes based on the state code and county name
    HttpRequest countyCodeRequest =
        HttpRequest.newBuilder()
            .uri(
                new URI(
                    "https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:"
                        + countyName
                        + "*&in=state:"
                        + stateCode))
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
      // Handle exceptions o
      throw new RuntimeException("Failed to fetch county code from ACS API", e);
    }
  }

  private String fetchDataFromACS(String stateCode, String countyCode) throws URISyntaxException {
    // Send a request to the ACS API based on the state and county codes
    HttpRequest acsApiRequest =
        HttpRequest.newBuilder()
            .uri(
                new URI(
                    "https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:"
                        + countyCode
                        + "*&in=state:"
                        + stateCode))
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
      // Handle exceptions
      throw new RuntimeException("Failed to fetch data from ACS API", e);
    }
  }

  private String extractStateCodeFromResponse(String response) {
    return response;
  }

  private String extractCountyCodeFromResponse(String response) {
    return response;
  }
}
