package pl.edu.mimuw.expression.binary;


import pl.edu.mimuw.expression.Expression;

public abstract class BinaryOperator extends Expression {
    private final char opertorChar;
    protected Expression left;
    protected Expression right;
    public BinaryOperator(Expression left, char opertorChar, Expression right) {
        this.left = left;
        this.opertorChar = opertorChar;
        this.right = right;
    }

    public String toString() {
        return  left + String.valueOf(opertorChar) + right;
    }

}
