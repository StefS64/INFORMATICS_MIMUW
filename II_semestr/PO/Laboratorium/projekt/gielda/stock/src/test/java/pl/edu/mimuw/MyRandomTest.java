package pl.edu.mimuw;

import org.junit.jupiter.api.Test;
import pl.edu.mimuw.MyRandom;

import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Map;

public class MyRandomTest {

    @Test
    public void testRandWithinRange() {
        int a = 5;
        int b = 10;
        for (int i = 0; i < 1000; i++) {
            int result = MyRandom.rand(a, b);
            assertTrue(result >= a && result < b, "Result " + result + " should be between " + a + " (inclusive) and " + b + " (exclusive)");
        }
    }

    @Test
    public void testRandWithNegativeBound() {
        int a = -5;
        int b = 8;
        for (int i = 0; i < 1000; i++) {
            int result = MyRandom.rand(a, b);
            assertTrue(result >= a && result < b, "Result " + result + " should be between " + a + " (inclusive) and " + b + " (exclusive)");
        }
    }

    @Test
    public void testKeyRandWithMap() {
        Map<String, Integer> testMap = new HashMap<>();
        testMap.put("key1", 1);
        testMap.put("key2", 2);
        testMap.put("key3", 3);

        try {
            String result = MyRandom.keyRand(testMap);
            assertTrue(testMap.containsKey(result), "Result " + result + " should be a key in the map");
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException thrown");
        }
    }

    @Test
    public void testKeyRandWithEmptyMap() {
        Map<String, Integer> emptyMap = new HashMap<>();

        assertThrows(IllegalArgumentException.class, () -> MyRandom.keyRand(emptyMap));
    }

    @Test
    public void testKeyRandWithArray() {
        String[] testArray = {"item1", "item2", "item3"};

        try {
            String result = MyRandom.keyRand(testArray);
            boolean found = false;
            for (String item : testArray) {
                if (item.equals(result)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found, "Result " + result + " should be an element in the array");
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException thrown");
        }
    }
}
