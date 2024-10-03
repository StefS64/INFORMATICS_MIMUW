package pl.edu.mimuw.eventqueue;

import pl.edu.mimuw.Passenger;
import pl.edu.mimuw.lines.Stop;

import java.time.LocalTime;

public class PassengerEvent extends Event {
    private final Passenger passenger;
    private final PassengerEventType eventType;
    public PassengerEvent(LocalTime timeOfEvent, Stop stop, Passenger passenger, PassengerEventType eventType) {
        super(timeOfEvent, stop);
        this.passenger = passenger;
        this.eventType = eventType;
    }
    public String toString(){
        return "\t" + super.toString() + passenger.toString() + " Event: " + eventType.toString();
    }
    public Passenger getPassenger() {
        return passenger;
    }
    public PassengerEventType getEventType() {
        return eventType;
    }
}
