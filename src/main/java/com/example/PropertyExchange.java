package com.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class representing the property exchange logic.
 */
public class PropertyExchange {
    private final Map<Integer, List<Property>> ownersPropertyList;
    private final List<Property> properties;
    private double previousAverage;
    private double newAverage;

    /**
     * Constructor for PropertyExchange.
     *
     * @param properties         The list of properties.
     * @param ownersPropertyList The map of owners and their properties.
     * @param potencialOfSwap    The potential of swap threshold.
     */
    public PropertyExchange(List<Property> properties, Map<Integer, List<Property>> ownersPropertyList,
            double potencialOfSwap) {
        this.properties = properties;
        this.ownersPropertyList = ownersPropertyList;
        this.previousAverage = calculateAverageAreaByOwner();
        this.newAverage = calculateNewAverageAreaByOwner(potencialOfSwap);
    }

    /**
     * Gets the previous average area per owner.
     *
     * @return The previous average area per owner.
     */
    public double getPreviousAverage() {
        return previousAverage;
    }

    /**
     * Generates swap suggestions based on the properties and potential of swap.
     *
     * @param properties      The list of properties.
     * @param potencialOfSwap The potential of swap threshold.
     * @return A list of suggested exchanges.
     */
    public static List<SuggestedExchange> generateSwapSuggestions(List<Property> properties, double potencialOfSwap) {
        List<SuggestedExchange> suggestions = new ArrayList<>();

        // Iterate over all pairs of properties
        for (int i = 0; i < properties.size(); i++) {
            Property property1 = properties.get(i);
            for (int j = i + 1; j < properties.size(); j++) {
                Property property2 = properties.get(j);

                // Skip pairs with the same owner
                if (property1.getOwner() == (property2.getOwner())) {
                    continue;
                }

                // Check if property2 is adjacent to any property owned by property1's owner
                boolean hasAdjacentToOwner = false;
                for (Property property : properties) {
                    if (property.getOwner() == (property1.getOwner()) && property.isAdjacent(property2)) {
                        hasAdjacentToOwner = true;
                        break;
                    }
                }

                // Skip if no adjacency to the owner
                if (!hasAdjacentToOwner) {
                    continue;
                }

                try {
                    // Calculate swap potential based on the areas of the two properties
                    double area1 = property1.getShapeArea();
                    double area2 = property2.getShapeArea();
                    double maiorArea = Math.max(area1, area2);
                    double menorArea = Math.min(area1, area2);
                    double potential = (menorArea / maiorArea) * 100;

                    // Add the swap suggestion if the potential meets the threshold (>= 0.75)
                    if (potential >= potencialOfSwap && area1 > area2) {
                        suggestions.add(new SuggestedExchange(property1, property2, area1 - area2, potential));
                    }
                } catch (NumberFormatException e) {
                    // Log error for invalid area values
                    System.err.println("Invalid area value for properties " + property1.getObjectId() +
                            " or " + property2.getObjectId());
                }
            }
        }

        // Sort suggestions by potential in descending order
        suggestions.sort((s1, s2) -> Double.compare(s2.getProbabilityOfTrade(), s1.getProbabilityOfTrade()));

        return suggestions;
    }

    /**
     * Applies the swaps based on the suggested exchanges.
     *
     * @param properties  The list of properties.
     * @param suggestions The list of suggested exchanges.
     */
    public static void applySwaps(List<Property> properties, List<SuggestedExchange> suggestions) {
        // Iterate through the list of swap suggestions
        for (SuggestedExchange suggestion : suggestions) {
            int newOwner = suggestion.getProperty2().getOwner(); // New owner for property1
            for (Property property : properties) {
                // Update the owner of property1 to the owner of property2
                if (property.getObjectId() == suggestion.getProperty1().getObjectId()) {
                    property.setOwner(newOwner);
                }
            }
        }
    }

    /**
     * Retrieves the set of owner IDs involved in the swap suggestions.
     *
     * @param suggestions The list of PropertySwapSuggestion objects representing
     *                    potential swaps.
     * @return A set of owner IDs involved in the swaps.
     */
    public static Set<Integer> getInvolvedOwners(List<SuggestedExchange> suggestions) {
        Set<Integer> owners = new HashSet<>();
        // Collect unique owner IDs from the suggestions
        for (SuggestedExchange suggestion : suggestions) {
            owners.add(suggestion.getProperty1().getOwner());
            owners.add(suggestion.getProperty2().getOwner());
        }
        return owners;
    }

    /**
     * Calculates the average area per owner.
     *
     * @return The average area per owner.
     */
    private double calculateAverageAreaByOwner() {
        List<Double> ownerAreas = new ArrayList<>();
        for (int owner : ownersPropertyList.keySet()) {
            Set<Property> uniqueProperties = new HashSet<>();
            double totalArea = 0;
            List<Property> ownerProperties = new ArrayList<>(ownersPropertyList.get(owner));

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

    /**
     * Calculates the new average area per owner after potential swaps.
     *
     * @param potencialOfSwap The potential of swap threshold.
     * @return The new average area per owner.
     */
    private double calculateNewAverageAreaByOwner(double potencialOfSwap) {
        List<SuggestedExchange> suggestions = generateSwapSuggestions(properties, potencialOfSwap);
        applySwaps(properties, suggestions);
        return calculateAverageAreaByOwner();
    }

    /**
     * Finds connected properties for a given property.
     *
     * @param start      The starting property.
     * @param properties The list of properties.
     * @param visited    The set of visited properties.
     * @return A set of connected properties.
     */
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
}