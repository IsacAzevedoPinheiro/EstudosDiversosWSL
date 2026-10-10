#include <string.h>
#include <stdlib.h>
#include <stdio.h>
#define TAM_NOME 100

typedef struct Departamento{
    int cod_dept;
    int sala;
    char nome[TAM_NOME];
}TDepartamento;

typedef struct Funcionario{
    int cod_func;
    int cod_dept;
    char nome[TAM_NOME];
}TFuncionario;

TFuncionario *le_funcionario(FILE *in){
    TFuncionario *func = (TFuncionario *) malloc(sizeof(TFuncionario));
    char linha[150];
    if (fgets(linha, 150, in) == NULL ){
        free(func);
        return NULL;
    }
    char delimitador[] = ";";
    char *ptr;
    int cod;
    //quebra a linha
    ptr = strtok(linha, delimitador);
    cod = atoi(ptr);
    func->cod_func = cod;
    ptr = strtok(NULL, delimitador);
    cod = atoi(ptr);
    func->cod_dept = cod;
    ptr = strtok(NULL, delimitador);
    strcpy(func->nome, ptr);

    return func;
}

TDepartamento *le_departamento(FILE *in){
    TDepartamento *dept = (TDepartamento *) malloc(sizeof(TDepartamento));
    char linha[150];
    if (fgets (linha, 150, in) == NULL ) {
        free(dept);
        return NULL;
    }
    char delimitador[] = ";";
    char *ptr;
    int cod, sala;
    //quebra a linha
    ptr = strtok(linha, delimitador);
    cod = atoi(ptr);
    dept->cod_dept = cod;
    ptr = strtok(NULL, delimitador);
    sala = atoi(ptr);
    dept->sala = sala;
    ptr = strtok(NULL, delimitador);
    strcpy(dept->nome, ptr);

    return dept;
}

void imprime_arquivo(char *name){
    FILE *arq; //declara ponteiro para arquivo
    // abre arquivo para leitura
    arq = fopen(name, "r");
    if (arq != NULL) {// checa se não deu erro na abertura do arquivo
        char linha[150];
        fgets (linha, 150, arq);
        while (!feof(arq)) {//testa se chegou ao final do arquivo
            printf("%s", linha);
            fgets (linha, 150, arq);
        }
        fclose(arq); //fecha arquivo
    } else printf("Erro ao abrir arquivo\n");
}

void leftOuterJoin(char *nome_arq_dept, char *nome_arq_funcionarios, char *nome_arq_join) {
    FILE *arqDept = fopen(nome_arq_dept, "r");
    FILE *arqFunc = fopen(nome_arq_funcionarios, "r");
    FILE *arqJoin = fopen(nome_arq_join, "w");
    TDepartamento *atualDept = le_departamento(arqDept);
    while (atualDept != NULL){
        int achou = 0;
        rewind(arqFunc);
        TFuncionario *atualFunc = le_funcionario(arqFunc);
        while (atualFunc != NULL){
            if (atualFunc->cod_dept == atualDept->cod_dept){
                fprintf(arqJoin, "%d;", atualDept->cod_dept);
                fprintf(arqJoin, "%d;", atualDept->sala);
                fprintf(arqJoin, "%s;", atualDept->nome);
                fprintf(arqJoin, "%d;", atualFunc->cod_func);
                fprintf(arqJoin, "%s;\n", atualFunc->nome);
                achou = 1;
            }
            atualFunc = le_funcionario(arqFunc);
        }
        if (!achou){
            fprintf(arqJoin, "%d;", atualDept->cod_dept);
            fprintf(arqJoin, "%d;", atualDept->sala);
            fprintf(arqJoin, "%s;0;;\n", atualDept->nome);
        }
        atualDept = le_departamento(arqDept);
    }
    fclose(arqJoin);
    fclose(arqDept);
    fclose(arqFunc);
}
/*
Tem o fgets(char *Buffer, int TamMax, FILE *stream) que analogo ao fscanf(FILE *stream, "%d\n", &num)
Para ler até o ; tem que usar: [^;]; dentro das "" do fscanf()
*/
int main() {
    leftOuterJoin("departamentos.txt", "funcionarios.txt", "join.txt");
    imprime_arquivo("join.txt");
}