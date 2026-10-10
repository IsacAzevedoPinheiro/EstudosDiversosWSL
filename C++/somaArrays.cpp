#include <iostream>
#include <chrono>
using namespace std;

void somaArraysCPU(int N, float *a, float *b){
    for (int i=0; i<N; i++){
        b[i] = a[i] + b[i];
    }
}

int main(){
    auto start = chrono::high_resolution_clock::now();

    int N = 1<<20;
    float *a = new float[N];
    float *b = new float[N];
    for (int i=0; i<N; i++){
        a[i] = 1.0f;
        b[i] = 2.0f;
    }
    somaArraysCPU(N, a, b);

    delete a;
    delete b;

    auto end = chrono::high_resolution_clock::now();

    chrono::duration<float, nano> duration = end - start;

    cout<<duration.count()<<endl;
    return 0;
}