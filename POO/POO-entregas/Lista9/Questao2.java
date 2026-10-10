import java.util.*;

class ContadorUnicas {
    public static int contarPalavrasUnicas(String frase) {
        Set<String> unicas = new HashSet<>(Arrays.asList(frase.split(" ")));
        return unicas.size();
    }
}