package pl.edu.mimuw.eventqueue;

import pl.edu.mimuw.lines.Stop;

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
        return timeOfEvent.compareTo(o.getTimeOfEvent());
    }

    public String toString() {
        return timeOfEvent + ", Stop: " + stop.getName() + " ";
    }
}
