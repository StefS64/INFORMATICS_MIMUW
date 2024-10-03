package pl.edu.mimuw;

import java.util.Objects;

public class Circle extends Shape{//extends  dla abstract inmplements dla interfaca
    private final double radius;
    //private final double pi = 3.1415926539;

    public Circle(double radius) {
        super(
                radius * radius * Math.PI,
            radius * Math.PI * 2
        );
        this.radius = radius;
    }
    @Override
    public double getArea() {

        return radius * radius * Math.PI;
    }
    @Override
    public double getCircumference() {
        return radius * Math.PI * 2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;//dlaczego porównanie referencji jest ok?
        Circle circle = (Circle) o;
        return Double.compare(radius, circle.radius) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(radius);
    }

    @Override
    public String toString() {
        return STR."Circle{radius=\{radius}\{'}'}";
    }
}

