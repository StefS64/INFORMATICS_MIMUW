package pl.edu.mimuw;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    /*public static void main(String[] args) {
        //1. policz sumę elemantów
        var scanner = new Scanner(System.in);
        String slowo = scanner.nextLine();
        var tab = slowo.split(" ");
        int size = tab.length;
        int[] T = new int[size];
        int sum = 0;
        for(int i = 0; i < size; i++){
            T[i] = Integer.parseInt(tab[i]);
            sum+=T[i];
        }
        System.out.println("SUM IS: " + sum);
    }*/
    public static void main(String[] args) {
        //1. policz sumę elemantów
        System.out.println("Podaj elementy tablicy:");
        var scanner = new Scanner(System.in);
        String slowo = scanner.nextLine();
        var tab = slowo.split(" ");
        int size = tab.length;
        Long[] T = new Long[size];
        Long sum = 0L;
        Long maxx = 0L;
        for(int i = 0; i < size; i++){
            T[i] = Long.parseLong(tab[i]);
            sum+=T[i];
            if(maxx < T[i]){
                maxx = T[i];
            }
        }
        System.out.println("SUM IS: " + sum);
        System.out.println("MAX IS: " + maxx);
        /*int[] wystap = new int[Math.toIntExact(maxx)];
        int ile = 0;
        for(int i = 0; i < size; i++ ) {
            if( wystap[Math.toIntExact(T[i])] != 1 ) {
                wystap[Math.toIntExact(T[i])] = 1;
                ile++;
            }
        }
        int[] wyn = new int[ile];
        int indeks = 0;
        for(int i = 0; i < size; i++ ) {
            if( wystap[Math.toIntExact(T[i])] == 1 ) {
                wyn[indeks] = Math.toIntExact(T[i]);
                wystap[Math.toIntExact(T[i])] = 2;
                indeks++;
            }
        }
        System.out.println(Arrays.toString(wyn));
    */

    }
}
