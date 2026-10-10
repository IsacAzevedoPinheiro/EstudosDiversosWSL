class CalculadorPrimos extends Thread {
    int inicio, fim, contagem = 0;
    public CalculadorPrimos(int inicio, int fim) { 
        this.inicio = inicio; this.fim = fim; 
    }
    
    private boolean isPrimo(int num) {
        if (num < 2) 
            return false;
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) 
                return false;
        }
        return true;
    }
    
    public void run() {
        for (int i = inicio; i <= fim; i++) 
            if (isPrimo(i)) 
                contagem++;
    }
}

// No Main: O esforço de 0 a n é dividido entre Thread 1 (0 a n/2) e Thread 2 (n/2 + 1 a n).