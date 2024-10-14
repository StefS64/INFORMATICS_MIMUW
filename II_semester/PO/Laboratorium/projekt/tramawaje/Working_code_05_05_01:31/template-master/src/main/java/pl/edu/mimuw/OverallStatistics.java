package pl.edu.mimuw;

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
        return "OVERALL STATISTICS\n-------------------------\n" + super.toString();
    }
}
