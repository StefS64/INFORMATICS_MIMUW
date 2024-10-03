package pl.edu.mimuw;

import static java.lang.StringTemplate.STR;

public record ComplexNumber(
    int real,
    int imaginary
) {
   @Override
    public String toString() {
       return STR."\{real} + \{imaginary}i";
    }

}
