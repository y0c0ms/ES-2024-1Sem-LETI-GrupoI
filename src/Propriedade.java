import java.util.ArrayList;
import java.util.List;

public class Propriedade {
    private String objectId;
    private String parId;
    private String parNum;
    private double shapeLength;
    private double shapeArea;
    private String geometry;
    private String owner;
    private String area;
    private String tipo;
    private List<Propriedade> adjacentes;

    public Propriedade(String objectId, String parId, String parNum, double shapeLength, double shapeArea,
            String geometry, String owner) {
        this.objectId = objectId;
        this.parId = parId;
        this.parNum = parNum;
        this.shapeLength = shapeLength;
        this.shapeArea = shapeArea;
        this.geometry = geometry;
        this.owner = owner;
        this.adjacentes = new ArrayList<>();
    }

    public String getObjectId() {
        return objectId;
    }

    public String getParId() {
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

    public String getGeometry() {
        return geometry;
    }

    public String getOwner() {
        return owner;
    }

    public String getArea() {
        return area;
    }

    public String getTipo() {
        return tipo;
    }

    public List<Propriedade> getAdjacentes() {
        return adjacentes;
    }

    public void addAdjacente(Propriedade propriedade) {
        this.adjacentes.add(propriedade);
    }

    private boolean pertenceArea(Propriedade propriedade, String area, String tipo) {
        return propriedade.getArea().equalsIgnoreCase(area) && propriedade.getTipo().equalsIgnoreCase(tipo);
    }
}