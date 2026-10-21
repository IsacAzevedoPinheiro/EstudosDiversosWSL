#include <iostream>
#include <math.h>
#include <vector>
using namespace std;
//Gaus-Jacobi vai ter primeiro o critério de linhas tal que: alphax é a soma dos módulos dos elementos da linha dividido pelo módulo do elemento da matriz principal
//Se passar vai pro método que precisa do chute inicial que vai fazer a próxima incógnita seja [b - (soma dos produtos das incógnitas pelos elementos da linha)] / elemento da diagonal principal
/* 
{
    5.0f, 1.0f, 1.0f, 
    1.0f, 4.0f, 1.0f, 
    2.0f, 0.0f, 3.0f
};
{
    6,
    8,
    -1
};
 */
__global__
void gaussJacobi(double *matrizA_d, double *vetorB_d, double *vetXAntigo_d, double *vetXNovo_d, int ladoMatriz){
    //aqui vamos ter uma iteração do método
    for (int i = 0; i < ladoMatriz; i++){
        double soma = 0;
        for (int j = 0; j < ladoMatriz; j++){
            if (i != j)
                soma += matrizA_d[ladoMatriz*i+j]*vetXAntigo_d[j];
        }
        vetXNovo_d[i] = (vetorB_d[i] - soma) / matrizA_d[ladoMatriz*i+i];
    }
    
}
int main(){
    int N = 3;
    int Tam = N*N;
    double *matrizA_h = new double[Tam]{
        5, 1, 1,
        1, 4, 1,
        2, 0, 3
    };
    double *vetorB_h = new double[N]{
        6,
        8,
        -1
    };
    double *vetXAntigo_h = new double[N]{
        0,
        0,
        0
    };
//===============================================
    double *vetXNovo_h = new double[N];
    double *vetAlphas = new double[N];
//===============================================
    double epsilon = 1e-06;
    double erro = 1;
    double dif = 0;
    double alphaMax = -1;
    int maxIteracoes = 20;
    int iteracaoAtual = 0;
//===============================================
    double *matrizA_d;
    double *vetorB_d;
    double *vetXAntigo_d;
    double *vetXNovo_d;

    cudaMalloc((void**)&matrizA_d, Tam*(sizeof(double)));
    cudaMalloc((void**)&vetorB_d, N*(sizeof(double)));
    cudaMalloc((void**)&vetXAntigo_d, N*(sizeof(double)));
    cudaMalloc((void**)&vetXNovo_d, N*(sizeof(double)));

    cudaMemcpy(matrizA_d, matrizA_h, Tam*sizeof(double), cudaMemcpyHostToDevice);
    cudaMemcpy(vetorB_d, vetorB_h, N*sizeof(double), cudaMemcpyHostToDevice);
    cudaMemcpy(vetXAntigo_d, vetXAntigo_h, N*sizeof(double), cudaMemcpyHostToDevice);
    cudaMemcpy(vetXNovo_d, vetXNovo_h, N*sizeof(double), cudaMemcpyHostToDevice);

    int tamanho_bloco = 1<<8;
    int quant_blocos = (N+tamanho_bloco-1)/tamanho_bloco;

    for (int i = 0; i < N; i++){
        double soma = 0;
        for(int j = 0; j < N; j++){
            if (i != j)
                soma += fabs(matrizA_h[N*i+j]);
        }
        double alpha = soma /fabs(matrizA_h[N*i+i]);
        if (alpha >= alphaMax)
            alphaMax = alpha;
    }
    cout<<"Alpha max: "<<alphaMax<<endl;
    if (alphaMax > 1){
        cout<<"Alpha maior que 1";
        exit(-1);
    }
    
    //laço principal é controlado pelo host
    while (erro > epsilon && iteracaoAtual < maxIteracoes){
        erro = 0; //Se não for zerado no começo só vai guardar o primeiro erro
        gaussJacobi<<<quant_blocos, tamanho_bloco>>>(matrizA_d, vetorB_d, vetXAntigo_d, vetXNovo_d, N);
        cudaMemcpy(vetXAntigo_h, vetXAntigo_d, N*sizeof(double), cudaMemcpyDeviceToHost);
        cudaMemcpy(vetXNovo_h, vetXNovo_d, N*sizeof(double), cudaMemcpyDeviceToHost);
        for (int i = 0; i<N; i++){
            dif = vetXAntigo_h[i] - vetXNovo_h[i];
            dif = fabs(dif);
            if (dif >= erro)
                erro = dif;
        }
        cudaMemcpy(vetXAntigo_d, vetXNovo_h, N*sizeof(double), cudaMemcpyHostToDevice);
        iteracaoAtual++;
    }
    
    cout<<"Esse é o vetor de incógnitas: "<<endl;
    for(int i=0; i<N; i++){
        cout<<vetXNovo_h[i]<<endl;
    }
    cout<<"Esse é o total de iterações: "<<iteracaoAtual<<endl;
    if (erro <= epsilon)
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