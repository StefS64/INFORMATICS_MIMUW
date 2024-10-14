package pl.edu.mimuw.expression.unaryoperator;

import pl.edu.mimuw.expression.Expression;

public class NegativeValue extends UnaryOperator {
    public NegativeValue(Expression expression) {
        super("-(", expression,')');
    }

    @Override
    public double evaluate(double x) {
        return -expression.evaluate(x);
    }
}