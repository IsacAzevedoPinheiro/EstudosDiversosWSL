#include <iostream>
using namespace std;

__global__
void add(int n, float *x, float *y)
{
  int index = blockIdx.x * blockDim.x + threadIdx.x;
  int stride = blockDim.x * gridDim.x;
/* 
  threadIdx.x: O ID local da thread dentro do seu próprio bloco (ex: de 0 a 255).
  blockDim.x: O número de threads em cada bloco (ex: 256).
  blockIdx.x: O ID do bloco no qual a thread atual está (ex: Bloco 0, Bloco 1, Bloco 2...).

  gridDim.x: O número total de blocos lançados na Grid.
  stride (passo): É o número total de threads em execução na GPU simultaneamente.
   */

  /* cout<<"BUG!"<<endl; só printf() dentro do kernel*/
  // Forma corrigida:
  printf("blockIdx.x: %d threadIdx.x: %d index: %d\n", blockIdx.x, threadIdx.x, index);

  for (int i = index; i < n; i += stride) //Grid-stride loop
    y[i] = x[i] + y[i];

  /*Padrão grid-stride loop: Após processar a posição i, ela avança stride posições para a frente (i += stride).
  
  Flexibilidade na quantidade de Threads: 

  Se você tiver $1.000.000$ de elementos ($N$), mas lançar a GPU com apenas $256$ threads no total, o código não quebra! As $256$ threads vão processar os primeiros 256 elementos, dar um salto de 256 posições, processar os próximos, e assim por diante, até cobrir todos os $1.000.000$ de itens.

  Reaproveitamento e Decoupling: 

  Você pode alterar a quantidade de blocos no arquivo .cu (por exemplo, testando <<<1, 256>>>, <<<32, 256>>> ou <<<4096, 256>>>) sem ter que alterar uma linha sequer da função add.Acesso 

  Coalescido à Memória: 

  Como threads adjacentes no mesmo bloco acessam elementos de memória contíguos (index, index+1, index+2), a GPU consegue agrupar as buscas de memória em uma única transação rápida pelo barramento de dados.
  
  */
}
int main(){
    int N = 1<<5;
    float *x;
    cudaMallocManaged(&x, N*sizeof(float));// o malloc do cuda, se parece com o do C novamente, diferente do em C++ que é o new
    float *y;
    cudaMallocManaged(&y, N*sizeof(float));

    for (int i=0; i<N; i++){
        x[i] = 1.0f;
        y[i] = 2.0f;
    }
    int blockSize = 1<<3;
    int numBlocks = (N + blockSize - 1) / blockSize;

    add<<<numBlocks, blockSize>>>(N, x, y);// em cuda tem que ter esses aqui <<<>>> o que controla a quantidade de blocos e o número de threads por bloco para executar a função add nesse caso
    cout << "\ngridDim.x (numBlocks): " << numBlocks 
         << " | blockDim.x (blockSize): " << blockSize 
         << " | stride: " << (numBlocks * blockSize) 
         <<endl;
    cudaDeviceSynchronize();

    cudaFree(x);//Liberando a memória, praticamente uma cópia do em C
    cudaFree(y);
    cout<<"Fim"<<endl;
    return 0;
}
/* para ver o tempo só escrever isso: 
nsys profile --trace=cuda --sample=none --cpuctxsw=none --force-overwrite=true -o meu_teste .\a.exe
nsys stats --force-export=true -r cuda_gpu_sum .\meu_teste.nsys-rep */
/*
disponível em: 
https://developer.nvidia.com/blog/even-easier-introduction-cuda/
*/