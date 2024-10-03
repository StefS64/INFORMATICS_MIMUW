package pl.edu.mimuw;

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
            stops[i].clearCollectedData();
        }
        for(Passenger passenger : passengers){
            sumOfRides+=passenger.getNumberOfRides();
            passenger.clearCollectedData();
        }
        this.sumOfTimeWaited = sumOfTimeWaitedByPassengers;
    }

    public String toString(){
        String answer = "\nTotal Number of rides: " + sumOfRides+"\n\n";
        answer += "Stops:\n";
        for(int i = 0; i < sumOfTimeWaited.length; i++){
            answer += stops[i].getName()+"\n";
            answer += "Total time waited at stop: " + sumOfTimeWaited[i] + "\n";
            answer += "How many times someone waited at stop: " + numberOfPeopleThatWaited[i] + "\n";
            if(numberOfPeopleThatWaited[i] > 0){
                answer += "Average waiting time: " + (float)sumOfTimeWaited[i]/(float)numberOfPeopleThatWaited[i] + "\n";
            }
            answer += "\n";
        }
        return answer;
    }
}
