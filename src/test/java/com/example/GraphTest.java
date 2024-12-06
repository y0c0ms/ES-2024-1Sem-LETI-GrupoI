package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;

import static org.junit.jupiter.api.Assertions.*;

public class GraphTest {
    private Graph graph;
    private Property property1;
    private Property property2;
    private Property isolatedProperty;
    @BeforeEach
    public void setUp() {
        graph = new Graph();

        try {
            Geometry geometry1 = new WKTReader().read("MULTIPOLYGON (((0 0, 1 0, 1 1, 0 1, 0 0)))");
            Geometry geometry2 = new WKTReader().read("MULTIPOLYGON (((1 1, 2 1, 2 2, 1 2, 1 1)))");
            Geometry geometry3 = new WKTReader().read("MULTIPOLYGON (((5 5, 6 5, 6 6, 5 6, 5 5)))");
            property1 = new Property(1, 123, 456, 100, 100, geometry1.toText(), 1, "a", "b", "c");
            property2 = new Property(2, 789, 101, 200, 200, geometry2.toText(), 2,"a", "b", "c");
            isolatedProperty = new Property(3, 999, 888, 150, 150, geometry3.toText(), 3, "a", "b", "c");
        } catch (Exception e) {
            fail("Failed to create test properties.");
        }
    }

    @Test
    public void testAddProperty() {
        graph.addProperty(property1);
        assertEquals(1, graph.getProperties().size(), "Graph should contain one property.");
    }

    @Test
    public void testAddEdgeBetweenAdjacentProperties() {
        graph.addProperty(property1);
        graph.addProperty(property2);

        assertTrue(graph.getAdjacentProperties(property1).contains(property2));
        assertTrue(graph.getAdjacentProperties(property2).contains(property1));
    }

    @Test
    public void testNoEdgeBetweenNonAdjacentProperties() {
        graph.addProperty(property1);
        graph.addProperty(property2);
        graph.addProperty(isolatedProperty);

        assertFalse(graph.getAdjacentProperties(property1).contains(isolatedProperty));
        assertFalse(graph.getAdjacentProperties(property2).contains(isolatedProperty));
    }
}
