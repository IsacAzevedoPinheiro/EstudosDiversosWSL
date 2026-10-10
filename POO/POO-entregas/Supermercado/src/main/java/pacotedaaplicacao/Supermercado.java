package pacotedaaplicacao;
import java.util.Scanner;
public class Supermercado {

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        Produto produto = new Produto(); //objeto só pra acessar os métodos
        Pagamento pagamento = new Pagamento();
        Pedido pedido = new Pedido();
        boolean continua = true;
        boolean respotaInavalida = true;
        boolean recomecaCompra = true;
        float valorTotal = 0f;
        int quantProduto = 0;
        String nomeProduto = new String();
        while(continua){
            while (recomecaCompra){
                System.out.println("Bem vindo ao mercado que só tem batata!");
                System.out.println("Entre com o nome do produto: ");
                nomeProduto = teclado.nextLine().toLowerCase().strip();
                if (!(nomeProduto.equals("batata"))){
                    System.out.println("Só tem batata");
                    System.out.println("Recomeçar compra? (sim ou não)");
                    String recomeco = teclado.nextLine().strip();
                    if (recomeco.equals("não")){
                        System.out.println("Seja mal vindo");
                        recomecaCompra = false;
                    }
                }

                System.out.println("Entre com a quantidade dele: ");
                quantProduto += teclado.nextInt(); teclado.nextLine(); //Para pegar o buffer
                if (quantProduto <= produto.retornaQuantidade(nomeProduto, quantProduto)){
                    valorTotal = produto.retornaValor(nomeProduto, quantProduto);
                    recomecaCompra = false;
                }
                else{
                    System.out.println("Quantidade indisponível...");
                }
            }
            while (respotaInavalida){
                System.out.println("Deseja continuar comprando? (responda com sim ou não)");
                String respota = teclado.nextLine().strip().toLowerCase();
                if (respota.equals("não")){
                    continua = false;
                    respotaInavalida = false;
                }
                else if (respota.equals("sim")) {
                    respotaInavalida = false; // Sai deste mini-loop e volta lá pro topo comprar mais batata
                } 
                else {
                    System.out.println("Resposta inválida.");
                }
            }
            recomecaCompra = true;
            respotaInavalida = true;
        }
        
        if (!(continua)){
            System.out.println("Entre com método de pagamento ");
            System.out.println("PIX ou cartão, a segunda opção terá um acrécimo de 10% da maquina: ");
            String metodoDePagamento = teclado.nextLine().strip().toLowerCase();
            if (metodoDePagamento.equals("pix"))
                valorTotal = pagamento.pagamentoPIX(valorTotal);
            else if (metodoDePagamento.equals("cartao") || metodoDePagamento.equals("cartão"))
                valorTotal = pagamento.pagamentoCartao(valorTotal);
            String CPF = new String();
            System.out.println("CPF na nota? (sim ou não)");
            String respotaCPF = teclado.nextLine().strip().toLowerCase();
            if (respotaCPF.equals("sim")){
                System.out.println("Entre com o CPF: ");
                CPF = teclado.nextLine().strip();
            }
            else{
                CPF = "Não informado";
            }
            pedido.imprimeNota(valorTotal, quantProduto, CPF, metodoDePagamento, nomeProduto);
        }
        teclado.close();
    }
}