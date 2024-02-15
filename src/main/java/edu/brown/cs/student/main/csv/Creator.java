package edu.brown.cs.student.main.csv;

import java.util.ArrayList;
import java.util.List;

public class Creator<T> implements CreatorFromRow<T> {
  @Override
  public List<T> create(List<String> row) throws FactoryFailureException {
    List<T> objectRow = new ArrayList<T>();
    for (String item : row) {
      objectRow.add((T) item);
    }
    return objectRow;
  }
}
