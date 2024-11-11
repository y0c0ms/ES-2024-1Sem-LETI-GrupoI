package com.example;

import java.io.IOException;
import java.util.List;
import java.io.Reader;
import java.io.FileReader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CSVReader {
    public static void main(String[] args) {
        String csvFilePath = "D:\\ProjEngSoft\\Madeira-Moodle.csv";

        try (Reader reader = new FileReader(csvFilePath);
                CSVParser csvParser = new CSVParser(reader,
                        CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader())) {

            // Get the header map
            List<String> headers = csvParser.getHeaderNames();
            System.out.println("Headers: " + headers);

            // Iterate through the records
            for (CSVRecord csvRecord : csvParser) {
                // Accessing values by the names assigned to each column
                String objectId = csvRecord.get("OBJECTID");
                String parId = csvRecord.get("PAR_ID");
                String parNum = csvRecord.get("PAR_NUM");
                String shapeLength = csvRecord.get("Shape_Length");
                String shapeArea = csvRecord.get("Shape_Area");
                String geometry = csvRecord.get("geometry");
                String owner = csvRecord.get("OWNER");

                System.out.println("Record No - " + csvRecord.getRecordNumber());
                System.out.println("---------------");
                System.out.println("OBJECTID: " + objectId);
                System.out.println("PAR_ID: " + parId);
                System.out.println("PAR_NUM: " + parNum);
                System.out.println("Shape_Length: " + shapeLength);
                System.out.println("Shape_Area: " + shapeArea);
                System.out.println("geometry: " + geometry);
                System.out.println("OWNER: " + owner);
                System.out.println("---------------\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}