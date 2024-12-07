package com.example;

import org.locationtech.jts.geom.Geometry;

/**
 * This class represents a property with attributes such as geometry, owner, and location.
 * It includes methods to determine adjacency between properties and access property details.
 */
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

    /**
     * Constructs a Property instance.
     * @param objectid The unique identifier for the property.
     * @param par_id The parcel ID of the property.
     * @param par_num The parcel number of the property.
     * @param shapeArea The area of the property.
     * @param shapeLength The perimeter length of the property.
     * @param geometryStr The WKT representation of the property geometry.
     * @param owner The owner ID of the property.
     * @param freguesia The freguesia (parish) where the property is located.
     * @param municipio The municipio (municipality) where the property is located.
     * @param ilha The ilha (island) where the property is located.
     */
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

    /**
     * Determines if this property is adjacent to another property.
     * @param other The other property to check adjacency with.
     * @return True if the properties are adjacent, false otherwise.
     */
    public boolean isAdjacent(Property other) {
        return this.geometry != null && other.geometry != null && this.geometry.isAdjacent(other.geometry);
    }

    /**
     * Retrieves the OBJECTID of the property.
     * @return The OBJECTID of the property.
     */
    public int getObjectId() {
        return objectid;
    }

    /**
     * Retrieves the parcel ID of the property.
     * @return The parcel ID of the property.
     */
    public double getParId() {
        return par_id;
    }

    /**
     * Retrieves the parcel number of the property.
     * @return The parcel number of the property.
     */
    public double getParNum() {
        return par_num;
    }

    /**
     * Retrieves the area of the property.
     * @return The area of the property.
     */
    public double getShapeArea() {
        return shapeArea;
    }

    /**
     * Retrieves the perimeter length of the property.
     * @return The perimeter length of the property.
     */
    public double getShapeLength() {
        return shapeLength;
    }

    /**
     * Retrieves the geometry of the property.
     * @return The geometry of the property as a Geometry object.
     */
    public Geometry getGeometry() {
        return geometry;
    }

    /**
     * Retrieves the freguesia (parish) where the property is located.
     * @return The freguesia of the property.
     */
    public String getFreguesia() {
        return freguesia;
    }

    /**
     * Retrieves the municipio (municipality) where the property is located.
     * @return The municipio of the property.
     */
    public String getMunicipio() {
        return municipio;
    }

    /**
     * Retrieves the ilha (island) where the property is located.
     * @return The ilha of the property.
     */
    public String getDistrito() {
        return ilha;
    }

    /**
     * Retrieves the owner ID of the property.
     * @return The owner ID of the property.
     */
    public int getOwner() {
        return owner;
    }
}
