package com.example;

import java.util.List;

import org.apache.commons.csv.CSVRecord;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

public class MultipolygonGraph extends Application {

    @Override
    public void start(Stage stage) {
        // Coordenadas do multipolígono

        CSVReader csv = new CSVReader();
        csv.readCSV();
        
        double[][] coordinates1 = extrairCoordenadas(csv.getPolygon(csv.getRecord(1)));
        double[][] coordinates2 = extrairCoordenadas(csv.getPolygon(csv.getRecord(2)));

       
        // Definindo os eixos
        NumberAxis xAxis = new NumberAxis(0, 1000000, 100); // Ajuste os valores conforme necessário
        NumberAxis yAxis = new NumberAxis(0, 1000000, 100); // Ajuste os valores conforme necessário
        xAxis.setLabel("Coordenada X");
        yAxis.setLabel("Coordenada Y");

        // Criando o gráfico de dispersão
        ScatterChart<Number, Number> scatterChart = new ScatterChart<>(xAxis, yAxis);
        scatterChart.setTitle("Grafo do Multipolygon com Arestas Conectadas");

        // Adicionando pontos e conectando as arestas para o primeiro conjunto de
        // coordenadas
        addSeriesAndLines(scatterChart, coordinates1);

        // Adicionando pontos e conectando as arestas para o segundo conjunto de
        // coordenadas
        addSeriesAndLines(scatterChart, coordinates2);

        // Configurando a cena e exibindo o gráfico
        Pane pane = new Pane(scatterChart);
        Scene scene = new Scene(pane, 800, 800);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Multipolygon Graph");
        stage.show();
    }


    public static double[][] extrairCoordenadas(String multipolygon) {
        // Remover a estrutura "MULTIPOLYGON (((" e ")))" e obter apenas as coordenadas
        String coordenadasStr = multipolygon.replace("MULTIPOLYGON (((", "")
                                              .replace(")))", "");
        
        // Dividir em pares de coordenadas por vírgulas
        String[] paresCoordenadas = coordenadasStr.split(", ");
        
        // Criar a matriz para armazenar os pares (x, y)
        double[][] matrizCoordenadas = new double[2][paresCoordenadas.length];

        // Preencher a matriz com valores x e y
        for (int i = 0; i < paresCoordenadas.length; i++) {
            String[] xy = paresCoordenadas[i].split(" ");
            matrizCoordenadas[0][i] = Double.parseDouble(xy[0]); // x
            matrizCoordenadas[1][i] = Double.parseDouble(xy[1]); // y
        }

        return matrizCoordenadas;
    }

    private void addSeriesAndLines(ScatterChart<Number, Number> scatterChart, double[][] coordinates) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (int i = 0; i < coordinates.length; i++) {
            double x = coordinates[i][0];
            double y = coordinates[i][1];
            series.getData().add(new XYChart.Data<>(x, y));

            // Conectando os pontos
            if (i > 0) {
                double prevX = coordinates[i - 1][0];
                double prevY = coordinates[i - 1][1];
                XYChart.Series<Number, Number> lineSeries = new XYChart.Series<>();
                lineSeries.getData().add(new XYChart.Data<>(prevX, prevY));
                lineSeries.getData().add(new XYChart.Data<>(x, y));
                scatterChart.getData().add(lineSeries);
            }
        }
        // Fechando o loop
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