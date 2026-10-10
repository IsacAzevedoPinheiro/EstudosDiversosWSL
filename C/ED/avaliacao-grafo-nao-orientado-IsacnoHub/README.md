[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/GnyhxBpl)
#### Problema 

Implemente uma função que recebe como parâmetro um grafo valorado e retorna 1 caso ele seja não orientado, e 0 caso contrário. Como o grafo é valorado (nesse exercício, as arestas têm peso, mas o vértices não), note que o grafo só será não orientado se para toda aresta (v1,v2) com peso p existir uma aresta (v2, v1) com o mesmo peso p. 

Use o arquivo fornecido nesse exercício, pois ele já contém o tratamento de entrada e saída. 

#### Entrada: 
Grafo a ser analisado, informado como segue (cada dado deve ser informado em uma nova linha): 
- número de vértices
- um inteiro que representa o id do vértice (x número de vértices)
- número de arestas
- id do vértice origem, id do vértice destino e peso da aresta, separados por traço (x número de arestas)

Exemplo de entrada (comentários sobre o significado estão informados entre parênteses no exemplo abaixo): 

```
2 (número de vértices)
1 (primeiro vértice)
2
2 (número de arestas) 
1-2-3 (primeira aresta: origem-destino-peso)
2-1-3
```

#### Saída:
- 1, caso o grafo seja não orientado, 0 caso contrário.  

#### Exemplo 1:
##### Entrada
```
2 
1 
2
2
1-2-3 
2-1-3
```

##### Saída
```
1
```

#### Exemplo 2

##### Entrada
```
5
1
2
3
4
5
3
1-2-10
2-1-10
2-4-2
```

##### Saída
```
0
```

#### Dicas Importantes:

- A entrada e a saída já são tratadas no arquivo fornecido para ler e imprimir os dados no formato esperado pela questão. Vocês devem APENAS implementar a função solicitada no problema
- Não use arquivos .h (coloque todas as definições de tipo no arquivo .c)
- Veja outras dicas em http://www.ic.uff.br/~vanessa/courses/runcodes.html