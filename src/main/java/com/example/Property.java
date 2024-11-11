package com.example;
import org.locationtech.jts.geom.Geometry;

public class Property {
    private int objectid;
    private double par_id;
    private double par_num;
    private double shapeArea;
    private double shapeLength;
    private Geometry geometry;
    private int owner;

    public Property(int objectid, double par_id, double par_num, double shapeArea, double shapeLength, Geometry geometry, int owner){
        this.objectid = objectid;
        this.par_id= par_id;
        this.par_num = par_num;
        this.shapeArea = shapeArea;
        this.shapeLength = shapeLength;
        this.geometry = geometry;
        this.owner = owner;
    }

    //Utilizes the library geometry to confirm if the property1 and 2 are adjacent
    public boolean isAdjacent(Property p1, Property p2){
        Geometry g1 = p1.getGeometry();
        Geometry g2 = p2.getGeometry();
        if(g1.touches(g2)){
            return true;
        }
        return false;
    }

    //getters
    public int getObjectId(){
        return objectid;
    }

    public double getParId() {
        return par_id;
    }
    
    public double getParNum() {
        return par_num;
    }
    
    public double getShapeArea() {
        return shapeArea;
    }
    
    public double getShapeLength() {
        return shapeLength;
    }
    
    public Geometry getGeometry() {
        return geometry;
    }
    
    public int getOwner() {
        return owner;
    }
}
