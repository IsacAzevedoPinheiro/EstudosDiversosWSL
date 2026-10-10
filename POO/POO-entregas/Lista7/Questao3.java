abstract class Funcionario {
    String nome;
    float salarioBruto;
    
    float calcularIR(float base) {
        if (base <= 900) 
            return 0;
        if (base <= 1500) 
            return base * 0.15f;
        return base * 0.20f;
    }
    
    public abstract float salarioLiquido();
}

class Estagiario extends Funcionario {
    public float salarioLiquido() {
        return salarioBruto; // Sem desconto de IR
    }
}

class Vendedor extends Funcionario {
    float bonusPercentual;
    
    public float salarioLiquido() {
        float total = salarioBruto + (salarioBruto * bonusPercentual / 100);
        return total - calcularIR(total);
    }
}