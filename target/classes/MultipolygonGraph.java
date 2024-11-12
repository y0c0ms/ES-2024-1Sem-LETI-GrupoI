
//Classe antiga com interpretação errado do enunciado
import java.util.List;

import org.apache.commons.csv.CSVRecord;

import com.example.CSVReader;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;

public class MultipolygonGraph extends Application {

    @Override
    public void start(Stage stage) {
        CSVReader csv = new CSVReader();
        csv.readCSV();

        // double[][] coordinates1 =
        // extractCoordinates(csv.getPolygon(csv.getRecord(1)));
        // double[][] coordinates2 =
        // extractCoordinates(csv.getPolygon(csv.getRecord(2)));

        // Defining the axes
        NumberAxis xAxis = new NumberAxis(0, 1000000, 100); // Adjust values as necessary
        NumberAxis yAxis = new NumberAxis(0, 1000000, 100); // Adjust values as necessary
        xAxis.setLabel("X Coordinate");
        yAxis.setLabel("Y Coordinate");

        // Creating the scatter chart
        ScatterChart<Number, Number> scatterChart = new ScatterChart<>(xAxis, yAxis);
        scatterChart.setTitle("Multipolygon Graph with Connected Edges");

        // Adding points and connecting edges for the first set of coordinates
        // addSeriesAndLines(scatterChart, coordinates1);

        // Adding points and connecting edges for the second set of coordinates
        // addSeriesAndLines(scatterChart, coordinates2);

        // Setting up the scene and displaying the chart
        Pane pane = new Pane(scatterChart);
        Scene scene = new Scene(pane, 800, 800);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Multipolygon Graph");
        stage.show();
    }

    public static double[][] extractCoordinates(String multipolygon) {
        // Remove the "MULTIPOLYGON (((" and ")))" structure and get only the
        // coordinates
        String coordinatesStr = multipolygon.replace("MULTIPOLYGON (((", "")
                .replace(")))", "");

        // Split into coordinate pairs by commas
        String[] coordinatePairs = coordinatesStr.split(", ");

        // Create the matrix to store the (x, y) pairs
        double[][] coordinateMatrix = new double[2][coordinatePairs.length];

        // Fill the matrix with x and y values
        for (int i = 0; i < coordinatePairs.length; i++) {
            String[] xy = coordinatePairs[i].split(" ");
            coordinateMatrix[0][i] = Double.parseDouble(xy[0]); // x
            coordinateMatrix[1][i] = Double.parseDouble(xy[1]); // y
        }

        return coordinateMatrix;
    }

    private void addSeriesAndLines(ScatterChart<Number, Number> scatterChart, double[][] coordinates) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (int i = 0; i < coordinates.length; i++) {
            double x = coordinates[i][0];
            double y = coordinates[i][1];
            series.getData().add(new XYChart.Data<>(x, y));

            // Connecting the points
            if (i > 0) {
                double prevX = coordinates[i - 1][0];
                double prevY = coordinates[i - 1][1];
                XYChart.Series<Number, Number> lineSeries = new XYChart.Series<>();
                lineSeries.getData().add(new XYChart.Data<>(prevX, prevY));
                lineSeries.getData().add(new XYChart.Data<>(x, y));
                scatterChart.getData().add(lineSeries);
            }
        }
        // Closing the loop
        double firstX = coordinates[0][0];
        double firstY = coordinates[0][1];
        double lastX = coordinates[coordinates.length - 1][0];
        double lastY = coordinates[coordinates.length - 1][1];
        XYChart.Series<Number, Number> lineSeries = new XYChart.Series<>();
        lineSeries.getData().add(new XYChart.Data<>(lastX, lastY));
        lineSeries.getData().add(new XYChart.Data<>(firstX, firstY));
        scatterChart.getData().add(lineSeries);
    }

    public static void main(String[] args) {
        launch(args);
    }
}