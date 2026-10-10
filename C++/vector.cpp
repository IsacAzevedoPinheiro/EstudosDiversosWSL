#include <vector>
using namespace std;

int main(){
    vector<int> v;
    vector<int> w(5, 30); // 5 elementos, cada um com o valor 30
    vector<int> x{1, 2, 3, 4, 5}; // inicialização com lista de valores
    vector<int> y(w); // cópia do vetor w
    vector<int> z(x.begin(), x.end()); // cópia do vetor x
    vector<int> u(10); // 10 elementos, cada um com o valor 0 (padrão)

    v.push_back(10); // adiciona o valor 10 ao final do vetor v
    v.push_back(20); // adiciona o valor 20 ao final do vetor v

    
    return 0;
}
