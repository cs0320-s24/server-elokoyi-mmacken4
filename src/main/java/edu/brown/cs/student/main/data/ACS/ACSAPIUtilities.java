package edu.brown.cs.student.main.data.ACS;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.data.DatasourceException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import kotlin.text.UStringsKt;
import okio.Buffer;

public class ACSAPIUtilities {

  /**
   *  A datasource for weather forecasts via NWS API. This class uses the _real_
   *  API to return results. It has no caching in itself, and is focused on working
   *  with the real API.
   */

  /*
  given state code ("01")
  returns a String of the state name "Alabama"
   */
    private static StateResponse resolveStateCode(String stateNum) throws DatasourceException {
      try {
        //makes URL given state/county numbers
        URL requestURL = new URL("https", "api.census.gov", "/data/2010/dec/sf1?get=NAME&for=state:" + stateNum);
        HttpURLConnection connection = connect(requestURL);
        connection.setRequestMethod("GET");

        // sets up Moshi parser
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<StateResponse> jsonadapter = moshi.adapter(StateResponse.class).nonNull();

        // parses JSON response
        StateResponse body = jsonadapter.fromJson(new Buffer().readFrom(connection.getInputStream()));
        System.out.println(body); // records are nice for giving auto toString
        connection.disconnect();

        if(body == null || body.stateName() == null)
          throw new DatasourceException("No data received from ACS API");
        return body;
      } catch(IOException e) {
        throw new DatasourceException(e.getMessage());
      }
    }

  private static CountyResponse resolveCountyCode(String countyNum, String stateNum) throws DatasourceException {
    try {
      //makes URL given state/county numbers
      URL requestURL = new URL("https", "api.census.gov", "/data/2010/dec/sf1?get=NAME&for=county:" + countyNum + "*&in=state:" + stateNum);
      HttpURLConnection connection = connect(requestURL);
      connection.setRequestMethod("GET");

      // sets up Moshi parser
      Moshi moshi = new Moshi.Builder().build();
      JsonAdapter<CountyResponse> jsonadapter = moshi.adapter(CountyResponse.class).nonNull();

      // parses JSON response
      CountyResponse body = jsonadapter.fromJson(new Buffer().readFrom(connection.getInputStream()));
      System.out.println(body); // records are nice for giving auto toString
      connection.disconnect();

      if(body == null || body.name() == null)
        throw new DatasourceException("No data received from ACS API");
      return body;
    } catch(IOException e) {
      throw new DatasourceException(e.getMessage());
    }
  }

    /**
     * Private helper method; throws IOException so different callers
     * can handle differently if needed.
     */
    private static HttpURLConnection connect(URL requestURL) throws DatasourceException, IOException {
      URLConnection urlConnection = requestURL.openConnection();
      if(! (urlConnection instanceof HttpURLConnection))
        throw new DatasourceException("unexpected: result of connection wasn't HTTP");
      HttpURLConnection clientConnection = (HttpURLConnection) urlConnection;
      clientConnection.connect(); // GET
      if(clientConnection.getResponseCode() != 200)
        throw new DatasourceException("unexpected: API connection not success status "+clientConnection.getResponseMessage());
      return clientConnection;
    }

    private static ACSResponse getBroadband(String countyNum, String stateNum) throws DatasourceException, IllegalArgumentException {
      try {
        URL requestURL = new URL("https", "api.census.gov", "/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:" + countyNum + "&in=state:" + stateNum);
        HttpURLConnection connection = connect(requestURL);
        connection.setRequestMethod("GET");

        // sets up Moshi parser
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<ACSResponse> jsonadapter = moshi.adapter(ACSResponse.class).nonNull();

        // parses JSON response
        ACSResponse body = jsonadapter.fromJson(new Buffer().readFrom(connection.getInputStream()));
        System.out.println(body); // records are nice for giving auto toString
        connection.disconnect();

        if (body == null) {
          throw new DatasourceException("No data received from ACS API");
        }
        return body;
      } catch(IOException e) {
        throw new DatasourceException(e.getMessage(), e);
      }
    }

    ////////////////////////////////////////////////////////////////
    // ACS API data classes. These must be public for Moshi.
    ////////////////////////////////////////////////////////////////

    public record StateResponse(String stateName, String stateNum) {}
    public record CountyResponse(String name, String countyNum, String stateNum) {}
    public record ACSResponse(String stateName, String countyName, String broadbandValue) { }

}

