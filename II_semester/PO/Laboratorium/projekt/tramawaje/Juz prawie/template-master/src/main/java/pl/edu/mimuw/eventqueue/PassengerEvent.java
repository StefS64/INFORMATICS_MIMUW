package pl.edu.mimuw.eventqueue;

import pl.edu.mimuw.Passenger;
import pl.edu.mimuw.lines.Stop;

import java.time.LocalTime;

public class PassengerEvent extends Event {
    private Passenger passenger;
    private PassengerEventType passengerEventType;
    public PassengerEvent(LocalTime timeOfEvent, Stop stop, Passenger passenger, PassengerEventType eventType) {
        super(timeOfEvent, stop, EventType.PASSENGER);
        this.passenger = passenger;
        this.passengerEventType = eventType;
    }
    public String toString(){
        return "\t" + super.toString() + passenger.toString() + " Event: " + passengerEventType.toString();
    }
    public Passenger getPassenger() {
        return passenger;
    }
    public PassengerEventType getPassengerEventType() {
        return passengerEventType;
    }
}
