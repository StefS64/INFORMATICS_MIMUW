package pl.edu.mimuw.expression.singleelements;

import pl.edu.mimuw.expression.Expression;

public class Variable extends Expression {
    @Override
    public double evaluate(double x) {
        return x;
    }

    @Override
    public String toString() {
        return String.valueOf('x');
    }
}
