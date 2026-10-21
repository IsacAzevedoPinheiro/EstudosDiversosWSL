#include <iostream>
#include <math.h>
using namespace std;

__global__
void saxpy(int N, int *x, int *y){
    for (int i = 0; i < N; i++)
    {
        y[i] = x[i] * 2;
    }
}

int main(){
    int N = 1<<20;
    //vou alocar os vetores h_x e h_y no host
    int *h_x = new int[N];
    int *h_y = new int[N];
    //agora dois novos direto no device (d_x e d_y)
    int *d_x;
    int *d_y;
    cudaMalloc((void**)&d_x, N*(sizeof(int)));
    cudaMalloc((void**)&d_y, N*(sizeof(int)));
    //esse void é regra pra dizer que tô passando um ponteiro de 2 dimensões

    for (int i = 0; i < N; i++)
    {
        h_x[i] = i;
    }

    cudaMemcpy(d_x, h_x, N*sizeof(int), cudaMemcpyHostToDevice);
    cudaMemcpy(d_y, h_y, N*sizeof(int), cudaMemcpyHostToDevice);
    //copia o vetor inteiro direto pro primeiro parâmetro rodando internamente com acesso direto a memória (DMA)

    int tamanho_bloco = 1<<8;
    int quant_blocos = (N+tamanho_bloco-1)/tamanho_bloco;
    saxpy<<<quant_blocos, tamanho_bloco>>>(N, d_x, d_y); 

    cudaMemcpy(h_x, d_x, N*sizeof(int), cudaMemcpyDeviceToHost);
    cudaMemcpy(h_y, d_y, N*sizeof(int), cudaMemcpyDeviceToHost);
    //essa viagem de volta faz a GPU esperar a CPU ent n preciso de cudaDeviceSynchronize()

    cout<<"Xzes: "<<h_x[10]<<" "<<h_x[100]<<" "<<h_x[1000]<<"\n";
    cout<<"Yons: "<<h_y[10]<<" "<<h_y[100]<<" "<<h_y[1000]<<"\n";
    cout<<"Fim!"<<h_y[1<<20-1]<<"\n";

    delete[] h_x;
    delete[] h_y;
    cudaFree(d_x);
    cudaFree(d_y);

    return 0;
}