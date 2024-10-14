package pl.edu.mimuw.lines;

import pl.edu.mimuw.Passenger;
import pl.edu.mimuw.vehicles.Vehicle;

import java.util.Arrays;

public class Stop {
    private String name;
    private Passenger[] waitingPassengers;
    private int emptySpots;
    public Stop(String name, Passenger[] waitingPassengers) {
        this.name = name;
        this.waitingPassengers = waitingPassengers;
        emptySpots = waitingPassengers.length;
    }
    public String getName() {
        return name;
    }
    public Passenger[] getWaitingPassengers() {
        return waitingPassengers;
    }
    public boolean equals(String checkedName){
        return name.equals(checkedName);
    }
    public int getEmptySpots() {
        return emptySpots;
    }

    public Passenger[] leavingPassengers(Vehicle vehicle) {
        Passenger[] leavingPassengers = new Passenger[vehicle.getEmptySpaces()];
        int numOfLeavingPassengers = 0;
        for(int i = 0; i < waitingPassengers.length && numOfLeavingPassengers < vehicle.getEmptySpaces(); i++){
            if(waitingPassengers[i] != null){
                leavingPassengers[numOfLeavingPassengers] = waitingPassengers[i];
                numOfLeavingPassengers++;
                waitingPassengers[i] = null;
                emptySpots++;
            }
        }
        return Arrays.copyOfRange(leavingPassengers, 0, numOfLeavingPassengers);
    }

    public void accommodateExitingPassengers(Passenger[] exitingPassengers) {
        if(exitingPassengers == null || exitingPassengers.length == 0){
            return;
        }
        int accommodated = 0;
        for(int i = 0; i < waitingPassengers.length && accommodated < exitingPassengers.length; i++){
            if(waitingPassengers[i] == null){
                waitingPassengers[i] = exitingPassengers[accommodated];
                accommodated++;
                emptySpots--;
            }
        }
    }
}
