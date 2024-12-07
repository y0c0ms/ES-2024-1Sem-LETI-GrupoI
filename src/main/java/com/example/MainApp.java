package com.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

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
        Button btnGraphOwners = new Button("Grafo de donos");

        btnAdjacencyChecker.setOnAction(_ -> openAdjacencyChecker());
        btnCalculateArea.setOnAction(_ -> openRegionSelector(primaryStage));
        btnGraphOwners.setOnAction(_ -> showGraphOwners());

        mainMenu = new VBox(10, btnAdjacencyChecker, btnCalculateArea, btnGraphOwners);
        mainMenu.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Load the background image
        Image backgroundImage = new Image(getClass().getResourceAsStream("/images.jpeg"));
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        mainMenu.setBackground(new Background(background));

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
        Button btnConcelho = new Button("Concelho");
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

        ScrollPane scrollPane = new ScrollPane(regionButtons);
        scrollPane.setFitToWidth(true);
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

    private void showGraphOwners() {
        CSVReader r = new CSVReader();
        r.readCSV();

        // Create GraphOwners instance and build the owner graph
        GraphOwners go = new GraphOwners(r.createProperties(), r.getOwnersList());
        Graph<Integer, DefaultEdge> g = go.getOwnerGraph();
        Set<Integer> ownerIds = go.getOwnerIds();
        int maxOwnerId = ownerIds.stream().max(Integer::compareTo).orElse(0);

        // Create a TextField for user input
        TextField ownerIdInput = new TextField();
        ownerIdInput.setPromptText("Enter Owner ID (Max: " + maxOwnerId + ")");

        Button showNeighborsButton = new Button("Show Neighbors");
        TextArea resultTextArea = new TextArea();
        resultTextArea.setEditable(false);
        resultTextArea.setWrapText(true);

        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);

        showNeighborsButton.setOnAction(event -> {
            try {
                Integer ownerId = Integer.parseInt(ownerIdInput.getText());
                if (ownerId > maxOwnerId) {
                    errorLabel.setText(
                            "Invalid Owner ID. Please enter a number less than or equal to " + maxOwnerId + ".");
                    resultTextArea.clear();
                } else {
                    errorLabel.setText("");
                    resultTextArea.setText("Neighbors of owner " + ownerId + ": " + g.edgesOf(ownerId));
                }
            } catch (NumberFormatException e) {
                errorLabel.setText("Invalid Owner ID. Please enter a valid number.");
                resultTextArea.clear();
            } catch (NullPointerException e) {
                errorLabel.setText("Owner ID not found.");
                resultTextArea.clear();
            }
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(_ -> showMainMenu());

        VBox layout = new VBox(10, new Label("Enter Owner ID:"), ownerIdInput, showNeighborsButton, errorLabel,
                resultTextArea, backButton);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-alignment: center;");

        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10));

        mainScene.setRoot(scrollPane);
    }

    public void showMainMenu() {
        mainScene.setRoot(mainMenu);
    }

    public static void main(String[] args) {
        launch(args);
    }
}