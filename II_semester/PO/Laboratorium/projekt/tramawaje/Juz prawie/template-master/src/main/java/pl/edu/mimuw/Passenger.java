package pl.edu.mimuw;

import pl.edu.mimuw.eventqueue.PassengerEvent;
import pl.edu.mimuw.lines.Line;
import pl.edu.mimuw.lines.Stop;

import java.sql.Time;
import java.time.LocalTime;

public class Passenger {
    private int id;
    private Stop destinationStop;
    private Stop closestStopToHome;
    public Passenger(int id, Stop closestStopToHome) {
        this.id = id;
        this.destinationStop = closestStopToHome;
        this.closestStopToHome = closestStopToHome;
    }


    public void chooseDestinationStop(Line line){
        destinationStop = line.getStops()[Losowanie.losuj(0, line.getNumberOfStops()-1)];
    }
    public Stop getClosestStopToHome(){
        return closestStopToHome;
    }

    public String toString(){
        return "passanger id: " + id + " destination: " + destinationStop.getName();
    }
    public Stop getDestinationStop() {
        return destinationStop;
    }
    public LocalTime chooseTime(){
        return LocalTime.of(Losowanie.losuj(6, 12), 0);
    }
}
