package pacotedaaplicacao;

public class Pedido{
    public void imprimeNota(float valorTotal, int quantProduto, String CPF, String metodoDePagamento, String nomeProduto){
        System.out.println("=====================================");
        System.out.println("NOTA FINCAL");
        System.out.println("Produto comprado: "+nomeProduto+"  quantidade: "+quantProduto);
        System.out.println("Metodo de pagamento: "+metodoDePagamento);
        System.out.println("CPF do cliente: "+CPF);
        System.out.println("Valor total da compra: R$"+valorTotal);
        System.out.println("=====================================");
        System.out.println("\n\nVolte sempre!!");
        return;
    }
}
