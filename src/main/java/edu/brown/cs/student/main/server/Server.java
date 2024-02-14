package edu.brown.cs.student.main.server;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Server {

  public static void main(String[] args) throws IOException {
    if (args.length < 3) {
      System.err.println("Enter the paramaters");
      System.exit(1);
    }

    String filename = args[0];
    String searchValue = args[1];
    int columnIndex = Integer.parseInt(args[2]);

    CSVParser csvParser = new CSVParser(new FileReader(filename));
    List<String[]> csvData = csvParser.parse(filename, true);
    if (csvData != null) {
      csvParser.searchCSV(csvData, searchValue, columnIndex);
    }
  }
}
