package com.example;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

/**
 * This class represents a graph of property owners, where vertices are owner
 * IDs and edges indicate adjacency
 * between properties owned by different owners. The graph is built using
 * JGraphT's SimpleGraph.
 */
public class GraphOwners {
    private List<Property> allProperties;
    private Map<Integer, List<Property>> ownersPropertyList;
    private Graph<Integer, DefaultEdge> ownerGraph;

    /**
     * Constructor for the GraphOwners class.
     * Initializes the owner graph based on the provided list of properties and
     * owner-property mappings.
     * 
     * @param allProperties      A list of all properties.
     * @param ownersPropertyList A map where keys are owner IDs and values are lists
     *                           of properties owned by them.
     */
    public GraphOwners(List<Property> allProperties, Map<Integer, List<Property>> ownersPropertyList) {
        this.allProperties = allProperties;
        this.ownersPropertyList = ownersPropertyList;
        this.ownerGraph = new SimpleGraph<>(DefaultEdge.class);
        initializeOwnerGraph();
    }

    /**
     * Initializes the owner graph by adding vertices for each owner and edges
     * between owners with adjacent properties.
     */
    private void initializeOwnerGraph() {
        // Add all owner IDs as vertices in the graph
        for (Integer owner : ownersPropertyList.keySet()) {
            ownerGraph.addVertex(owner);
        }

        // Add edges between owners whose properties are adjacent
        for (Property property : allProperties) {
            int ownerId = property.getOwner();
            for (Property otherProperty : allProperties) {
                if (property != otherProperty && property.isAdjacent(otherProperty)) {
                    int otherOwnerId = otherProperty.getOwner();
                    if (ownerId != otherOwnerId) {
                        ownerGraph.addEdge(ownerId, otherOwnerId);
                    }
                }
            }
        }
    }

    /**
     * Retrieves the owner graph.
     * 
     * @return A graph where vertices represent owner IDs and edges represent
     *         adjacency between their properties.
     */
    public Graph<Integer, DefaultEdge> getOwnerGraph() {
        return ownerGraph;
    }

    public Set<Integer> getOwnerIds() {
        return ownersPropertyList.keySet();
    }

    /**
     * Main method for testing the GraphOwners class.
     * Reads data from a CSV file, builds the owner graph, and prints information
     * about the graph.
     * 
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        CSVReader r = new CSVReader();
        r.readCSV();

        // Create GraphOwners instance and build the owner graph
        GraphOwners go = new GraphOwners(r.createProperties(), r.getOwnersList());
        Graph<Integer, DefaultEdge> g = go.getOwnerGraph();

        // Example: Print neighbors of a specific owner
        System.out.println("Neighbors of owner 93: " + g.edgesOf(93));
    }
}
