import java.util.*;

class Frequencia {
    public static void contarOcorrencias(String frase) {
        Map<String, Integer> contagem = new HashMap<>();
        for (String palavra : frase.split(" ")) {
            contagem.put(palavra, contagem.getOrDefault(palavra, 0) + 1);
        }
        System.out.println(contagem);
    }
}