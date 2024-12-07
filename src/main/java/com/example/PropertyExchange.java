package com.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        //this.previousAverage = calculateAverageAreaByOwner();
    }

    public double getPreviousAverage(){
        return previousAverage;
    }
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
                   if (potential >= potencialOfSwap && area1<area2) {
                       suggestions.add(new SuggestedExchange(property1, property2, area1 - area2,potential));
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
    * @param suggestions The list of PropertySwapSuggestion objects representing potential swaps.
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
}