package com.example;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;

public class Propriedade {
    private String objectId;
    private Geometry geometry;

    public Propriedade(String objectId, String wktGeometry) {
        this.objectId = objectId;
        if (wktGeometry == null || wktGeometry.trim().isEmpty()) {
            System.err.println("Aviso: Geometria WKT nula ou vazia para a propriedade ID: " + objectId);
            this.geometry = null;
        } else {
            try {
                WKTReader reader = new WKTReader();
                this.geometry = reader.read(wktGeometry);
            } catch (ParseException e) {
                System.err.println("Erro ao interpretar a geometria WKT para a propriedade ID: " + objectId + " - "
                        + e.getMessage());
                this.geometry = null;
            }
        }
    }

    public String getObjectId() {
        return objectId;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public String getGeometryWKT() {
        if (geometry != null) {
            WKTWriter writer = new WKTWriter();
            return writer.write(geometry);
        }
        return null;
    }
}