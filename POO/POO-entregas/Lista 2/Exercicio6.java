package com.mycompany.converteemmoedas;
import java.lang.Math;
import java.util.Scanner;

public class ConverteEmMoedas {
    /*Faça um programa que para cada produto informado (nome, preço e
quantidade), escreva o nome do produto comprado e o valor total a ser pago, considerando
que são oferecidos descontos pelo número de unidades compradas, segundo as opções
abaixo:
a. Até 10 unidades: valor total
b. de 11 a 20 unidades: 10% de desconto
c. de 21 a 50 unidades: 20% de desconto
d. acima de 50 unidades: 25% de desconto*/
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        System.out.println("Entre com o nome: ");
        String nome = teclado.nextLine();
        System.out.println("Entre com o preço: ");
        float preco = teclado.nextInt();
        System.out.println("Entre com a quantidade :");
        int quant = teclado.nextInt();
        if (quant <= 10)
            System.out.println("Preço = "+preco);
        else if (11 <= quant && quant <= 20)
            System.out.println("Preço = "+(preco*0.9));
        else if (21 <= quant && quant <= 50)
            System.out.println("Preço = "+(preco*0.8));
        else
            System.out.println("Preço = "+(preco*0.75));
    }
}
