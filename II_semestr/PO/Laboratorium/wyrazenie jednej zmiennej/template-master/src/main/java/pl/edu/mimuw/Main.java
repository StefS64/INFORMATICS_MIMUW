package pl.edu.mimuw;
//presentation mode menu- view - appearance - presentation mode
//import java.util.Arrays;

import pl.edu.mimuw.expression.binary.Addition;
import pl.edu.mimuw.expression.binary.Multiplication;
import pl.edu.mimuw.expression.Expression;
import pl.edu.mimuw.expression.binary.Subtraction;
import pl.edu.mimuw.expression.singleelements.Constant;
import pl.edu.mimuw.expression.singleelements.Variable;
import pl.edu.mimuw.expression.unaryfunction.Sinus;
import pl.edu.mimuw.expression.unaryoperator.Brackets;
import pl.edu.mimuw.expression.unaryoperator.NegativeValue;

public class Main {

    public static void main(String[] args) {
        Expression A = new Variable();
        Expression B = new Constant(3.14/2);
        B = new Sinus(B);
        A = new Sinus(A);
        A = new Brackets(new Multiplication(A, B));
        A = new Addition(A, B);
        A = new NegativeValue(A);
        A = new Multiplication(new Constant(-1), A);
        B = new Subtraction(A, B);
        A = new Subtraction(A, A);
        System.out.println("A value:");
        System.out.println(A.evaluate(0.125));
        System.out.println(A.toString());
        System.out.println("B value:");
        System.out.println(B.evaluate(0.125));
        System.out.println(B.toString());
    }
}
