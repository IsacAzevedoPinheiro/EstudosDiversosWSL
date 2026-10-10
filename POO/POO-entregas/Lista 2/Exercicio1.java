package com.mycompany.calculadora;
import java.lang.Math;
import java.util.Scanner;
public class Calculadora {
    //Leia duas variáveis inteiras e imprima a soma, subtração, multiplicação e
    //divisão entre elas.
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        System.out.println("Entre com o primeiro: ");
        int a = teclado.nextInt();
        System.out.println("Entre com o segundo: ");
        int b = teclado.nextInt();
        System.out.println("Essa é a doma: "+(a+b));
        System.out.println("Essa é a subtração: "+(a-b));
        System.out.println("Essa é a multiplicação: "+(a*b));
        if (b!=0)
            System.out.println("Essa é a divisão: "+(a/b));
        else
            System.out.println("B é 0");
        
    }
}
