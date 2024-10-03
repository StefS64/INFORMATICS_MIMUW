package pl.edu.mimuw.simulationstatistics;

import pl.edu.mimuw.Passenger;
import pl.edu.mimuw.lines.Stop;

public class SimulationDayStatistics {
    protected int sumOfRides = 0;
    protected int[] numberOfPeopleThatWaited;
    protected int[] sumOfTimeWaited;
    protected Stop[] stops;
    public SimulationDayStatistics(Stop[] stops, Passenger[] passengers) {
        this.stops = stops;
        int[] sumOfTimeWaitedByPassengers = new int[stops.length];
        this.numberOfPeopleThatWaited = new int[stops.length];
        for(int i = 0; i < stops.length; i++) {
            sumOfTimeWaitedByPassengers[i] = stops[i].getTimeSpentWaiting();
            numberOfPeopleThatWaited[i] = stops[i].getNumOfPassengersThatWaited();
            stops[i].clearData();
        }
        for(Passenger passenger : passengers){
            sumOfRides+=passenger.getNumberOfRides();
            passenger.clearCollectedData();
        }
        this.sumOfTimeWaited = sumOfTimeWaitedByPassengers;
    }

    public String toString(){
        int totalWaitingTime = 0;
        for (int j : sumOfTimeWaited) {
            totalWaitingTime += j;
        }
        StringBuilder answer = new StringBuilder("\nTotal Number of rides: " + sumOfRides + "\n\n");
        answer.append("Total Waiting Time: ").append(totalWaitingTime).append("\n");
        answer.append("\nStops:\n");
        for(int i = 0; i < sumOfTimeWaited.length; i++){
            answer.append(stops[i].toString()).append("\n");
            answer.append("Total time waited at stop: ").append(sumOfTimeWaited[i]).append("\n");
            answer.append("How many times someone waited at stop: ").append(numberOfPeopleThatWaited[i]).append("\n");
            if(numberOfPeopleThatWaited[i] > 0){
                answer.append("Average waiting time: ").append((float) sumOfTimeWaited[i] / (float) numberOfPeopleThatWaited[i]).append("\n");
            }
            answer.append("\n");
        }
        return answer.toString();
    }
}
