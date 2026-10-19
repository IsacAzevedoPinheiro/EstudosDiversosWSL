#include <iostream>
#include <math.h>
#include <bitset>
using namespace std;

__global__
void saxpy(int a, int N, int *x, int *y){
    for (int i=0; i<N; i++){
        y[i] = a*x[i];
    }
}

int main(){
    //tamanho dos vetores
    int N = 1<<20;
    int *x, *y;
    cudaMallocManaged(&x, N*(sizeof(int)));
    cudaMallocManaged(&y, N*(sizeof(int)));

    //inicializando os vetores
    for (int i = 0; i < N; i++)
    {
        x[i] = i;
    }

    //agora definir so blocos de threads
    int tamanho_bloco = 1<<8;
    //a quantidade de blocos é o sufiente para ter uma thread por número no vetor, o truque é somar o tamanho do bloco menos 1 ao N, pq se essa divisão em especial der mais de 1, aumenta a quantidade de blocos em 1
    int quant_blocos = (N+tamanho_bloco-1)/tamanho_bloco;

    //chama a função
    saxpy<<<quant_blocos, tamanho_bloco>>>(2, N, x, y);

    cudaDeviceSynchronize();

    cout<<"Xzes: "<<x[10]<<" "<<x[100]<<" "<<x[1000]<<"\n";
    cout<<"Yons: "<<y[10]<<" "<<y[100]<<" "<<y[1000]<<"\n";
    cout<<"Fim!"<<bitset<21>(y[1<<20-1])<<"\n";

    //liberando memória:
    cudaFree(x);
    cudaFree(y);
    

    return 0;
}