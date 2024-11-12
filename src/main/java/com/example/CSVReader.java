package com.example;

import java.util.ArrayList;
import java.io.IOException;
import java.util.List;
import java.io.Reader;
import java.io.FileReader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CSVReader {
    private String csvFilePath = "src\\main\\java\\com\\example\\Madeira-Moodle.csv";
    private List<CSVRecord> data;

    //CSVReader constructor
    public CSVReader() {
        data = new ArrayList<CSVRecord>();
    }


       //Method that stores de information read in the csv file in the variable data, making it accessible whenever we want to use it without having to read the file again
       public void readCSV() {
        try (Reader reader = new FileReader(csvFilePath);
                CSVParser csvParser = new CSVParser(reader,
                        CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader())) {

            // Adding the read values to the variable data
            for (CSVRecord csvRecord : csvParser) {
                data.add(csvRecord);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public CSVRecord getRecord(int index){
        if(index <= 0 || index >= data.size()){
            System.err.println("Invalid index");
            return null;
        }
        return data.get(index-1);
    }

    public String getPolygon(CSVRecord record){
        return record.get("geometry");
    }
    //getting a record by its id
    public CSVRecord getRecordByOBJECTID(int objId){
        if(objId <= 0 || objId >= data.size()){
            System.out.println("Invalid object id! Id doesn't exist");
            return null;
        }
        return data.get(objId-1);
    }


    //Debug method used to see if the file is being read correctly
    public void printCSV(){
        try (Reader reader = new FileReader(csvFilePath);
                CSVParser csvParser = new CSVParser(reader,
                        CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader())) {

            // Get the header map
            List<String> headers = csvParser.getHeaderNames();
            System.out.println("Headers: " + headers);

            // Outputting the records
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
                System.out.println("OBJECTID: " + objectId);
                System.out.println("PAR_ID: " + parId);
                System.out.println("PAR_NUM: " + parNum);
                System.out.println("Shape_Length: " + shapeLength);
                System.out.println("Shape_Area: " + shapeArea);
                System.out.println("geometry: " + geometry);
                System.out.println("OWNER: " + owner);
                System.out.println("---------------------------");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //main for debugging the class
    public static void main(String [] args){
        CSVReader test = new CSVReader();
        //printCSV() its working
        //test.printCSV()
        
        //teste
        //readCSB() its working
        test.readCSV();

        //getRecordByOBJECTID() its working
        System.out.println(test.getRecordByOBJECTID(1).get("PAR_ID")); //getting the PAR_ID of the object with an id 16999
    }
 
}