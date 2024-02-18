package edu.brown.cs.student.main.datasource.ACS;

public record ACSResponse(String stateName, String countyName, String broadbandValue) {
  /**
   * You can provide a custom constructor for records. We do so here for input-validity checking.
   * Note the lack of arguments! They are defined by the record declaration itself, above.
   */
  public ACSResponse {
    if (!ACSResponse.isValidResponse(countyName, stateName)) {
      throw new IllegalArgumentException("Invalid response");
    }
  }

  // im not sure the range for the county num so that is prob wrong
  public static boolean isValidResponse(String countyName, String stateName) {
    Integer countyNum = Integer.parseInt(countyName);
    Integer stateNum = Integer.parseInt(stateName);
    return countyNum >= 01 && countyNum <= 1000 && stateNum >= 01 && stateNum <= 72;
  }
}
