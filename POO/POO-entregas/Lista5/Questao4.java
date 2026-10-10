class ContaCorrente {
    private double saldo;
    
    public void depositar(double valor) {
        this.saldo += valor;
    }
    
    public boolean sacar(double valor) {
        double taxa = 1.50;
        if (this.saldo >= (valor + taxa)) {
            this.saldo -= (valor + taxa);
            return true;
        }
        return false;
    }
    
    public double getSaldo() {
        return this.saldo;
    }
}