package pl.edu.mimuw;

import pl.edu.mimuw.eventqueue.*;
import pl.edu.mimuw.lines.Stop;
import pl.edu.mimuw.lines.TramLine;
import pl.edu.mimuw.vehicles.Tram;
import pl.edu.mimuw.vehicles.Vehicle;

import java.time.LocalTime;

public class DaySimulation {
    private final Passenger[] passengers;
    private final TramLine[] tramLines;
    private final String day;
    public DaySimulation(Passenger[] passengers, TramLine[] tramLines, int day){
        this.passengers = passengers;
        this.tramLines = tramLines;
        this.day = day + " ";
    }

    public void simulateDay(){
        EventQueue queue = new EventQueueVector();
        createVehicleEvents(queue);
        addPassengers(queue);
        while(!queue.isEmpty()){
            Event happeningEvent = queue.getNextEvent();
            System.out.println(day + happeningEvent.toString());
            Stop stop = happeningEvent.getStop();
            LocalTime time = happeningEvent.getTimeOfEvent();
            if(happeningEvent instanceof VehicleEvent vehicleEvent){
                if(vehicleEvent.getEventType() == VehicleEventType.ARRIVAL){

                    if(vehicleEvent.getVehicle() instanceof Tram tram){
                        tram.incStopNumOfLoop();
                        if (tram.getStopNumOfLoop() == tram.getLineLength()*2 - 1){
                            tram.resetStopNumOfLoop();
                            System.out.println(day + time + " " + tram + " FINISHED LOOP");
                        }
                    }

                    Vehicle vehicle = vehicleEvent.getVehicle();
                    Passenger[] exitingPassengers = vehicle.exitingPassengers(stop);

                    for (Passenger exitingPassenger : exitingPassengers) {
                        System.out.println(day + exitingPassenger.happeningEventString("EXITED VEHICLE: " + vehicle + ",at station " + stop, time));
                        exitingPassenger.incNumberOfRides();
                        exitingPassenger.startWaitingAt(time);
                    }
                    stop.accommodateExitingPassengers(exitingPassengers);

                } else if (vehicleEvent.getEventType() == VehicleEventType.DEPARTURE){

                    Vehicle vehicle = vehicleEvent.getVehicle();
                    Passenger[] leavingPassengers = stop.leavingPassengers(vehicle);
                    vehicle.boardPassengers(leavingPassengers);
                    for (Passenger leavingPassenger : leavingPassengers) {
                        System.out.println(day + leavingPassenger.happeningEventString("BOARDED VEHICLE: " + vehicle +", from: "+ stop, time) + " traveling to: " + leavingPassenger.getDestinationStop().toString());
                        stop.addWaitingTime(leavingPassenger.stopWaitingAtAndGetDuration(time));
                    }
                }
            } else if(happeningEvent instanceof PassengerEvent){
                Passenger passenger = ((PassengerEvent) happeningEvent).getPassenger();
                if(stop.getEmptySpots() == 0){
                    System.out.println(day + passenger.happeningEventString("NOT ENOUGH SPACE RETURNING HOME ", happeningEvent.getTimeOfEvent()));
                } else {
                    Passenger[] p = new Passenger[1];
                    p[0] = passenger;
                    stop.accommodateExitingPassengers(p);
                    System.out.println(day + passenger.happeningEventString("WAITING AT STATION ", happeningEvent.getTimeOfEvent()));
                    passenger.startWaitingAt(time);
                }
            }
        }
    }


    private void createVehicleEvents(EventQueue queue){
        for (TramLine tramLine : tramLines) {
            tramLine.addScheduleToEventQueue(queue);
        }
        //queue.printEvents();
    }
    private void addPassengers(EventQueue queue){
        for (Passenger passenger : passengers) {
            PassengerEvent newEvent = new PassengerEvent(passenger.chooseTime(), passenger.getClosestStopToHome(), passenger, PassengerEventType.ARRIVALTOSTOP);
            queue.add(newEvent);
        }
    }
    public void clearPassengersFromSimulation(){
        for (TramLine tramLine : tramLines) {
            tramLine.clearData();
        }
        for (Passenger passenger : passengers) {
            passenger.clearCollectedData();
        }
    }
}
