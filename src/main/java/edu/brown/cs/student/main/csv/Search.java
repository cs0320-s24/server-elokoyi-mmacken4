package edu.brown.cs.student.main.csv;

import java.io.BufferedReader;
import java.util.List;

public class Search<T> {

  private CSVParser<T> parser;
  private CreatorFromRow<T> creator;

  public Search(CSVParser<T> parser, CreatorFromRow<T> creator) {
    this.parser = parser;
    this.creator = creator;
  }

  public void search(String filename, String value, String columnID)
      throws FactoryFailureException {
    try {
      if (filename != null) {
        this.parser.reader = new BufferedReader(this.parser.reader);

        List<List<T>> parsedData = this.parser.parse();

        int index = -1; // column index

        for (int i = 0; i < this.parser.headers.size(); i++) {
          if (this.parser.headers.get(i).equals(columnID)) {
            index = i;
            break;
          }
        }

        if (index == -1) {
          System.err.println("Column " + columnID + " not found");
          return;
        }

        boolean found = false;
        for (List<T> row : parsedData) {
          if (index < row.size()) {
            T columnValue = row.get(index);
            if (columnValue.toString().toLowerCase().equals(value.toLowerCase())) {
              found = true;
              System.out.println(row);
              // return row;
            }
          }
        }
        if (!found) {
          System.out.println("No matching rows found");
        }
      }
    } catch (FactoryFailureException e) {
      System.err.println("Error parsing CSV: " + e.getMessage());
    }
  }
}
