package edu.brown.cs.student.main.csv;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public class Search<T> {

  private CSVParser<T> parser;
  private CreatorFromRow<T> creator;

  public List<List<T>> searchResults;

  public Search(CSVParser<T> parser, CreatorFromRow<T> creator) {
    this.parser = parser;
    this.creator = creator;
    this.searchResults = new ArrayList<>();
  }

  public void search(String filename, String value, String columnID)
      throws FactoryFailureException {
    try {
      if (filename != null) {
        this.parser.reader = new BufferedReader(this.parser.reader);

        List<List<T>> parsedData = this.parser.parse();

        boolean columnIDGiven = (columnID == null || columnID.isEmpty());

        for (List<T> row : parsedData) {
          if (columnIDGiven) {
            // search through all columns if columnID isn't provided
            for (T item : row) {
              if (item.toString().equals(value)) {
                searchResults.add(row);
                break; // match found in row, no need to check further
              }
            }
          } else {
            // search in specific column
            int index = getColumnIndex(columnID);
            if (index != -1 && row.get(index).equals(value)) {
              searchResults.add(row);
            }
          }
        }
      }
    } catch (FactoryFailureException e) {
      System.err.println("Error parsing CSV: " + e.getMessage());
    }
  }

  private int getColumnIndex(String columnID) throws IllegalArgumentException {
    int index = -1;
    try {
      index = Integer.parseInt(columnID);
    } catch (NumberFormatException e) {
      // columnID is not a number, treat it as a header name
      index = this.parser.headers.indexOf(columnID);
    }
    if (index < 0 || index >= this.parser.headers.size()) {
      throw new IllegalArgumentException("Invalid column index or name.");
    }
    return index;
  }
}
