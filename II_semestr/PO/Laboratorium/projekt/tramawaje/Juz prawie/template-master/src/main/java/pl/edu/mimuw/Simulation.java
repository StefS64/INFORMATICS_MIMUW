package pl.edu.mimuw;


import pl.edu.mimuw.eventqueue.*;
import pl.edu.mimuw.lines.Stop;
import pl.edu.mimuw.lines.TramLine;
import pl.edu.mimuw.vehicles.Tram;

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
            if(happeningEvent.getEventType() == EventType.PASSENGER){
                happeningEvent.
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
            stops[i] = new Stop(scanner.next());
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
                trams[j] = new Tram(tramNumber, null, passengerArray);
                tramNumber++;
            }

            Stop[] stops = new Stop[numberOfStops];
            int[] timeBetweenStops = new int[numberOfStops];

            int duration = 0;
            for(int j = 0; j < numberOfStops; j++){
                stops[j] = findStop(scanner.next());
                timeBetweenStops[j] = scanner.nextInt();
                duration += timeBetweenStops[j];
            }
            tramLines[i] = new TramLine(i, duration, stops, trams, timeBetweenStops);
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
        System.out.println("days: " + numberOfDays + ", stops: " + numberOfAllStops + ", trams: " + numberOfTramLines+ ", passengers: " + numberOfPassengers + ", number of lines: "+ numberOfTramLines);
    }

    public void addPassengers(EventQueue queue){
        for(int i = 0; i < numberOfPassengers; i++){
            Passenger newPassenger = new Passenger(i, stops[Losowanie.losuj(0, stops.length - 1)]);
            PassengerEvent newEvent = new PassengerEvent(newPassenger.chooseTime(), newPassenger.getClosestStopToHome(), newPassenger, PassengerEventType.ARRIVALTOSTOP);
            queue.add(newEvent);
        }
    }
}





