package org.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.opencsv.CSVWriter;

import javax.swing.*;

public class FileConverter {

    public boolean toCSVFile(String filePath) {
        if (filePath.toLowerCase().endsWith(".csv")) {
            return true;
        } else if (filePath.toLowerCase().endsWith(".xls") || filePath.toLowerCase().endsWith(".xlsx")) {
            return convertExcelToCSV(filePath);
        }
        return false;
    }

    private boolean convertExcelToCSV(String excelFilePath) {
        try (Workbook workbook = WorkbookFactory.create(new File(excelFilePath))) {
            Sheet sheet = workbook.getSheetAt(0);
            Path csvFilePath = Paths.get(excelFilePath.substring(0, excelFilePath.lastIndexOf(".")) + ".csv");

            try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath.toFile()))) {
                for (Row row : sheet) {
                    int numberOfCells = row.getLastCellNum();
                    String[] rowData = new String[numberOfCells];
                    for (int cellIndex = 0; cellIndex < numberOfCells; cellIndex++) {
                        Cell cell = row.getCell(cellIndex);
                        rowData[cellIndex] = getCellValueAsString(cell);
                    }
                    writer.writeNext(rowData);
                }
            }
            System.out.println("Excel file converted to CSV: " + csvFilePath.toString());
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return Boolean.toString(cell.getBooleanCellValue());
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return Double.toString(cell.getNumericCellValue());
                }
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }


}

