package com.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LeitorCSV {
    public List<Propriedade> lerCSV(String filePath) {
        List<Propriedade> propriedades = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                if (values.length > 5) {
                    String objectId = values[0];
                    String wktGeometry = values[5];
                    if (wktGeometry == null || wktGeometry.trim().isEmpty()) {
                        System.err.println("Aviso: Geometria WKT nula ou vazia para a propriedade ID: " + objectId);
                        continue;
                    }
                    Propriedade propriedade = new Propriedade(objectId, wktGeometry);
                    if (propriedade.getGeometry() != null) {
                        propriedades.add(propriedade);
                    } else {
                        System.err.println("Aviso: Geometria inválida para a propriedade ID: " + objectId);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return propriedades;
    }
}