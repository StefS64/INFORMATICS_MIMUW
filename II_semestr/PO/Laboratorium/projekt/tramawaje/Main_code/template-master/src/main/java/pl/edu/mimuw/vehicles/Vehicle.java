package pl.edu.mimuw.vehicles;

import pl.edu.mimuw.lines.Line;
import pl.edu.mimuw.Passenger;
import pl.edu.mimuw.lines.Stop;

import java.util.Arrays;

public abstract class Vehicle {
    protected final int sideNumber;
    protected Line line;
    protected Passenger[] passengers;
    protected int emptySpaces;
    public Vehicle(int sideNumber, Line line, Passenger[] passengers) {
        this.sideNumber = sideNumber;
        this.line = line;
        this.passengers = passengers;
        emptySpaces = passengers.length;
    }

    @Override
    public String toString(){
        return line.getNumber() + " (Side num: " + sideNumber + ")";
    }

    public void setLine(Line line){
        this.line = line;
    }

    public int getLineLength(){
        return line.getStops().length;
    }
    public Line getLine(){
        return line;
    }

    //This function gets a table which size doesn't exceed number of empty spaces int tram.
    public void boardPassengers(Passenger[] newcomers) {
        if(newcomers == null || newcomers.length == 0){
            return;
        }
        int onBoard = 0;
       for(int i = 0; i < passengers.length && onBoard < newcomers.length; i++){
           if(passengers[i] == null){
               passengers[i] = newcomers[onBoard];
               passengers[i].chooseDestinationStop(this);
               onBoard++;
               emptySpaces--;
           }
       }
    }

    public Passenger[] exitingPassengers(Stop stop) {
        Passenger[] exitingPassengers = new Passenger[stop.getEmptySpots()];
        int numOfExitingPassengers = 0;
        for(int i = 0; i < passengers.length && numOfExitingPassengers < stop.getEmptySpots(); i++){
            if(passengers[i] != null && passengers[i].getDestinationStop().equals(stop)){
                exitingPassengers[numOfExitingPassengers] = passengers[i];
                numOfExitingPassengers++;
                passengers[i] = null;
                emptySpaces++;
            }
        }
        return Arrays.copyOfRange(exitingPassengers, 0, numOfExitingPassengers);
    }


    public int getEmptySpaces() {
        return emptySpaces;
    }

    public void clearPassengerData(){
        Arrays.fill(passengers, null);
        emptySpaces = passengers.length;
    }
}
