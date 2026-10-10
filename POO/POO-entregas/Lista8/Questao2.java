class OperacaoMatematica {
    public double dividir(double a, double b) throws Exception {
        if (b == 0) 
            throw new Exception("Divisão por zero em reais gera Infinity/NaN!");
        return a / b;
    }
    
    public double raizQuadradra(double a) throws Exception {
        if (a < 0) 
            throw new Exception("Raiz de número negativo!");
        return Math.sqrt(a);
    }
}