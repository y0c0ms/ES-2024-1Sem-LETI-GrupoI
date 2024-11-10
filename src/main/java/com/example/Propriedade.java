package main.java.com.example;

import java.util.ArrayList;
import java.util.List;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

public class Propriedade {
    private int objectId;
    private double parId;
    private String parNum;
    private double shapeLength;
    private double shapeArea;
    private String geometryWKT; // String que armazena a geometria no formato WKT (Well-Known Text)
    private String owner;

    // Geometria interpretada pela JTS (não armazenada diretamente, mas derivada de
    // geometryWKT)
    private Geometry geometry;

    // Construtor
    public Propriedade(int objectId, double parId, String parNum, double shapeLength, double shapeArea,
            String geometryWKT, String owner) {
        this.objectId = objectId;
        this.parId = parId;
        this.parNum = parNum;
        this.shapeLength = shapeLength;
        this.shapeArea = shapeArea;
        this.geometryWKT = geometryWKT;
        this.owner = owner;

        // Inicializando a geometria a partir do WKT
        try {
            WKTReader reader = new WKTReader();
            this.geometry = reader.read(geometryWKT);
        } catch (ParseException e) {
            System.err.println(
                    "Erro ao interpretar a geometria WKT para a propriedade ID: " + objectId + " - " + e.getMessage());
            this.geometry = null;
        }
    }

    // Getters e setters para os atributos principais
    public int getObjectId() {
        return objectId;
    }

    public double getParId() {
        return parId;
    }

    public String getParNum() {
        return parNum;
    }

    public double getShapeLength() {
        return shapeLength;
    }

    public double getShapeArea() {
        return shapeArea;
    }

    public String getGeometryWKT() {
        return geometryWKT;
    }

    public String getOwner() {
        return owner;
    }

    // Getter para a geometria JTS
    public Geometry getGeometry() {
        return geometry;
    }

    // Override do método equals e hashCode para garantir a comparação correta em
    // estruturas de dados, como Map ou Set
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        Propriedade that = (Propriedade) obj;

        return objectId == that.objectId && Double.compare(that.parId, parId) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = objectId;
        temp = Double.doubleToLongBits(parId);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Propriedade{" +
                "objectId=" + objectId +
                ", parId=" + parId +
                ", parNum='" + parNum + '\'' +
                ", shapeLength=" + shapeLength +
                ", shapeArea=" + shapeArea +
                ", geometryWKT='" + geometryWKT + '\'' +
                ", owner='" + owner + '\'' +
                '}';
    }
}