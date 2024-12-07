package com.example;

import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;

public class CustomPolygon extends Polygon {
    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();

    /**
     * Constructs a CustomPolygon from a given Polygon.
     *
     * @param polygon The Polygon to be converted to CustomPolygon.
     */
    public CustomPolygon(Polygon polygon) {
        super(polygon.getExteriorRing(), extractInteriorRings(polygon), GEOMETRY_FACTORY);
    }

    /**
     * Extracts the interior rings of a Polygon as a LinearRing array.
     *
     * @param polygon The Polygon from which to extract interior rings.
     * @return An array of LinearRings representing the interior rings of the
     *         Polygon.
     */
    private static LinearRing[] extractInteriorRings(Polygon polygon) {
        int numInteriorRings = polygon.getNumInteriorRing();
        LinearRing[] interiorRings = new LinearRing[numInteriorRings];

        for (int i = 0; i < numInteriorRings; i++) {
            interiorRings[i] = polygon.getInteriorRingN(i);
        }

        return interiorRings;
    }

    /**
     * Creates a CustomPolygon from a WKT string with OBJECTID logging.
     *
     * @param wkt      The WKT string representing the geometry.
     * @param objectId The OBJECTID of the property for logging purposes.
     * @return A CustomPolygon created from the WKT string, or null if the WKT is
     *         invalid.
     */
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

    /**
     * Checks if this polygon is adjacent to another CustomPolygon.
     *
     * @param other The other CustomPolygon to check adjacency with.
     * @return True if the polygons are adjacent, false otherwise.
     */
    public boolean isAdjacent(CustomPolygon other) {
        return other != null && this.intersects(other);
    }
}