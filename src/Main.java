import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Grafo grafo = new Grafo();
        String caminhoCSV = "D:\\ProjEngSoft\\Madeira-Moodle.csv";
        // Carregar dados do CSV para o grafo
        LeitorCSV.carregarDados(caminhoCSV, grafo);

        Scanner scanner = new Scanner(System.in);

        // Solicitar ao usuário a área geográfica/administrativa e o tipo
        System.out.println("Escolha a área geográfica/administrativa:");
        System.out.println("1. Freguesia");
        System.out.println("2. Concelho");
        System.out.println("3. Distrito");
        int escolha = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String tipo = getAreaTipo(escolha);

        if (tipo != null) {
            System.out.println("Digite o nome da " + tipo + ":");
            String area = scanner.nextLine();

            // Calcular a área média para a área e tipo especificados
            double areaMedia = grafo.calcularAreaMedia(area, tipo);
            System.out.println("Área média para a " + tipo + " " + area + ": " + areaMedia);
        } else {
            System.out.println("Escolha inválida.");
        }

        scanner.close();
    }

    private static String getAreaTipo(int escolha) {
        switch (escolha) {
            case 1:
                return "Freguesia";
            case 2:
                return "Concelho";
            case 3:
                return "Distrito";
            default:
                return null;
        }
    }
}