package pl.edu.mimuw.lines;

import pl.edu.mimuw.Passenger;

public class Stop {
    private String name;
    private Passenger[] waitingPassengers;
    private int emptySpots;
    public Stop(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public Passenger[] getWaitingPassangers() {
        return waitingPassengers;
    }
    public boolean equals(String checkedName){
        return name.equals(checkedName);
    }
    public int getEmptySpots() {
        return emptySpots;
    }
}
