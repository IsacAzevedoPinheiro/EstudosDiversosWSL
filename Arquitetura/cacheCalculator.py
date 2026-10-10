from math import log2
from math import floor

def parseDataSizeString(sizeString):
    size = 0
    achouNumero = False
    achouLetra = False
    for char in sizeString:
        try:
            size = (size*10)+int(char)
            if(achouLetra):
                raise Exception("Formato inválido de string.")
            achouNumero = True
        except ValueError:
            if(not achouNumero):
                raise Exception("Formato inválido de string.")
            achouLetra = True
            match char:
                case 'K':
                    size = size*1024
                case 'M':
                    size = size*1048576
                case 'G':
                    size = size*1073741824
                case 'B':
                    size = size*8
                case 'b':
                    pass
                case _:
                    raise Exception("Não identifiquei um caractere modificador de tamanho")
    return size

def calculaBitsNumBloco(tamanhoMemoria, tamanhoBloco):
    bits = log2(tamanhoMemoria/tamanhoBloco)
    if(bits%1 == 0):
        return int(bits)
    else:
        raise Exception("Confira os valores, número de bits não deu inteiro.")

def calculaAssociativo(tamanhoCache, tamanhoBloco, copyBack, bitsNumBloco):
    tamanhoLinha = bitsNumBloco+1+tamanhoBloco
    resposta = "\n=======================================CACHE ASSOCIATIVO (desconsidera política de substituição)=======================================\n\n"
    resposta += "|validade (1 bit)|"
    if(copyBack):
        resposta += " modificação (1 bit)|"
        tamanhoLinha += 1
    resposta += f" número do bloco ({bitsNumBloco} bits)| bloco({tamanhoBloco} bits)|\n\nQuantidade de linhas = {floor(tamanhoCache/tamanhoLinha)}.\nComo armazena um bloco por linha, essa é a quantidade de blocos armazenados."
    return resposta

def calculaMapeamentoDireto(tamanhoCache, tamanhoBloco, copyBack, bitsNumBloco):
    tamanhoLinha = 1+tamanhoBloco
    if(copyBack):
        tamanhoLinha += 1
    resposta = "\n=======================================CACHE MAPEAMENTO DIRETO=======================================\n\n"
    bitsPosicaoEstimados = floor(log2(tamanhoCache//tamanhoBloco))
    qtdLinhasEstimada = 2**bitsPosicaoEstimados
    bitsTag = bitsNumBloco-bitsPosicaoEstimados
    tamanhoLinha += bitsTag
    bitsOcupados = qtdLinhasEstimada*tamanhoLinha
    resposta += f"Estimando a quantidade de linhas, obtemos {qtdLinhasEstimada} linhas e {bitsPosicaoEstimados} bits para numerar todas as posições.\nTeremos, portanto, {bitsNumBloco}-{bitsPosicaoEstimados}={bitsTag} bits para a TAG.\n"
    if(bitsOcupados>tamanhoCache):
        bitsPosicaoEstimados -= 1
        bitsTag += 1
        tamanhoLinha += 1
        resposta+=f"Não coube na cache. Vamos diminuir a quantidade de linhas pela metade, sobrando {bitsPosicaoEstimados} para a posição e, por consequência, TAG passa a ter {bitsTag} bits, enquanto a linha fica com {tamanhoLinha} bits."
    resposta += "|validade (1 bit)|"
    if(copyBack):
        resposta += " modificação (1 bit)|"

    resposta += f" TAG ({bitsTag} bits)| bloco({tamanhoBloco} bits)|\n\nQuantidade de linhas = {2**bitsPosicaoEstimados}.\nComo armazena um bloco por linha, essa é a quantidade de blocos armazenados."
    return resposta

def calculaAssociativoPorConjunto(tamanhoCache, tamanhoBloco, copyBack, bitsNumBloco, tamanhoConjunto):
    tamanhoLinha = 1+tamanhoBloco
    if(copyBack):
        tamanhoLinha += 1
    resposta = "\n=======================================CACHE ASSOCIATIVO POR CONJUNTO (desconsidera política de substituição)=======================================\n\n"
    bitsPosicaoEstimados = floor(log2(tamanhoCache//(tamanhoBloco*tamanhoConjunto)))
    qtdLinhasEstimada = 2**bitsPosicaoEstimados
    bitsTag = bitsNumBloco-bitsPosicaoEstimados
    tamanhoLinha += bitsTag
    tamanhoLinha *= tamanhoConjunto
    bitsOcupados = qtdLinhasEstimada*tamanhoLinha
    resposta += f"Estimando a quantidade de linhas, obtemos {qtdLinhasEstimada} linhas e {bitsPosicaoEstimados} bits para numerar todas as posições.\nTeremos, portanto, {bitsNumBloco}-{bitsPosicaoEstimados}={bitsTag} bits para a TAG.\n"
    if(bitsOcupados>tamanhoCache):
        bitsPosicaoEstimados -= 1
        bitsTag += 1
        tamanhoLinha += (1*tamanhoConjunto)
        resposta+=f"Não coube na cache. Vamos diminuir a quantidade de linhas pela metade, sobrando {bitsPosicaoEstimados} para a posição e, por consequência, TAG passa a ter {bitsTag} bits, enquanto a linha fica com {tamanhoLinha} bits.\n\n"
    for i in range(tamanhoConjunto):
        resposta += "|validade (1 bit)|"
        if (copyBack):
            resposta += " modificação (1 bit)|"
        resposta += f" TAG ({bitsTag} bits)| bloco({tamanhoBloco} bits)|    "
    resposta+=f"\n\nQuantidade de linhas = {2**bitsPosicaoEstimados}.\nArmazena {tamanhoConjunto} blocos por linha, portanto, armazena {(2**bitsPosicaoEstimados)*tamanhoConjunto} blocos."
    return resposta

def main():
    tamanhoCache = parseDataSizeString(input("Informe o tamanho da cache: "))
    tamanhoMemoria = parseDataSizeString(input("Informe o tamanho da memória principal: "))
    tamanhoBloco = parseDataSizeString(input("Informe o tamanho do bloco: "))
    tamanhoConjunto = int(input("Digite o tamanho do conjunto: "))
    cb = input("Usa copy-back? (s/n)")
    if(cb == 's') or (cb == 'S'):
        cb = True
    else:
        cb = False
    bitsNumBloco = calculaBitsNumBloco(tamanhoMemoria, tamanhoBloco)
    print(f"O número do bloco terá {bitsNumBloco} bits.")

    print(calculaAssociativo(tamanhoCache, tamanhoBloco, cb, bitsNumBloco))
    print(calculaMapeamentoDireto(tamanhoCache, tamanhoBloco, cb, bitsNumBloco))
    print(calculaAssociativoPorConjunto(tamanhoCache, tamanhoBloco, cb, bitsNumBloco, tamanhoConjunto))
if __name__ == "__main__":
    main()