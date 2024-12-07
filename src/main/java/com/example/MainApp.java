package com.example;

import javafx.application.Application;
import javafx.geometry.Insets;
//import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
/* import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight; */
import javafx.stage.Stage;

import java.util.List;

public class MainApp extends Application {
    private Scene mainScene;
    private VBox mainMenu;

    private List<Property> properties;
    private AreaCalculator areaCalculator;

    @Override
    public void start(Stage primaryStage) {
        CSVReader csvReader = new CSVReader();
        csvReader.chooseCSVFile();
        csvReader.readCSV();
        properties = csvReader.createProperties();
        areaCalculator = new AreaCalculator(properties);

        Button btnAdjacencyChecker = new Button("Open Adjacency Checker");
        Button btnCalculateArea = new Button("Calcular área região");

        btnAdjacencyChecker.setOnAction(_ -> openAdjacencyChecker());
        btnCalculateArea.setOnAction(_ -> openRegionSelector(primaryStage));

        mainMenu = new VBox(10, btnAdjacencyChecker, btnCalculateArea);
        mainMenu.setStyle("-fx-padding: 20; -fx-alignment: center;");

        mainScene = new Scene(mainMenu, 400, 300);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Property Visualization App");
        primaryStage.show();
    }

    private void openAdjacencyChecker() {
        AdjacencyCheckerGUI adjacencyChecker = new AdjacencyCheckerGUI();
        Button backButton = new Button("Back to Main Menu");
        backButton.setOnAction(_ -> showMainMenu());

        VBox layoutWithBackButton = new VBox(backButton, adjacencyChecker.getLayout());
        mainScene.setRoot(layoutWithBackButton);
    }


    private void openRegionSelector(Stage stage) {
        Button btnFreguesia = new Button("Freguesia");
        Button btnConcelho = new Button("Cconcelho");
        Button btnDistrito = new Button("Distrito");
        Button backButton = new Button("Back to Main Menu");

        btnFreguesia.setOnAction(_ -> openRegionButtons("Freguesia", stage));
        btnConcelho.setOnAction(_ -> openRegionButtons("Concelho", stage));
        btnDistrito.setOnAction(_ -> openRegionButtons("Distrito", stage));
        backButton.setOnAction(_ -> showMainMenu());

        VBox regionMenu = new VBox(10, btnFreguesia, btnConcelho, btnDistrito, backButton);
        regionMenu.setPadding(new Insets(20));
        regionMenu.setStyle("-fx-alignment: center;");
        mainScene.setRoot(regionMenu);
    }

    private void openRegionButtons(String type, Stage stage) {
        List<String> uniqueRegions = areaCalculator.getUniqueRegions(type);

        VBox regionButtons = new VBox(10);
        regionButtons.setPadding(new Insets(20));

        Label titleLabel = new Label("Select a " + type + ":");
        regionButtons.getChildren().add(titleLabel);

        // Add a checkbox or toggle for "Group by Owner"
        Label groupByOwnerLabel = new Label("Consider adjacent properties with the same owner?");
        CheckBox groupByOwnerCheckbox = new CheckBox();
        groupByOwnerCheckbox.setSelected(false);

        regionButtons.getChildren().addAll(groupByOwnerLabel, groupByOwnerCheckbox);

        for (String region : uniqueRegions) {
            Button regionButton = new Button(region);
            regionButton.setOnAction(_ -> {
                boolean groupByOwner = groupByOwnerCheckbox.isSelected();
                showAverageArea(type, region, stage, groupByOwner);
            });
            regionButtons.getChildren().add(regionButton);
        }

        Button backButton = new Button("Back");
        backButton.setOnAction(_ -> openRegionSelector(stage));
        regionButtons.getChildren().add(backButton);

        // Wrap the VBox in a ScrollPane for scrolling functionality
        ScrollPane scrollPane = new ScrollPane(regionButtons);
        scrollPane.setFitToWidth(true); // Makes the scroll pane width match the buttons
        scrollPane.setPadding(new Insets(10));

        mainScene.setRoot(scrollPane);
    }

    private void showAverageArea(String type, String region, Stage stage, boolean groupByOwner) {
        double averageArea;
        if (groupByOwner) {
            averageArea = areaCalculator.calculateAverageAreaWithGroups(type, region);
        } else {
            averageArea = areaCalculator.calculateAverageArea(type, region);
        }

        Label resultLabel = new Label("Average Area for " + type + " \"" + region + "\": " + averageArea);
        Button backButton = new Button("Back");
        backButton.setOnAction(_ -> openRegionButtons(type, stage));

        VBox resultLayout = new VBox(10, resultLabel, backButton);
        resultLayout.setPadding(new Insets(20));
        resultLayout.setStyle("-fx-alignment: center;");
        mainScene.setRoot(resultLayout);
    }

    /*
     * private void showUnavailableMessage() {
     * // Create a label to show the "feature unavailable" message
     * Label unavailableLabel = new
     * Label("This feature is currently not available.");
     * unavailableLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
     * unavailableLabel.setTextFill(Color.RED);
     * 
     * // Back button to return to the main menu
     * Button backButton = new Button("Back to Main Menu");
     * backButton.setOnAction(_ -> showMainMenu());
     * 
     * VBox unavailableLayout = new VBox(20, unavailableLabel, backButton);
     * unavailableLayout.setAlignment(Pos.CENTER);
     * unavailableLayout.setStyle("-fx-padding: 20;");
     * 
     * // Set the new layout with the message as the root of the scene
     * mainScene.setRoot(unavailableLayout);
     * }
     */

    public void showMainMenu() {
        mainScene.setRoot(mainMenu);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
