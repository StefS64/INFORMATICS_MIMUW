package pl.edu.mimuw.vehicles;

import pl.edu.mimuw.lines.Line;
import pl.edu.mimuw.Passenger;
import pl.edu.mimuw.lines.Stop;

public class Tram extends Vehicle {
    private final int startStopNum;
    private int stopNumOfLoop = 1;
    public Tram(int sideNumber, Line line, Passenger[] passengers, int startStopNum) {
        super(sideNumber, line, passengers);
        this.startStopNum = startStopNum;
    }
    public int getStartStopNum() {
        return startStopNum;
    }
    public int getStopNumOfLoop() {
        return stopNumOfLoop;
    }
    public void incStopNumOfLoop() {
        stopNumOfLoop++;
    }
    public void resetStopNumOfLoop() {
        stopNumOfLoop = 1;
    }

    @Override
    public String toString(){
        return "tramline: " + super.toString();
    }

}
