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

        // Attempt to parse the geometry and handle any parsing issues gracefully
        this.geometry = parseGeometry(geometryStr); // Parse geometry and extract coordinates
    }

    // Parse MULTIPOLYGON geometry from WKT string and populate coordinates set
    private Geometry parseGeometry(String geometryStr) {
        if (geometryStr == null || geometryStr.trim().isEmpty()) {
            System.err.println("Warning: Empty geometry string for Property ID " + objectid);
            return null; // Return null or handle as needed
        }

        GeometryFactory geometryFactory = new GeometryFactory();
        WKTReader reader = new WKTReader(geometryFactory);

        try {
            Geometry geom = reader.read(geometryStr);
            for (Coordinate coord : geom.getCoordinates()) {
                coordinates.add(coord); // Add each coordinate to the set
            }
            return geom;
        } catch (ParseException e) {
            System.err.println("Error parsing geometry for Property ID " + objectid + ": " + e.getMessage());
            return null; // Return null or handle as needed
        }
    }

    // Check adjacency based on shared coordinates
    public boolean isAdjacent(Property other) {
        if (this.geometry == null || other.geometry == null) {
            return false; // Cannot be adjacent if one of the geometries is null
        }

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
