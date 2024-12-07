package com.example;

import java.util.ArrayList;
import java.util.List;

public class PropertyExchange {

    public static List<SuggestedExchange> generateSwapSuggestions(List<Property> properties, double potencialOfSwap) {
        List<SuggestedExchange> suggestions = new ArrayList<>();
        for (int i = 0; i < properties.size(); i++) {
            Property property1 = properties.get(i);
            for (int j = i + 1; j < properties.size(); j++) {
                Property property2 = properties.get(j);
                if (!(property1.getOwner() == (property2.getOwner()))) {
                    boolean isAlreadyAdjacent = false;
                    for (Property property : properties) {
                        if (property.getOwner() == (property1.getOwner()) && property.isAdjacent(property2)) {
                            isAlreadyAdjacent = true;
                            break;
                        }
                    }
                    if (!isAlreadyAdjacent) {
                        continue;
                    }
                }
                double area1 = property1.getShapeArea();
                double area2 = property2.getShapeArea();
                double maiorArea = Math.max(area1, area2);
                double menorArea = Math.min(area1, area2);
                double potential = (menorArea / maiorArea) * 100;
                if (potential >= potencialOfSwap && area1 > area2) {
                    suggestions.add(new SuggestedExchange(property1, property2, area1 - area2, potential));
                }
            }
        }
        suggestions.sort((s1, s2) -> Double.compare(s2.getProbabilityOfTrade(), s1.getProbabilityOfTrade()));
        return suggestions;
    }

    public static void Swap(List<Property> properties, List<SuggestedExchange> suggestions) {
        for (SuggestedExchange suggestion : suggestions) {
            int newOwnerAfterSwap = suggestion.getProperty2().getOwner();
            for (Property property : properties) {
                if (property.getObjectId() == suggestion.getProperty1().getObjectId()) {
                    property.setOwner(newOwnerAfterSwap);
                }
            }
        }
    }

}