package com.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;

public class PropertyExchange {
    private Map<Integer, List<Property>> ownersPropertyList;
    private List<Property> properties;
    private double potencialOfSwap;
    private double previousAverage;
    private double newAverage;

    public PropertyExchange(List<Property> properties, Map<Integer, List<Property>> ownersPropertyList,
            double potencialOfSwap) {
        this.properties = properties;
        this.ownersPropertyList = ownersPropertyList;
        this.potencialOfSwap = potencialOfSwap;
        this.previousAverage = calculateAverageAreaByOwner();
    }

    public List<SuggestedExchange> generateSugestions(String concelho) {
        List<Property> filteredProperties = properties.stream()
                .filter(property -> property.getMunicipio().equalsIgnoreCase(concelho))
                .collect(Collectors.toList());

        List<SuggestedExchange> suggestions = filteredProperties.parallelStream()
                .filter(property1 -> property1 != null)
                .flatMap(property1 -> filteredProperties.stream()
                        .filter(property2 -> property2 != null && property1.getOwner() != property2.getOwner())
                        .map(property2 -> {
                            double area1 = property1.getShapeArea();
                            double area2 = property2.getShapeArea();
                            double gain = calculateGain(property1, property2);
                            double probabilityOfTrade = calculateProbabilityOfTrade(area1, area2);
                            return new SuggestedExchange(property1, property2, gain, probabilityOfTrade);
                        })
                        .filter(exchange -> exchange.getProbabilityOfTrade() > potencialOfSwap))
                .sorted(Comparator.comparingDouble(SuggestedExchange::getGain).reversed()
                        .thenComparingDouble(SuggestedExchange::getProbabilityOfTrade).reversed())
                .collect(Collectors.toList());

        return suggestions;
    }

    private double calculateGain(Property property1, Property property2) {
        double initialAverage = calculateAverageAreaByOwner();
        executeExchange(property1, property2);
        double newAverage = calculateAverageAreaByOwner();
        undoExchange(property1, property2);
        return newAverage - initialAverage;
    }

    private double calculateProbabilityOfTrade(double area1, double area2) {
        return 1.0 - Math.abs(area1 - area2) / Math.max(area1, area2);
    }

    private void executeExchange(Property property1, Property property2) {
        int owner1 = property1.getOwner();
        int owner2 = property2.getOwner();
        ownersPropertyList.get(owner1).remove(property1);
        ownersPropertyList.get(owner2).remove(property2);
        ownersPropertyList.get(owner1).add(property2);
        ownersPropertyList.get(owner2).add(property1);
    }

    private void undoExchange(Property property1, Property property2) {
        int owner1 = property1.getOwner();
        int owner2 = property2.getOwner();
        ownersPropertyList.get(owner1).remove(property2);
        ownersPropertyList.get(owner2).remove(property1);
        ownersPropertyList.get(owner1).add(property1);
        ownersPropertyList.get(owner2).add(property2);
    }

    public double calculateAverageAreaByOwner() {
        List<Double> ownerAreas = new ArrayList<>();
        for (int owner : ownersPropertyList.keySet()) {
            Set<Property> uniqueProperties = new HashSet<>();
            double totalArea = 0;
            List<Property> ownerProperties = ownersPropertyList.get(owner);

            for (Property property : ownerProperties) {
                if (property != null && uniqueProperties.add(property)) {
                    totalArea += property.getShapeArea();
                    Set<Property> connectedProperties = findConnectedProperties(property, ownerProperties,
                            new HashSet<>());
                    uniqueProperties.addAll(connectedProperties);
                    totalArea += connectedProperties.stream().mapToDouble(Property::getShapeArea).sum();
                }
            }

            if (!uniqueProperties.isEmpty()) {
                ownerAreas.add(totalArea / uniqueProperties.size());
            }
        }
        return ownerAreas.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    private Set<Property> findConnectedProperties(Property start, List<Property> properties, Set<Property> visited) {
        Set<Property> connectedProperties = new HashSet<>(visited);
        connectedProperties.add(start);
        for (Property property : properties) {
            if (property != null && !connectedProperties.contains(property) && start.isAdjacent(property)) {
                connectedProperties.addAll(findConnectedProperties(property, properties, connectedProperties));
            }
        }
        return connectedProperties;
    }

    public void executeExchanges(List<SuggestedExchange> suggestions) {
        for (SuggestedExchange exchange : suggestions) {
            executeExchange(exchange.getProperty1(), exchange.getProperty2());
        }
        newAverage = calculateAverageAreaByOwner();
    }

    public static void main(String[] args) {
        // Create sample data
        List<Property> properties = new ArrayList<>();
        try {
            Geometry geometry1 = new WKTReader().read("MULTIPOLYGON (((0 0, 0 100, 100 100, 100 0, 0 0)))");
            Geometry geometry2 = new WKTReader().read("MULTIPOLYGON (((0 0, 0 150, 150 150, 150 0, 0 0)))");
            Geometry geometry3 = new WKTReader().read("MULTIPOLYGON (((0 0, 0 75, 75 75, 75 0, 0 0)))");
            Geometry geometry4 = new WKTReader().read("MULTIPOLYGON (((0 0, 0 125, 125 125, 125 0, 0 0)))");
            Geometry geometry5 = new WKTReader().read("MULTIPOLYGON (((0 0, 0 50, 50 50, 50 0, 0 0)))");
            properties.add(
                    new Property(1, 1, 1, 100, 100, geometry1.toString(), 1, "Freguesia1", "Concelho1", "Distrito1"));
            properties.add(
                    new Property(2, 2, 2, 150, 150, geometry2.toString(), 1, "Freguesia1", "Concelho1", "Distrito1"));
            properties.add(
                    new Property(3, 3, 3, 75, 75, geometry3.toString(), 2, "Freguesia2", "Concelho1", "Distrito1"));
            properties.add(
                    new Property(4, 4, 4, 125, 125, geometry4.toString(), 2, "Freguesia3", "Concelho2", "Distrito1"));
            properties.add(
                    new Property(5, 5, 5, 50, 50, geometry5.toString(), 3, "Freguesia1", "Concelho1", "Distrito1"));
        } catch (Exception e) {
            System.err.println("Error creating sample data.");
        }

        Map<Integer, List<Property>> ownersPropertyList = new HashMap<>();
        for (Property property : properties) {
            ownersPropertyList.computeIfAbsent(property.getOwner(), k -> new ArrayList<>()).add(property);
        }

        // Initialize PropertyExchange
        PropertyExchange pe = new PropertyExchange(properties, ownersPropertyList, 0.75);

        // Generate suggestions
        List<SuggestedExchange> suggestions = pe.generateSugestions("Concelho1");

        // Print initial average
        System.out.println("Initial average area per owner: " + pe.calculateAverageAreaByOwner());

        // Execute exchanges
        pe.executeExchanges(suggestions);

        // Print new average
        System.out.println("New average area per owner after exchanges: " + pe.calculateAverageAreaByOwner());

        // Print some sample exchanges
        System.out.println("\nSample exchanges:");
        for (int i = 0; i < 5 && i < suggestions.size(); i++) {
            SuggestedExchange exchange = suggestions.get(i);
            System.out.println("Exchange " + (i + 1) + ":");
            System.out.println("  Property 1: " + exchange.getProperty1().getParNum());
            System.out.println("  Property 2: " + exchange.getProperty2().getParNum());
            System.out.println("  Area gained: " + exchange.getGain());
            System.out.println("  Probability of trade: " + exchange.getProbabilityOfTrade());
            System.out.println();
        }
    }
}