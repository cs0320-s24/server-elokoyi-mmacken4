package edu.brown.cs.student.main.datasource.ACS;

public class ACS {

  // Note: Each of the fields match the JSON properties exactly... Moshi IS case-sensitive!
  private String state;
  // Note: we make the field 'final' to keep us from overwriting the List itself, not its contents!
  private String county;
  private double broadband;

  /**
   * @param state
   * @param county
   * @param broadband
   */
  public ACS(String state, String county, float broadband) {
    this.state = state;
    this.county = county;
    this.broadband = broadband;
  }

  /**
   * Override the toString to be a little more informative.
   *
   * @return
   */
  @Override
  public String toString() {
    return (this.county + ", " + this.state + "has broadband:" + this.broadband).toString();
  }
}
