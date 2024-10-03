package pl.edu.mimuw.simulationstatistics;

import pl.edu.mimuw.Passenger;
import pl.edu.mimuw.lines.Stop;

public class OverallStatistics extends SimulationDayStatistics {
    public OverallStatistics(Stop[] stops, Passenger[] passengers) {
        super(stops, passengers);
    }


    public void addDay(SimulationDayStatistics added){
        sumOfRides += added.sumOfRides;
        for(int i = 0; i < stops.length; i++){
            numberOfPeopleThatWaited[i] += added.numberOfPeopleThatWaited[i];
            sumOfTimeWaited[i] += added.sumOfTimeWaited[i];
        }
    }

    @Override
    public String toString() {
        int sumOfAllWaitedTime = 0;
        int numberOfPeopleThatWaitedSumOfAll = 0;
        for(int i = 0; i < stops.length; i++){
            numberOfPeopleThatWaitedSumOfAll += numberOfPeopleThatWaited[i];
            sumOfAllWaitedTime += sumOfTimeWaited[i];
        }
        return STR."""
OVERALL STATISTICS
-------------------------
How many times people waited: \{numberOfPeopleThatWaitedSumOfAll}
 Sum of waited time: \{sumOfAllWaitedTime}
 Average waiting time: \{sumOfAllWaitedTime / numberOfPeopleThatWaitedSumOfAll}
\{super.toString()}""";
    }
}
