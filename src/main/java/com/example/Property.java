package com.example;

import org.locationtech.jts.geom.Geometry;

public class Property {
    private int objectid;
    private double par_id;
    private double par_num;
    private double shapeArea;
    private double shapeLength;
    private CustomPolygon geometry;
    private int owner;
    private String freguesia;
    private String municipio;
    private String ilha;

    public Property(int objectid, double par_id, double par_num, double shapeArea, double shapeLength,
            String geometryStr, int owner, String freguesia, String municipio, String ilha) {
        this.objectid = objectid;
        this.par_id = par_id;
        this.par_num = par_num;
        this.shapeArea = shapeArea;
        this.shapeLength = shapeLength;
        this.owner = owner;
        this.freguesia = freguesia;
        this.municipio = municipio;
        this.ilha = ilha;
        this.geometry = CustomPolygon.fromWKT(geometryStr, objectid); // Updated call

        if (this.geometry == null) {
            System.err.println("Warning: Property with OBJECTID " + objectid + " has no valid geometry.");
        }
    }

    public boolean isAdjacent(Property other) {
        return this.geometry != null && other.geometry != null && this.geometry.isAdjacent(other.geometry);
    }

    // Getters
    public int getObjectId() {
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

    public String getFreguesia() {
        return freguesia;
    }

    public String getMunicipio() {
        return municipio;
    }

    public String getDistrito() {
        return ilha;
    }

    public int getOwner() {
        return owner;
    }
}
