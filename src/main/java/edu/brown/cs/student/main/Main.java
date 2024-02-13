package edu.brown.cs.student.main;

// import static com.squareup.moshi.adapters.Iso8601Utils.parse;
import static edu.brown.cs.student.main.CSVParser.parse;
import static edu.brown.cs.student.main.CSVParser.searchCSV;

import java.io.IOException;
import java.util.List;

/** The Main class of our project. This is where execution begins. */
public final class Main {

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) throws IOException {
    new Main(args).run();
  }

  private Main(String[] args) throws IOException {

    if (args.length < 3) {
      System.err.println("Usage: java CSVParser <filename> <searchValue> <columnIdentifier>");
      System.exit(1);
    }

    String filename = args[0];
    String searchValue = args[1];
    int columnIdentifier = Integer.parseInt(args[2]);

    List<String[]> csvData = parse(filename, true);
    if (csvData != null) {
      searchCSV(csvData, searchValue, columnIdentifier);
    }
  }

  private void run() {
    System.out.println(
        "Your horoscope for this project:\n"
            + "Entrust in the Strategy pattern, and it shall give thee the sovereignty to "
            + "decide and the dexterity to change direction in the realm of thy code.");
  }
}
