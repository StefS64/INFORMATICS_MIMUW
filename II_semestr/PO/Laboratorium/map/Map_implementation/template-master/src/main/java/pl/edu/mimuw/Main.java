package pl.edu.mimuw;
//presentation mode menu- view - appearance - presentation mode
//import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        int[] array = new int[]{1, 2};
        int size = 2;
        IntArray A = new IntArray(size , array);
        var B = A.addElement(34);
        var C = B.addElement(213);
        C.printArray();
        B = C.changeElement(1, 3);
        B = B.deleteElement();
        B.printArray();
    }
}
