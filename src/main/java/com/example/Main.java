package com.example;

public class Main {
    public static void main(String[] args) {
        String csvFilePath = "D:\\ProjEngSoft\\Madeira-Moodle.csv";
        CSVReader csvReader = new CSVReader(csvFilePath);
        csvReader.readCSV();
    }
}