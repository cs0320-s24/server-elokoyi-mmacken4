package edu.brown.cs.student.main.server;

// import static com.squareup.moshi.adapters.Iso8601Utils.parse;
import static edu.brown.cs.student.main.server.CSVParser.parse;
import static edu.brown.cs.student.main.server.CSVParser.searchCSV;

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
      System.err.println("Enter the paramaters");
      System.exit(1);
    }

    String filename = args[0];
    String searchValue = args[1];
    int columnIndex = Integer.parseInt(args[2]);

    List<String[]> csvData = parse(filename, true);
    if (csvData != null) {
      searchCSV(csvData, searchValue, columnIndex);
    }
  }

  private void run() {
    System.out.println(
        "Your horoscope for this project:\n"
            + "Entrust in the Strategy pattern, and it shall give thee the sovereignty to "
            + "decide and the dexterity to change direction in the realm of thy code.");
  }
}
