package com.example;


import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;

public class CustomPolygon extends Polygon {
    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();

    public CustomPolygon(Polygon polygon) {
        super(polygon.getExteriorRing(), extractInteriorRings(polygon), GEOMETRY_FACTORY);
    }

    // Extract the interior rings as a LinearRing array
    private static LinearRing[] extractInteriorRings(Polygon polygon) {
        int numInteriorRings = polygon.getNumInteriorRing();
        LinearRing[] interiorRings = new LinearRing[numInteriorRings];

        for (int i = 0; i < numInteriorRings; i++) {
            interiorRings[i] = (LinearRing) polygon.getInteriorRingN(i);
        }

        return interiorRings;
    }

    // Create a CustomPolygon from a WKT string with OBJECTID logging
    public static CustomPolygon fromWKT(String wkt, int objectId) {
        try {
            if (wkt == null || wkt.trim().isEmpty() || wkt.equalsIgnoreCase("MULTIPOLYGON EMPTY")) {
                System.err.println(
                        "Warning: Property with OBJECTID " + objectId + " has an empty or invalid MULTIPOLYGON.");
                return null;
            }

            WKTReader reader = new WKTReader(GEOMETRY_FACTORY);
            MultiPolygon multiPolygon = (MultiPolygon) reader.read(wkt);

            // Assume it's a single polygon within the MULTIPOLYGON
            if (multiPolygon.getNumGeometries() > 0) {
                Polygon polygon = (Polygon) multiPolygon.getGeometryN(0);
                return new CustomPolygon(polygon);
            }

            System.err.println("Warning: Property with OBJECTID " + objectId + " has an invalid MULTIPOLYGON.");
            return null;
        } catch (ParseException e) {
            System.err.println("Error parsing WKT for OBJECTID " + objectId + ": " + e.getMessage());
            return null;
        }
    }

    // Check if this polygon is adjacent to another
    public boolean isAdjacent(CustomPolygon other) {
        return other != null && this.intersects(other);
    }
}
