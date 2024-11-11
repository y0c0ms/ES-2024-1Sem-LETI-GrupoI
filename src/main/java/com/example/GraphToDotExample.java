package com.example;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.nio.dot.DOTExporter;

import java.io.FileWriter;
import java.io.IOException;

public class GraphToDotExample {
    public static void main(String[] args) {
        // Criando um grafo
        Graph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B");

        // Exportando o grafo para o formato DOT
        DOTExporter<String, DefaultEdge> exporter = new DOTExporter<>(v -> v);
        try (FileWriter writer = new FileWriter("graph.dot")) {
            exporter.exportGraph(graph, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Grafo exportado para 'graph.dot'");
    }
}