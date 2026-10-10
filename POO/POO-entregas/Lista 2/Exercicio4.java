package com.mycompany.vmediaeconsumo;
import java.lang.Math;
import java.util.Scanner;
public class VMediaEConsumo {
    //Faça um programa que leia a distância percorrida por um carro, o tempo gasto
    //e a quantidade de gasolina consumida. Em seguida, imprima a velocidade média (KM/h) e
    //o consumo de combustível (Km/l).
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        System.out.println("Entre com a distância: ");
        float dist = teclado.nextFloat();
        System.out.println("Entre com a gas consumida: ");
        float gas = teclado.nextFloat();
        System.out.println("Entre com o tempo: ");
        float temp = teclado.nextFloat();
        System.out.println("Vmédia = "+(dist/temp));
        System.out.println("Consumo = "+(gas/dist));
    }
}
