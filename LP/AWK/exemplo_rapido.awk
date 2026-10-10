# Função para formatar texto
function imprimir_cabecalho(texto){
    print "=================================="
    print "          " texto
    print "=================================="
}

# 1. Roda uma única vez no início
BEGIN {
    imprimir_cabecalho("Lista de Compras")
    total_linhas = 0
}

# 2. Regra principal: roda em LOOP até o final do aquivo ou exit
{
    # $1 é a primeira coluna, $2 é a segunda coluna
    print "Item " NR ": " $1 " - R$ " $2
    # Cada espaço no arquivo separa uma coluna da outra, " concatena string, melhor que 
    total_linhas++ # Conta quantas linhas processamos
}

/Cafe/ {
    print "-> Tem cafe no texto.txt!"
}

# 3. Roda uma única vez depois de acabarem as regras ou exit
END {
    print "----------------------------------"
    print "Total de itens lidos: " total_linhas
    print "Fim da execucao."
    print "\n\n\n"
}

#Rodando pelo terminal, AWK é interpretado, ent no terminal é igual Python
#Só copiar e colar:
#awk -f exemplo_rapido.awk texto.txt

#Agora vamos fazer isso mais fácil e mais legal
#RYAN ABRA O TERMINAL
#nessa mesma pasta por gentileza😁


#           REAL USO DO AWK ---------- PARTE LEGAL

#awk 'suas_regras_aqui' nome_do_arquivo.txt

#exemplos:

#awk '{ print $0 }' texto.txt
#awk '/Cafe/ { print $0 }' texto.txt
#awk 'BEGIN {print \"--- Início ---\"} /Cafe/ {print \"Achei o cafe!\"} {print $1} END {print \"--- Fim ---\"}' texto.txt

# \" é pra resolver um problema do powershell, no linux vai só: "