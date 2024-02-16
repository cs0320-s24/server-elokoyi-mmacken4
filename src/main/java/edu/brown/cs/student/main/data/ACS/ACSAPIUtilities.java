package edu.brown.cs.student.main.data.ACS;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.data.DatasourceException;
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

  /**
   * Given a geolocation, find the current weather at that location by invoking the NWS API. This
   * method will make real web requests.
   *
   * <p>//* @param the location to find weather at
   *
   * @return the current weather at the given location
   * @throws DatasourceException if there is an issue obtaining the data from the API
   */
  //  @Override
  //  public WeatherData getCurrentWeather(Geolocation loc)
  //      throws DatasourceException, IllegalArgumentException {
  //    return getCurrentWeather(loc.lat(), loc.lon());
  //  }
  //
  //  private static WeatherData getCurrentWeather(double lat, double lon)
  //      throws DatasourceException, IllegalArgumentException {
  //    try {
  //      // Double-check that the coordinates are valid. Yes, this has already
  //      // been checked, in principle, when the caller gave a Geolocation object.
  //      // But this is very cheap, and protects against mistakes I might make later
  //      // (such as making this method public, which would bypass the first check).
  //      if (!Geolocation.isValidGeolocation(lat, lon)) {
  //        throw new IllegalArgumentException("Invalid geolocation");
  //      }
  //
  //      // NWS is not robust to high precision; limit to X.XXXX
  //      lat = Math.floor(lat * 10000.0) / 10000.0;
  //      lon = Math.floor(lon * 10000.0) / 10000.0;
  //
  //      System.out.println("Debug: getCurrentWeather: " + lat + ";" + lon);
  //
  //      GridResponse gridResponse = resolveGridCoordinates(lat, lon);
  //      String gid = gridResponse.properties().gridId();
  //      String gx = gridResponse.properties().gridX();
  //      String gy = gridResponse.properties().gridY();
  //
  //      System.out.println("Debug: gridResponse: " + gid + ";" + gx + ";" + gy);
  //
  //      URL requestURL = new URL("https", "api.weather.gov",
  //          "/gridpoints/" + gid + "/" + gx + "," + gy);
  //      HttpURLConnection clientConnection = connect(requestURL);
  //      Moshi moshi = new Moshi.Builder().build();
  //
  //      // NOTE WELL: THE TYPES GIVEN HERE WOULD VARY ANYTIME THE RESPONSE TYPE VARIES
  //      JsonAdapter<ForecastResponse> adapter = moshi.adapter(ForecastResponse.class).nonNull();
  //
  //      ForecastResponse body = adapter.fromJson(
  //          new Buffer().readFrom(clientConnection.getInputStream()));
  //
  //      System.out.println(body); // records are nice for giving auto toString

  //      clientConnection.disconnect();
  //
  //      // Validity checks for response
  //      if (body == null || body.properties() == null || body.properties().temperature() == null)
  //        throw new DatasourceException("Malformed response from NWS");
  //      if (body.properties().temperature().values().isEmpty())
  //        throw new DatasourceException("Could not obtain temperature data from NWS");
  //
  //      // TODO not well protected, always takes first timestamp of report
  //      return new WeatherData(body.properties().temperature().values().get(0).value());
  //    } catch (IOException e) {
  //      throw new DatasourceException(e.getMessage(), e);
  //    }
  //  }

  public static ACS deserializeACS(String jsonACS) {
    try {
      // Initializes Moshi
      Moshi moshi = new Moshi.Builder().build();

      // Initializes an adapter to an ACS class then uses it to parse the JSON.
      JsonAdapter<ACS> adapter = moshi.adapter(ACS.class);

      ACS ACS = adapter.fromJson(jsonACS);

      return ACS;
    }
    // Returns an empty activity... Probably not the best handling of this error case...
    // Notice an alternative error throwing case to the one done in OrderHandler. This catches
    // the error instead of pushing it up.
    catch (IOException e) {
      e.printStackTrace();
      // return new ACS(resolveStateCode(), countyName, broadband);
    }
    return null;
  }

  ////////////////////////////////////////////////////////////////
  // NWS API data classes. These must be public for Moshi.
  // They are "inner classes"; NWSAPIDataSource.GridResponse, etc.
  ////////////////////////////////////////////////////////////////

  public record ACSResponse(String stateNum, String countyNum, String broadband) {}
  // Note: case matters! "gridID" will get populated with null, because "gridID" != "gridId"
  public record ACSResponseProperties(
      String gridId, String gridX, String gridY, String timeZone, String radarStation) {}

  public record ForecastResponse(String id, ForecastResponseProperties properties) {}

  public record ForecastResponseProperties(
      String updateTime, ForecastResponseTemperature temperature) {}

  public record ForecastResponseTemperature(String uom, List<ForecastResponseTempValue> values) {}

  public record ForecastResponseTempValue(String validTime, double value) {}
}
