package com.example;

import java.util.List;
import java.util.Map;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
public class GraphOwners {
    private List<Property> allProperties;
    private Map<Integer, List<Property>> ownersPropertyList;
    private Graph<Integer, DefaultEdge> ownerGraph;

    public GraphOwners(List<Property> allProperties, Map<Integer, List<Property>> ownersPropertyList) {
        this.allProperties = allProperties;
        this.ownersPropertyList = ownersPropertyList;
        this.ownerGraph = new SimpleGraph<>(DefaultEdge.class);
        initializeOwnerGraph();
    }

    private void initializeOwnerGraph() {
        for (Integer owner : ownersPropertyList.keySet()) {
            ownerGraph.addVertex(owner);
        }

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

    public Graph<Integer, DefaultEdge> getOwnerGraph() {
        return ownerGraph;
    }

}