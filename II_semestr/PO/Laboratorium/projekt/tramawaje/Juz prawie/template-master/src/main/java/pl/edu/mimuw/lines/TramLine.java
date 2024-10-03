package pl.edu.mimuw.lines;

import pl.edu.mimuw.eventqueue.Event;
import pl.edu.mimuw.eventqueue.EventQueue;
import pl.edu.mimuw.eventqueue.VehicleEvent;
import pl.edu.mimuw.eventqueue.VehicleEventType;
import pl.edu.mimuw.vehicles.Tram;

import java.time.LocalTime;

public class TramLine extends Line {
    private final Tram[] trams;

    public TramLine (int number, int lineDuration, Stop[] stops, Tram[] trams, int[] timeBetweenStops) {
        super(number, lineDuration, stops, timeBetweenStops);
        for (Tram tram : trams) {
            tram.setLine(this);
        }
        this.trams = trams;
    }

    public void addScheduleToEventQueue(EventQueue eventQueue) {
        int interval  = (lineDuration + 1) / trams.length;//zaokrąglamy czas do góry w przypadku niepewności
        for (int i = 0; i < trams.length; i++) {

            LocalTime time = LocalTime.of(6, 0);
            Tram tram = trams[i];

            int stopNum = 0;
            int startingStopNum = 0;
            boolean directionRight = true;

            if(i >= trams.length/2) {
                directionRight = false;
                stopNum = stops.length-1;
                startingStopNum = stops.length-1;
                time = time.plusMinutes((long) interval * (i - trams.length/2));
            } else {
                time = time.plusMinutes((long) interval * i);
            }
            while(time.isBefore(LocalTime.of(23,0)) || stopNum != startingStopNum){
                if(!(stopNum == 0 && directionRight) && !(stopNum == stops.length - 1 && !directionRight)){
                    eventQueue.add( new VehicleEvent(time, stops[stopNum], tram, VehicleEventType.ARRIVAL));
                }
                if(!(stopNum == 0 && !directionRight) && !(stopNum == stops.length - 1 && directionRight)){
                    eventQueue.add( new VehicleEvent(time, stops[stopNum], tram, VehicleEventType.DEPARTURE));
                }

                if(directionRight){
                    time = time.plusMinutes(timeBetweenStops[stopNum]);
                    stopNum++;
                    if(stopNum == stops.length - 1){
                        directionRight = false;
                        eventQueue.add( new VehicleEvent(time, stops[stopNum], tram, VehicleEventType.ARRIVAL));
                        time = time.plusMinutes(timeBetweenStops[stops.length-1]);
                    }
                } else {
                    stopNum--;
                    time = time.plusMinutes(timeBetweenStops[stopNum]);
                    if(stopNum == 0){
                        directionRight = true;
                        eventQueue.add( new VehicleEvent(time, stops[stopNum], tram, VehicleEventType.ARRIVAL));
                        time = time.plusMinutes(timeBetweenStops[stops.length-1]);
                    }
                }
            }
        }
    }
}
