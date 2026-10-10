class Tempo {
    public void converterEImprimir(int totalSegundos) {
        int horas = totalSegundos / 3600;
        int minutos = (totalSegundos % 3600) / 60;
        int segundos = totalSegundos % 60;
        System.out.println(horas + "h " + minutos + "m " + segundos + "s");
    }
}