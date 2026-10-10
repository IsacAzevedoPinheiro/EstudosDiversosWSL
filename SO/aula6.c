#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
// #include "timer.h" // Comentado pois contém erros de sintaxe e não é utilizado neste código

#define NTHREADS 10

void *PrintHello(void *args){
  int *idThreads = (int*) args;
  printf("\nHola como estas %d", *idThreads);
  free(args);
  pthread_exit(NULL);
}

int main(){
  pthread_t tid_sistema[NTHREADS];
  int *args;
  
  for (int i=0; i<NTHREADS; i++){
    args = malloc(sizeof(int));
    *args = i;
    if (pthread_create(&tid_sistema[i], NULL, PrintHello, (void*) args)){
      printf("\n--Erro create");
      exit(-1);
    }
  }
  
  for (int i=0; i<NTHREADS; i++){
    // Corrigido de pthreads_join para pthread_join
    if (pthread_join(tid_sistema[i], NULL)){
      // Corrigido de print para printf
      printf("\n--Erro join");
      exit(-1);
    }
  }
  
  printf("\n");
  return 0;
}
