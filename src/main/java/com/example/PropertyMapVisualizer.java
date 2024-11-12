package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.MultiPolygon;
import java.util.List;

public class PropertyMapVisualizer extends Application {
    private Graph graph;
    private double minX, maxX, minY, maxY;

    @Override
    public void start(Stage primaryStage) {
        // Load the properties and create the graph
        CSVReader csvReader = new CSVReader();
        csvReader.readCSV();
        List<Property> properties = csvReader.createProperties();
        graph = new Graph();
        for (Property property : properties) {
            graph.addProperty(property);
        }

        // Calculate the bounding box of all coordinates
        calculateBoundingBox(properties);

        // Set up JavaFX Canvas and draw the properties
        Pane root = new Pane();
        Canvas canvas = new Canvas(800, 800); // Canvas size
        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        drawMap(gc);

        Scene scene = new Scene(root, 800, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Property Map Visualization");
        primaryStage.show();
    }

    private void calculateBoundingBox(List<Property> properties) {
        minX = Double.MAX_VALUE;
        maxX = Double.MIN_VALUE;
        minY = Double.MAX_VALUE;
        maxY = Double.MIN_VALUE;

        for (Property property : properties) {
            if (property.getGeometry() instanceof MultiPolygon) {
                MultiPolygon multiPolygon = (MultiPolygon) property.getGeometry();
                for (int i = 0; i < multiPolygon.getNumGeometries(); i++) {
                    Polygon polygon = (Polygon) multiPolygon.getGeometryN(i);
                    for (Coordinate coord : polygon.getCoordinates()) {
                        minX = Math.min(minX, coord.x);
                        maxX = Math.max(maxX, coord.x);
                        minY = Math.min(minY, coord.y);
                        maxY = Math.max(maxY, coord.y);
                    }
                }
            }
        }
    }

    private void drawMap(GraphicsContext gc) {
        gc.setFill(Color.LIGHTBLUE);

        // Draw each property as a polygon
        for (Property property : graph.getProperties()) {
            if (property.getGeometry() instanceof MultiPolygon) {
                MultiPolygon multiPolygon = (MultiPolygon) property.getGeometry();
                for (int i = 0; i < multiPolygon.getNumGeometries(); i++) {
                    Polygon polygon = (Polygon) multiPolygon.getGeometryN(i);
                    drawPolygon(gc, polygon);
                }
            }
        }

        // Draw edges for adjacency
        gc.setStroke(Color.GRAY);
        for (Property property : graph.getProperties()) {
            List<Property> adjacentProperties = graph.getAdjacentProperties(property);
            for (Property adjacent : adjacentProperties) {
                drawEdge(gc, property, adjacent);
            }
        }
    }

    private void drawPolygon(GraphicsContext gc, Polygon polygon) {
        gc.setFill(Color.LIGHTGREEN);

        // Scale and convert coordinates to screen points
        Coordinate[] coords = polygon.getCoordinates();
        double[] xPoints = new double[coords.length];
        double[] yPoints = new double[coords.length];

        for (int i = 0; i < coords.length; i++) {
            xPoints[i] = scaleX(coords[i].x);
            yPoints[i] = scaleY(coords[i].y);
        }

        // Draw the polygon
        gc.fillPolygon(xPoints, yPoints, coords.length);
    }

    private void drawEdge(GraphicsContext gc, Property p1, Property p2) {
        // Draw a line between centroids of two adjacent polygons
        double[] p1Centroid = getCentroid(p1);
        double[] p2Centroid = getCentroid(p2);
        gc.strokeLine(p1Centroid[0], p1Centroid[1], p2Centroid[0], p2Centroid[1]);
    }

    private double[] getCentroid(Property property) {
        Coordinate centroid = property.getGeometry().getCentroid().getCoordinate();
        return new double[] { scaleX(centroid.x), scaleY(centroid.y) };
    }

    private double scaleX(double x) {
        double canvasWidth = 800;
        return ((x - minX) / (maxX - minX)) * canvasWidth;
    }

    private double scaleY(double y) {
        double canvasHeight = 800;
        return canvasHeight - ((y - minY) / (maxY - minY)) * canvasHeight;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
