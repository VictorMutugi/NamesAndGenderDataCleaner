package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;


public class Controller {

    private GUI gui;

    private String filePath;

    public Controller(GUI gui) {
        this.gui = gui;
        FileConverter fileConverter = new FileConverter();

        // Link ActionListener to the JButton in View
        gui.getJButton1().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Action to perform when JButton is clicked
                String filePath = gui.getJTextField1().getText().trim(); // Get file path from JTextField

                // Remove surrounding quotes if they exist
                if (filePath.startsWith("\"") && filePath.endsWith("\"")) {
                    filePath = filePath.substring(1, filePath.length() - 1);
                }

                if (isValidFile(filePath))  {
                    System.out.println("Valid file path: " + filePath);

                    // Perform further actions here based on valid file input
                    try {
                        System.out.println(fileConverter.toCSVFile(filePath));
                        if (fileConverter.toCSVFile(filePath)) {
                            System.out.println("CSV file detected.");

                            if (containsNameAndGenderColumns(filePath)) {
                                System.out.println("File contains 'Name' and 'Gender' columns.");

                                // Proceed with data cleaning or PMML model handling
                                DataProcessor dataProcessor = new DataProcessor(filePath);


                            } else {
                                System.out.println("File does not contain 'Name' and 'Gender' columns.");
                                JOptionPane.showMessageDialog(gui, "The selected CSV file does not contain 'Name' and 'Gender' columns.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            System.out.println("Selected file is either not an Excel file or is open on Excel ");
                            JOptionPane.showMessageDialog(gui, "Please select a valid Excel file or make sure that the file is not open on Excel", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                } else {
                    System.out.println("Invalid file path: " + filePath);
                    JOptionPane.showMessageDialog(gui, "Invalid file path. Please enter a valid file path.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                gui.getJTextField1().setText("");
            }
        });

    }

    // Method to validate if the input string is a valid file path
    private boolean isValidFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }

        File file = new File(filePath);
        return file.exists() && file.isFile();
    }

    // Method to check if the file is a CSV file
    private boolean isCSVFile(String filePath) {
        return filePath.toLowerCase().endsWith(".csv");
    }

    // Method to check if the CSV file contains 'Name' and 'Gender' columns
    private boolean containsNameAndGenderColumns(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Read the first line (header)
            String headerLine = reader.readLine();
            if (headerLine == null) {
                System.out.println("Header line is null.");
                return false;
            }

            // Debug print to show the header line read
            System.out.println("Header line: " + headerLine);

            // Split the header line by comma to check column names
            String[] columns = headerLine.split(",");

            // Debug print to show the columns found
            System.out.println("Columns found: " + Arrays.toString(columns));

            // Check if 'Name' and 'Gender' columns exist
            boolean hasNameColumn = false;
            boolean hasGenderColumn = false;

            for (String column : columns) {
                String trimmedColumn = column.trim();
                if (trimmedColumn.equalsIgnoreCase("Name")) {
                    hasNameColumn = true;
                }
                if (trimmedColumn.equalsIgnoreCase("Gender")) {
                    hasGenderColumn = true;
                }
            }

            // Debug print to show the results of the checks
            System.out.println("Has Name column: " + hasNameColumn);
            System.out.println("Has Gender column: " + hasGenderColumn);

            return hasNameColumn && hasGenderColumn;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


public static void main(String[] args) {
        GUI gui = new GUI();
        Controller controller = new Controller(gui);
        gui.setVisible(true);
    }
}

/*

public JButton getJButton1() {
        return jButton1;
    }

    public JTextField getJTextField1() {
        return jTextField1;
    }

 */