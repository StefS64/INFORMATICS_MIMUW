package pl.edu.mimuw;

public class Shape { // uwaga ma przynajmniej jedną methodę która"nic nie robi"
    private final double area;
    private final double circumference;

    public Shape(double area, double circumference) {
        this.area = area;
        this.circumference = circumference;
    }
    public  double getArea() {
        return area;
    }

    public double getCircumference() {
        return circumference;
    }
    //abstract double getArea();
    // uwaga tu mogą być methody
    //abstract double getCircumference();
}
