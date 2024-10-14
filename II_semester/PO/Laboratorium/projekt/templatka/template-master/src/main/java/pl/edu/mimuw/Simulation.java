package pl.edu.mimuw;


import pl.edu.mimuw.eventqueue.*;
import pl.edu.mimuw.lines.Stop;
import pl.edu.mimuw.lines.TramLine;
import pl.edu.mimuw.vehicles.Tram;
import pl.edu.mimuw.vehicles.Vehicle;

import java.time.LocalTime;
import java.util.Scanner;

public class Simulation {
    private int numberOfDays;
    private int stopCapacity;
    private int tramCapacity;
    private int numberOfAllStops;
    private int numberOfTramLines;
    private int numberOfPassengers;
    private Stop[] stops;
    private TramLine[] tramLines;

    public void simulate(){
        scanInput();
        print();
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
                            System.out.println(time +" "+ tram.toString() + " FINISHED LOOP");
                        }
                    }

                    Vehicle vehicle = ((VehicleEvent) happeningEvent).getVehicle();
                    Passenger[] exitingPassengers = vehicle.exitingPassengers(destination);

                    for(int i = 0; i < exitingPassengers.length; i++){
                        System.out.println(exitingPassengers[i].happeningEventString("EXITED VEHICLE: " + vehicle.toString(), time));
                    }
                    destination.accommodateExitingPassengers(exitingPassengers);

                } else if (((VehicleEvent) happeningEvent).getEventType() == VehicleEventType.DEPARTURE){

                    Vehicle vehicle = ((VehicleEvent) happeningEvent).getVehicle();
                    Passenger[] leavingPassengers = destination.leavingPassengers(vehicle);

                    for(int i = 0; i < leavingPassengers.length; i++){
                        System.out.println(leavingPassengers[i].happeningEventString("BOARDED VEHICLE: " + vehicle.toString(), time) +" destination: " + leavingPassengers[i].getDestinationStop().getName());
                    }
                    vehicle.boardPassengers(leavingPassengers);

                }
            } else if(happeningEvent instanceof PassengerEvent){
                Passenger passenger = ((PassengerEvent) happeningEvent).getPassenger();
                if(destination.getEmptySpots() == 0){
                    System.out.println(passenger.happeningEventString("NOT ENOUGH SPACE RETURNING HOME ", happeningEvent.getTimeOfEvent()));
                } else {
                    Passenger[] p = new Passenger[1];
                    p[0] = passenger;
                    destination.accommodateExitingPassengers(p);
                    System.out.println(passenger.happeningEventString("WAITING AT STATION ", happeningEvent.getTimeOfEvent()));
                }
            }
        }
    }


    private void scanInput(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of days: ");
        numberOfDays = scanner.nextInt();
        System.out.print("Enter Capacity of stops: ");
        stopCapacity = scanner.nextInt();
        System.out.print("Enter number of all stops: ");

        numberOfAllStops = scanner.nextInt();
        stops = new Stop[numberOfAllStops];
        for(int i = 0; i < numberOfAllStops; i++){
            stops[i] = new Stop(scanner.next(),new Passenger[stopCapacity]);
        }
        System.out.print("Enter number of passengers: ");
        numberOfPassengers = scanner.nextInt();
        System.out.print("Enter capacity of trams: ");
        tramCapacity = scanner.nextInt();
        System.out.print("Enter number of tramlines: ");
        numberOfTramLines = scanner.nextInt();

        tramLines = new TramLine[numberOfTramLines];
        int tramNumber = 0;
        for(int i = 0; i < numberOfTramLines; i++){
            System.out.print("Enter number of trams: ");
            int numberOfTrams = scanner.nextInt();
            System.out.print("Enter number of stops: ");
            int numberOfStops = scanner.nextInt();

            Tram[] trams = new Tram[numberOfTrams];

            for(int j = 0; j < numberOfTrams; j++){
                Passenger[] passengerArray = new Passenger[tramCapacity];
                int startStopNum = 0;
                if(j >= trams.length/2) {
                    startStopNum  = trams.length-1;
                }
                trams[j] = new Tram(tramNumber, null, passengerArray, startStopNum);
                tramNumber++;
                System.out.println(tramNumber);
            }

            Stop[] stops = new Stop[numberOfStops];
            int[] timeBetweenStops = new int[numberOfStops];

            int duration = 0;
            for(int j = 0; j < numberOfStops; j++){
                stops[j] = findStop(scanner.next());
                timeBetweenStops[j] = scanner.nextInt();
                duration += timeBetweenStops[j];
            }
            tramLines[i] = new TramLine(i, 2*duration, stops, trams, timeBetweenStops);
        }

    }

    public Stop findStop(String stopName){
        for(Stop stop : stops){
            if(stop.equals(stopName)){
                return stop;
            }
        }
        System.out.println("Stop not found");
        return null;
    }

    private void createVehicleEvents(EventQueue queue){

        for(int i = 0; i < numberOfTramLines; i++){
            tramLines[i].addScheduleToEventQueue(queue);
        }
        //queue.printEvents();
    }

    public void print(){
        System.out.println("\ndays: " + numberOfDays + "\nstops: " + numberOfAllStops + "\nnumber of tramlines: " + numberOfTramLines);
        System.out.println("------------------------------");
        for(int i = 0; i < tramLines.length; i ++){
            System.out.println(tramLines[i].toString() + "\n");
        }
        System.out.println("------------------------------");
        System.out.println("number of passengers: " + numberOfPassengers);
    }

    public void addPassengers(EventQueue queue){
        for(int i = 0; i < numberOfPassengers; i++){
            Passenger newPassenger = new Passenger(i, stops[Losowanie.losuj(0, stops.length - 1)]);
            PassengerEvent newEvent = new PassengerEvent(newPassenger.chooseTime(), newPassenger.getClosestStopToHome(), newPassenger, PassengerEventType.ARRIVALTOSTOP);
            queue.add(newEvent);
        }
    }
}





