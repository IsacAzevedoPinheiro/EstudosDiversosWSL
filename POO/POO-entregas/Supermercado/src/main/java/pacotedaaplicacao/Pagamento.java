package pacotedaaplicacao;

public class Pagamento {
    public float pagamentoPIX(float valorTotal){
        return valorTotal;
    }
    public float pagamentoCartao(float valorTotal){
        return valorTotal*1.1f;
    }
}
