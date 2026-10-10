import java.util.Scanner;
import java.util.InputMismatchException;

public class Somatorio {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int soma = 0;
        while (true) {
            try {
                System.out.print("Digite um número: ");
                int num = sc.nextInt();
                if (num <= 0) break;
                soma += num;
            } catch (InputMismatchException e) {
                System.out.println("Erro: Entrada inválida. Digite um número inteiro.");
                sc.next(); // Limpar buffer
            }
        }
        System.out.println("Somatório: " + soma);
    }
}