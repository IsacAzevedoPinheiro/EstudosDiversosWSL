#include <iostream>
#include <cmath>
#include <cuda_runtime.h>

__global__ void gaussJacobiKernel(const float* A, const float* b, const float* x_old, float* x_new, int n) {
    int i = blockIdx.x * blockDim.x + threadIdx.x;
    
    if (i < n) {
        float soma = 0.0f;
        float diag = A[i * n + i];
        
        for (int j = 0; j < n; ++j) {
            if (j != i) {
                soma += A[i * n + j] * x_old[j];
            }
        }
        
        x_new[i] = (b[i] - soma) / diag;
    }
}

// Definição do kernel acima...

void resolverJacobi(const float* h_A, const float* h_b, float* h_x, int n, int max_iter, float tol) {
    size_t size = n * sizeof(float);
    float *d_A, *d_b, *d_old, *d_new;
    
    cudaMalloc(&d_A, n * n * sizeof(float));
    cudaMalloc(&d_b, size);
    cudaMalloc(&d_old, size);
    cudaMalloc(&d_new, size);
    
    cudaMemcpy(d_A, h_A, n * n * sizeof(float), cudaMemcpyHostToDevice);
    cudaMemcpy(d_b, h_b, size, cudaMemcpyHostToDevice);
    cudaMemcpy(d_old, h_x, size, cudaMemcpyHostToDevice);
    
    int threadsPerBlock = 256;
    int blocksPerGrid = (n + threadsPerBlock - 1) / threadsPerBlock;
    
    for (int iter = 0; iter < max_iter; ++iter) {
        gaussJacobiKernel<<<blocksPerGrid, threadsPerBlock>>>(d_A, d_b, d_old, d_new, n);
        cudaDeviceSynchronize();
        
        // Dica: Para máxima performance real, o teste de convergência 
        // também deveria ser feito na GPU usando Reductions (ex: Cublas/Thrust),
        // mas aqui alternamos os ponteiros para a próxima iteração:
        float* temp = d_old;
        d_old = d_new;
        d_new = temp;
    }
    
    cudaMemcpy(h_x, d_old, size, cudaMemcpyDeviceToHost);
    
    cudaFree(d_A);
    cudaFree(d_b);
    cudaFree(d_old);
    cudaFree(d_new);
}
