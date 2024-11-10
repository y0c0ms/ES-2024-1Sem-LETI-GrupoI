import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LeitorCSV {
    public static void carregarDados(String filePath, Grafo grafo) {
        String linha;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Ignora o cabeçalho
            br.readLine();
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                String objectId = dados[0];
                String parId = dados[1];
                String parNum = dados[2];
                double shapeLength = Double.parseDouble(dados[3]);
                double shapeArea = Double.parseDouble(dados[4]);
                String geometry = dados[5];
                String owner = dados[6];

                Propriedade propriedade = new Propriedade(objectId, parId, parNum, shapeLength, shapeArea, geometry,
                        owner);
                grafo.adicionarPropriedade(propriedade);

                // Adicione as conexões/adjacências se disponível no CSV
                // grafo.conectarPropriedades(objectId, adjId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}