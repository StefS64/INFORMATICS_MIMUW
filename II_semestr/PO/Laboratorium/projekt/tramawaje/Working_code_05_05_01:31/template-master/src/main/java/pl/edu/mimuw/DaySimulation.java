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
    private final Stop[] stops;
    private final String day;
    public DaySimulation(Passenger[] passengers, TramLine[] tramLines, Stop[] stops, int day){
        this.passengers = passengers;
        this.tramLines = tramLines;
        this.stops = stops;
        this.day = day + " ";
    }

    public void simulateDay(){
        EventQueue queue = new EventQueueVector();
        createVehicleEvents(queue);
        addPassengers(queue);
        while(!queue.isEmpty()){
            Event happeningEvent = queue.getNextEvent();
            System.out.println(happeningEvent.toString());
            Stop destination = happeningEvent.getStop();
            LocalTime time = happeningEvent.getTimeOfEvent();
            if(happeningEvent instanceof VehicleEvent){//tu zamianka ewnetualna
                if(((VehicleEvent) happeningEvent).getEventType() == VehicleEventType.ARRIVAL){

                    if(((VehicleEvent) happeningEvent).getVehicle() instanceof Tram tram){
                        tram.incStopNumOfLoop();
                        if (tram.getStopNumOfLoop() == tram.getLineLength()*2 - 1){
                            tram.resetStopNumOfLoop();
                            System.out.println(day + time + " " + tram.toString() + " FINISHED LOOP");
                        }
                    }

                    Vehicle vehicle = ((VehicleEvent) happeningEvent).getVehicle();
                    Passenger[] exitingPassengers = vehicle.exitingPassengers(destination);

                    for(int i = 0; i < exitingPassengers.length; i++){
                        System.out.println(day + exitingPassengers[i].happeningEventString("EXITED VEHICLE: " + vehicle.toString(), time));
                        exitingPassengers[i].incNumberOfRides();
                        exitingPassengers[i].startWaitingAt(time);
                    }
                    destination.accommodateExitingPassengers(exitingPassengers);

                } else if (((VehicleEvent) happeningEvent).getEventType() == VehicleEventType.DEPARTURE){

                    Vehicle vehicle = ((VehicleEvent) happeningEvent).getVehicle();
                    Passenger[] leavingPassengers = destination.leavingPassengers(vehicle);

                    for(int i = 0; i < leavingPassengers.length; i++){
                        System.out.println(day + leavingPassengers[i].happeningEventString("BOARDED VEHICLE: " + vehicle.toString(), time) +" destination: " + leavingPassengers[i].getDestinationStop().getName());
                        destination.addWaitingTime(leavingPassengers[i].stopWaitingAtAndGetDuration(time));
                    }
                    vehicle.boardPassengers(leavingPassengers);
                }
            } else if(happeningEvent instanceof PassengerEvent){
                Passenger passenger = ((PassengerEvent) happeningEvent).getPassenger();
                if(destination.getEmptySpots() == 0){
                    System.out.println(day + passenger.happeningEventString("NOT ENOUGH SPACE RETURNING HOME ", happeningEvent.getTimeOfEvent()));
                } else {
                    Passenger[] p = new Passenger[1];
                    p[0] = passenger;
                    destination.accommodateExitingPassengers(p);
                    System.out.println(day + passenger.happeningEventString("WAITING AT STATION ", happeningEvent.getTimeOfEvent()));
                    passenger.startWaitingAt(time);
                }
            }
        }
    }


    private void createVehicleEvents(EventQueue queue){
        for(int i = 0; i < tramLines.length; i++){
            tramLines[i].addScheduleToEventQueue(queue);
        }
        //queue.printEvents();
    }
    private void addPassengers(EventQueue queue){
        for(int i = 0; i < passengers.length; i++){
            PassengerEvent newEvent = new PassengerEvent(passengers[i].chooseTime(), passengers[i].getClosestStopToHome(), passengers[i], PassengerEventType.ARRIVALTOSTOP);
            queue.add(newEvent);
        }
    }
}
