[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/QyM16vHl)
#### Problema

Dados dois arquivos texto, um contendo registros de departamentos e outro contendo registros de funcionários, implemente a função leftOuterJoin, que realiza a junção externa esquerda dos dois arquivos, produzindo como resultado um terceiro arquivo que contém registros de departamentos + funcionários. Na junção comum de dois arquivos, um registro está no resultado da junção quando o código do departamento do registro no arquivo de departamentos é igual ao código de departamento no registro do arquivo de funcionários. Nesse caso, o registro contém todos os dados de departamento e do funcionário, sem repetir a chave (código do departamento). No entanto, como o exercício pede a junção externa esquerda, deve-se garantir também que os dados de departamento (a tabela da esquerda da junção) apareçam no resultado, mesmo que não exista nenhum funcionário associado a esse departamento. 

Assuma que os arquivos não estão ordenados. 

A função a ser implementada, além de funções para tratamento de entrada e saída, estão no arquivo left_outer_join.c em anexo.

#### Exemplo 

##### Arquivo de Departamentos

Cada linha do arquivo de departamentos contém: 

- código do departamento
- sala do departamento
- nome do departamento 

```
1;101;RH;
2;201;TI;
4;102;Diretoria;
3;201;Suporte;
&nbsp;  
```
Note que há sempre uma linha vazia no final do arquivo. 

##### Arquivo de Funcionários

Cada linha do arquivo de funcionários contém: 

- código do funcionário
- código do departamento
- nome do funcionário 

```
1;1;Maria;
2;1;Carlos;
3;2;Ana;
4;3;Bia;
5;1;Joao;
&nbsp;
```

Note que há sempre uma linha vazia no final do arquivo. 

##### Arquivo resultante do Left Outer Join: 

Cada linha do arquivo contém: 

- código do departamento
- sala do departamento
- nome do departamento
- código do funcionário
- nome do funcionário

Todos esses dados são separados por ponto e vírgula. 

Apesar do atributo código de departamento pertencer tanto ao arquivo de departamento quanto ao arquivo de funcionários, **o atributo código de departamento aparece apenas uma vez** no arquivo resultante do join. 

Caso não exista funcionário associado a um determinado departamento, os valores de funcionário devem aparecer da seguinte forma: código do funcionário igual a zero, e o nome deve ser a string vazia. 

O arquivo deve ser gerado com uma linha vazia no final. 

```
1;101;RH;1;Maria;
1;101;RH;2;Carlos;
1;101;RH;5;Joao;
2;201;TI;3;Ana;
4;102;Diretoria;0;;
3;201;Suporte;4;Bia;
&nbsp;
```

Veja que no exemplo acima, o departamento de código 4 não tem funcionário associado, e portando a penúltima linha do arquivo fica com código de funcionário zero e nome vazio. 

#### Entrada 

A função recebe como parâmetro os nomes dos três arquivos a serem manipulados (nome dos dois arquivos de entrada e o nome do arquivo que deve ser gerado pela função). São fornecidos dois arquivos de entrada para testes de implementação (departamentos.txt e funcionarios.txt).

#### Saída

A saída é o conteúdo do arquivo resultante da junção, que já é tratada pelo código fornecido em anexo. 

#### Dicas Importantes:

- A entrada e a saída já são tratadas no arquivo fornecido para ler e imprimir os dados no formato esperado pela questão. Vocês devem APENAS implementar a função solicitada no problema
- Não use arquivos .h (coloque todas as definições de tipo no arquivo .c)
- Veja outras dicas em http://www.ic.uff.br/~vanessa/courses/runcodes.html

Para baixar os arquivos exemplo, faça o seguinte: Clique no arquivo. Na página que seu navegador web abre mostrando o conteúdo do arquivo, clique com o botão da direita em qualquer parte da página. Os próximos passos dependem do navegador que vc está usando.  

No Google Chrome: 
- escolha a opção Salvar Como. Ele salva o arquivo já no formato TXT, direitinho. 

No Safari 
- escolha a opção Salvar Página Como (escolha o formato do arquivo como sendo "Código Fonte da Página). 
