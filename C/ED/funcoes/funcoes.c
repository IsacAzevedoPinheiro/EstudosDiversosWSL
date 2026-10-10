#include <stdio.h>
#include <stdlib.h>

typedef struct isso_eh_um_no{
    int info;         
    struct isso_eh_um_no *prox;
}NO;
void Imprime(NO *p){
    if (p != NULL){
        printf("%d ", p->info); 
        Imprime(p->prox);
    }
}
int Busca(NO *p, NO **pre, int elem){
    NO *aux;
    NO *prel;

    aux = p;
    prel = NULL;

    while ((aux != NULL)&&(elem > aux->info)){
        prel = aux;
        aux = aux->prox;
    }
    (*pre) = prel;
    if ((aux != NULL) && (aux->info == elem))
        return 1;                            
    return 0;                                      
}
NO *Insere(NO *p, int elem){
    NO *pre, *el;
    
    if (!Busca(p, &pre, elem)){
        el = (NO*) malloc(sizeof(NO));
        el->info = elem;
        
        if ((pre == NULL)||(p == NULL)){
            el->prox = p; 
            p = el;
        }
        else{
            el->prox = pre->prox; 
            pre->prox = el;
        }
    }else{
        printf(" Já está na lista\n");
    }
    return p; 
}
NO *RemoveElem(NO *p, int elem){
    NO *lixo, *pre; 
    if (Busca(p, &pre, elem)){
        
        if (p->info == elem){
            lixo = p; 
            p = p->prox; 
        }
        else{
            lixo = pre->prox; 
            pre->prox = lixo->prox;
            free(lixo);
        }
    }else{
        printf("Não tinha esse elemento, quis atoa\n");
    }
    return p;
}
int main(){
    NO* p; 
    p = NULL; 
    int elem, op = 456;
    while (op != 4){
        printf("[1]print\n[2]insere\n[3]remove\n[4]sair\n\n-->");
        scanf("%d", &op);
        switch (op)
        {
        case 1:
            printf("{");
            Imprime(p);
            printf("}");
            printf("\n");
            break;
        case 2:
            printf("Entre com elem: ");
            scanf("%d", &elem);
            p = Insere(p, elem);
            printf("\n");
            break;
        case 3:
            printf("Elem para remover: ");
            scanf("%d", &elem);
            p = RemoveElem(p, elem);
            printf("\n");
            break;
        case 4:
            printf("Deu trabalho, tomara que dê tudo certo");
            break;
        }
    }
}