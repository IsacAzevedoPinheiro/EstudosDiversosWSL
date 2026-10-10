class Aluno {
    public double calcularMedia(double t1, double p1, double t2, double p2) {
        return (t1 + p1 + t2 + p2) / 4.0;
    }
    
    public String obterStatus(double media) {
        if (media > 6) 
            return "Aprovado";
        if (media >= 4 && media <= 6) 
            return "Verificação Suplementar";
        return "Reprovado";
    }
}