package com.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

public class MainApp extends Application {
    private Scene mainScene;
    private VBox mainMenu;

    @Override
    public void start(Stage primaryStage) {
        CSVReader csvReader = new CSVReader();
        csvReader.chooseCSVFile();
        csvReader.readCSV();
        List<Property> properties = csvReader.createProperties();
        AreaCalculator areaCalculator = new AreaCalculator(properties);

        Button btnAdjacencyChecker = new Button("Open Adjacency Checker");
        Button btnCalculateArea = new Button("Calcular área região");
        Button btnGraphOwners = new Button("Grafo de donos");
        Button btnPropertyExchange = new Button("Property Exchange");

        btnAdjacencyChecker.setOnAction(_ -> openAdjacencyChecker());
        btnCalculateArea.setOnAction(_ -> openRegionSelector(primaryStage, areaCalculator));
        btnGraphOwners.setOnAction(_ -> showGraphOwners());
        btnPropertyExchange.setOnAction(_ -> openPropertyExchange(properties, areaCalculator));

        mainMenu = new VBox(10, btnAdjacencyChecker, btnCalculateArea, btnGraphOwners, btnPropertyExchange);
        mainMenu.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Load the background image
        InputStream imageStream = getClass().getResourceAsStream("/images.jpeg");
        if (imageStream == null) {
            System.err.println("Error loading background image: Resource not found.");
        } else {
            Image backgroundImage = new Image(imageStream);
            if (backgroundImage.isError()) {
                System.err.println("Error loading background image.");
            } else {
                BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
                BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
                mainMenu.setBackground(new Background(background));
            }
        }

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

    private void openRegionSelector(Stage stage, AreaCalculator areaCalculator) {
        Button btnFreguesia = new Button("Freguesia");
        Button btnConcelho = new Button("Concelho");
        Button btnDistrito = new Button("Distrito");
        Button backButton = new Button("Back to Main Menu");

        btnFreguesia.setOnAction(_ -> openRegionButtons("Freguesia", stage, areaCalculator));
        btnConcelho.setOnAction(_ -> openRegionButtons("Concelho", stage, areaCalculator));
        btnDistrito.setOnAction(_ -> openRegionButtons("Distrito", stage, areaCalculator));
        backButton.setOnAction(_ -> showMainMenu());

        VBox regionMenu = new VBox(10, btnFreguesia, btnConcelho, btnDistrito, backButton);
        regionMenu.setPadding(new Insets(20));
        regionMenu.setStyle("-fx-alignment: center;");
        mainScene.setRoot(regionMenu);
    }

    private void openRegionButtons(String type, Stage stage, AreaCalculator areaCalculator) {
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
                showAverageArea(type, region, stage, groupByOwner, areaCalculator);
            });
            regionButtons.getChildren().add(regionButton);
        }

        Button backButton = new Button("Back");
        backButton.setOnAction(_ -> openRegionSelector(stage, areaCalculator));
        regionButtons.getChildren().add(backButton);

        ScrollPane scrollPane = new ScrollPane(regionButtons);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10));

        mainScene.setRoot(scrollPane);
    }

    private void showAverageArea(String type, String region, Stage stage, boolean groupByOwner,
            AreaCalculator areaCalculator) {
        double averageArea;
        if (groupByOwner) {
            averageArea = areaCalculator.calculateAverageAreaWithGroups(type, region);
        } else {
            averageArea = areaCalculator.calculateAverageArea(type, region);
        }

        Label resultLabel = new Label("Average Area for " + type + " \"" + region + "\": " + averageArea);
        Button backButton = new Button("Back");
        backButton.setOnAction(_ -> openRegionButtons(type, stage, areaCalculator));

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
                int ownerId = Integer.parseInt(ownerIdInput.getText());
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

    private void openPropertyExchange(List<Property> properties, AreaCalculator areaCalculator) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-alignment: center;");

        Label concelhoLabel = new Label("Select Concelho:");
        ComboBox<String> concelhoComboBox = new ComboBox<>();
        concelhoComboBox.getItems().addAll(areaCalculator.getUniqueRegions("Concelho"));

        Label promptLabel = new Label("Enter potencialOfSwap:");
        TextField potencialOfSwapInput = new TextField();
        Button generateButton = new Button("Generate Suggestions");

        TextArea resultTextArea = new TextArea();
        resultTextArea.setEditable(false);
        resultTextArea.setWrapText(true);

        generateButton.setOnAction(event -> {
            try {
                String selectedConcelho = concelhoComboBox.getValue();
                if (selectedConcelho == null || selectedConcelho.isEmpty()) {
                    resultTextArea.setText("Please select a Concelho.");
                    return;
                }

                double potencialOfSwap = Double.parseDouble(potencialOfSwapInput.getText());
                List<Property> filteredProperties = properties.stream()
                        .filter(property -> property.getMunicipio().equalsIgnoreCase(selectedConcelho))
                        .collect(Collectors.toList());

                Map<Integer, List<Property>> ownersPropertyList = new HashMap<>();
                for (Property property : filteredProperties) {
                    ownersPropertyList.computeIfAbsent(property.getOwner(), k -> new ArrayList<>()).add(property);
                }

                PropertyExchange pe = new PropertyExchange(filteredProperties, ownersPropertyList, potencialOfSwap);
                List<SuggestedExchange> suggestions = pe.generateSugestions(selectedConcelho);

                StringBuilder resultText = new StringBuilder();
                resultText.append("Initial average area per owner: ").append(pe.calculateAverageAreaByOwner())
                        .append("\n");

                pe.executeExchanges(suggestions);

                resultText.append("New average area per owner after exchanges: ")
                        .append(pe.calculateAverageAreaByOwner()).append("\n\n");
                resultText.append("Sample exchanges:\n");
                for (int i = 0; i < 5 && i < suggestions.size(); i++) {
                    SuggestedExchange exchange = suggestions.get(i);
                    resultText.append("Exchange ").append(i + 1).append(":\n");
                    resultText.append("  Property 1: ").append(exchange.getProperty1().getParNum()).append("\n");
                    resultText.append("  Property 2: ").append(exchange.getProperty2().getParNum()).append("\n");
                    resultText.append("  Area gained: ").append(exchange.getGain()).append("\n");
                    resultText.append("  Probability of trade: ").append(exchange.getProbabilityOfTrade())
                            .append("\n\n");
                }

                resultTextArea.setText(resultText.toString());
            } catch (NumberFormatException e) {
                resultTextArea.setText("Invalid input! Please enter a valid number.");
            }
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(_ -> showMainMenu());

        layout.getChildren().addAll(concelhoLabel, concelhoComboBox, promptLabel, potencialOfSwapInput, generateButton,
                resultTextArea, backButton);

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