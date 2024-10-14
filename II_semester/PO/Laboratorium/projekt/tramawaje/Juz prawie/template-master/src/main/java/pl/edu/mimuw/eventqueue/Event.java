package pl.edu.mimuw.eventqueue;

import pl.edu.mimuw.lines.Stop;

import java.time.LocalTime;

public abstract class Event implements Comparable<Event> {
    private final LocalTime timeOfEvent;
    private final Stop stop;
    private final EventType eventType;

    public Event(LocalTime timeOfEvent, Stop stop, EventType eventType) {
        this.timeOfEvent = timeOfEvent;
        this.stop = stop;
        this.eventType = eventType;
    }
    public LocalTime getTimeOfEvent() {
        return timeOfEvent;
    }

    public Stop getStop() {
        return stop;
    }

    public EventType getEventType() {
        return eventType;
    }

    @Override
    public int compareTo(Event o) {
        return timeOfEvent.compareTo(o.getTimeOfEvent());
    }

    public String toString() {
        return timeOfEvent + ", Stop: " + stop.getName() + " ";
    }
}
