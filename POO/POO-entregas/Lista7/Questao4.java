abstract class Conta {
    int numero;
    float saldo;
    public void depositar(float valor) { 
        saldo += valor;
    }
    public boolean sacar(float valor) {
        if (saldo >= valor) { 
            saldo -= valor; 
            return true; 
        }
        return false;
    }
}

class ContaComum extends Conta {
    
}

class ContaPoupanca extends Conta {
    public void render(float percentual) { 
        saldo += saldo * (percentual / 100); 
    }
}

class ContaEspecial extends Conta {
    float limite;
    @Override
    public boolean sacar(float valor) {
        if (saldo + limite >= valor) {
            saldo -= valor; return true; 
        }
        return false;
    }
}