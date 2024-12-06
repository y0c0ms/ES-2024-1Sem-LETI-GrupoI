package com.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphOwners {
    private List<Property> allProperties;
    private Map<Integer, List<Integer>> neighborsList;
    private Map<Integer, List<Property>> ownersPropertyList; //funcional
    
    public GraphOwners(List<Property> allProperties, Map<Integer, List<Property>> ownersPropertyList){
        this.allProperties = allProperties;
        neighborsList = new HashMap<>();
        this.ownersPropertyList = ownersPropertyList;
        initializeNeighbours();
    }
    

    public void initializeNeighbours(){
        
    }


    public Map<Integer, List<Property>> getProperties(){
        return ownersPropertyList;
    }


    public static void main(String [] args){
        CSVReader csvr = new CSVReader();
        csvr.readCSV();
        GraphOwners go = new GraphOwners(csvr.createProperties(), csvr.getOwnersList());
        Map<Integer, List<Property>> map = go.getProperties();
        List<Property> list = map.get(92);
        for(Property p : list){
            System.out.println(p.getObjectId());
        }
        

    }

}
