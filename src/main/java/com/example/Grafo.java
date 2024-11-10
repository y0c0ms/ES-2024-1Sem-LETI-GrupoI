package com.example;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Grafo {
    private Map<Propriedade, Set<Propriedade>> listaAdjacencia = new HashMap<>();
    private GeometryFactory geometryFactory = new GeometryFactory();
    private WKTReader reader = new WKTReader(geometryFactory);

    public void adicionarPropriedade(Propriedade propriedade) {
        listaAdjacencia.putIfAbsent(propriedade, new HashSet<>());
    }

    public void calcularAdjacencias(List<Propriedade> propriedades) {
        for (int i = 0; i < propriedades.size(); i++) {
            Propriedade prop1 = propriedades.get(i);
            String geom1WKT = prop1.getGeometryWKT();

            if (geom1WKT == null || geom1WKT.isEmpty()) {
                System.err.println("Aviso: Geometria WKT nula ou vazia para a propriedade ID: " + prop1.getObjectId());
                continue; // Skip this property if WKT is not valid
            }

            // Clean up geometry WKT (remove extra spaces, trim, etc.)
            geom1WKT = geom1WKT.trim().replaceAll("\\s+", " ");

            try {
                Geometry geom1 = reader.read(geom1WKT);

                for (int j = i + 1; j < propriedades.size(); j++) {
                    Propriedade prop2 = propriedades.get(j);
                    String geom2WKT = prop2.getGeometryWKT();

                    if (geom2WKT == null || geom2WKT.isEmpty()) {
                        System.err.println(
                                "Aviso: Geometria WKT nula ou vazia para a propriedade ID: " + prop2.getObjectId());
                        continue; // Skip this property if WKT is not valid
                    }

                    // Clean up geometry WKT
                    geom2WKT = geom2WKT.trim().replaceAll("\\s+", " ");

                    Geometry geom2 = reader.read(geom2WKT);

                    if (geom1.touches(geom2)) {
                        adicionarAdjacencia(prop1, prop2);
                    }
                }
            } catch (ParseException e) {
                System.err.println("Erro ao interpretar a geometria WKT para a propriedade ID: " + prop1.getObjectId()
                        + " - " + e.getMessage());
                System.err.println("Geometria com problema: " + geom1WKT);
            }
        }
    }

    private void adicionarAdjacencia(Propriedade prop1, Propriedade prop2) {
        listaAdjacencia.get(prop1).add(prop2);
        listaAdjacencia.get(prop2).add(prop1);
    }

    public Set<Propriedade> getAdjacentes(Propriedade propriedade) {
        return listaAdjacencia.getOrDefault(propriedade, new HashSet<>());
    }
}
