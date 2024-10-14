package pl.edu.mimuw.expression.singleelements;

import pl.edu.mimuw.expression.Expression;

public class Constant extends Expression {
    private final double value;
    public Constant(double value) {
        this.value = value;
    }

    @Override
    public double evaluate(double x) {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
