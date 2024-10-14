package pl.edu.mimuw;
//presentation mode menu- view - appearance - presentation mode
//import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        var firm = new Company();
        firm.addNewEmployee("Bob","Golden", 42, "plumber");
        firm.printEmployes();
    }
}
