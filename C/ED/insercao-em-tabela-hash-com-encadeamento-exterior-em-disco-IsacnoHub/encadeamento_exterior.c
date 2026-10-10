#include <limits.h>
#include <stdio.h>

#include "cliente.c"
#include "compartimento_hash.c"

#define M 7

void imprime_arquivos(char *nome_arquivo_hash, char *nome_arquivo_dados) {
    //Imprime arquivo de hash
    printf("TABELA HASH:\n");
    imprime_arquivo_hash(nome_arquivo_hash);

    //Imprime arquivo de dados
    printf("\nDADOS:\n");
    imprime_arquivo_dados(nome_arquivo_dados);
}

/* Executa insercao em Arquivos por Encadeamento Exterior (Hash)
 * cod_cli: chave do cliente que esta sendo inserido
 * nome_cli: nome do cliente a ser inserido
 * nome_arquivo_hash: nome do arquivo que contem a tabela hash
 * nome_arquivo_dados: nome do arquivo onde os dados estao armazenados
 * Retorna o endereco (lógico) onde o cliente foi inserido, ou -1 se nao conseguiu inserir
 */
int insere(int cod_cli, char *nome_cli, char *nome_arquivo_hash, char *nome_arquivo_dados, int m) {
    FILE *arq_hash = fopen(nome_arquivo_hash, "rb+"); //abre com b de binário
    FILE *arq_dados = fopen(nome_arquivo_dados, "rb+");

    int pos_hash = cod_cli % m;
    fseek(arq_hash, pos_hash * tamanho_compartimento(), SEEK_SET);
    TCompartimento *comp = le_compartimento(arq_hash);

    int curr_pos = comp->prox; 
    int prev_pos = -1;
    int liberado_pos = -1;

    while (curr_pos != -1) { //É o NULL só q em arquivo, errei na prova
        fseek(arq_dados, curr_pos * tamanho_cliente(), SEEK_SET);
        TCliente *cli = le_cliente(arq_dados);

        if (cli->ocupado == 1 && cli->cod == cod_cli) {
            free(cli);
            free(comp);
            fclose(arq_hash);
            fclose(arq_dados);
            return -1; 
        }

        if (cli->ocupado == 0 && liberado_pos == -1) {
            liberado_pos = curr_pos;
        }

        prev_pos = curr_pos;
        curr_pos = cli->prox;
        free(cli);
    }

    int pos_insercao;

    if (liberado_pos != -1) {
        pos_insercao = liberado_pos;
        fseek(arq_dados, pos_insercao * tamanho_cliente(), SEEK_SET);
        TCliente *cli_lib = le_cliente(arq_dados);
        
        cli_lib->cod = cod_cli;
        strcpy(cli_lib->nome, nome_cli);
        cli_lib->ocupado = 1; 

        fseek(arq_dados, pos_insercao * tamanho_cliente(), SEEK_SET);
        salva_cliente(cli_lib, arq_dados);
        free(cli_lib);
    } else {
        fseek(arq_dados, 0, SEEK_END);
        pos_insercao = ftell(arq_dados) / tamanho_cliente();

        TCliente *novo_cli = cliente(cod_cli, nome_cli, -1, 1);
        fseek(arq_dados, pos_insercao * tamanho_cliente(), SEEK_SET);
        salva_cliente(novo_cli, arq_dados);
        free(novo_cli);

        if (prev_pos == -1) {
            comp->prox = pos_insercao;
            fseek(arq_hash, pos_hash * tamanho_compartimento(), SEEK_SET);
            salva_compartimento(comp, arq_hash);
        } else {
            fseek(arq_dados, prev_pos * tamanho_cliente(), SEEK_SET);
            TCliente *ultimo_cli = le_cliente(arq_dados);
            ultimo_cli->prox = pos_insercao;
            
            fseek(arq_dados, prev_pos * tamanho_cliente(), SEEK_SET);
            salva_cliente(ultimo_cli, arq_dados);
            free(ultimo_cli);
        }
    }

    free(comp);
    fclose(arq_hash);
    fclose(arq_dados);

    return pos_insercao;
}

int main() {
    /* Essa função gera a saída que é usada no teste do run.codes. Ela NÃO DEVE SER MODIFICADA */
    int cod;
    char nome[TAM_NOME] = "";
    int pont;

    //le dados a serem inseridos
    scanf("%d", &cod);
    scanf("%s", nome);

    //Imprime arquivos de entrada
    //imprime_arquivos("tabHash.dat", "clientes.dat");

    pont = insere(cod, nome, "tabHash.dat", "clientes.dat", M);
    imprime_arquivos("tabHash.dat", "clientes.dat");
    //Imprime resultado da função
    printf("\nRESULTADO: %d", pont);
}