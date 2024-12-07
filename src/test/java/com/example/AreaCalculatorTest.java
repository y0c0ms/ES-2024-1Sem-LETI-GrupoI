package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class AreaCalculatorTest {

    private AreaCalculator areaCalculator;
    private List<Property> properties;

    // Configuração inicial antes de cada teste
    @BeforeEach
    public void setUp() {
        // Criando algumas propriedades fictícias para testar
        properties = new ArrayList<>();
        try{
            Geometry geometry1 = new WKTReader().read("MULTIPOLYGON (((0 0, 1 0, 1 1, 0 1, 0 0)))");
            Geometry geometry2 = new WKTReader().read("MULTIPOLYGON (((3 0, 4 0, 4 2, 3 2, 3 0)))");
            Geometry geometry3 = new WKTReader().read("MULTIPOLYGON (((2 0, 3 0, 3 1, 2 1, 2 0)))");
            Geometry geometry4 = new WKTReader().read("MULTIPOLYGON (((1 0, 2 0, 2 1, 1 1, 1 0)))");
            Geometry geometry5 = new WKTReader().read("MULTIPOLYGON (((5 0, 6 0, 6 1, 5 1, 5 0)), ((5.3 0.3, 5.7 0.3, 5.7 0.7, 5.3 0.7, 5.3 0.3)))");
            
            
            properties.add(new Property(1, 1, 1, 100, 100, geometry1.toText(), 1, "Freguesia1", "Concelho1", "Distrito1"));
            properties.add(new Property(2, 2, 2, 150, 150, geometry2.toText(), 2, "Freguesia1", "Concelho1", "Distrito1"));
            properties.add(new Property(3, 3, 3, 200, 200, geometry3.toText(), 1, "Freguesia2", "Concelho1", "Distrito1"));
            properties.add(new Property(4, 4, 4, 120, 120, geometry4.toText(), 1, "Freguesia1", "Concelho2", "Distrito1"));
            properties.add(new Property(5, 5, 5, 130, 130, geometry5.toText(), 2, "Freguesia1", "Concelho1", "Distrito2"));
        }catch(Exception e){
            fail("Failed to create test properties.");
        }
        // Inicializando o AreaCalculator com a lista de propriedades
        areaCalculator = new AreaCalculator(properties);
    }

    // Testando o cálculo da área média para uma região específica
    @Test
    public void testCalculateAverageArea() {
        double averageArea = areaCalculator.calculateAverageArea("Freguesia", "Freguesia1");
        double averageArea2 = areaCalculator.calculateAverageArea("Freguesia", "Freguesia2");
        assertEquals(125.0, averageArea, "Average Area isn't equal to the expected area!");
        assertEquals(200, averageArea2, "Average Area isn't equal to the expected area!");

    }

    // Testando o cálculo da área média considerando as propriedades agrupadas por proprietário
    @Test
    public void testCalculateAverageAreaWithGroups() {
        double averageAreaWithGroups = areaCalculator.calculateAverageAreaWithGroups("Freguesia", "Freguesia1");
        double averageAreaWithGroups2 = areaCalculator.calculateAverageAreaWithGroups("Freguesia", "Freguesia2");
        assertEquals(166.6, averageAreaWithGroups, 0.1,"Average Area isn't equal to the expected area!");
        assertEquals(200, averageAreaWithGroups2, "Average Area isn't equal to the expected area!");
    }

    // Testando o filtro de regiões únicas para um tipo de região
    @Test
    public void testGetUniqueRegions() {
        List<String> uniqueRegions = areaCalculator.getUniqueRegions("Freguesia");
        assertTrue(uniqueRegions.contains("Freguesia1"));
        assertTrue(uniqueRegions.contains("Freguesia2"));
        assertEquals(2, uniqueRegions.size());
    }
}
