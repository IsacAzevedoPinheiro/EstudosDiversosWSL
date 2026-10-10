package com.mycompany.tabuada;
public class Tabuada {
    /*Construa a tabela de multiplicação de números de 1 a 10
(Ex.: 1 x 1 = 1, 1 x 2 = 2, etc.).*/
    public static void main(String[] args) {
        for (int i=0; i<10; i++){
            System.out.printf("\n");
            for (int j=0; j<10; j++){
                System.out.printf("%dx%d = %d\t", j+1, i+1, ((i+1)*(j+1)));
                //O \t é um tab, aí fica bonitinho
            }
        }
    }
}

