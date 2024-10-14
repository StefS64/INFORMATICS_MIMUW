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
        int interval  = (lineDuration + 1) / trams.length;//zaokrąglamy czas do góry w przypadku niepewności dlatego "+1"
        System.out.println("Number of trams: " + trams.length);
        System.out.println("Interval: " + interval);
        LocalTime time;
        for (int i = 0; i < trams.length; i++) {
            time = LocalTime.of(6,0);

            Tram tram = trams[i];

            if(i >= trams.length/2) {
                time = time.plusMinutes((long) interval * (i - (trams.length/2)));
            } else {
                time = time.plusMinutes((long) interval * i);
            }

            while(time.isBefore(LocalTime.of(23,0))){
                time = addTramLoopToEventQueue(eventQueue, tram, time, tram.getStartStopNum());
                time = time.plusMinutes(timeBetweenStops[stops.length - 1]);
            }
        }
    }

    private LocalTime addTramLoopToEventQueue(EventQueue eventQueue, Tram tram, LocalTime timeOfDeparture, int startStopNum) {
        if(startStopNum == 0){
           timeOfDeparture = addRightToLeftRoute(eventQueue, tram, timeOfDeparture);
           timeOfDeparture = timeOfDeparture.plusMinutes(timeBetweenStops[stops.length - 1]);
           timeOfDeparture = addLeftToRightRoute(eventQueue, tram, timeOfDeparture);
        }else if(startStopNum == stops.length-1){
            timeOfDeparture = addLeftToRightRoute(eventQueue, tram, timeOfDeparture);
            timeOfDeparture = timeOfDeparture.plusMinutes(timeBetweenStops[0]);
            timeOfDeparture = addRightToLeftRoute(eventQueue, tram, timeOfDeparture);
        }
        return timeOfDeparture;
    }

    private LocalTime addLeftToRightRoute(EventQueue eventQueue, Tram tram, LocalTime timeOfDeparture){
        eventQueue.add( new VehicleEvent(timeOfDeparture, stops[0], tram, VehicleEventType.DEPARTURE));
        for(int i = 1; i < stops.length - 1; i++){
            timeOfDeparture = timeOfDeparture.plusMinutes(timeBetweenStops[i - 1]);
            eventQueue.add(new VehicleEvent(timeOfDeparture, stops[i], tram, VehicleEventType.ARRIVAL));
            eventQueue.add(new VehicleEvent(timeOfDeparture, stops[i], tram, VehicleEventType.DEPARTURE));
        }
        timeOfDeparture = timeOfDeparture.plusMinutes(timeBetweenStops[stops.length - 2]);
        eventQueue.add(new VehicleEvent(timeOfDeparture, stops[stops.length - 1], tram, VehicleEventType.ARRIVAL));
        return timeOfDeparture;
    }

    private LocalTime addRightToLeftRoute(EventQueue eventQueue, Tram tram, LocalTime timeOfDeparture){
        eventQueue.add( new VehicleEvent(timeOfDeparture, stops[stops.length - 1], tram, VehicleEventType.DEPARTURE));
        for(int i = stops.length - 2; i > 0; i--){
            timeOfDeparture = timeOfDeparture.plusMinutes(timeBetweenStops[i]);
            eventQueue.add(new VehicleEvent(timeOfDeparture, stops[i], tram, VehicleEventType.ARRIVAL));
            eventQueue.add(new VehicleEvent(timeOfDeparture, stops[i], tram, VehicleEventType.DEPARTURE));
        }
        timeOfDeparture = timeOfDeparture.plusMinutes(timeBetweenStops[0]);
        eventQueue.add(new VehicleEvent(timeOfDeparture, stops[0], tram, VehicleEventType.ARRIVAL));
        return timeOfDeparture;
    }
    @Override
    public String toString(){
        return super.toString() + "\nnumber of trams: " + trams.length;
    }
}
