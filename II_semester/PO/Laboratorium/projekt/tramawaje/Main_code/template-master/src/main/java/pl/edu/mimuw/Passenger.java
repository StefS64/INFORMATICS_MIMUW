package pl.edu.mimuw;

import pl.edu.mimuw.lines.Line;
import pl.edu.mimuw.lines.Stop;
import pl.edu.mimuw.vehicles.Tram;
import pl.edu.mimuw.vehicles.Vehicle;

import java.time.Duration;
import java.time.LocalTime;

public class Passenger {
    private final int id;
    private Stop destinationStop;
    private final Stop closestStopToHome;
    private LocalTime timeSpentWaiting;
    private LocalTime startedWaitingAt;
    private int numberOfRides = 0;
    public Passenger(int id, Stop closestStopToHome) {
        this.id = id;
        this.destinationStop = closestStopToHome;
        this.closestStopToHome = closestStopToHome;
        this.timeSpentWaiting = LocalTime.of(0, 0);
    }


    public void chooseDestinationStop(Vehicle vehicle){
        if(vehicle instanceof Tram tram) {
            int help = tram.getStopNumOfLoop() < tram.getLineLength() - 1 ? 0 : tram.getLineLength();
            int numOfStop = Randomize.random(tram.getStopNumOfLoop() % tram.getLineLength(),  tram.getLineLength());
            numOfStop += help;
            Line line = tram.getLine();
            if(tram.getStartStopNum() == 0){
                destinationStop = line.getStops()[numOfStop % tram.getLineLength()];
            } else if(tram.getStartStopNum() == tram.getLineLength()-1) {
                destinationStop = line.getStops()[tram.getStartStopNum() - (numOfStop % tram.getLineLength())];
            }
        }
    }
    public Stop getClosestStopToHome(){
        return closestStopToHome;
    }
    public String toString(){
        return "passanger id: " + id;
    }
    public String happeningEventString(String eventString, LocalTime timeOfEvent){
        return "\t" + timeOfEvent  + " by: "+ this +" event : "+ eventString;
    }
    public Stop getDestinationStop() {
        return destinationStop;
    }
    public LocalTime chooseTime(){
        return LocalTime.of(Randomize.random(6, 12), 0);
    }
    public LocalTime getTimeSpentWaiting() {
        return timeSpentWaiting;
    }
    public void startWaitingAt(LocalTime time){
        this.startedWaitingAt = time;
    }
    public int stopWaitingAtAndGetDuration(LocalTime time){
        int duration = (int) Duration.between(startedWaitingAt, time).toMinutes();
        this.timeSpentWaiting = this.timeSpentWaiting.plusMinutes(duration);
        return duration;
    }
    public void incNumberOfRides(){
        numberOfRides++;
    }
    public int getNumberOfRides(){
        return numberOfRides;
    }
    public void clearCollectedData() {
        this.numberOfRides = 0;
        this.timeSpentWaiting = LocalTime.of(0, 0);
        this.startedWaitingAt = LocalTime.of(0, 0);
    }
}
