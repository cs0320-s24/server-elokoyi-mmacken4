package edu.brown.cs.student.main.endpoints;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.data.ACS.ACS;
import edu.brown.cs.student.main.data.ACS.ACSAPIUtilities;
import edu.brown.cs.student.main.data.ACS.ACSResponse;
import spark.Route;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

import spark.Request;
import spark.Response;


public class LoadCSVHandler implements Route {
    private ACSResponse data;

    public LoadCSVHandler(ACSResponse data) {
        this.data = data;
    }

    public List<List<String>> loadcsv(String filepath) {
        try {
            List<List<String>> data = null;
            BufferedReader br = new BufferedReader(new FileReader(filepath));
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                List<String> row = new ArrayList<>();
                for (String value : values) {
                    row.add(value.trim());
                }
                data.add(row);
            }
            return data;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
