package com.simumic;

public class Cache implements AcessoMemoria {
    private final String nome;
    private final int[] tags;
    private final int[][] dados;
    private final boolean[] validos;

    private final int tamCache;
    private final int tamBloco;
    private final AcessoMemoria proximoNivel;

    private long hits;
    private long misses;
    private boolean ultimoAcessoHit = false;
    private int ultimoEndereco = -1;
    private int ultimoBloco = -1;
    private int ultimaLinha = -1;

    public Cache(AcessoMemoria proximoNivel, int tamCache, int tamBloco) {
        this("CACHE", proximoNivel, tamCache, tamBloco);
    }

    public Cache(String nome, AcessoMemoria proximoNivel, int tamCache, int tamBloco) {
        if (tamCache <= 0 || tamBloco <= 0) {
            throw new IllegalArgumentException("Tamanhos da cache devem ser positivos.");
        }

        this.nome = (nome == null || nome.trim().isEmpty()) ? "CACHE" : nome.trim();
        this.proximoNivel = proximoNivel;
        this.tamCache = tamCache;
        this.tamBloco = tamBloco;
        this.dados = new int[tamCache][tamBloco];
        this.tags = new int[tamCache];
        this.validos = new boolean[tamCache];
    }

    @Override
    public int read(int endereco) {
        if (endereco < 0) {
            return 0;
        }

        int bloco = endereco / tamBloco;
        int linha = linhaDoBloco(bloco);
        int offset = endereco % tamBloco;

        boolean hit = validos[linha] && tags[linha] == bloco;

        if (!hit) {
            misses++;
            ultimoAcessoHit = false;
            carregarBloco(bloco, linha);
            logAcesso("READ", endereco, bloco, linha, false);
        } else {
            hits++;
            ultimoAcessoHit = true;
            logAcesso("READ", endereco, bloco, linha, true);
        }

        ultimoEndereco = endereco;
        ultimoBloco = bloco;
        ultimaLinha = linha;

        return dados[linha][offset] & 0xFFFF;
    }

    @Override
    public void write(int endereco, int dado) {
        if (endereco < 0) {
            return;
        }

        int valor = dado & 0xFFFF;
        int bloco = endereco / tamBloco;
        int linha = linhaDoBloco(bloco);
        int offset = endereco % tamBloco;

        boolean hit = validos[linha] && tags[linha] == bloco;

        if (!hit) {
            misses++;
            carregarBloco(bloco, linha);
            logAcesso("WRITE", endereco, bloco, linha, false);
        } else {
            hits++;
            logAcesso("WRITE", endereco, bloco, linha, true);
        }

        ultimoEndereco = endereco;
        ultimoBloco = bloco;
        ultimaLinha = linha;

        dados[linha][offset] = valor;
        proximoNivel.write(endereco, valor);
    }

    public void clear() {
        for (int i = 0; i < tamCache; i++) {
            validos[i] = false;
            tags[i] = 0;
            for (int j = 0; j < tamBloco; j++) {
                dados[i][j] = 0;
            }
        }
        hits = 0;
        misses = 0;
        ultimoEndereco = -1;
        ultimoBloco = -1;
        ultimaLinha = -1;
    }

    public long getHits() {
        return hits;
    }

    public long getMisses() {
        return misses;
    }
    public boolean isUltimoAcessoHit() {
        return ultimoAcessoHit;
    }
    public String getNome() {
        return nome;
    }

    public int getTamCache() {
        return tamCache;
    }

    public int getTamBloco() {
        return tamBloco;
    }

    public boolean isValida(int linha) {
        return validos[linha];
    }

    public int getTag(int linha) {
        return tags[linha];
    }

    public int getDado(int linha, int offset) {
        return dados[linha][offset] & 0xFFFF;
    }

    public int linhaParaEndereco(int endereco) {
        int bloco = endereco / tamBloco;
        return linhaDoBloco(bloco);
    }

    public int blocoDoEndereco(int endereco) {
        return endereco / tamBloco;
    }

    public int getUltimoEndereco() {
        return ultimoEndereco;
    }

    public int getUltimoBloco() {
        return ultimoBloco;
    }

    public int getUltimaLinha() {
        return ultimaLinha;
    }

    public String getVisualizacao() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s | blocos=%d | bloco=%d\n", nome, tamCache, tamBloco));
        sb.append(String.format("HITS=%d | MISSES=%d\n\n", hits, misses));

        for (int linha = 0; linha < tamCache; linha++) {
            int bloco = tags[linha];
            int inicio = bloco * tamBloco;
            int fim = inicio + tamBloco - 1;
            sb.append(String.format("L%02d | V=%d | TAG=%04d | [%04d-%04d] | ",
                    linha,
                    validos[linha] ? 1 : 0,
                    bloco,
                    inicio,
                    fim));

            for (int i = 0; i < tamBloco; i++) {
                sb.append(String.format("%04X", dados[linha][i] & 0xFFFF));
                if (i < tamBloco - 1) {
                    sb.append(' ');
                }
            }
            sb.append('\n');
        }

        return sb.toString();
    }

    private int linhaDoBloco(int bloco) {
        return bloco % tamCache;
    }

    private void carregarBloco(int bloco, int linha) {
        int inicioBloco = bloco * tamBloco;
        for (int i = 0; i < tamBloco; i++) {
            dados[linha][i] = proximoNivel.read(inicioBloco + i) & 0xFFFF;
        }
        tags[linha] = bloco;
        validos[linha] = true;
    }

    private void logAcesso(String operacao, int endereco, int bloco, int linha, boolean hit) {
        System.out.printf(
                "%s %s %s | end=%04d bloco=%03d linha=%02d tag=%03d%n",
                nome,
                operacao,
                hit ? "HIT" : "MISS",
                endereco,
                bloco,
                linha,
                tags[linha]);
    }
}