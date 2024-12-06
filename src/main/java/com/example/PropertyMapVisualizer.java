package com.example;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;

import java.util.List;

//Class in tests and just as an addition
public class PropertyMapVisualizer extends VBox {
    private VBox layout;
    private Graph graph;
    private double minX, maxX, minY, maxY;

    public PropertyMapVisualizer(MainApp mainApp) {
        CSVReader csvReader = new CSVReader();
        csvReader.readCSV();
        List<Property> properties = csvReader.createProperties();
        graph = new Graph();
        for (Property property : properties) {
            graph.addProperty(property);
        }

        calculateBoundingBox(properties);

        Canvas canvas = new Canvas(8000000, 8000000);
        drawMap(canvas.getGraphicsContext2D());

        Button backButton = new Button("Back to Main Menu");
        backButton.setOnAction(_ -> mainApp.showMainMenu());

        layout = new VBox(10, backButton, canvas);
        layout.setStyle("-fx-padding: 20;");
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
        gc.setFill(Color.LIGHTGREEN);

        for (Property property : graph.getProperties()) {
            if (property.getGeometry() instanceof MultiPolygon) {
                MultiPolygon multiPolygon = (MultiPolygon) property.getGeometry();
                for (int i = 0; i < multiPolygon.getNumGeometries(); i++) {
                    Polygon polygon = (Polygon) multiPolygon.getGeometryN(i);
                    drawPolygon(gc, polygon);
                }
            }
        }

        gc.setStroke(Color.GRAY);
        for (Property property : graph.getProperties()) {
            List<Property> adjacentProperties = graph.getAdjacentProperties(property);
            for (Property adjacent : adjacentProperties) {
                drawEdge(gc, property, adjacent);
            }
        }
    }

    private void drawPolygon(GraphicsContext gc, Polygon polygon) {
        Coordinate[] coords = polygon.getCoordinates();
        double[] xPoints = new double[coords.length];
        double[] yPoints = new double[coords.length];

        for (int i = 0; i < coords.length; i++) {
            xPoints[i] = scaleX(coords[i].x);
            yPoints[i] = scaleY(coords[i].y);
        }

        gc.fillPolygon(xPoints, yPoints, coords.length);
    }

    private void drawEdge(GraphicsContext gc, Property p1, Property p2) {
        double[] p1Centroid = getCentroid(p1);
        double[] p2Centroid = getCentroid(p2);
        gc.strokeLine(p1Centroid[0], p1Centroid[1], p2Centroid[0], p2Centroid[1]);
    }

    private double[] getCentroid(Property property) {
        Coordinate centroid = property.getGeometry().getCentroid().getCoordinate();
        return new double[] { scaleX(centroid.x), scaleY(centroid.y) };
    }

    private double scaleX(double x) {
        double canvasWidth = 8000000;
        return ((x - minX) / (maxX - minX)) * canvasWidth;
    }

    private double scaleY(double y) {
        double canvasHeight = 8000000;
        return canvasHeight - ((y - minY) / (maxY - minY)) * canvasHeight;
    }

    public VBox getLayout() {
        return layout;
    }
}
