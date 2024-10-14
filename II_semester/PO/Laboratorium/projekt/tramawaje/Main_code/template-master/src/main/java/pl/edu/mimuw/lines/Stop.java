package pl.edu.mimuw.lines;

import pl.edu.mimuw.Passenger;
import pl.edu.mimuw.vehicles.Vehicle;
import java.util.Arrays;

public class Stop {
    private final String name;
    private final Passenger[] waitingPassengers;
    private int emptySpots;
    private int timeSpentWaiting;
    private int numOfPassengersThatWaited;

    public Stop(String name, Passenger[] waitingPassengers) {
        this.name = name;
        this.waitingPassengers = waitingPassengers;
        emptySpots = waitingPassengers.length;
        numOfPassengersThatWaited =0 ;
        timeSpentWaiting = 0;
    }
    @Override
    public String toString() {
        return name;
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
    public void addWaitingTime(int timeInMinutes){
        numOfPassengersThatWaited++;
        timeSpentWaiting += timeInMinutes;
    }
    public int getTimeSpentWaiting(){
        return timeSpentWaiting;
    }
    public int getNumOfPassengersThatWaited(){
        return numOfPassengersThatWaited;
    }
    public void clearData(){
        Arrays.fill(waitingPassengers, null);
        emptySpots = waitingPassengers.length;
        numOfPassengersThatWaited = 0;
        timeSpentWaiting = 0;
    }
}
