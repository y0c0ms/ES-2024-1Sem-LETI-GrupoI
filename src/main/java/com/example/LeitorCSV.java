package com.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LeitorCSV {
    public List<Propriedade> lerPropriedades(String caminhoArquivo) {
        List<Propriedade> propriedades = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            br.readLine(); // Ignorar a primeira linha (cabeçalho)
            while ((linha = br.readLine()) != null) {
                String[] valores = linha.split(";");
                if (valores.length < 7) {
                    System.err.println("Linha mal formatada ou incompleta, pulando: " + linha);
                    continue;
                }

                try {
                    int objectId = Integer.parseInt(valores[0].trim());
                    double parId = Double.parseDouble(valores[1].trim());
                    String parNum = valores[2].trim();
                    double shapeLength = Double.parseDouble(valores[3].trim());
                    double shapeArea = Double.parseDouble(valores[4].trim());
                    String geometry = valores[5].trim();
                    String owner = valores[6].trim();

                    // Ensure the geometry is properly formatted
                    if (geometry.isEmpty() || !geometry.startsWith("MULTIPOLYGON")) {
                        System.err.println("Geometria mal formatada ou vazia para a propriedade ID: " + objectId);
                        continue; // Skip this entry if geometry is invalid
                    }

                    Propriedade propriedade = new Propriedade(objectId, parId, parNum, shapeLength, shapeArea, geometry,
                            owner);
                    propriedades.add(propriedade);
                } catch (NumberFormatException e) {
                    System.err.println(
                            "Erro ao converter um dos valores numéricos: " + e.getMessage() + " - Linha: " + linha);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return propriedades;
    }
}