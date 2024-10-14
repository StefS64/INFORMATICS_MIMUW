package pl.edu.mimuw;
//presentation mode menu- view - appearance - presentation mode
//import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        String word = scanner.nextLine();
        var tab = word.split(" ");
        int size = tab.length;
        int[] votes = new int[size+1];

        for(int i = 0; i < size; i++) {
            int vote = Integer.parseInt(tab[i]);
            if(i + 1 == vote) {
                System.out.println("Numer: " + (i+1) + " zagłosował na siebie ");
                return;
            } else {
                votes[vote]++;
            }
        }
        int max = 0;
        boolean draw = false;
        int victorious = 0;
        for(int i = 1; i <= size; i++) {
            if( max < votes[i]) {
                max = votes[i];
                victorious = i;
                draw = false;
            } else if(max == votes[i]) {
                draw = true;
            }
        }
        if(draw){
            System.out.println("REMIS");
        } else {
            System.out.println("Wygrał: " + victorious);
        }
    }
}
