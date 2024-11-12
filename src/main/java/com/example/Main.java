package com.example;

import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        CSVReader csvReader = new CSVReader();
        csvReader.readCSV();
        // csvReader.printCSV(); //Outputs the readings to the console
        Application.launch(AdjacencyCheckerGUI.class, args);
        // Application.launch(PropertyMapVisualizer.class, args);
    }
}