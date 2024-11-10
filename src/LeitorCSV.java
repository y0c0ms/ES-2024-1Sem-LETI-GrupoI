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
                String[] dados = linha.split(",");
                String id = dados[0];
                String owner = dados[1];
                double area = Double.parseDouble(dados[2]);
                String freguesia = dados[3];
                String concelho = dados[4];
                String distrito = dados[5];

                Propriedade propriedade = new Propriedade(id, owner, area, freguesia, concelho, distrito);
                grafo.adicionarPropriedade(propriedade);

                // Adicione as conexões/adjacências se disponível no CSV
                // grafo.conectarPropriedades(id, adjId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
