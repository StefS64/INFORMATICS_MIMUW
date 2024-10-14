package pl.edu.mimuw.expression.binary;

import pl.edu.mimuw.expression.Expression;

public class Multiplication extends BinaryOperator {
   public Multiplication(Expression left, Expression right) {
       super(left, '*',right);
   }

    @Override
    public double evaluate(double x) {
        return left.evaluate(x) * right.evaluate(x);
    }
}