package com.simumic;

public interface AcessoMemoria {
    int read(int endereco);

    void write(int endereco, int dado);
}