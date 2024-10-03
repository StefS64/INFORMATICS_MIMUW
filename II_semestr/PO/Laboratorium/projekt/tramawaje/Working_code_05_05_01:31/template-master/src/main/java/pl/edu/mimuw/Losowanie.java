package pl.edu.mimuw;

import java.util.Random;

public class Losowanie {
    public static int losuj(int a, int b) {
        Random rand  = new Random();
        return rand.nextInt(b - a + 1) + a;
    }
}
