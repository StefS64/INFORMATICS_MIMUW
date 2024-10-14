package pl.edu.mimuw.eventqueue;

import pl.edu.mimuw.lines.Stop;
import pl.edu.mimuw.vehicles.Vehicle;

import java.time.LocalTime;

public class VehicleEvent extends Event {

    private final Vehicle vehicle;
    VehicleEventType eventType;
    public VehicleEvent(LocalTime timeOfEvent, Stop stop, Vehicle vehicle, VehicleEventType eventType) {
        super(timeOfEvent, stop);
        this.vehicle = vehicle;
        this.eventType = eventType;
    }


    public String toString(){
        return super.toString() + vehicle.toString() + " Event: " + eventType.toString();
    }
    public Vehicle getVehicle(){
        return vehicle;
    }
    public VehicleEventType getEventType(){
        return eventType;
    }
}
