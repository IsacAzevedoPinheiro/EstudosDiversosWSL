#include <stdio.h>
#include <stdlib.h>

/* Definição do nó da lista */
typedef struct Node {
    int data;
    struct Node *next;
} Node;

/* Protótipos das funções */
Node* createNode(int value);
void insertAtBeginning(Node **head, int value);
void insertAtEnd(Node **head, int value);
void deleteNode(Node **head, int value);
void printList(Node *head);
void freeList(Node **head);

int main() {
    Node *lista = NULL;

    printf("=== Inserir no INÍCIO ===\n");
    insertAtBeginning(&lista, 10);
    insertAtBeginning(&lista, 20);
    insertAtBeginning(&lista, 30);
    printList(lista);

    printf("\n=== Inserir no FINAL ===\n");
    insertAtEnd(&lista, 40);
    printList(lista);

    printf("\n=== Deletar valor 20 ===\n");
    deleteNode(&lista, 20);
    printList(lista);

    printf("\n=== Tentativa de deletar valor inexistente ===\n");
    deleteNode(&lista, 99);

    printf("\n=== Listar novamente ===\n");
    printList(lista);

    printf("\n=== Liberar memória da lista ===\n");
    freeList(&lista);
    printf("Lista liberada com sucesso.\n");

    return 0;
}

/* Cria um novo nó alocando memória dinamicamente */
Node* createNode(int value) {
    Node *newNode = (Node *)malloc(sizeof(Node));
    if (!newNode) {
        printf("Erro: Falha na alocação de memória!\n");
        exit(EXIT_FAILURE);
    }
    newNode->data = value;
    newNode->next = NULL;
    return newNode;
}

/* Insere um novo nó no início da lista */
void insertAtBeginning(Node **head, int value) {
    Node *newNode = createNode(value);
    newNode->next = *head;
    *head = newNode;
}

/* Insere um novo nó no final da lista */
void insertAtEnd(Node **head, int value) {
    Node *newNode = createNode(value);
    if (*head == NULL) {
        *head = newNode;
        return;
    }
    Node *current = *head;
    while (current->next != NULL) {
        current = current->next;
    }
    current->next = newNode;
}

/* Remove o primeiro nó cujo valor seja igual a 'value' */
void deleteNode(Node **head, int value) {
    if (*head == NULL) {
        printf("Lista vazia. Nada para deletar.\n");
        return;
    }

    Node *temp = *head;
    
    /* Caso o nó a ser removido seja o primeiro */
    if (temp->data == value) {
        *head = temp->next;
        free(temp);
        printf("Valor %d deletado.\n", value);
        return;
    }

    /* Percorre buscando o predecessor do nó alvo */
    while (temp->next != NULL && temp->next->data != value) {
        temp = temp->next;
    }

    if (temp->next != NULL) {
        Node *nodeToDelete = temp->next;
        temp->next = nodeToDelete->next;
        free(nodeToDelete);
        printf("Valor %d deletado.\n", value);
    } else {
        printf("Valor %d não encontrado na lista.\n", value);
    }
}

/* Percorre e exibe todos os elementos da lista */
void printList(Node *head) {
    if (head == NULL) {
        printf("(lista vazia)\n");
        return;
    }
    Node *current = head;
    while (current != NULL) {
        printf("%d -> ", current->data);
        current = current->next;
    }
    printf("NULL\n");
}

/* Libera toda a memória alocada pela lista */
void freeList(Node **head) {
    Node *current = *head;
    while (current != NULL) {
        Node *next = current->next;
        free(current);
        current = next;
    }
    *head = NULL; /* Garante que o ponteiro fique nulo após a liberação */
}
