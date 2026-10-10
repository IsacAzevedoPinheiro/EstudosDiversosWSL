// Memória externa que contém endereços, dados e se comunica com MBR e MAR
package com.simumic;

public class Memoria implements AcessoMemoria {
    private int[] enderecos = new int[4096];

    public Memoria() {
        // Debugando lod do endereço 10
        enderecos[0] = 0x000A;
    }

    // Sinal RD
    public int read(int endereco) {
        // Confere se existe para não dar out of index
        if (endereco >= 0 && endereco < enderecos.length) {
            return enderecos[endereco];
        }
        System.out.println("FORA DE INDEXX");
        return 0;
    }

    // Sinal WR
    public void write(int endereco, int dado) {
        if (endereco >= 0 && endereco < enderecos.length) {
            enderecos[endereco] = dado;
        }
    }
}
