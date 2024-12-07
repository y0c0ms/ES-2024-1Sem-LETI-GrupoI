package com.example;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

public class AreaCalculator {
    private List<Property> properties;

    public AreaCalculator(List<Property> properties) {
        this.properties = properties;
    }

    /**
     * Filters properties by a specific region type and name (e.g., Freguesia,
     * Concelho, Distrito).
     *
     * @param regionType The type of the region ("Freguesia", "Concelho",
     *                   "Distrito").
     * @param regionName The name of the region to filter.
     * @return A list of filtered properties.
     */
    private List<Property> filterPropertiesByRegion(String regionType, String regionName) {
        return properties.stream()
                .filter(property -> {
                    switch (regionType.toLowerCase()) {
                        case "freguesia":
                            return property.getFreguesia().equalsIgnoreCase(regionName);
                        case "concelho":
                            return property.getMunicipio().equalsIgnoreCase(regionName);
                        case "distrito":
                            return property.getDistrito().equalsIgnoreCase(regionName);
                        default:
                            return false;
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * Calculates the average area of properties in a specific region.
     *
     * @param regionType The type of the region ("Freguesia", "Concelho",
     *                   "Distrito").
     * @param regionName The name of the region to filter.
     * @return The average area of properties in the region.
     */
    public double calculateAverageArea(String regionType, String regionName) {
        List<Property> filteredProperties = filterPropertiesByRegion(regionType, regionName);

        if (filteredProperties.isEmpty()) {
            return 0.0;
        }

        double totalArea = filteredProperties.stream()
                .mapToDouble(Property::getShapeArea)
                .sum();

        return totalArea / filteredProperties.size();
    }

    /**
     * Calculates the average area of properties, treating adjacent properties with
     * the same owner as a single property.
     *
     * @param regionType The type of the region ("Freguesia", "Concelho",
     *                   "Distrito").
     * @param regionName The name of the region to filter.
     * @return The average area of grouped properties in the region.
     */
    public double calculateAverageAreaWithGroups(String regionType, String regionName) {
        // Filter properties for the given region
        List<Property> filteredProperties = properties.stream()
                .filter(property -> {
                    switch (regionType.toLowerCase()) {
                        case "freguesia":
                            return property.getFreguesia().equalsIgnoreCase(regionName);
                        case "concelho":
                            return property.getMunicipio().equalsIgnoreCase(regionName);
                        case "distrito":
                            return property.getDistrito().equalsIgnoreCase(regionName);
                        default:
                            return false;
                    }
                })
                .toList();

        // Group adjacent properties by the same owner
        List<Set<Property>> groupedProperties = groupAdjacentPropertiesByOwner(filteredProperties);

        // Calculate total area from grouped properties
        double totalArea = groupedProperties.stream()
                .mapToDouble(group -> group.stream().mapToDouble(Property::getShapeArea).sum())
                .sum();

        // Calculate and return average area
        return groupedProperties.isEmpty() ? 0 : totalArea / groupedProperties.size();
    }

    /**
     * Retrieves the unique region names based on the region type.
     *
     * @param regionType The type of the region ("Freguesia", "Concelho",
     *                   "Distrito").
     * @return A list of unique region names.
     */
    public List<String> getUniqueRegions(String regionType) {
        return properties.stream()
                .map(property -> {
                    switch (regionType.toLowerCase()) {
                        case "freguesia":
                            return property.getFreguesia();
                        case "concelho":
                            return property.getMunicipio();
                        case "distrito":
                            return property.getDistrito();
                        default:
                            return null;
                    }
                })
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    private List<Set<Property>> groupAdjacentPropertiesByOwner(List<Property> properties) {
        List<Set<Property>> groups = new ArrayList<>();
        Set<Property> visited = new HashSet<>();

        for (Property property : properties) {
            if (!visited.contains(property)) {
                // Start a new group
                Set<Property> group = new HashSet<>();
                exploreGroup(property, properties, group, visited);
                groups.add(group);
            }
        }

        return groups;
    }

    // Helper method to explore and group adjacent properties recursively
    private void exploreGroup(Property property, List<Property> allProperties, Set<Property> group,
            Set<Property> visited) {
        if (visited.contains(property))
            return;

        visited.add(property);
        group.add(property);

        // Find adjacent properties with the same owner
        for (Property other : allProperties) {
            if (!visited.contains(other) &&
                    property.getOwner() == other.getOwner() &&
                    property.isAdjacent(other)) {
                exploreGroup(other, allProperties, group, visited);
            }
        }
    }

    public static void main(String [] args){
        CSVReader r = new CSVReader();
        r.readCSV();
        List<Property> l = r.createProperties();
        AreaCalculator c = new AreaCalculator(l);
        List<Set<Property>> list = c.groupAdjacentPropertiesByOwner(l);
        for(Set<Property> set: list){
            for(Property p: set){
                System.out.println("Owner: " + p.getOwner() + " pId: " + p.getObjectId());
            }
            System.out.println("-------------------");
        }

    }
}
