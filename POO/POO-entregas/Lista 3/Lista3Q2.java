package com.mycompany.corrigindo;
import java.util.Scanner;
public class Corrigindo {
    /*Considere os programas a seguir, que leem um código repetidamente e imprimem o
código lido até que o código lido seja igual a -1. O código -1 não deve ser impresso.
a. Qual das duas soluções é a correta?
b. Como a solução incorreta poderia ser corrigida?*/
    public static void main(String[] args) {
//       a) O correto é o programa A
//       b) Existem 2 diferenças entre os dois, 
//       a primeira é o laço, um é do while, o outro 
//       é while do. A segunda diferença é que no 
//       programa A, temos o bloco de repetição 
//       completo apenas dentro do while, fora dele
//       (primeira execução), não temos o print com
//       o código. já no Programa B, o do while vai printar
//       o código da primeira tentativa independentemente
//       de qual seja, desde que seja int.
        Scanner teclado = new Scanner(System.in);
        int codigo;

        do {
            System.out.print("Informe o código: ");
            codigo = teclado.nextInt();
            if (codigo != -1)
                System.out.println("Código: " + codigo);
            else
                System.out.println("Inválido");
        } while (codigo != -1);
    }
}

