package pl.edu.mimuw;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {
        //System.out.println(args[0]);
        //System.out.println("Hello world! ddddd");
        // typy proste:
        int a = 1;
        //long b = 1L;
        // double, float, boolean, char
        //typy referencyjne, pisane dużą literą:
        //String s = null;
        StringBuilder s = new StringBuilder(); //- inicjalizacja nowego obiektu
        s.append("hello");//uwaga ta methoda przy finalu nadal doda do liczby słowo hello
        System.out.println(s);
        // możliwa jest dekalracja jednoa linijkowa( lecz wnuk woli jedna deklaracaja jedna linia. )
        //Alternatywnie inferencja typów(infered type)
        var b = 2;
        if(2 > 1){

        } else {

        }//uwaga to są statementy a nie można ich zwrócić jako boola
        for(int i = 0; i< 1; i ++){
            System.out.println(i);
        }
        for(var str : args){
            System.out.println(str);
        }
        //final int i = 0;//zachowuje zmienną jako ostateczną nie da się jej zmienić
        //i = 2; - nie kompiluje się
        //Tablice:
        //int[] ints = new int[5];
        //int[] ints = new int[]{ 1, 2, 3, 4, 5 };
        //String[] strings = new String[]{"MIM", "UW"};

        //Object[] objects = strings;// UWAGA jest to wskaźnik wiec zmiany w strings wracają do zmiena objects
        //objects[0] = new StringBuilder();
        // Jeżeli mamy klasę A i B
        // kowariancaja

        //String o = "hello";
        //System.out.println(o.getBytes());
        //System.out.println(ints);//wypisuje adres w pamięci gdzie znajduje sie nasza tablica początek mówi że jest to tablica intów
        //System.out.println(Arrays.toString(ints));
        //System.out.println(ints.length);
        //System.out.println(ints[0]);
        //implementacja choinki:
        int n = Integer.parseInt(args[0]);
        int p = Integer.parseInt(args[1]);
        for(int i = 0; i < n/2; i++){//n/2
            for(int j = 0; j < n/2 - i - 1; j++) {
                System.out.print(" ");
            }
            for(int j = 0; j < 2*i + 1; j++){
                System.out.print("*");
            }
            System.out.println();
        }
        for(int i = 0; i < p; i++){
            for(int j = 0; j < n/2-2; j++){
                System.out.print(" ");
            }
            System.out.print("| |" + '\n');
        }
    }
}
