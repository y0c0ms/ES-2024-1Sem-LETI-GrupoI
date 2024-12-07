package com.example;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;

public class GraphOwnersTest {

    private List<Property> properties;
    private Map<Integer, List<Property>> ownersPropertyList;
    private GraphOwners graphOwners;

    @BeforeEach
    public void setup() {
        // Create sample properties
        try{
            Geometry geometry1 = new WKTReader().read("MULTIPOLYGON (((0 0, 1 0, 1 1, 0 1, 0 0)))");
            Geometry geometry2 = new WKTReader().read("MULTIPOLYGON (((1 0, 2 0, 2 1, 1 1, 1 0)))");
            Geometry geometry3 = new WKTReader().read("MULTIPOLYGON (((2 0, 3 0, 3 1, 2 1, 2 0)))");
            Geometry geometry4 = new WKTReader().read("MULTIPOLYGON (((3 0, 4 0, 4 1, 3 1, 3 0)))");

            Property property1 = new Property(1, 1, 1, 1, 1, geometry1.toText(), 1, "Freguesia1", "Concelho1", "Distrito1");
            Property property2 = new Property(2, 2, 2, 2, 2, geometry2.toText(), 2, "Freguesia1", "Concelho1", "Distrito1");
            Property property3 = new Property(3, 3, 3, 3, 3, geometry3.toText(), 1, "Freguesia1", "Concelho1", "Distrito1");
            Property property4 = new Property(4, 4, 4, 4, 4, geometry4.toText(), 3, "Freguesia1", "Concelho1", "Distrito1");

            properties = Arrays.asList(property1, property2, property3, property4);

            // Create owner-to-properties map
            ownersPropertyList = new HashMap<>();
            ownersPropertyList.put(1, Arrays.asList(property1, property3));
            ownersPropertyList.put(2, Collections.singletonList(property2));
            ownersPropertyList.put(3, Collections.singletonList(property4));
        }catch(Exception e){

        }
        // Initialize GraphOwners
        graphOwners = new GraphOwners(properties, ownersPropertyList);
    }

    @Test
    public void testGraph() {
        Graph<Integer, DefaultEdge> graph = graphOwners.getOwnerGraph();

        // Check vertices
        assertTrue(graph.containsVertex(1));
        assertTrue(graph.containsVertex(2));
        assertTrue(graph.containsVertex(3));
        assertFalse(graph.containsVertex(4));

        // Check edges

        assertTrue(graph.containsEdge(1, 2));
        assertTrue(graph.containsEdge(1, 3));
        assertFalse(graph.containsEdge(2, 3));
    }

    @Test
    public void testNeighborsOfVertex() {
        Graph<Integer, DefaultEdge> graph = graphOwners.getOwnerGraph();
        // Check neighbors of owner 1
        Set<DefaultEdge> edges = graph.edgesOf(1);
        assertEquals(2, edges.size());
    }
}
