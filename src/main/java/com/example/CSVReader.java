package com.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
    private String csvFilePath = "src\\main\\java\\com\\example\\Madeira-Moodle.csv";
    private List<CSVRecord> data;

    // CSVReader constructor
    public CSVReader() {
        data = new ArrayList<>();
    }

    // Reads CSV and stores the information in the `data` list
    public void readCSV() {
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

    // Converts CSV data into a list of Property objects
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

                // Create a Property instance
                Property property = new Property(objectid, par_id, par_num, shapeArea, shapeLength, geometryStr, owner);
                properties.add(property);

            } catch (NumberFormatException e) {
                System.err.println("Error parsing numeric values in CSV: " + e.getMessage());
            }
        }

        return properties;
    }

    // Helper method to replace commas with dots and parse the number
    private double parseDouble(String value) {
        // Replace commas with dots
        String normalizedValue = value.replace(',', '.');
        return Double.parseDouble(normalizedValue);
    }

    // Retrieves a record by its row index (for debugging purposes)
    public CSVRecord getRecord(int index) {
        if (index < 0 || index >= data.size()) {
            System.err.println("Invalid index");
            return null;
        }
        return data.get(index);
    }

    // Retrieves a record by its OBJECTID
    public CSVRecord getRecordByOBJECTID(int objId) {
        for (CSVRecord record : data) {
            if (Integer.parseInt(record.get("OBJECTID")) == objId) {
                return record;
            }
        }
        System.out.println("Object ID not found!");
        return null;
    }

    // Debug method to print CSV contents
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
                System.out.println("---------------------------");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Main method for testing/debugging
    public static void main(String[] args) {
        CSVReader test = new CSVReader();

        // Read CSV and create Property objects
        test.readCSV();
        List<Property> properties = test.createProperties();

        // Example: Print the properties created
        for (Property property : properties) {
            System.out.println("Property ID: " + property.getObjectId() + ", Owner: " + property.getOwner());
        }
    }
}