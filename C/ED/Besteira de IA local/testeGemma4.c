#include <stdio.h>
#include <stdlib.h> // Necessário para usar malloc()
//Tentativa 3
// 1. DEFINIÇÃO DA ESTRUTURA DO NÓ
typedef struct Node {
    int data;             // O dado que o nó irá guardar (um inteiro)
    struct Node *next;    // Ponteiro para o próximo nó da sequência
} Node;

/**
 * @brief Cria e retorna um novo nó com os dados especificados.
 * 
 * @param data O valor inteiro a ser armazenado no novo nó.
 * @return struct Node* Um ponteiro para o nó recém-criado.
 */
Node* createNode(int data) {
    // Aloca memória dinamicamente para um novo Nó
    Node* newNode = (Node*)malloc(sizeof(Node));
    
    if (newNode == NULL) {
        perror("Erro ao alocar memória");
        exit(EXIT_FAILURE);
    }
    
    // Inicializa o nó: coloca o dado e aponta 'next' para NULL
    newNode->data = data;
    newNode->next = NULL;
    
    return newNode;
}

/**
 * @brief Insere um novo elemento no final da lista.
 * 
 * O parâmetro head_ref é um ponteiro para o ponteiro que aponta ao início da lista,
 * pois podemos precisar alterar o 'head' em cenários como inserir em lista vazia.
 * 
 * @param head_ref Ponteiro para o ponteiro inicial da lista (Node**).
 * @param data O valor inteiro a ser inserido no final.
 */
void insertAtEnd(Node** head_ref, int data) {
    // 1. Cria o novo nó
    Node* newNode = createNode(data);

    // Caso 1: A lista está vazia (o ponteiro inicial é NULL)
    if (*head_ref == NULL) {
        // O novo nó se torna a cabeça da lista
        *head_ref = newNode;
        printf("-> Elemento %d inserido. Lista estava vazia.\n", data);
        return;
    }

    // Caso 2: A lista não está vazia
    Node* current = *head_ref; // Começa do início da lista (cabeça)
    
    // Percorre a lista até chegar ao último nó (onde next é NULL)
    while (current->next != NULL) {
        current = current->next;
    }

    // 3. Conecta o novo nó ao final da lista atual
    current->next = newNode;
    printf("-> Elemento %d inserido no final.\n", data);
}


/**
 * @brief Exibe todos os elementos da lista encadeada em ordem crescente.
 * 
 * @param head O ponteiro que aponta para o primeiro nó da lista.
 */
void displayList(Node* head) {
    if (head == NULL) {
        printf("\nA lista está vazia.\n");
        return;
    }

    printf("\n--- Conteúdo da Lista ---\n");
    Node* current = head; // Ponteiro temporário para percorrer a lista

    // Percorre a lista até encontrar um ponteiro NULL
    while (current != NULL) {
        printf("| %d ", current->data);
        current = current->next; // Avança para o próximo nó
    }
    printf("|\n------------------------\n");
}


/**
 * @brief Libera toda a memória alocada pela lista encadeada. 
 * (Muito importante para evitar vazamento de memória - memory leak)
 * 
 * @param head Ponteiro para o primeiro nó da lista.
 */
void freeList(Node** head_ref) {
    Node* current = *head_ref;
    Node* nextNode;

    while (current != NULL) {
        nextNode = current->next; // Guarda o ponteiro do próximo nó
        free(current);            // Libera a memória do nó atual
        current = nextNode;       // Move para o próximo nó
    }
    // Após liberar todos os nós, resetamos o ponteiro da cabeça
    *head_ref = NULL; 
}


int main() {
    // Declaração da variável que será a 'cabeça' (Head) da nossa lista.
    // Inicializamos como NULL, indicando que a lista está vazia.
    Node* head = NULL;

    printf("=========================================\n");
    printf("SIMULAÇÃO DE LISTA ENCADEADA EM C\n");
    printf("=========================================\n");
    
    // --- TESTANDO INSERÇÕES ---
    
    // 1. Insere o primeiro elemento (Lista vazia)
    insertAtEnd(&head, 10); 
    

    // 2. Insere outro elemento no final
    insertAtEnd(&head, 25);
    
    
    // 3. Insere mais elementos
    insertAtEnd(&head, 30);
    

    insertAtEnd(&head, 22);
    insertAtEnd(&head, 22);
    insertAtEnd(&head, 22);
    displayList(head);

    printf("\n*** Processo de inserção concluído ***\n");


    // --- LIMPEZA DE MEMÓRIA (CRUCIAL) ---
    freeList(&head);
    printf("\nLista liberada e ponteiro 'head' resetado para NULL.\n");

    return 0;
}
