package pl.edu.mimuw;

import java.math.BigInteger;
import java.util.Objects;
public class Fraction {
    private final BigInteger numerator;
    private final BigInteger denominator;

    public Fraction(BigInteger numerator, BigInteger denominator) {
        this.numerator = numerator.divide(numerator.gcd(denominator));
        if(denominator.equals(BigInteger.ZERO)){
            throw new ArithmeticException("DIVISION BY ZERO");
        }
        this.denominator = denominator.divide(numerator.gcd(denominator));
    }

    public Fraction(int numerator, int denominator) {

        this.numerator = new BigInteger(Integer.toString(numerator));
        if(denominator == 0){
            throw new ArithmeticException("DIVISION BY ZERO");
        }
        this.denominator = new BigInteger(Integer.toString(denominator));
    }

    public Fraction multiply(Fraction multiplied) {
        BigInteger newNumerator = numerator.multiply(multiplied.numerator);
        BigInteger newDenominator = denominator.multiply(multiplied.denominator);
        return toIrreducible(newNumerator, newDenominator);
    }

    public Fraction add(Fraction added) {
        BigInteger newDenominator = denominator.multiply(added.denominator);
        BigInteger newNumerator = numerator.multiply(added.denominator).add(denominator.multiply(added.numerator));
        return toIrreducible(newNumerator, newDenominator);
    }

    private static Fraction toIrreducible(BigInteger num, BigInteger denom) {
        if (denom.equals(BigInteger.ZERO)){
            throw new ArithmeticException("DIVISION BY ZERO");
        }
        if (denom.equals(BigInteger.ZERO)) {
            return new Fraction(BigInteger.ONE, BigInteger.ZERO);
        }
        BigInteger divides =  num.gcd(denom);
        BigInteger newNumerator = num.divide(divides);
        BigInteger newDenominator = denom.divide(divides);
        return new Fraction(newNumerator, newDenominator);
    }

    public Fraction subtract(Fraction subtracted) {
        BigInteger newDenominator = denominator.multiply(subtracted.denominator);
        BigInteger newNumerator = numerator.multiply(subtracted.denominator.subtract(denominator.multiply(subtracted.numerator)));
        return toIrreducible(newNumerator, newDenominator);
    }

    public Fraction divide(Fraction divided) {
        BigInteger newNumerator = numerator.multiply(divided.denominator);
        BigInteger newDenominator = denominator.multiply(divided.numerator);
        return toIrreducible(newNumerator, newDenominator);
    }

    public String toString() {
        return "\\frac{" + numerator.toString() + "}{" + denominator.toString() + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fraction fraction = (Fraction) o;
        return Objects.equals(numerator, fraction.numerator) && Objects.equals(denominator, fraction.denominator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numerator, denominator);
    }
}
