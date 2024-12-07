package com.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;

/**
 * This class provides functionality for reading and processing data from a CSV
 * file.
 * It parses the CSV data and organizes it into a list of Property objects and a
 * map
 * that associates owners with their respective properties.
 */
public class CSVReader {
    private String csvFilePath = "src\\main\\java\\com\\example\\Madeira-Moodle-1.1.csv";
    private List<CSVRecord> data;
    private Map<Integer, List<Property>> ownersPropertyList;

    /**
     * Constructor for the CSVReader class.
     * Initializes the data list and ownersPropertyList map.
     */
    public CSVReader() {
        data = new ArrayList<>();
        ownersPropertyList = new HashMap<>();
    }

    public void chooseCSVFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select CSV File");
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            csvFilePath = selectedFile.getAbsolutePath();
        }
    }

    /**
     * Reads the CSV file and stores the records in the data list.
     * Uses Apache Commons CSV to parse the file.
     */
    public void readCSV() {
        if (csvFilePath == null || csvFilePath.isEmpty()) {
            System.out.println("CSV file path is not set. Please choose a CSV file first.");
            return;
        }

        try (Reader reader = new FileReader(csvFilePath);
                CSVParser csvParser = new CSVParser(reader,
                        CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader())) {

            // Adding the read values to the `data` list
            for (CSVRecord csvRecord : csvParser) {
                data.add(csvRecord);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts the CSV data into a list of Property objects.
     * 
     * @return A list of Property objects created from the CSV data.
     */
    public List<Property> createProperties() {
        List<Property> properties = new ArrayList<>();

        for (CSVRecord record : data) {
            try {
                int objectid = Integer.parseInt(record.get("OBJECTID"));
                double par_id = parseDouble(record.get("PAR_ID"));
                double par_num = parseDouble(record.get("PAR_NUM"));
                double shapeArea = parseDouble(record.get("Shape_Area"));
                double shapeLength = parseDouble(record.get("Shape_Length"));
                String geometryStr = record.get("geometry");
                int owner = Integer.parseInt(record.get("OWNER"));
                String freguesia = record.get("Freguesia");
                String municipio = record.get("Municipio");
                String ilha = record.get("Ilha");

                // Create a Property instance
                Property property = new Property(objectid, par_id, par_num, shapeArea, shapeLength, geometryStr, owner,
                        freguesia, municipio, ilha);
                properties.add(property);

                // Populate ownersPropertyList map
                if (!ownersPropertyList.containsKey(property.getOwner())) {
                    List<Property> list = new ArrayList<>();
                    list.add(property);
                    ownersPropertyList.put(property.getOwner(), list);
                } else {
                    ownersPropertyList.get(property.getOwner()).add(property);
                }

            } catch (NumberFormatException e) {
                System.err.println("Error parsing numeric values in CSV: " + e.getMessage());
            }
        }

        return properties;
    }

    /**
     * Helper method to replace commas with dots and parse a string as a double.
     * 
     * @param value The string value to be parsed.
     * @return The parsed double value.
     */
    private double parseDouble(String value) {
        String normalizedValue = value.replace(',', '.');
        return Double.parseDouble(normalizedValue);
    }

    /**
     * Retrieves a record by its row index.
     * 
     * @param index The index of the record to retrieve.
     * @return The CSVRecord at the specified index, or null if the index is
     *         invalid.
     */
    public CSVRecord getRecord(int index) {
        if (index < 0 || index >= data.size()) {
            System.err.println("Invalid index");
            return null;
        }
        return data.get(index);
    }

    /**
     * Retrieves the map of owners and their associated properties.
     * 
     * @return A map where keys are owner IDs and values are lists of properties
     *         owned by them.
     */
    public Map<Integer, List<Property>> getOwnersList() {
        return ownersPropertyList;
    }

    /**
     * Retrieves a record by its OBJECTID.
     * 
     * @param objId The OBJECTID of the record to retrieve.
     * @return The CSVRecord with the specified OBJECTID, or null if not found.
     */
    public CSVRecord getRecordByOBJECTID(int objId) {
        for (CSVRecord record : data) {
            if (Integer.parseInt(record.get("OBJECTID")) == objId) {
                return record;
            }
        }
        System.out.println("Object ID not found!");
        return null;
    }

    /**
     * Prints the contents of the CSV file to the console for debugging purposes.
     */
    public void printCSV() {
        try (Reader reader = new FileReader(csvFilePath);
                CSVParser csvParser = new CSVParser(reader,
                        CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader())) {

            // Print headers
            List<String> headers = csvParser.getHeaderNames();
            System.out.println("Headers: " + headers);

            // Print each record
            for (CSVRecord csvRecord : csvParser) {
                System.out.println("Record No - " + csvRecord.getRecordNumber());
                System.out.println("OBJECTID: " + csvRecord.get("OBJECTID"));
                System.out.println("PAR_ID: " + csvRecord.get("PAR_ID"));
                System.out.println("PAR_NUM: " + csvRecord.get("PAR_NUM"));
                System.out.println("Shape_Length: " + csvRecord.get("Shape_Length"));
                System.out.println("Shape_Area: " + csvRecord.get("Shape_Area"));
                System.out.println("geometry: " + csvRecord.get("geometry"));
                System.out.println("OWNER: " + csvRecord.get("OWNER"));
                System.out.println("Freguesia: " + csvRecord.get("Freguesia"));
                System.out.println("Municipio: " + csvRecord.get("Municipio"));
                System.out.println("Ilha: " + csvRecord.get("Ilha"));
                System.out.println("---------------------------");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Main method for testing the CSVReader class.
     * 
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        CSVReader csvReader = new CSVReader();
        csvReader.readCSV();

        // Create the list of Property objects
        List<Property> properties = csvReader.createProperties();

        // Print all loaded properties
        for (Property property : properties) {
            System.out.println(property);
        }
    }
}
