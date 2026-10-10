#include <iostream>
#include <math.h>
using namespace std;

__global__ void add(int n, float *x, float *y){
    int index = blockDim.x * blockIdx.x + threadIdx.x;
    int stride = blockDim.x * gridDim.x;
    for(int i=index; i<n; i+=stride){
        x[i] = x[i] + y[i];
    }
}
int main(){
    int N = 1<<25;
    float *x, *y;
    cudaMallocManaged(&x, N*(sizeof(float)));
    cudaMallocManaged(&y, N*(sizeof(float)));
    for(int i=0; i<N; i++){
        x[i] = 1.0f;
        y[i] = 2.0f;
    }
    int blockSize = 1<<10;
    int numBlocks = (N+blockSize-1)/blockSize;
    add<<<numBlocks, blockSize>>>(N, x, y);
    cudaDeviceSynchronize();
    cudaFree(x);
    cudaFree(y);
    return 0;
}