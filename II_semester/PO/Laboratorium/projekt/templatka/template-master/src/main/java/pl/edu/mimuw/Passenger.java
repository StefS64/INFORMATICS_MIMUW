package pl.edu.mimuw;

import pl.edu.mimuw.eventqueue.PassengerEvent;
import pl.edu.mimuw.lines.Line;
import pl.edu.mimuw.lines.Stop;
import pl.edu.mimuw.vehicles.Tram;
import pl.edu.mimuw.vehicles.Vehicle;

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


    public void chooseDestinationStop(Vehicle vehicle){
        if(vehicle instanceof Tram tram) {
            int numOfstop = Losowanie.losuj(tram.getStopNumOfLoop(), 2 * tram.getLineLength());
            Line line = tram.getLine();
            if(tram.getStartStopNum() == 0){
                destinationStop = line.getStops()[numOfstop % tram.getLineLength()];
            } else if(tram.getStartStopNum() == tram.getLineLength()-1){
                destinationStop = line.getStops()[tram.getStartStopNum() - (numOfstop % tram.getLineLength())];
            }
        }
    }
    public Stop getClosestStopToHome(){
        return closestStopToHome;
    }

    public String toString(){
        return "passanger id: " + id + " at stop: " + destinationStop.getName();
    }
    public String happeningEventString(String eventString, LocalTime timeOfEvent){
        return "\t" + timeOfEvent  + " by: "+ this.toString() +" event : "+ eventString;
    }
    public Stop getDestinationStop() {
        return destinationStop;
    }
    public LocalTime chooseTime(){
        return LocalTime.of(Losowanie.losuj(6, 12), 0);
    }
}
