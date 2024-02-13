package edu.brown.cs.student.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CSVParser {

  private static BufferedReader reader;
  // private final CreatorFromRow<T> creator;

  public CSVParser(Reader reader) {
    this.reader = new BufferedReader(reader);
    // this.create = creator;
  }

  public static List<String[]> parse(String filename, boolean hasHeaders) throws IOException {
    List<String[]> rows = new ArrayList<>();

    if (hasHeaders) {
      reader.readLine(); // Skip headers
    }

    String line;
    while ((line = reader.readLine()) != null) {
      String[] columns = line.split(",");
      for (int i = 0; i < columns.length; i++) {
        columns[i] = columns[i].trim();
      }
      //        try {
      //          T object = creator.create(List.of(columns));
      //          objects.add(object);
      //        } catch (FactoryFailureException e) {
      //          // Handle the exception as appropriate
      //          System.err.println("Factory failure: " + e.getMessage());
      //        }
      rows.add(columns);
      //   System.out.println("Columns: " + Arrays.toString(columns));
    }

    return rows;
  }

  public static void searchCSV(List<String[]> csvData, String searchValue, int columnIdentifier) {
    for (String[] row : csvData) {
      if (columnIdentifier < row.length) {
        String columnValue = row[columnIdentifier];
        //   System.out.println("Column Value: " + columnValue);
        if (columnValue.contains(searchValue)) {
          System.out.println(String.join(",", row));
        }
        if (!columnValue.contains(searchValue)) {
          System.out.println("That search value was not found in this file! Sorry!");
        }
      }
    }
  }
}

//  public
//  void csvRead(String filePath){
//
//    try {
//      Scanner scanner = new Scanner(new File(filePath));
//      scanner.useDelimiter(",");
//      while (scanner.hasNext()){
//        System.out.println(scanner.next() + " ");
//      }
//
//      scanner.close();
//
//    } catch (FileNotFoundException e) {
//      throw new RuntimeException(e);
//    }
//
//  }

//  public static void parse(String[] args) {
//    String filePath = new File("").getPath();
//  }
// }
