import java.util.ArrayList;
import java.util.List;

class Produto {
    String nome;
    double preco;
    int quantidadeEstoque;
}

class ItemPedido {
    Produto produto;
    int quantidade;
}

class Pedido {
    List<ItemPedido> itens = new ArrayList<>();
    String formaPagamento; // "dinheiro", "cheque" ou "cartão"
    
    void adicionarItem(Produto p, int qtd) {
        ItemPedido item = new ItemPedido();
        item.produto = p;
        item.quantidade = qtd;
        itens.add(item);
    }
}