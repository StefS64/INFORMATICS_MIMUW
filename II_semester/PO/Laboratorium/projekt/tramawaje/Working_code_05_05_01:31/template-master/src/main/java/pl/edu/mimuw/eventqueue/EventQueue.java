package pl.edu.mimuw.eventqueue;

public interface EventQueue {
    public void add(Event event);
    public Event getNextEvent();
    public int size();
    public void printEvents();
    public boolean isEmpty();
}
