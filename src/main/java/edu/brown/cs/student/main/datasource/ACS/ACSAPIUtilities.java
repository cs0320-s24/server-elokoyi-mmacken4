package edu.brown.cs.student.main.datasource.ACS;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.datasource.DatasourceException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import okio.Buffer;

public class ACSAPIUtilities {

  /**
   * A datasource for weather forecasts via NWS API. This class uses the _real_ API to return
   * results. It has no caching in itself, and is focused on working with the real API.
   */
  private static ACSResponse resolveStateCode(String stateName) throws DatasourceException {

    try {
      // makes URL given state/county numbers
      URL requestURL =
          new URL(
              "https",
              "api.census.gov",
              "/data/2010/dec/sf1?get=S2802_C03_022E&for=county:*" + "&in=state:*");
      HttpURLConnection connection = connect(requestURL);
      connection.setRequestMethod("GET");

      // sets up Moshi parser
      Moshi moshi = new Moshi.Builder().build();
      JsonAdapter<ACSResponse> jsonadapter = moshi.adapter(ACSResponse.class).nonNull();

      // parses JSON response
      ACSResponse body = jsonadapter.fromJson(new Buffer().readFrom(connection.getInputStream()));
      connection.disconnect();

      if (body == null || body.broadband() == null)
        throw new DatasourceException("Malformed response from ACS");
      return body;
    } catch (IOException e) {
      throw new DatasourceException(e.getMessage());
    }
  }

  /**
   * Private helper method; throws IOException so different callers can handle differently if
   * needed.
   */
  private static HttpURLConnection connect(URL requestURL) throws DatasourceException, IOException {
    URLConnection urlConnection = requestURL.openConnection();
    if (!(urlConnection instanceof HttpURLConnection))
      throw new DatasourceException("unexpected: result of connection wasn't HTTP");
    HttpURLConnection clientConnection = (HttpURLConnection) urlConnection;
    clientConnection.connect(); // GET
    if (clientConnection.getResponseCode() != 200)
      throw new DatasourceException(
          "unexpected: API connection not success status " + clientConnection.getResponseMessage());
    return clientConnection;
  }

  public static ACS deserializeACS(String jsonACS) {
    try {
      // Initializes Moshi
      Moshi moshi = new Moshi.Builder().build();

      // Initializes an adapter to an ACS class then uses it to parse the JSON.
      JsonAdapter<ACS> adapter = moshi.adapter(ACS.class);

      ACS ACS = adapter.fromJson(jsonACS);

      return ACS;
    }

    catch (IOException e) {
      e.printStackTrace();
      // return new ACS(resolveStateCode(), countyName, broadband);
    }
    return null;
  }


  public record ACSResponse(String stateNum, String countyNum, String broadband) {}

}
