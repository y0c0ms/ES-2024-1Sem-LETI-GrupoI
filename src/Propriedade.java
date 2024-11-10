import java.util.ArrayList;
import java.util.List;

public class Propriedade {
    private String id;
    private String owner;
    private double area;
    private String freguesia;
    private String concelho;
    private String distrito;
    private List<Propriedade> adjacentes;

    public Propriedade(String id, String owner, double area, String freguesia, String concelho, String distrito) {
        this.id = id;
        this.owner = owner;
        this.area = area;
        this.freguesia = freguesia;
        this.concelho = concelho;
        this.distrito = distrito;
        this.adjacentes = new ArrayList<>();
    }

    public void addAdjacente(Propriedade propriedade) {
        this.adjacentes.add(propriedade);
    }

    public double getArea() {
        return area;
    }

    public String getFreguesia() {
        return freguesia;
    }

    public String getConcelho() {
        return concelho;
    }

    public String getDistrito() {
        return distrito;
    }

    public String getId() {
        return id;
    }

    // getters e outros métodos se necessários
}
