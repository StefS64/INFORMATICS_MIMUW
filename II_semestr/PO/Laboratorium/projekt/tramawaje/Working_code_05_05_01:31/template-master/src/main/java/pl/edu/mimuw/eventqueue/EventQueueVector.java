package pl.edu.mimuw.eventqueue;

import java.util.Arrays;

public class EventQueueVector implements EventQueue {
    private Event[] array;
    private int size;
    private int capacity;
    private int topElemntIndex;

    public EventQueueVector() {
        capacity = 1;
        array = new Event[capacity];
        size = 0;
        topElemntIndex = 0;
    }

    public void add(Event element) {
        if (size == capacity) {
            capacity *= 2;
            array = Arrays.copyOf(array, capacity);
        }
        array[size] = element;
        size++;
        Arrays.sort(array, 0 , size);
    }

    public Event getNextEvent() {
        if(topElemntIndex == size) {
            return null;
        }

        topElemntIndex++;
        return array[topElemntIndex-1];
    }

    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        if(topElemntIndex == size) {
            return true;
        } else {
            return false;
        }
    }

    public void printEvents(){
        for(int i = 0; i < size; i++){
            System.out.println(array[i]);
        }
        System.out.println();
    }
}
