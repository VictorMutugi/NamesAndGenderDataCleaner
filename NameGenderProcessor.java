package org.example;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class NameGenderProcessor {

    String inputFile;
    String outputFile;

    public NameGenderProcessor(String inputFile) {
        this.inputFile = inputFile;
        this.outputFile = this.inputFile;
    }

    public boolean processGender() {
        // Define the path to your CSV files
        String trainingFile = "C:\\Users\\southern star\\Documents\\NamesAndGenderDataCleanerExcelFiles\\name_gender_dataset.csv";

        // Create a HashMap to store the processed names and genders
        Map<String, String> nameGenderMap = new HashMap<>();

        // Read the input file and process each name
        try (BufferedReader brInput = new BufferedReader(new FileReader(inputFile))) {
            String inputLine;
            while ((inputLine = brInput.readLine()) != null) {
                String name = inputLine.trim().toLowerCase();
                String gender = getGenderFromTrainingFile(trainingFile, name);
                nameGenderMap.put(name, gender);
                // Debugging information
                System.out.println("Processed name: " + name + ", Assigned gender: " + gender);
            }
            System.out.println("Gender assignment complete.");

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error processing input file.");
            return false;
        }

        // Write the updated names and genders back to the input file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
            for (Map.Entry<String, String> entry : nameGenderMap.entrySet()) {
                String name = entry.getKey();
                String gender = entry.getValue();
                bw.write(name + "," + gender);
                bw.newLine();
            }
            System.out.println("Output written to file: " + outputFile);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error writing to output file.");
            return false;
        }

        return true;
    }

    // Method to read the training file and get the gender for a given name
    private String getGenderFromTrainingFile(String trainingFile, String name) {
        try (BufferedReader brTraining = new BufferedReader(new FileReader(trainingFile))) {
            String line;
            boolean isFirstLine = true;
            while ((line = brTraining.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] values = line.split(",");
                if (values.length >= 2) {
                    String trainingName = values[0].trim().toLowerCase();
                    String gender = values[1].trim();

                    // Check each word in the input name
                    String[] nameParts = name.split("\\s+");
                    for (String part : nameParts) {
                        if (trainingName.equals(part)) {
                            return gender;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error reading training file: " + trainingFile);
        }
        return "Unknown";
    }



}
