package com.mycompany.trinangulo;
import java.lang.Math;
import java.util.Scanner;

public class Triangulo {
    //Faça um programa que leia três coordenadas num espaço 2D e indique se
    //formam um triângulo, juntamente com o seu tipo (equilátero, isósceles e escaleno)
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        System.out.println("Entre com as coordenadas: ");
        String a = teclado.next();
        String b = teclado.next();
        String c = teclado.next();
        String d = teclado.next();
        String e = teclado.next();
        String f = teclado.next();
        
        int x1 = Integer.parseInt(a);
        int y1 = Integer.parseInt(b);
        int x2 = Integer.parseInt(c);
        int y2 = Integer.parseInt(d);
        int x3 = Integer.parseInt(e);
        int y3 = Integer.parseInt(f);
        
        double ladoA = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        double ladoB = Math.sqrt(Math.pow(x3 - x2, 2) + Math.pow(y3 - y2, 2));
        double ladoC = Math.sqrt(Math.pow(x3 - x1, 2) + Math.pow(y3 - y1, 2));

        if ((ladoA + ladoB > ladoC) && (ladoA + ladoC > ladoB) && (ladoB + ladoC > ladoA)) {
            System.out.println("\nOs pontos formam um triângulo válido!");

            if (ladoA == ladoB && ladoB == ladoC) {
                System.out.println("Classificação: Triângulo Equilátero (todos os lados iguais).");
            } else if (ladoA == ladoB || ladoA == ladoC || ladoB == ladoC) {
                System.out.println("Classificação: Triângulo Isósceles (dois lados iguais).");
            } else {
                System.out.println("Classificação: Triângulo Escaleno (todos os lados diferentes).");
            }
            
        } else {
            System.out.println("\nOs pontos NÃO formam um triângulo");
        }
        
        teclado.close();
    }
}
