class Data {
    int dia, mes, ano;
    
    // Construtor com nomes de parâmetros diferentes dos atributos
    public Data(int d, int m, int a) {
        dia = d; 
        mes = m; 
        ano = a;
    }
    
    // Método para verificar se o ano é bissexto
    public boolean anoBissexto() {
        if (!dataValida()) return false;
        return (ano % 4 == 0 && ano % 100 != 0) || (ano % 400 == 0);
    }
    
    // Método para validar a data
    public boolean dataValida() {
        if (mes < 1 || mes > 12 || dia < 1) return false;
        
        int diasNoMes = 31;
        if (mes == 4 || mes == 6 || mes == 9 || mes == 11) {
            diasNoMes = 30;
        } else if (mes == 2) {
            diasNoMes = anoBissexto() ? 29 : 28;
        }
        
        return dia <= diasNoMes;
    }
    
    // Método para imprimir a data
    public void imprime(String separador) {
        if (!dataValida()) {
            System.out.println("INVALIDA");
        } else {
            System.out.println(dia + separador + mes + separador + ano);
        }
    }
}