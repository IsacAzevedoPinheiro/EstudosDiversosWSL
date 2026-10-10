package com.mycompany.analisapalavra;
import java.util.Scanner;
public class AnalisaPalavra{
    /*Escreva um programa em Java que analise uma palavra ou frase digitada pelo usuário e:
○ Exiba o número total de caracteres
○ Exiba tudo em maiúsculo
○ Exiba tudo em minúsculo
○ Conte quantas vezes a letra A aparece*/
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        System.out.println("Entre com a frase: ");
        String frase = teclado.nextLine();
        int tam = frase.replace(" ", "").length();
        System.out.println(tam);
        frase = frase.toUpperCase();
        System.out.println(frase);
        frase = frase.toLowerCase();
        System.out.println(frase);
        int quantA = 0;
        for (int i=0; i<tam; i++){
            if (frase.charAt(i) == 'a')
                quantA++;
        }
        System.out.println(quantA);
    }
}

