package com.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public List<List<Property>> findOwnerGroups() {
        List<List<Property>> ownerGroups = new ArrayList<>();
        Set<Property> visited = new HashSet<>();

        for (Property property : adjacencyList.keySet()) {
            if (!visited.contains(property)) {
                List<Property> group = new ArrayList<>();
                dfs(property, visited, group);
                ownerGroups.add(group);
            }
        }
        return ownerGroups;
    }

    private void dfs(Property property, Set<Property> visited, List<Property> group) {
        visited.add(property);
        group.add(property);

        for (Property neighbor : getAdjacentProperties(property)) {
            if (!visited.contains(neighbor) && neighbor.getOwner() == property.getOwner()) {
                dfs(neighbor, visited, group);
            }
        }
    }

    // Method to retrieve adjacent properties
    public List<Property> getAdjacentProperties(Property property) {
        return adjacencyList.getOrDefault(property, new ArrayList<>());
    }

    // Method to retrieve all properties in the graph
    public List<Property> getProperties() {
        return properties;
    }
}
