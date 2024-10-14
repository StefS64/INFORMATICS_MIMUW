package pl.edu.mimuw;



public class LinkedList {
    private Node top;
    private int size;
    public LinkedList() {
        this.top = null;
    }

    public void push(int data) {
        top = new Node(data, top);
        size++;
    }

    public void pop() {
        if (top != null) {
            top = top.next();
            size--;
        }
    }

    public int peek() {
        if (top != null) {
            return top.data();
        }
        return -1;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public int getSize() {
        return size;
    }

}

