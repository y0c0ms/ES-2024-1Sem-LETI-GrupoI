package com.example;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;

public class Propriedade {
    private String objectId;
    private Geometry geometry;

    public Propriedade(String objectId, String wktGeometry) {
        this.objectId = objectId;
        try {
            WKTReader reader = new WKTReader();
            this.geometry = reader.read(wktGeometry);
        } catch (Exception e) {
            System.err.println("Erro: Geometria inválida para a propriedade ID: " + objectId);
            this.geometry = null;
        }
    }

    public String getObjectId() {
        return objectId;
    }

    public Geometry getGeometry() {
        return geometry;
    }
}