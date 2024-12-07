package com.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class represents a graph where nodes are properties, and edges indicate adjacency between properties.
 * It supports adding properties, identifying adjacent properties, and finding groups of properties
 * owned by the same owner.
 */
public class Graph {
    private List<Property> properties;
    private Map<Property, List<Property>> adjacencyList;

    /**
     * Constructor for the Graph class.
     * Initializes the list of properties and the adjacency list.
     */
    public Graph() {
        properties = new ArrayList<>();
        adjacencyList = new HashMap<>();
    }

    /**
     * Adds a property to the graph and establishes adjacency relationships with existing properties.
     * @param property The property to be added to the graph.
     */
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

    /**
     * Creates a bidirectional edge between two properties in the graph.
     * @param p1 The first property.
     * @param p2 The second property.
     */
    private void addEdge(Property p1, Property p2) {
        adjacencyList.get(p1).add(p2);
        adjacencyList.get(p2).add(p1); // Ensure the edge is bidirectional
    }

    /**
     * Finds and groups properties owned by the same owner, considering adjacency relationships.
     * @return A list of groups where each group is a list of properties owned by the same owner.
     */
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

    /**
     * Performs a depth-first search (DFS) to find all connected properties with the same owner.
     * @param property The starting property for the DFS.
     * @param visited A set of properties already visited during the search.
     * @param group The group of properties being formed.
     */
    private void dfs(Property property, Set<Property> visited, List<Property> group) {
        visited.add(property);
        group.add(property);

        for (Property neighbor : getAdjacentProperties(property)) {
            if (!visited.contains(neighbor) && neighbor.getOwner() == property.getOwner()) {
                dfs(neighbor, visited, group);
            }
        }
    }

    /**
     * Retrieves the list of properties adjacent to a given property.
     * @param property The property whose adjacent properties are to be retrieved.
     * @return A list of properties adjacent to the given property.
     */
    public List<Property> getAdjacentProperties(Property property) {
        return adjacencyList.getOrDefault(property, new ArrayList<>());
    }

    /**
     * Retrieves all properties in the graph.
     * @return A list of all properties in the graph.
     */
    public List<Property> getProperties() {
        return properties;
    }
}
