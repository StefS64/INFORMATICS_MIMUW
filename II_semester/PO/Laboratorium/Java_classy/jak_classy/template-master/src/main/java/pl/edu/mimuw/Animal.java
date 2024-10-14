package pl.edu.mimuw;

public interface Animal {
    default void walk(){
        System.out.println("I'm walking");
    }
    // void walk(); - nie zadziała interfejs sie zepsuje nie kompiluje default robic przy nadpisywnaiu
    //interfejs nie zawiera implementacji
    //interfejs nie ma constructora
    void eat(int food);
}
