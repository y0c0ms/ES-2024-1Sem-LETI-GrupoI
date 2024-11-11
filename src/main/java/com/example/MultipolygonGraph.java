package com.example;

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
        double[][] coordinates1 = {
                { 299218.5203999998, 3623637.4791 },
                { 299218.5033999998, 3623637.4715 },
                { 299218.04000000004, 3623638.4800000004 },
                { 299232.7400000002, 3623644.6799999997 },
                { 299236.6233999999, 3623637.1974 },
                { 299236.93709999975, 3623636.7885999996 },
                { 299238.04000000004, 3623633.4800000004 },
                { 299222.63999999966, 3623627.1799999997 },
                { 299218.5203999998, 3623637.4791 } // Fechando o loop
        };

        double[][] coordinates2 = {
                { 298724.1991999997, 3623192.6094000004 },
                { 298724.3200000003, 3623192.619999999 },
                { 298724.26999999955, 3623185.7200000007 },
                { 298723.8854, 3623185.681500001 },
                { 298723.8854, 3623185.6338 },
                { 298717.2167999996, 3623184.6405999996 },
                { 298716.2909000004, 3623184.495100001 },
                { 298716.1699999999, 3623184.5700000003 },
                { 298711.51999999955, 3623184.17 },
                { 298709.1414000001, 3623183.7961999997 },
                { 298708.48000000045, 3623183.3200000003 },
                { 298705.6799999997, 3623183.2200000007 },
                { 298704.5800000001, 3623183.3200000003 },
                { 298703.98000000045, 3623184.119999999 },
                { 298703.48000000045, 3623190.7200000007 },
                { 298704.0525000002, 3623190.7905 },
                { 298704.0488999998, 3623190.8441000003 },
                { 298705.574, 3623190.9777000006 },
                { 298709.98000000045, 3623191.5199999996 },
                { 298710.0937999999, 3623191.3737000003 },
                { 298724.1991999997, 3623192.6094000004 } // Fechando o loop
        };

        // Definindo os eixos
        NumberAxis xAxis = new NumberAxis(298700, 299300, 100); // Ajuste os valores conforme necessário
        NumberAxis yAxis = new NumberAxis(3623100, 3623700, 100); // Ajuste os valores conforme necessário
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