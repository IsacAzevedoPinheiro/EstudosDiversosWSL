package com.mycompany.ordenandopessoas;
import java.util.Scanner;
public class OrdenandoPessoas {
    /*Leia o nome e a idade de 10 pessoas e liste as pessoas
– Em ordem alfabética
– Em ordem crescente de idade*/
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        String[] nomes = new String[10];
        int[] idades = new int[10];
        for (int q=0; q<10; q++){
            System.out.println("Entre com o nome "+(q+1));
            String nome = teclado.nextLine();
            nomes[q] = nome;
        }
        for (int y=0; y<10; y++){
            System.out.println("Entre com a idade "+(y+1));
            int idade = teclado.nextInt();
            idades[y] = idade;
        }
        for (int i=0; i<10; i++){
            for (int j=0; j<9; j++){
                if (nomes[i].compareTo(nomes[j]) < 0){
                    String aux = nomes[j];
                    nomes[j] = nomes[i];
                    nomes[i] = aux;
                    
                    int aux2 = idades[j];
                    idades[j] = idades[i];
                    idades[i] = aux2;
                }
            }
        }
        System.out.println("");
        System.out.println("");
        for (int g=0; g<10; g++)
            System.out.println(nomes[g]+" "+idades[g]);
        for (int i=0; i<10; i++){
            for (int j=0; j<9; j++){
                if (idades[i] < idades[j]){
                    String aux = nomes[j];
                    nomes[j] = nomes[i];
                    nomes[i] = aux;
                    
                    int aux2 = idades[j];
                    idades[j] = idades[i];
                    idades[i] = aux2;
                }
            }
        }
        System.out.println("");
        System.out.println("");
        for (int g=0; g<10; g++)
            System.out.println(nomes[g]+" "+idades[g]);
    }
}
