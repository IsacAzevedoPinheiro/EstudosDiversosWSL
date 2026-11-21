#include <iostream>
#include <math.h>
#include <vector>
#include <utility>
using namespace std;

__device__
void normaInfinita(double *vet1, double *vet2, int ladoMatriz, double *norma){
    *norma = 0;
    double dif;
    for (int i = 0; i < ladoMatriz; i++){
        dif = vet1[i] - vet2[i];
        dif = fabs(dif);
        if (dif > *norma)
            *norma = dif;
    }
}

__global__
void gaussJacobi(double *matrizA_d, double *vetorB_d, double *vetXAntigo_d, double *vetXNovo_d, int ladoMatriz, double *norma){ 
    int i = threadIdx.x; 
    if (i<ladoMatriz){
        double soma = 0;
        for (int j = 0; j<ladoMatriz; j++){
            if (i != j)
                soma += matrizA_d[i*ladoMatriz+j]*vetXAntigo_d[j];
        }
        vetXNovo_d[i] = (vetorB_d[i] - soma)/matrizA_d[i*ladoMatriz+i];
    }
    if (i == 0)
        normaInfinita(vetXAntigo_d, vetXNovo_d, ladoMatriz, norma);
}
__host__
int main(){
    int ladoMatriz = 3;
    int Tam = ladoMatriz*ladoMatriz;
    double *matrizA_h = new double[Tam]{
        5, 1, 1,
        1, 4, 1,
        2, 0, 3
    };
    double *vetorB_h = new double[ladoMatriz]{
        6,
        8,
        -1
    };
    double *vetXAntigo_h = new double[ladoMatriz]{
        0,
        0,
        0
    };
    double *vetXNovo_h = new double[ladoMatriz];
    double *vetAlphas = new double[ladoMatriz];
    double *vetSaida = new double[ladoMatriz];
    double epsilon = 1e-06;
    double norma = 0;
    double *norma_h = &norma;
    double alphaMax = -1;
    int maxIteracoes = 25;
    int iteracaoAtual = 0;
    double *matrizA_d;
    double *vetorB_d;
    double *vetXAntigo_d;
    double *vetXNovo_d;
    double *norma_d;

    cudaMalloc((void**)&matrizA_d, Tam*(sizeof(double)));
    cudaMalloc((void**)&vetorB_d, ladoMatriz*(sizeof(double)));
    cudaMalloc((void**)&vetXAntigo_d, ladoMatriz*(sizeof(double)));
    cudaMalloc((void**)&vetXNovo_d, ladoMatriz*(sizeof(double)));
    cudaMalloc((void**)&norma_d, sizeof(double));

    cudaMemcpy(matrizA_d, matrizA_h, Tam*sizeof(double), cudaMemcpyHostToDevice);
    cudaMemcpy(vetorB_d, vetorB_h, ladoMatriz*sizeof(double), cudaMemcpyHostToDevice);
    cudaMemcpy(vetXAntigo_d, vetXAntigo_h, ladoMatriz*sizeof(double), cudaMemcpyHostToDevice);
    cudaMemcpy(vetXNovo_d, vetXNovo_h, ladoMatriz*sizeof(double), cudaMemcpyHostToDevice);
    cudaMemcpy(norma_d, norma_h, sizeof(double), cudaMemcpyHostToDevice);

    dim3 tamanho_bloco(ladoMatriz);
    int quant_blocos = 1;

    for (int i = 0; i < ladoMatriz; i++){
        double soma = 0;
        for(int j = 0; j < ladoMatriz; j++){
            if (i != j)
                soma += fabs(matrizA_h[ladoMatriz*i+j]);
        }
        double alpha = soma /fabs(matrizA_h[ladoMatriz*i+i]);
        if (alpha >= alphaMax)
            alphaMax = alpha;
    }
    cout<<"Alpha max: "<<alphaMax<<endl;
    if (alphaMax > 1){
        cout<<"Alpha maior que 1";
        exit(-1);
    }
    do{
        gaussJacobi<<<quant_blocos, tamanho_bloco>>>(matrizA_d, vetorB_d, vetXAntigo_d, vetXNovo_d, ladoMatriz, norma_d);
        cudaMemcpy(norma_h, norma_d, sizeof(double), cudaMemcpyDeviceToHost);
        swap(vetXAntigo_d, vetXNovo_d);
        iteracaoAtual++;
    }while (*norma_h > epsilon && iteracaoAtual < maxIteracoes);
    
    cudaMemcpy(vetSaida, vetXAntigo_d, ladoMatriz*sizeof(double), cudaMemcpyDeviceToHost);
    cout<<"Esse é o vetor de incógnitas: "<<endl;
    for(int i=0; i<ladoMatriz; i++){
        cout<<vetSaida[i]<<endl;
    }
    cout<<"Esse é o total de iterações: "<<iteracaoAtual<<endl;
    if (*norma_h <= epsilon)
        cout << "Terminado por precisao\n";
    else if (iteracaoAtual == maxIteracoes)
        cout << "Terminado por maximo de iteracoes atingido\n";
    
    delete[] matrizA_h;
    delete[] vetorB_h;
    delete[] vetXAntigo_h;
    delete[] vetXNovo_h;
    delete[] vetAlphas;
    cudaFree(matrizA_d);
    cudaFree(vetorB_d);
    cudaFree(vetXAntigo_d);
    cudaFree(vetXNovo_d);
    return 0;
}