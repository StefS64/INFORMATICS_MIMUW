package pl.edu.mimuw;


import pl.edu.mimuw.lines.Stop;
import pl.edu.mimuw.lines.TramLine;
import pl.edu.mimuw.simulationstatistics.OverallStatistics;
import pl.edu.mimuw.simulationstatistics.SimulationDayStatistics;
import pl.edu.mimuw.vehicles.Tram;

import java.util.Scanner;

public class Simulation {
    private int numberOfDays;
    private int numberOfAllStops;
    private int numberOfTramLines;
    private int numberOfPassengers;
    private Stop[] stops;
    private TramLine[] tramLines;
    private Passenger[] passengers;

    public void simulate(){
        scanInput();
        print();
        OverallStatistics mainStatistics = new OverallStatistics(stops,passengers);
        SimulationDayStatistics[] data = new SimulationDayStatistics[numberOfDays];
        for(int i = 1; i <= numberOfDays; i++) {
            DaySimulation simulateDay = new DaySimulation(passengers, tramLines, i);
            simulateDay.clearPassengersFromSimulation();
            simulateDay.simulateDay();
            data[i-1] = new SimulationDayStatistics(stops, passengers);
            mainStatistics.addDay(data[i-1]);
            simulateDay.clearPassengersFromSimulation();
        }
        System.out.println(mainStatistics);
        for(int i = 1; i <= numberOfDays; i++) {
            System.out.println("Day: "+ i +"\nSTATISTICS\n" + data[i-1].toString());
        }


    }


    private void scanInput(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of days: ");
        numberOfDays = scanner.nextInt();
        System.out.print("Enter Capacity of stops: ");
        int stopCapacity = scanner.nextInt();
        System.out.print("Enter number of all stops: ");

        numberOfAllStops = scanner.nextInt();
        stops = new Stop[numberOfAllStops];
        for(int i = 0; i < numberOfAllStops; i++){
            stops[i] = new Stop(scanner.next(),new Passenger[stopCapacity]);
        }
        System.out.print("Enter number of passengers: ");
        numberOfPassengers = scanner.nextInt();

        this.passengers = new Passenger[numberOfPassengers];
        for(int i = 0; i < numberOfPassengers; i++){
            passengers[i] = new Passenger(i, stops[Randomize.random(0, stops.length - 1)]);
        }



        System.out.print("Enter capacity of trams: ");
        int tramCapacity = scanner.nextInt();
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
                    startStopNum  = numberOfStops - 1;
                }
                trams[j] = new Tram(tramNumber, null, passengerArray, startStopNum);
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
            tramLines[i] = new TramLine(i, 2*duration, stops, trams, timeBetweenStops);
        }

    }

    private Stop findStop(String stopName){
        for(Stop stop : stops){
            if(stop.equals(stopName)){
                return stop;
            }
        }
        System.out.println("Stop not found");
        return null;
    }

    private void print(){
        System.out.println("\ndays: " + numberOfDays + "\nstops: " + numberOfAllStops + "\nnumber of tramlines: " + numberOfTramLines);
        System.out.println("------------------------------");
        for(int i = 0; i < tramLines.length; i ++){
            System.out.println(tramLines[i].toString() + "\n");
        }
        System.out.println("------------------------------");
        System.out.println("number of passengers: " + numberOfPassengers);
    }
}





