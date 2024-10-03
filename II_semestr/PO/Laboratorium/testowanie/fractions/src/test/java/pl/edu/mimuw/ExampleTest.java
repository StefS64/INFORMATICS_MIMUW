package pl.edu.mimuw;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

public class ExampleTest {

    @Test
    void shouldFractionConstructionFail(){
        BigInteger denom = new BigInteger("123");
        BigInteger numerator = new BigInteger("2443");
        var testAns = new Fraction(denom, numerator);
        var frac = new Fraction(numerator,denom);
        Assertions.assertEquals(frac.toString(), testAns.toString());
    }
    @Test
    void shouldAddFail(){
        var frac = new Fraction(11,14);
        var frac2 = new Fraction(3,5);
        var ans = frac.add(frac2);
        var testAns = new Fraction(97,70);
        Assertions.assertEquals(ans.toString(), testAns.toString());
    }
    @Test
    void shouldSubtractFail(){
        var frac = new Fraction(11,14);
        var frac2 = new Fraction(3,5);
        var ans = frac.subtract(frac2);
        var testAns = new Fraction(13,70);
        Assertions.assertEquals(ans.toString(), testAns.toString());
    }
    @Test
    void shouldMultiplyFail(){
        var frac = new Fraction(11,14);
        var frac2 = new Fraction(3,5);
        var ans = frac.multiply(frac2);
        var testAns = new Fraction(33,70);
        Assertions.assertEquals(ans.toString(), testAns.toString());
    }
    @Test
    void shouldDivideFail(){
        var frac = new Fraction(12,7);
        var frac2 = new Fraction(3,5);
        var ans = frac.divide(frac2);
        var testAns = new Fraction(20,7);
        Assertions.assertEquals(ans.toString(), testAns.toString());

    }
    @Test
    void shouldThrowOnDivisionByZero(){
        Assertions.assertThrows(ArithmeticException.class, ()->{

            ;});
    }
}
