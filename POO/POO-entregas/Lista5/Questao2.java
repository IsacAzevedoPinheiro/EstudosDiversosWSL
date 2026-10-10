import java.util.ArrayList;
import java.util.List;

class Contato {
    String nome;
    String telefone;
}

class Agenda {
    List<Contato> contatos = new ArrayList<>();
    
    void adicionarContato(String nome, String telefone) {
        Contato c = new Contato();
        c.nome = nome;
        c.telefone = telefone;
        contatos.add(c);
    }
}