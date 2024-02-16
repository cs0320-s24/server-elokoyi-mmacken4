package edu.brown.cs.student.main.csv;

/** The Main class of our project. This is where execution begins. */
// public final class Main {
//  private final String[] args;
//
////  /**
//   * The initial method called when execution begins.
//   *
//   * @param args An array of command line arguments
//   */
////  public static void main(String[] args) {
//    new Main(args).run();
//  }
//
//  private Main(String[] args) {
//    this.args = args;
//  }
//
//  private void run() {
//    if (args.length < 3) {
//      System.err.println("Usage: java Main <filename> <search_value> <columnID>");
//      return;
//    }
//
//    String filename = args[0];
//    String value = args[1];
//    String columnID = args[2];
//
//    try {
//      Creator<String> creator = new Creator<>();
//      CSVParser<String> csvParser = new CSVParser<>(new FileReader(filename), creator, true);
//      Search<String> search = new Search<>(csvParser, creator);
//      search.search(filename, value, columnID);
//    } catch (Exception e) {
//      System.out.println(filename);
//      System.err.println("Error: " + e.getMessage());
//    }
//  }
// }
