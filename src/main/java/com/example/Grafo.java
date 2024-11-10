package main.java.com.example;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import java.util.HashMap;
import java.util.HashSet;
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
            try {
                Geometry geom1 = reader.read(prop1.getGeometry());
                for (int j = i + 1; j < propriedades.size(); j++) {
                    Propriedade prop2 = propriedades.get(j);
                    Geometry geom2 = reader.read(prop2.getGeometry());
                    if (geom1.touches(geom2)) {
                        adicionarAdjacencia(prop1, prop2);
                    }
                }
            } catch (ParseException e) {
                System.err.println("Erro ao interpretar a geometria: " + e.getMessage());
            }
        }
    }

    private void adicionarAdjacencia(Propriedade prop1, Propriedade prop2) {
        listaAdjacencia.get(prop1).add(prop2);
        listaAdjacencia.get(prop2).add(prop1);
    }
}
