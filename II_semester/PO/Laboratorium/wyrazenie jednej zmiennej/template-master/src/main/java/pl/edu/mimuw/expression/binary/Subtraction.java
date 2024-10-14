package pl.edu.mimuw.expression.binary;

import pl.edu.mimuw.expression.Expression;
import pl.edu.mimuw.expression.unaryoperator.Brackets;

public class Subtraction extends BinaryOperator {
    public Subtraction(Expression left, Expression right) {
        super(left, '-',new Brackets(right));
    }

    @Override
    public double evaluate(double x) {
        return left.evaluate(x) - right.evaluate(x);
    }
}