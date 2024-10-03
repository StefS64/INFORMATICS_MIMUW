package pl.edu.mimuw.expression.unaryoperator;

import pl.edu.mimuw.expression.Expression;

public class Brackets extends UnaryOperator {
    public Brackets(Expression single) {
        super("(", single,')');
    }

    @Override
    public double evaluate(double x) {
                return expression.evaluate(x);
    }
}
