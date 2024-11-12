package com.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
    private List<Property> properties;
    private Map<Property, List<Property>> adjacencyList;

    public Graph() {
        properties = new ArrayList<>();
        adjacencyList = new HashMap<>();
    }

    public void addProperty(Property property) {
        properties.add(property);
        adjacencyList.put(property, new ArrayList<>());

        // Check adjacency with existing properties and add edges if necessary
        for (Property other : properties) {
            if (other != property && property.isAdjacent(other)) {
                addEdge(property, other);
            }
        }
    }

    private void addEdge(Property p1, Property p2) {
        adjacencyList.get(p1).add(p2);
        adjacencyList.get(p2).add(p1); // Ensure the edge is bidirectional
    }

    // Method to retrieve adjacent properties
    public List<Property> getAdjacentProperties(Property property) {
        return adjacencyList.getOrDefault(property, new ArrayList<>());
    }
}
