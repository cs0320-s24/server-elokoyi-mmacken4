package edu.brown.cs.student.main.data.ACS;

public record ACSResponse(String stateName, String countyName, String broadbandValue) {
    /**
     * You can provide a custom constructor for records. We do so here
     * for input-validity checking. Note the lack of arguments! They are
     * defined by the record declaration itself, above.
     */
    public ACSResponse {
        if(!ACSResponse.isValidResponse(countyName, stateName)) {
            throw new IllegalArgumentException("Invalid response");
        }
    }

    /**
     * Convenience function to convert this geolocation to an API parameter string
     * @return API parameter string, for the NWS API, corresponding to this location
     */
    /*public String toOurServerParams() {
        return "lat="+lat+"&lon="+lon;
    }
*/
    /**
     * Static validity checker for geocoordinates. Since lat and lon are in degrees,
     * we expect them to fall into a particular range. If they do not, the coordinate
     * is invalid.
     *
     * @return true if and only if this is a valid coordinate pair
     */
    // im not sure the range for the county num so that is prob wrong
    public static boolean isValidResponse(String countyName, String stateName) {
        Integer countyNum = Integer.parseInt(countyName);
        Integer stateNum = Integer.parseInt(stateName);
        return countyNum >= 01 && countyNum <= 1000 && stateNum >= 01 && stateNum <= 72;
    }
}
