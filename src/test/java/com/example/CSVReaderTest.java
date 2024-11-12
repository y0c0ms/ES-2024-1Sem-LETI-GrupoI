package com.example;

import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CSVReaderTest {
    private CSVReader csvReader;

    @BeforeEach
    public void setUp() {
        csvReader = new CSVReader();
        csvReader.readCSV();
    }

    @Test
    public void testReadCSV() {
        List<Property> properties = csvReader.createProperties();
        assertNotNull(properties);
        assertTrue(properties.size() > 0, "Properties should be loaded from CSV file.");
    }

    @Test
    public void testGetRecordByOBJECTID() {
        CSVRecord record = csvReader.getRecordByOBJECTID(1);
        assertNotNull(record);
        assertEquals("1", record.get("OBJECTID"));
    }

    @Test
    public void testInvalidOBJECTID() {
        CSVRecord record = csvReader.getRecordByOBJECTID(999999);
        assertNull(record, "Record should be null for an invalid OBJECTID.");
    }
}
