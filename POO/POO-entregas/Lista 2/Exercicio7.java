package com.mycompany.intervalo;
import java.lang.Math;
import java.util.Scanner;

public class Intervalo {
    //Leia um número inteiro n e mais dois números (maior ou menor) que formam
    //um intervalo. Ao final, imprima uma mensagem informando se n está antes, dentro ou
depois do intervalo.
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        System.out.println("Entre com os 3 nums: ");
        int num = teclado.nextInt();
        int inter1 = teclado.nextInt();
        int inter2 = teclado.nextInt();
        if (num >= inter1 && num <= inter2)
            System.out.println("Está no intervalo");
        else
            System.out.println("Não está no intervalo");
    }
}
