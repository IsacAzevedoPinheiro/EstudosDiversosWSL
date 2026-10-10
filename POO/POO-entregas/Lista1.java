package com.mycompany.lista1;
import java.util.Scanner;
public class Lista1{
    //nomeação de classe dtem que seseguir PascalCase
    //camelCase para métodos UPPER_SNAKE_CASE para constantes, esse eu lembrava
    public static void main(String[] args){
        //1 - Crie um programa que peça ao usuário para digitar seu nome e, em seguida, exiba uma
        //mensagem personalizada com o nome informado.
        Scanner teclado = new Scanner(System.in);
        System.out.print("Digite seu nome: ");
        String nome = teclado.nextLine();
        System.out.println("O nome é: "+nome+"\n");  
        
        //2 - Crie um programa que converta uma temperatura fornecida em graus Celsius para
        //Fahrenheit usando a fórmula:
        Scanner teclado1 = new Scanner(System.in);
        System.out.print("Digite a temperatura em Celsius: ");
        float temp = teclado1.nextFloat();
        float antigo = temp;
        temp = (temp*1.8f)+32;
        System.out.println(antigo+"*C equivale a: "+temp+"*F\n");
        Scanner teclado2 = new Scanner(System.in);
        
        //3 - Crie um programa que peça um número ao usuário e diga se ele é par ou ímpar.
        System.out.print("Entre com o número: ");
        int num = teclado2.nextInt();
        if (num%2 == 0){
            System.out.println("É par\n");
        }
        else{
            System.out.println("É ímpar\n");
        }
        
        
        //4 - Crie um programa para, a partir de um valor informado em centavos, indicar a menor
        //quantidade de moedas que representa esse valor
        Scanner teclado3 = new Scanner(System.in);
        System.out.print("Entre com uma quantidade de dinheiro: ");
        float real = teclado3.nextFloat();
        
        int centavos = Math.round(real * 100); //Método que trunca o número, retorna int de 32 bit
        int Um = centavos/100;
        centavos = centavos%100;
        int Cin = centavos/50;
        centavos = centavos%50;
        int Vin = centavos/25;
        centavos = centavos%25;
        int Dez = centavos/10;
        centavos = centavos%10;
        int Co = centavos/5;
        centavos = centavos%5;
        int um = centavos;
        
        System.out.println("Moedas de um: "+Um);
        System.out.println("Moedas de 50: "+Cin);
        System.out.println("Moedas de 25: "+Vin);
        System.out.println("Moedas de 10: "+Dez);
        System.out.println("Moedas de 5: "+Co);
        System.out.println("Moedas de 1: "+um+"\n");
    }
}
