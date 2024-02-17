package edu.brown.cs.student.main.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CSVParser<T> {

  BufferedReader reader;

  private CreatorFromRow<T> creator = null;

  public List<String> headers;
  private Boolean hasHeaders;

  private String line;

  public CSVParser(Reader reader, CreatorFromRow<T> creator, boolean hasHeaders) {
    this.reader = new BufferedReader(reader);
    this.creator = creator;
    this.hasHeaders = hasHeaders;
    this.headers = new ArrayList<>();
  }

  public List<List<T>> parse() throws FactoryFailureException {
    List<List<T>> data = new ArrayList<>();
    // List<T> = one row

    try {

      line = this.reader.readLine();

      if (this.hasHeaders && line != null) {

        this.headers = parseHelper(line);
      }
      while ((line = this.reader.readLine()) != null) {
        List<String> parsedRow = parseHelper(line);
        List<T> objectsRow = this.creator.create(parsedRow);
        data.add(objectsRow);
      }

      return data;
    } catch (IOException e) {
      throw new FactoryFailureException(
          "Error reading CSV data: " + e.getMessage(), new ArrayList<>());
    }
  }

  // row here is 1 string (ie. "0, Sol, 0, 0, 0")
  public List<String> parseHelper(String row) throws FactoryFailureException {
    List<String> rowList = new ArrayList<String>();
    Pattern regexSplitCSVRow = Pattern.compile(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*(?![^\\\"]*\\\"))");
    List<String> splitList = List.of(regexSplitCSVRow.split(row));
    for (String item : splitList) {
      String trimmedItem = item.trim().replaceAll("^\"|\"$", "").replaceAll("\"\"", "\"");
      rowList.add(trimmedItem);
    }
    return rowList;
  }
}
