package pacotedaaplicacao;
public class Produto {
        public int quantidadeBatata = 10;
    public float retornaValor(String nomeProduto, int quant){
        if (nomeProduto.equals("batata")){
            return 3.5f * quant;
        }
        return 0f;
    }
    public int retornaQuantidade(String nomeProduto, int quant){
        if (nomeProduto.equals("batata")){
            return (quantidadeBatata - 1);
        }
        return 0;
    }
}
