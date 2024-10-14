package pl.edu.mimuw.eventqueue;

import pl.edu.mimuw.lines.Stop;
import pl.edu.mimuw.vehicles.Vehicle;

import java.time.LocalTime;

public abstract class Event implements Comparable<Event> {
    private final LocalTime timeOfEvent;
    private final Stop stop;

    public Event(LocalTime timeOfEvent, Stop stop) {
        this.timeOfEvent = timeOfEvent;
        this.stop = stop;
    }

    public LocalTime getTimeOfEvent() {
        return timeOfEvent;
    }

    public Stop getStop() {
        return stop;
    }


    @Override
    public int compareTo(Event o) {
        if (timeOfEvent.compareTo(o.getTimeOfEvent()) == 0) {
            if (o instanceof PassengerEvent) {
                return 1;
            } else if (this instanceof PassengerEvent) {
                return -1;
            }
            return 0;
        } else {
            return timeOfEvent.compareTo(o.getTimeOfEvent());
        }
    }

    public String toString() {
        return timeOfEvent + ", Stop: " + stop.toString() + " ";
    }
}
