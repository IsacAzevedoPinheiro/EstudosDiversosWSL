#ifdef _MSC_VER
#define _CRT_SECURE_NO_WARNINGS
#endif

#include "cliente.c"
#include "dirent.h"
#include <string.h>

typedef struct vetor {
    TCliente *cli;
    int congelado;
} TVet;

/* typedef struct Cliente {
    int cod_cliente;
    char nome[TAM_NOME];
} TCliente; 
 */

void imprime_arquivo(char *nome) {
    FILE *arq; //declara ponteiro para arquivo
    // abre arquivo para leitura
    arq = fopen(nome, "r");
    if (arq != NULL) {// checa se não deu erro na abertura do arquivo
        char linha[150];
        fgets(linha, 150, arq);
        while (!feof(arq)) {//testa se chegou ao final do arquivo
            printf("%s", linha);
            fgets(linha, 150, arq);
        }
        fclose(arq); //fecha arquivo
    } else printf("Erro ao abrir arquivo\n");
}

char *gera_nome_particao(int num_part) {
    char *nome_particao = (char *) malloc(sizeof(char[10]));
    char num_particao[10];

    strcpy(nome_particao, "p");
    sprintf(num_particao, "%d", num_part);
    strcat(nome_particao, num_particao);
    strcat(nome_particao, ".txt");

    return nome_particao;
}

int selecao_substituicao(char *nome_arquivo_entrada, int tam_memoria) {
    //if de segurança
    FILE *arq = fopen(nome_arquivo_entrada, "r");
    int contador = 1;
    if (arq != NULL){
        TVet *memoria = (TVet *) malloc(tam_memoria * sizeof(TVet)); //aloquei a memoria inteira
        int i = 0;
        while (i < tam_memoria){
            if (feof(arq)){ //só por segurança 
                break;
            }
            TCliente *vai_pra_lista = le_cliente(arq);
            memoria[i].cli = vai_pra_lista;
            memoria[i].congelado = 0;
            i++;
        }//memoria inicial montada
        
        int memoria_c_dados = 1;
        while (memoria_c_dados){
            FILE *dir_atual;
            dir_atual = fopen(gera_nome_particao(contador), "w");
            int anterior = 0;
            int todos_congelados = 1; //com a primeira memoria, vamos começar a preencher o arquivo
                                      //esse while deve rodar a quantidade de vezes igual a quantidade de partiçoes
            
            do{
                TVet *menor = NULL;
                // CORREÇÃO 4: Acha o primeiro elemento válido (não nulo e não congelado) com segurança
                for (int k = 0; k < tam_memoria; k++) {
                    if (memoria[k].cli != NULL && !memoria[k].congelado) {
                        menor = &memoria[k];
                        break;
                    }
                }

                // Procura se tem alguém ainda menor
                for (int j = 0; j < tam_memoria; j++){
                    if (memoria[j].cli != NULL && !memoria[j].congelado && memoria[j].cli->cod_cliente < menor->cli->cod_cliente)
                        menor = &memoria[j];
                }

                fprintf(dir_atual, "%d;", menor->cli->cod_cliente);
                fprintf(dir_atual, "%s;\n", menor->cli->nome);

                anterior = menor->cli->cod_cliente;
                if (!feof(arq)){ 
                    menor->cli = le_cliente(arq);
                } else {
                    menor->cli = NULL;
                }
                
                if (menor->cli != NULL && anterior > menor->cli->cod_cliente)
                    menor->congelado = 1;

                todos_congelados = 1;
                for (int i=0; i<tam_memoria; i++){
                    if (memoria[i].cli != NULL && !memoria[i].congelado)
                        todos_congelados =  0;
                }
                memoria_c_dados = 0;
                for (int x=0; x<tam_memoria; x++){
                    if (memoria[x].cli != NULL){
                        memoria_c_dados = 1;
                        break;
                    }
                }
                
            }while (memoria_c_dados && !todos_congelados); 
            
            contador++;
            fclose(dir_atual);
            //descongelando:
            for (int k = 0; k < tam_memoria; k++)
                memoria[k].congelado = 0;
            
        }
    }
    else{
        printf("Arquivo inválido!");
        exit(1);
    }
    return contador - 1;
}

int main() {
    int tam_memoria;
    scanf("%d", &tam_memoria);
    int num_part = selecao_substituicao("entrada.txt", tam_memoria);
    for (int i = 1; i < num_part+1; i++) {
        printf("*** %s\n", gera_nome_particao(i));
        imprime_arquivo(gera_nome_particao(i));
    }
}