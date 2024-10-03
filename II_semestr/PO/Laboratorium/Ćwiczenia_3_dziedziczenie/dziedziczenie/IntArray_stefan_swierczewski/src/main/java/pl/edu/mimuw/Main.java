package pl.edu.mimuw;
//presentation mode menu- view - appearance - presentation mode
//import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        var circle = new Circle(5.0);
       //Alternatywnie Shape circle = new Circle();
        var shapes = new Shape[1];
        shapes[0] = circle;
        var num = circle.getClass();
        System.out.println(num);


        /*
        cos tu jest nie tak.
        acceptShape(circle);
        private static void acceptShape(Shape shape);
        */


    }
}
