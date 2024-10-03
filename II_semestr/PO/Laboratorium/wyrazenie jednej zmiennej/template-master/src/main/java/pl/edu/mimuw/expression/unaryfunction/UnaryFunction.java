package pl.edu.mimuw.expression.unaryfunction;

import pl.edu.mimuw.expression.Expression;

public abstract class UnaryFunction extends Expression {
    private final String functionName;
    protected Expression single;
    public UnaryFunction(  String functionName, Expression single) {
        this.single = single;
        this.functionName = functionName;
    }

    @Override
    public String toString() {
        return  functionName+'(' + single.toString() + ')';
    }
}
