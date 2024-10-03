package pl.edu.mimuw;


public class LinkedListStack extends AbstractStack {
    private final LinkedList array;

    public LinkedListStack() {
        array = new LinkedList();
    }
    @Override
    public void push(int value) {
        array.push(value);
    }
    @Override
    public void pop() {
        array.pop();
    }
    @Override
    public int peek() {
        return array.peek();
    }
    @Override
    public int size() {
        return array.getSize();
    }
    @Override
    public boolean isEmpty() {
        return array.isEmpty();
    }
}
