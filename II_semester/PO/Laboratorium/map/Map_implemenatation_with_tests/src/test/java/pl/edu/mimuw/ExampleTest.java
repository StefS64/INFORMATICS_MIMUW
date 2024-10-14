package pl.edu.mimuw;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;


public class ExampleTest {

    @Test
    void shouldSizeFail(){
        var map = new MapArrayList<>();
        Assertions.assertEquals(map.size(), 0);
        for(int i = 1; i <= 10; i++){
            map.put("test"+i, i);
            Assertions.assertEquals(map.size(), i);
        }
    }
    @Test
    void shouldEqualsFail(){
        var map = new MapArrayList<>();
        var map2 = new MapArrayList<>();
        Assertions.assertEquals(map, map2);
        for(int i = 1; i <= 10; i++){
            map.put("test"+i, i);
        }
        for(int i = 1; i <= 10; i++){
            map2.put("test"+i, i);
        }
        Assertions.assertEquals(map, map2);
    }
    @Test
    void shouldContainsFail(){
        var map = new MapArrayList<>();
        map.put("test", 123);
        map.put("test2", 456);
        map.put("test3", 789);
        map.put("test4", 456);
        Assertions.assertFalse(map.containsKey("test"));
    }
    @Test
    void shouldPutFail(){
        var map = new MapArrayList<>();
        map.put("test", 123);
        map.put("test2", 456);
        map.put("test3", 789);
        map.put("test4", 456);
        Assertions.assertEquals(map.put("test3", 100), 789);
    }
    @Test
    void shouldClearFail(){
        var map = new MapArrayList<>();
        var map2 = new MapArrayList<>();
        map.put("test", 123);
        map.put("test2", 456);
        map.put("test3", 789);
        map.put("test4", 456);
        map.clear();
        Assertions.assertEquals(map, map2);
    }
}
