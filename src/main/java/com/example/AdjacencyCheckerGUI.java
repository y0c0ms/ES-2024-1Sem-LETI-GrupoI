package com.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.List;

public class AdjacencyCheckerGUI extends Application {
    private List<Property> properties;

    @Override
    public void start(Stage primaryStage) {
        // Load the properties data
        CSVReader csvReader = new CSVReader();
        csvReader.readCSV();
        properties = csvReader.createProperties();

        // Set up the GUI components
        primaryStage.setTitle("Property Adjacency Checker");

        Label label1 = new Label("Enter OBJECTID 1:");
        TextField objectIdField1 = new TextField();

        Label label2 = new Label("Enter OBJECTID 2:");
        TextField objectIdField2 = new TextField();

        Button checkButton = new Button("Check Adjacency");
        Label resultLabel = new Label();

        // Action when the check button is clicked
        checkButton.setOnAction(event -> {
            String idText1 = objectIdField1.getText();
            String idText2 = objectIdField2.getText();
            try {
                int objectId1 = Integer.parseInt(idText1);
                int objectId2 = Integer.parseInt(idText2);
                boolean areAdjacent = checkAdjacency(objectId1, objectId2);
                resultLabel.setText("Are properties " + objectId1 + " and " + objectId2 + " adjacent? " + areAdjacent);
            } catch (NumberFormatException e) {
                resultLabel.setText("Invalid OBJECTID! Please enter valid integers.");
            }
        });

        // Layout the components in a GridPane
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(8);
        grid.setHgap(10);

        grid.add(label1, 0, 0);
        grid.add(objectIdField1, 1, 0);
        grid.add(label2, 0, 1);
        grid.add(objectIdField2, 1, 1);
        grid.add(checkButton, 1, 2);
        grid.add(resultLabel, 1, 3);

        Scene scene = new Scene(grid, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to check adjacency based on OBJECTIDs
    private boolean checkAdjacency(int objectId1, int objectId2) {
        Property property1 = null;
        Property property2 = null;

        // Find the properties by OBJECTID
        for (Property property : properties) {
            if (property.getObjectId() == objectId1) {
                property1 = property;
            } else if (property.getObjectId() == objectId2) {
                property2 = property;
            }
            if (property1 != null && property2 != null) {
                break;
            }
        }

        // Return adjacency result if both properties are found
        if (property1 != null && property2 != null) {
            return property1.isAdjacent(property2);
        } else {
            System.out.println("One or both OBJECTIDs not found.");
            return false;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}