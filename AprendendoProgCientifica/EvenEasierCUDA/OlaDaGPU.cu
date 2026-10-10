#include <iostream>
#include <stdio.h>
using namespace std;

__global__ 
void helloFromGPU() { //device
    printf("\nOla da GPU! Eu sou a thread %d", threadIdx.x);
}

int main() { //host
    cout << "Ola da CPU! Iniciando o teste do compilador NVCC...";

    helloFromGPU<<<1, 10>>>();

    cudaDeviceSynchronize();

    printf("\n");
    cout << "Teste concluido! Se voce viu as 10 mensagens da GPU acima, o ambiente esta perfeito.";
    
    return 0;
}

// nvcc OlaDaGPU.cu
// ./a