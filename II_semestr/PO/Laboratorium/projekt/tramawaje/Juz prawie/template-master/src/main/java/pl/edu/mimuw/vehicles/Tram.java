package pl.edu.mimuw.vehicles;

import pl.edu.mimuw.lines.Line;
import pl.edu.mimuw.Passenger;

public class Tram extends Vehicle {
    public Tram(int sideNumber, Line line, Passenger[] passengers) {
        super(sideNumber, line, passengers);
    }


    @Override
    public String toString(){
        return "tramline: " + super.toString();
    }

}
