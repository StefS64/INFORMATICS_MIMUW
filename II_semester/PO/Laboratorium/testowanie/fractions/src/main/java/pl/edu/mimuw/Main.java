package pl.edu.mimuw;

public class Main {

    public static void main(String[] args) {
        var frac = new Fraction(1, 2);
        var frac1 = new Fraction(2, 3);
        var frac2 = new Fraction(3, 4);
        var frac3 = new Fraction(4, 5);
        frac = frac.add(frac1);
        System.out.println(frac.toString());
        frac = frac.multiply(frac2);
        System.out.println(frac.toString());
        frac = frac.subtract(frac3);
        System.out.println(frac.toString());
        var equal = new Fraction(1, 2);
        var equal1 = new Fraction(1, 2);
        System.out.println(equal.equals(equal1));
        var equal2 = new Fraction(1, 4);
        var equal3 = new Fraction(2, 8);
        System.out.println(equal2.equals(equal3));
        var equal4 = new Fraction(1, 4);
        var equal5 = new Fraction(2, 0);
        System.out.println(equal2.equals(equal3));
    }
}
