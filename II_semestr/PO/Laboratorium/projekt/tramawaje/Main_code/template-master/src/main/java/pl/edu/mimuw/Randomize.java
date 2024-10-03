package pl.edu.mimuw;

public class Randomize {
    public static int random(int a, int b) {
        java.util.Random rand  = new java.util.Random();
        return rand.nextInt(b - a + 1) + a;
    }
}
