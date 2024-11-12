package com.example;

import java.util.HashSet;
import java.util.Set;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

public class Property {
    private int objectid;
    private double par_id;
    private double par_num;
    private double shapeArea;
    private double shapeLength;
    private Geometry geometry;
    private int owner;
    private Set<Coordinate> coordinates;

    public Property(int objectid, double par_id, double par_num, double shapeArea, double shapeLength,
            String geometryStr, int owner) {
        this.objectid = objectid;
        this.par_id = par_id;
        this.par_num = par_num;
        this.shapeArea = shapeArea;
        this.shapeLength = shapeLength;
        this.owner = owner;
        this.coordinates = new HashSet<>();
        this.geometry = parseGeometry(geometryStr); // Parse geometry and extract coordinates
    }

    // Parse MULTIPOLYGON geometry from WKT string and populate coordinates set
    private Geometry parseGeometry(String geometryStr) {
        GeometryFactory geometryFactory = new GeometryFactory();
        WKTReader reader = new WKTReader(geometryFactory);
        try {
            Geometry geom = reader.read(geometryStr);
            for (Coordinate coord : geom.getCoordinates()) {
                coordinates.add(coord); // Add each coordinate to the set
            }
            return geom;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Check adjacency based on shared coordinates
    public boolean isAdjacent(Property other) {
        for (Coordinate coord : this.coordinates) {
            if (other.coordinates.contains(coord)) {
                return true; // They are adjacent if any coordinate is shared
            }
        }
        return false;
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

    public int getOwner() {
        return owner;
    }
}
