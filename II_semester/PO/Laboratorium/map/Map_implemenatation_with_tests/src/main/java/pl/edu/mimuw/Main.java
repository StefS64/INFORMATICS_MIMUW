package pl.edu.mimuw;

public class Main {

    public static void main(String[] args) {
        var map = new MapArrayList<>();
        map.put("test", 123);
        map.put("test2", 456);
        map.put("test3", 789);
        map.put("test4", 456);
        if(map.containsKey("test")){
            System.out.println(map.get("test"));
        }
    }
}
