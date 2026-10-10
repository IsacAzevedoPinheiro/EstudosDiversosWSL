package com.mycompany.parafloat;
import java.lang.Math;
import java.util.Scanner;
public class ParaFloat {
    //Altere o tipo das duas variáveis do exercício 1 para ponto flutuante.
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        System.out.println("Entre com o primeiro: ");
        float a = teclado.nextFloat();
        System.out.println("Entre com o segundo: ");
        float b = teclado.nextFloat();
        System.out.println("Essa é a doma: "+(a+b));
        System.out.println("Essa é a subtração: "+(a-b));
        System.out.println("Essa é a multiplicação: "+(a*b));
        if (b!=0.0)
            System.out.println("Essa é a divisão: "+(a/b));
        else
            System.out.println("B é 0");
        
    }
}
