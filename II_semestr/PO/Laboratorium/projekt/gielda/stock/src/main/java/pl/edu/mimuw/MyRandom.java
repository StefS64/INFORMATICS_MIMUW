package pl.edu.mimuw;

import pl.edu.mimuw.commissions.Commission;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MyRandom {
    public static int  rand(int a, int b) { //a inclusive. b inclusive
        Random rand  = new Random();
        if(a <= 0 && b <= 0 || a == b){
            throw new ArithmeticException("At least one of b and a should be non-negative");
        }
        if(a <= 0){
           a = 1;
        }
        if(b <= 0){
            b = 1;
        }
        if (a > b) {
            int temp = a;
            a = b;
            b = temp;
        }
        return a + rand.nextInt(b - a);
    }

    public static <K,V> K keyRand(Map<K, V> map){
        if (map.isEmpty()) {
            throw new IllegalArgumentException("Map is empty");
        }
        List<K> keys = new ArrayList<>(map.keySet());
        int index = rand(0, keys.size());
        return keys.get(index);
    }
    public static <K> K keyRand(K[] array){
        int index = rand(0, array.length);
        return array[index];
    }
}
