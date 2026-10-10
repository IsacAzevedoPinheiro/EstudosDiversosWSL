lista = [1, 2 , "banana"]
print(lista[2]+"1")
print(lista)
a = 2
b = 1.1
print(a+b)
c = a+b
print(type(c))
resultado = []
for x in range(3):
    for y in range(2):
        resultado.append((x, y))
print(resultado)
resultadoLinha = [(x, y) for x in range(3) for y in range(2)]
resultadoLinha2 = [(x, y, z, w) for x in range(2) for y in range(2) for z in range(2) for w in range(2)]
print(resultadoLinha)
print(resultadoLinha2)
resultado3 = []
for a, b in zip([1, 2], [3, 4]):
    resultado3.append(a + b)
print(resultado3)
resultadoEmUmaLinha = []
for i in range(4): 
    resultadoEmUmaLinha.append(i)
print(resultadoEmUmaLinha[0])