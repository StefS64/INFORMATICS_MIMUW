package pl.edu.mimuw.expression.unaryoperator;

import pl.edu.mimuw.expression.Expression;

public abstract class UnaryOperator extends Expression {
    private final String operatorLeft;
    private final Character operatorRight;
    protected Expression expression;
    public UnaryOperator( String operatorLeft, Expression single, Character operatorRight) {
        this.expression = single;
        this.operatorLeft = operatorLeft;
        this.operatorRight = operatorRight;
    }

    @Override
    public String toString() {
        if(operatorRight == null){
            return  String.valueOf(operatorLeft) + expression.toString();
        }
        return  String.valueOf(operatorLeft) + expression.toString() + String.valueOf(operatorRight);
    }
}
