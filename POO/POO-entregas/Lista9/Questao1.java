import java.util.*;

class Inversor {
    public static void inverteFrase(String frase) {
        List<String> palavras = Arrays.asList(frase.split(" "));
        Collections.reverse(palavras);
        System.out.println(String.join(" ", palavras));
    }
}