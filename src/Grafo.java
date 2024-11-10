import java.util.*;

public class Grafo {
    private Map<String, Propriedade> propriedades;
    private Map<String, List<String>> adjacencias;

    public Grafo() {
        this.propriedades = new HashMap<>();
        this.adjacencias = new HashMap<>();
    }

    public void adicionarPropriedade(Propriedade propriedade) {
        propriedades.put(propriedade.getObjectId(), propriedade);
        adjacencias.putIfAbsent(propriedade.getObjectId(), new ArrayList<>());
    }

    public void conectarPropriedades(String id1, String id2) {
        adjacencias.get(id1).add(id2);
        adjacencias.get(id2).add(id1);
    }

    public double calcularAreaMedia(String area, String tipo) {
        double somaArea = 0;
        int contador = 0;

        for (Propriedade propriedade : propriedades.values()) {
            // Assuming we have a method to check if the property belongs to the specified
            // area
            if (pertenceArea(propriedade, area, tipo)) {
                somaArea += propriedade.getShapeArea();
                contador++;
            }
        }

        return contador > 0 ? somaArea / contador : 0;
    }

    private boolean pertenceArea(Propriedade propriedade, String area, String tipo) {
        // Implement logic to check if the property belongs to the specified area
        // This could be based on additional attributes in the Propriedade class
        return true; // Placeholder
    }
}