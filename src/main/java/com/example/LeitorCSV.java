package main.java.com.example;

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
                try {
                    int objectId = Integer.parseInt(valores[0].trim());
                    double parId = Double.parseDouble(valores[1].trim());
                    String parNum = valores[2].trim();
                    double shapeLength = Double.parseDouble(valores[3].trim());
                    double shapeArea = Double.parseDouble(valores[4].trim().replace(",", "."));
                    String geometry = valores[5].trim();
                    String owner = valores[6].trim();

                    Propriedade propriedade = new Propriedade(objectId, parId, parNum, shapeLength, shapeArea, geometry,
                            owner);
                    propriedades.add(propriedade);
                } catch (NumberFormatException e) {
                    System.err.println("Erro ao converter um dos valores: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return propriedades;
    }
}
