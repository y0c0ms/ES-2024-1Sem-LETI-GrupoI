package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MainApp extends Application {
    private Scene mainScene;
    private VBox mainMenu;

    @Override
    public void start(Stage primaryStage) {
        // Project name label
        Label projectNameLabel = new Label("ES-2024-1Sem-LETI-GrupoI");
        projectNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        projectNameLabel.setTextFill(Color.BLACK);

        // Create buttons for main menu
        Button btnAdjacencyChecker = new Button("Open Adjacency Checker");
        Button btnMapVisualizer = new Button("Open Map Visualizer");

        // Set actions for buttons
        btnAdjacencyChecker.setOnAction(event -> openAdjacencyChecker());
        btnMapVisualizer.setOnAction(event -> showUnavailableMessage());

        // Main menu layout
        mainMenu = new VBox(10, projectNameLabel, btnAdjacencyChecker, btnMapVisualizer);
        mainMenu.setStyle("-fx-padding: 20;");
        mainMenu.setAlignment(Pos.CENTER); // Align the project name label and menu to the top-left

        // Set up the scene
        mainScene = new Scene(mainMenu, 400, 300);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Property Visualization App");
        primaryStage.show();
    }

    private void openAdjacencyChecker() {
        AdjacencyCheckerGUI adjacencyChecker = new AdjacencyCheckerGUI();
        Button backButton = new Button("Back to Main Menu");
        backButton.setOnAction(event -> showMainMenu());

        VBox layoutWithBackButton = new VBox(backButton, adjacencyChecker.getLayout());
        mainScene.setRoot(layoutWithBackButton);
    }

    private void showUnavailableMessage() {
        // Create a label to show the "feature unavailable" message
        Label unavailableLabel = new Label("This feature is currently not available.");
        unavailableLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        unavailableLabel.setTextFill(Color.RED);

        // Back button to return to the main menu
        Button backButton = new Button("Back to Main Menu");
        backButton.setOnAction(event -> showMainMenu());

        VBox unavailableLayout = new VBox(20, unavailableLabel, backButton);
        unavailableLayout.setAlignment(Pos.CENTER);
        unavailableLayout.setStyle("-fx-padding: 20;");

        // Set the new layout with the message as the root of the scene
        mainScene.setRoot(unavailableLayout);
    }

    // Method to return to the main menu
    public void showMainMenu() {
        mainScene.setRoot(mainMenu);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
