package com.example;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class LeitorCSV {

    public List<Propriedade> lerCSV(String filePath) {
        List<Propriedade> propriedades = new ArrayList<>();

        try (Reader reader = new FileReader(filePath);
                CSVParser csvParser = new CSVParser(reader,
                        CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader())) {

            for (CSVRecord csvRecord : csvParser) {
                String objectId = csvRecord.get("OBJECTID");
                String geometry = csvRecord.get("geometry");
                // Add other fields as needed

                Propriedade propriedade = new Propriedade(objectId, geometry);
                propriedades.add(propriedade);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Error parsing CSV file: " + e.getMessage());
        }

        return propriedades;
    }
}