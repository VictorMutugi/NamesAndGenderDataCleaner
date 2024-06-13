package org.example;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataProcessor {
    private String csvFile;


    public DataProcessor(String csvFile){
        this.csvFile = csvFile;
    }

    public void processData() {
        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            String[] headers = reader.readNext(); // Read headers
            List<String[]> rows = new ArrayList<>();

            String[] row;
            while ((row = reader.readNext()) != null) {
                rows.add(row);
            }

            // Find column indices for 'name' and 'gender'
            int nameIndex = findColumnIndex(headers, "Name");
            int genderIndex = findColumnIndex(headers, "Gender");

            // Process each row
            for (String[] rowData : rows) {
                String name = rowData[nameIndex];
                String gender = rowData[genderIndex];

                // Example: Print name and gender
                System.out.println("Name: " + name + ", Gender: " + gender);

                // Add your PMML Random Forest classification logic here
            }

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    // Helper method to find column index by header name
    private static int findColumnIndex(String[] headers, String columnName) {
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].equalsIgnoreCase(columnName)) {
                return i;
            }
        }
        return -1; // Column not found
    }
}