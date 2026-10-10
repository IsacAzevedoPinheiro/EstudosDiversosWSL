package com.mycompany.passaalistaprafrente;
import java.util.Scanner;
public class PassaAListaPraFrente {
    /*Crie um programa para ler um número n e um vetor v com n números inteiros. Depois,
ler mais um número k e um número p. Em seguida, inserir o número k na posição p do
vetor, deslocando os números da posição p em diante para frente uma posição (o
último número será perdido) e imprimir o vetor resultante. Caso p seja inválido
apresente a mensagem "Posição inválida".*/
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        System.out.println("Entre com n: ");
        int n = teclado.nextInt();
        int[] v = new int[n];
        for (int i=0; i<n; i++){
            System.out.println("Entre com o número: "+(i+1)+" :");
            int num = teclado.nextInt();
            v[i] = num;
        }
        System.out.println("Entre com k: ");
        int k = teclado.nextInt();
        System.out.println("Entre com p: ");
        int p = teclado.nextInt();
        if (p > n)
            System.out.println("P inválido");
        else{
            int passos = n - p;
            for (int j=p+1; j<passos; j++){
                v[j+1] = v[j];
            }
            v[p] = k;
            System.out.println("{");
            for (int l=0; l<n; l++)
                System.out.printf("%d ", v[l]);
            System.out.printf("}");
        }
    }
}

