package pl.edu.mimuw.eventqueue;

import pl.edu.mimuw.lines.Stop;
import pl.edu.mimuw.vehicles.Vehicle;

import java.time.LocalTime;

public class VehicleEvent extends Event {

    private final Vehicle vehicle;
    VehicleEventType eventVehicleType;
    public VehicleEvent(LocalTime timeOfEvent, Stop stop, Vehicle vehicle, VehicleEventType eventVehicleType) {
        super(timeOfEvent, stop, EventType.VEHICLE);
        this.vehicle = vehicle;
        this.eventVehicleType = eventVehicleType;
    }


    public String toString(){
        return super.toString() + vehicle.toString() + " Event: " + eventVehicleType.toString();
    }
    public Vehicle getVehicle(){
        return vehicle;
    }
    public VehicleEventType getVehicleEventType(){
        return eventVehicleType;
    }
}
