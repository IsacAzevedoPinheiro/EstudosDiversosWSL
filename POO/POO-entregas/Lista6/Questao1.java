class Calculadora {
    private double memoria = 0;
    
    public void somar(double valor) {
         memoria += valor; 
        }
    public void subtrair(double valor) { 
        memoria -= valor; 
    }
    public void multiplicar(double valor) { 
        memoria *= valor; 
    }
    public void dividir(double valor) { 
        if(valor != 0) memoria /= valor; 
    }
    public void limpar() { 
        memoria = 0; 
    }
    public double getMemoria() {
         return memoria; 
        }
}