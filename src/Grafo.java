import java.util.HashMap;
import java.util.Map;

public class Grafo {
    private Map<String, Propriedade> propriedades;

    public Grafo() {
        this.propriedades = new HashMap<>();
    }

    public void adicionarPropriedade(Propriedade propriedade) {
        propriedades.put(propriedade.getId(), propriedade);
    }

    public void conectarPropriedades(String id1, String id2) {
        Propriedade p1 = propriedades.get(id1);
        Propriedade p2 = propriedades.get(id2);
        if (p1 != null && p2 != null) {
            p1.addAdjacente(p2);
            p2.addAdjacente(p1);
        }
    }

    public double calcularAreaMedia(String localidade, String nivel) {
        double totalArea = 0;
        int contador = 0;

        for (Propriedade p : propriedades.values()) {
            if ((nivel.equals("freguesia") && p.getFreguesia().equals(localidade)) ||
                    (nivel.equals("concelho") && p.getConcelho().equals(localidade)) ||
                    (nivel.equals("distrito") && p.getDistrito().equals(localidade))) {
                totalArea += p.getArea();
                contador++;
            }
        }
        return contador > 0 ? totalArea / contador : 0;
    }

    // Outros métodos para manipulação do grafo
}
