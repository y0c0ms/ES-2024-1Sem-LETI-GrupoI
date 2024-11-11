package com.example;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grafo {
    private SimpleGraph<Propriedade, DefaultEdge> graph;
    private Map<String, Propriedade> propriedadesMap;

    public Grafo() {
        this.graph = new SimpleGraph<>(DefaultEdge.class);
        this.propriedadesMap = new HashMap<>();
    }

    public void addPropriedade(Propriedade propriedade) {
        graph.addVertex(propriedade);
        propriedadesMap.put(propriedade.getObjectId(), propriedade);
    }

    public void addAdjacencias() {
        for (Propriedade p1 : graph.vertexSet()) {
            for (Propriedade p2 : graph.vertexSet()) {
                if (!p1.equals(p2) && p1.getGeometry().touches(p2.getGeometry())) {
                    graph.addEdge(p1, p2);
                }
            }
        }
    }

    public SimpleGraph<Propriedade, DefaultEdge> getGraph() {
        return graph;
    }

    public Propriedade getPropriedadeById(String objectId) {
        return propriedadesMap.get(objectId);
    }
}