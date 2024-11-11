package com.example;

import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        // Step 1: Load properties from CSV file
        String csvFilePath = "D:\\ProjEngSoft\\src\\main\\resources\\Madeira-Moodle.csv";
        LeitorCSV leitor = new LeitorCSV();
        List<Propriedade> propriedades = leitor.lerCSV(csvFilePath);

        // Check if properties were loaded correctly
        if (propriedades.isEmpty()) {
            System.out.println("Nenhuma propriedade foi carregada. Verifique o caminho do arquivo e o formato do CSV.");
            return;
        }

        // Step 2: Create a graph instance and add properties
        Grafo grafo = new Grafo();

        for (Propriedade propriedade : propriedades) {
            grafo.adicionarPropriedade(propriedade);
        }

        // Step 3: Calculate adjacencies between properties
        grafo.calcularAdjacencias();

    }
}