abstract class Animal {
    abstract String fala();
}

class Homem extends Animal { 
    String fala() { 
        return "Oi"; 
    } 
}
class Cao extends Animal { 
    String fala() { 
        return "Au au"; 
    } 
}
class Gato extends Animal { 
    String fala() { 
        return "Miau"; 
    } 
}

// No método main (loop):
// Animal[] animais = new Animal[10];
// Instanciar aleatoriamente...
// for(Animal a : animais) { System.out.println(a.fala()); }