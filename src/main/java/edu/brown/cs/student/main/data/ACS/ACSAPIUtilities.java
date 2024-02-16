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

    private static GridResponse resolveStateAndCounty(String stateNum, String countyNum) throws DatasourceException {
      try {
        URL requestURL = new URL("https", "api.census.gov", "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:" + countyNum  &in=state:1:"+ stateNum);
        HttpURLConnection clientConnection = connect(requestURL);
        Moshi moshi = new Moshi.Builder().build();

        // NOTE WELL: THE TYPES GIVEN HERE WOULD VARY ANYTIME THE RESPONSE TYPE VARIES
        JsonAdapter<GridResponse> adapter = moshi.adapter(GridResponse.class).nonNull();
        // NOTE: important! pattern for handling the input stream
        GridResponse body = adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
        clientConnection.disconnect();
        if(body == null || body.properties() == null || body.properties().gridId() == null)
          throw new DatasourceException("Malformed response from NWS");
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

    /**
     * Given a geolocation, find the current weather at that location by
     * invoking the NWS API. This method will make real web requests.
     * @param loc the location to find weather at
     * @return the current weather at the given location
     * @throws DatasourceException if there is an issue obtaining the data from the API
     */
    @Override
    public WeatherData getCurrentWeather(Geolocation loc) throws DatasourceException, IllegalArgumentException {
      return getCurrentWeather(loc.lat(), loc.lon());
    }

    private static WeatherData getCurrentWeather(double lat, double lon) throws DatasourceException, IllegalArgumentException {
      try {
        // Double-check that the coordinates are valid. Yes, this has already
        // been checked, in principle, when the caller gave a Geolocation object.
        // But this is very cheap, and protects against mistakes I might make later
        // (such as making this method public, which would bypass the first check).
        if(!Geolocation.isValidGeolocation(lat, lon)) {
          throw new IllegalArgumentException("Invalid geolocation");
        }

        // NWS is not robust to high precision; limit to X.XXXX
        lat = Math.floor(lat * 10000.0) / 10000.0;
        lon = Math.floor(lon * 10000.0) / 10000.0;

        System.out.println("Debug: getCurrentWeather: "+lat+";"+lon);

        GridResponse gridResponse = resolveGridCoordinates(lat, lon);
        String gid = gridResponse.properties().gridId();
        String gx = gridResponse.properties().gridX();
        String gy = gridResponse.properties().gridY();

        System.out.println("Debug: gridResponse: "+gid+";"+gx+";"+gy);

        URL requestURL = new URL("https", "api.weather.gov", "/gridpoints/"+gid+"/"+gx+","+gy);
        HttpURLConnection clientConnection = connect(requestURL);
        Moshi moshi = new Moshi.Builder().build();

        // NOTE WELL: THE TYPES GIVEN HERE WOULD VARY ANYTIME THE RESPONSE TYPE VARIES
        JsonAdapter<ForecastResponse> adapter = moshi.adapter(ForecastResponse.class).nonNull();

        ForecastResponse body = adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

        System.out.println(body); // records are nice for giving auto toString

        clientConnection.disconnect();

        // Validity checks for response
        if(body == null || body.properties() == null || body.properties().temperature() == null)
          throw new DatasourceException("Malformed response from NWS");
        if(body.properties().temperature().values().isEmpty())
          throw new DatasourceException("Could not obtain temperature data from NWS");

        // TODO not well protected, always takes first timestamp of report
        return new WeatherData(body.properties().temperature().values().get(0).value());
      } catch(IOException e) {
        throw new DatasourceException(e.getMessage(), e);
      }
    }

    ////////////////////////////////////////////////////////////////
    // NWS API data classes. These must be public for Moshi.
    // They are "inner classes"; NWSAPIDataSource.GridResponse, etc.
    ////////////////////////////////////////////////////////////////

    public record GridResponse(String id, GridResponseProperties properties) { }
    // Note: case matters! "gridID" will get populated with null, because "gridID" != "gridId"
    public record GridResponseProperties(String gridId, String gridX, String gridY, String timeZone, String radarStation) {}

    public record ForecastResponse(String id, ForecastResponseProperties properties) {}
    public record ForecastResponseProperties(String updateTime, ForecastResponseTemperature temperature) {}
    public record ForecastResponseTemperature(String uom, List<ForecastResponseTempValue> values) {}
    public record ForecastResponseTempValue(String validTime, double value) {}

  }

}

