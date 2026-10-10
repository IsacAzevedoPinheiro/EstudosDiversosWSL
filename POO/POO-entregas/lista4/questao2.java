package com.mycompany.procuraknovet;
import java.util.Scanner;
public class ProcuraKNoVet {
    /*Crie um programa para ler um número n e depois ler um vetor v com n números
inteiros. Ao final, ler mais um número k e informar e que posições do vetor v aparece o
número k. Caso k não exista no vetor v apresente a mensagem "Número não
encontrado".*/
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        int n = teclado.nextInt();
        float[] v = new float[n];
        for (int i=0; i<n; i++){
            System.out.println("Entre com a nota do aluno "+(i+1)+":");
            float nota = teclado.nextFloat();
            v[i] = nota;
        }
        System.out.println("Entre com k: ");
        float k = teclado.nextFloat();
        int apareceu = 0;
        for (int j = 0; j<n; j++){
            if (v[j] == k){
                System.out.println("Aparece na posição: "+(j+1));
                apareceu += 1;
            }
        }
        if (apareceu == 0){
            System.out.println("Posição inválida");
        }
    }
}

