#### Problema 

Implemente a função insere(int cod\_cli, char *nome\_cli, char *nome\_arquivo\_hash, char *nome\_arquivo\_dados, int m) que realiza a inserção de clientes em uma Tabela Hash armazenada em disco que usa tratamento de colisão por Encadeamento Exterior. A função recebe o código e nome do cliente a serem inseridos, além dos nomes dos dois arquivos que armazenam a tabela Hash. O arquivo de hash contém os compartimentos da tabela, e o arquivo de dados contém a lista encadeada. O parâmetro m indica o tamanho da tabela (número de compartimentos). A função hash a ser utilizada é h(x) = x mod m. 

Veja o slide 15 da aula de [Tratamento de Colisões por Encadeamento Exterior](http://www.ic.uff.br/~vanessa/material/ed/13-TabelasHash-Encadeamento-Exterior.pdf) para entender a estrutura dos arquivos utilizados. 

São fornecidos arquivos para testes (tabHash.dat e clientes.dat), os quais possuem o seguinte conteúdo: 

```
TABELA HASH:
[0]   0
[1]  -1
[2]   4
[3]   1
[4]  -1
[5]   2
[6]  -1

DADOS:
[  0]  49,         Joao,  -1,  OCUPADO
[  1]  59,        Maria,   3,  OCUPADO
[  2] 103,        Pedro,  -1,  OCUPADO
[  3]   3,        Janio,   5, LIBERADO
[  4]  51,          Ana,  -1,  OCUPADO
[  5]  87,     Mauricio,  -1,  OCUPADO
```

Os valores apresentados em colchetes mostram o **endereço lógico** do registro. São esses endereços lógicos que são usados nos valores dos ponteiros. Note que ponteiros para NULL são representados como -1. No exemplo acima, o compartimento do endereço [2] da tabela Hash aponta para o endereço 4 do arquivo de dados, e lá nesse endereço encontra-se o cliente de chave 51 e nome Ana. O tamanho da tabela (m) nesse exemplo, é 7. Note que 51 mod 7 = 2. 

#### Entrada

- código do cliente
- nome do cliente
- arquivo hash, que contém os compartimentos da tabela
- arquivo de dados, que contém os registros de clientes armazenados na lista encadeada 

#### Saída 

- a função retorna o endereço (lógico) onde a inserção foi realizada, ou -1 caso a inserção não tenha sido realizada por já existir registro com aquela chave na tabela 

#### Exemplo 1

Assumindo que se deseja inserir o cliente de chave **8** e nome **Vanessa**, seu programa deve realizar a inserção de forma que os arquivos fiquem com o seguinte conteúdo:  

```
TABELA HASH:
[0]   0
[1]   6  <<< HOUVE ALTERAÇÃO AQUI
[2]   4
[3]   1
[4]  -1
[5]   2
[6]  -1

DADOS:
[  0]  49,         Joao,  -1,  OCUPADO
[  1]  59,        Maria,   3,  OCUPADO
[  2] 103,        Pedro,  -1,  OCUPADO
[  3]   3,        Janio,   5, LIBERADO
[  4]  51,          Ana,  -1,  OCUPADO
[  5]  87,     Mauricio,  -1,  OCUPADO
[  6]   8,      Vanessa,  -1,  OCUPADO  <<< HOUVE ALTERAÇÃO AQUI 

RESULTADO: 6
```

#### Exemplo 2: 

Caso uma inserção seja feita em uma lista encadeada que tem um registro marcado como liberado, a inserção deve ser feita nessa posição, ao invés de ser feita no final da lista encadeada. Por exemplo, caso seja necessário inserir o cliente de chave **10** e nome **Gabriel**, a inserção será realizada na posição 3 do arquivo de dados. O resultado dessa inserção sobre o arquivo resultante do Exemplo 1 é o seguinte: 

```
TABELA HASH:
[0]   0
[1]   6
[2]   4
[3]   1
[4]  -1
[5]   2
[6]  -1

DADOS:
[  0]  49,         Joao,  -1,  OCUPADO
[  1]  59,        Maria,   3,  OCUPADO
[  2] 103,        Pedro,  -1,  OCUPADO
[  3]  10,      Gabriel,   5,  OCUPADO <<< HOUVE ALTERAÇÃO AQUI 
[  4]  51,          Ana,  -1,  OCUPADO
[  5]  87,     Mauricio,  -1,  OCUPADO
[  6]   8,      Vanessa,  -1,  OCUPADO

RESULTADO: 3
```

#### Exemplo 3

Se, sobre o resultado do Exemplo 2, for inserido o cliente de chave **3** e nome **Leo**, o resultado deve ser o seguinte (inserção no final da lista encadeada): 

```
TABELA HASH:
[0]   0
[1]   6
[2]   4
[3]   1
[4]  -1
[5]   2
[6]  -1

DADOS:
[  0]  49,         Joao,  -1,  OCUPADO
[  1]  59,        Maria,   3,  OCUPADO
[  2] 103,        Pedro,  -1,  OCUPADO
[  3]  10,      Gabriel,   5,  OCUPADO
[  4]  51,          Ana,  -1,  OCUPADO
[  5]  87,     Mauricio,   7,  OCUPADO <<< HOUVE ALTERAÇÃO AQUI
[  6]   8,      Vanessa,  -1,  OCUPADO
[  7]   3,          Leo,  -1,  OCUPADO <<< HOUVE ALTERAÇÃO AQUI

RESULTADO: 7
```

#### Arquivos a entregar

Entregue apenas o arquivo **encadeamento_exterior.c**, com sua função de inserção implementada. Os demais arquivos serão adicionados automaticamente pelo Run.Codes na hora da compilação. Por isso é importante que você faça modificações apenas no arquivo encadeamento_exterior.c. 

#### Dicas Importantes: 

- Os arquivos de entrada devem ser colocados dentro da pasta cmake\_build\_debug do projeto caso você esteja usando o CLion
- A entrada e a saída já são tratadas no arquivo fornecido para ler e imprimir os dados no formato esperado pela questão. Vocês devem APENAS implementar a função solicitada no problema
- Não use arquivos .h (coloque todas as definições de tipo no arquivo .c)
- Veja outras dicas em http://www.ic.uff.br/~vanessa/courses/runcodes.html