package pl.edu.mimuw.expression.unaryfunction;

import pl.edu.mimuw.expression.Expression;

public class Sinus extends UnaryFunction {

    public Sinus(Expression expression) {
        super("sin", expression);
    }
    @Override
    public double evaluate(double x) {
        return Math.sin(x);
    }
}
