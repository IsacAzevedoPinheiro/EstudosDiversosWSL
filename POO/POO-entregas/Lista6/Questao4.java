class Circulo {
    private double x;
    private double y;
    private double raio;
    
    public Circulo(double x, double y, double raio) {
        this.x = x; this.y = y; this.raio = raio;
    }
    
    public double area() {
        return Math.PI * raio * raio;
    }
}