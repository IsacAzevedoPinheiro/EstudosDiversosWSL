# ==============================================================================
# Exemplos e Conceitos Básicos do GAWK
# Baseado em: https://www.gnu.org/software/gawk/manual/html_node/Getting-Started.html
# ==============================================================================
#
# A função básica do awk é procurar em arquivos por linhas (ou outras unidades 
# de texto) que contenham certos padrões (patterns). Quando uma linha 
# corresponde a um padrão, o awk executa ações (actions) especificadas.
# 
# Diferente de linguagens procedurais, o awk é orientado a dados (data-driven).
# Um programa awk consiste em uma série de REGRAS (rules) e DEFINIÇÕES DE FUNÇÕES.
#
# Sintaxe de uma regra:
# pattern { action }
# ==============================================================================


# ------------------------------------------------------------------------------
# 1. FUNÇÕES (O usuário pode definir funções para reutilizar código)
# Embora seja um recurso avançado citado na página, funções seguem o formato:
# function nome(parametros) { ... }
# ------------------------------------------------------------------------------
function imprimir_cabecalho(texto) {
    print "========================================"
    print " " texto " "
    print "========================================"
}

function processar_linha(numero_linha, conteudo) {
    print "Processando linha " numero_linha ": " conteudo
}


# ------------------------------------------------------------------------------
# 2. PADRÃO ESPECIAL: BEGIN
# O padrão BEGIN é executado UMA ÚNICA VEZ antes de qualquer arquivo ser lido.
# Ideal para inicializar variáveis e imprimir cabeçalhos.
# ------------------------------------------------------------------------------
BEGIN {
    # Chamando a nossa função definida acima
    imprimir_cabecalho("INICIANDO O PROCESSAMENTO (Bloco BEGIN)")
    
    # Variáveis internas muito comuns (Built-in variables):
    FS = " "    # Field Separator: Define o separador de campos de entrada (padrão é espaço)
    OFS = " | " # Output Field Separator: Separador de campos na saída
    
    linhas_processadas = 0
}


# ------------------------------------------------------------------------------
# 3. REGRAS ORIENTADAS A DADOS (Data-Driven Rules)
# pattern { action }
# Se deixarmos o 'pattern' vazio, a ação é executada para TODAS as linhas.
# ------------------------------------------------------------------------------

# REGRA 1: Padrão que verifica se a linha tem mais de 2 campos (NF = Number of Fields)
NF > 2 {
    print "[Regra NF > 2] A linha tem " NF " campos."
}

# REGRA 2: Padrão de Expressão Regular (Regex)
# Se a linha contiver a palavra "awk" ou "gawk", ignorando maiúsculas/minúsculas
/awk|gawk|AWK|GAWK/ {
    print "[Regra Regex] Encontrou a palavra chave nesta linha!"
}

# REGRA 3: Executa para todas as linhas (sem padrão especificado)
{
    linhas_processadas++
    # Chamando função para processar a linha (NR = Number of Records / Linha atual)
    processar_linha(NR, $0)
    
    # $0 representa a linha inteira. 
    # $1, $2, $3... representam o primeiro, segundo, terceiro campo, etc.
}
# Regra do Isac, quando der 4 linhas acaba o programa

{
    if (linhas_processadas == 4){
        processar_linha(NR, $0)
        exit
    }
    else{
        print "ainda pode escrever"
    }
}

# ------------------------------------------------------------------------------
# 4. PADRÃO ESPECIAL: END
# O padrão END é executado UMA ÚNICA VEZ após todas as linhas serem lidas.
# Ideal para imprimir resumos ou totais.
# ------------------------------------------------------------------------------
END {
    imprimir_cabecalho("FIM DO PROCESSAMENTO (Bloco END)")
    print "Total de linhas lidas: " NR
    print "Variável de contagem manual: " linhas_processadas
}