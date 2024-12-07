package com.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PropertyExchange {

    private Map<Integer, List<Property>> ownersPropertyList;

    public List<SuggestedExchange> suggestPropertyExchanges(List<Property> properties) {
        List<SuggestedExchange> suggestions = new ArrayList<>();

        // Agrupar propriedades por proprietário
        ownersPropertyList = properties.stream()
                .collect(Collectors.groupingBy(Property::getOwner));

        // Calcular a área média inicial de cada proprietário
        Map<Integer, Double> initialAverageArea = calculateAverageAreaByOwner(ownersPropertyList);

        // Gerar todas as combinações possíveis de propriedades para troca
        for (int i = 0; i < properties.size(); i++) {
            Property property1 = properties.get(i);

            for (int j = i + 1; j < properties.size(); j++) {
                Property property2 = properties.get(j);

                // Propriedades precisam ser de donos diferentes
                if (!(property1.getOwner() == property2.getOwner())) {
                    // Avaliar a troca
                    double potentialGain1 = calculatePotentialGain(property1, property2, initialAverageArea);
                    double potentialGain2 = calculatePotentialGain(property2, property1, initialAverageArea);

                    // Se a troca for vantajosa para ambos
                    if (potentialGain1 > 0 && potentialGain2 > 0) {
                        double similarityScore = calculateSimilarityScore(property1, property2);
                        suggestions.add(new SuggestedExchange(property1, property2, potentialGain1, potentialGain2,
                                similarityScore));
                    }
                }
            }
        }

        // Ordenar sugestões por viabilidade (priorizando maior ganho e menor diferença
        // de área)
        suggestions.sort(Comparator.comparingDouble(SuggestedExchange::getTotalGain).reversed()
                .thenComparingDouble(SuggestedExchange::getSimilarityScore));

        return suggestions;
    }

    private Map<Integer, Double> calculateAverageAreaByOwner(Map<Integer, List<Property>> propertiesByOwner) {
        return propertiesByOwner.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey(), // Define explicitamente a chave (Integer)
                        entry -> entry.getValue().stream()
                                .mapToDouble(Property::getShapeArea)
                                .average()
                                .orElse(0.0) // Calcula a média ou usa 0.0 se vazio
                ));
    }

    private double calculatePotentialGain(Property giving, Property receiving, Map<Integer, Double> averageArea) {
        double currentAverage = averageArea.getOrDefault(giving.getOwner(), 0.0);
        double newAverage = (currentAverage * ownersPropertyList.get(giving.getOwner()).size() - giving.getShapeArea()
                + receiving.getShapeArea())
                / ownersPropertyList.get(giving.getOwner()).size();
        return newAverage - currentAverage;
    }

    private double calculateSimilarityScore(Property property1, Property property2) {
        return 1.0 / (1.0 + Math.abs(property1.getShapeArea() - property2.getShapeArea()));
    }

    public static class SuggestedExchange {
        private final Property property1;
        private final Property property2;
        private final double gain1;
        private final double gain2;
        private final double similarityScore;

        public SuggestedExchange(Property property1, Property property2, double gain1, double gain2,
                double similarityScore) {
            this.property1 = property1;
            this.property2 = property2;
            this.gain1 = gain1;
            this.gain2 = gain2;
            this.similarityScore = similarityScore;
        }

        public double getTotalGain() {
            return gain1 + gain2;
        }

        public double getSimilarityScore() {
            return similarityScore;
        }

        @Override
        public String toString() {
            return String.format("Exchange %s ↔ %s [Gain1: %.2f, Gain2: %.2f, Similarity: %.2f]",
                    property1, property2, gain1, gain2, similarityScore);
        }
    }
}