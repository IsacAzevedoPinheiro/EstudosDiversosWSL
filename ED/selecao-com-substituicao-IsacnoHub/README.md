[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/XV3J5hCi)
#### Problema

Implemente a função int selecao\_substituicao(char *nome\_arquivo\_entrada, int tam\_memoria) que recebe como parâmetros o nome do arquivo texto de entrada, que contém clientes, e a quantidade de registros que cabem em memória simultaneamente. A função deve implementar o algoritmo de Seleção com Substituição visto em aula para gerar partições classificadas a partir de um arquivo de entrada, desordenado. 

São fornecidos três arquivos: 

- geracao.c, que contém a função a ser implementada
- cliente.c que contém funções de apoio para ler e salvar clientes no arquivo (leia as dicas para compilar o código em http://www.ic.uff.br/~vanessa/courses/runcodes.html)
- entrada.txt, arquivo de exemplo de entrada

#### Entrada 

- o programa principal lê o número de registros que cabem em memória simultaneamente 
- a função recebe o nome do arquivo de entrada, e o número de registros que cabem em memória 

#### Saída

- a função retorna o número de partições geradas 
- o programa imprime na tela as partições geradas 

**IMPORTANTE:** use a função gera\_nome\_particao(int num\_part) para gerar os nomes dos arquivos das partições. O inteiro passado como parâmetro dessa função deve ser 1 na primeira chamada, e depois 2, 3, etc. Para exemplificar o funcionamento dessa função, quando recebe 1 como parâmetro, a função gera como resultado o nome de partição p1.txt. Portanto, se uma execução da função selecao_substituicao gerar como resultado 3 partições, os nomes dos arquivos gerados devem ser p1.txt, p2.txt e p3.txt. 

#### Exemplo 

Assumindo que cabem 3 registros na memória de cada vez, e o arquivo de entrada é como segue (note que há uma linha em branco após o último registro): 

```
29;Maria;
14;Joao;
76;Mirtes;
75;Mariana;
59;Matheus;
6;Jonas;
7;Vanessa;
74;Karla;
48;Tatiana;
46;Larissa;
10;Marcela;
```

O programa gera como saída: 

```
*** p1.txt
14;Joao;
29;Maria;
59;Matheus;
75;Mariana;
76;Mirtes;
*** p2.txt
6;Jonas;
7;Vanessa;
46;Larissa;
48;Tatiana;
74;Karla;
*** p3.txt
10;Marcela;
```

#### Arquivo a ser entregue:

- entregue apenas o arquivo geracao.c, alterado com a implementação da função solicitada nesse problema 

#### Dicas Importantes:

- A entrada e a saída já são tratadas no arquivo fornecido para ler e imprimir os dados no formato esperado pela questão. Vocês devem APENAS implementar a função solicitada no problema
- Não use arquivos .h (coloque todas as definições de tipo no arquivo .c)
- Veja outras dicas em http://www.ic.uff.br/~vanessa/courses/runcodes.html

Para baixar os arquivos exemplo, faça o seguinte: Clique no arquivo. Na página que seu navegador web abre mostrando o conteúdo do arquivo, clique com o botão da direita em qualquer parte da página. Os próximos passos dependem do navegador que vc está usando.

No Google Chrome:
- escolha a opção Salvar Como. Ele salva o arquivo já no formato TXT, direitinho.

No Safari
- escolha a opção Salvar Página Como (escolha o formato do arquivo como sendo "Código Fonte da Página).