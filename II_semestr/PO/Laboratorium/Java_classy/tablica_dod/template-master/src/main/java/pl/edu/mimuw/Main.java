package pl.edu.mimuw;
//presentation mode menu- view - appearance - presentation mode
//import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        IntArray A = new IntArray(1, 2, 3, 4);
        A = A.pushed(5);
        A.printArray();
        A = A.concat(new IntArray(6, 7, 8, 9, 10));
        A.printArray();
        System.out.println(A.at(5));
        A = A.reversed();
        A.printArray();
        A = A.popped();
        A.printArray();
        System.out.println(A.getLength());
        A = A.shifted();
        A.printArray();
        System.out.println(A.lastIndexOf(6) + A.indexOf(6));
        System.out.println(A);
        A = A.with(5, 100);
        A.printArray();
        A = A.unshifted( 101);
        A.printArray();
        System.out.println(A.includes(101));
       // IntArray B = new IntArray();
       // B = B.popped();
    }
}
