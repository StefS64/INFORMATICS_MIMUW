package pl.edu.mimuw;

public class DynamicArrayStack extends AbstractStack {
    private final DynamicArray array;

    public DynamicArrayStack() {
        array = new DynamicArray();
    }
    @Override
    public void pop() {
        array.pop();
    }

    @Override
    public void push(int value) {
        array.push(value);
    }
    @Override
    public boolean isEmpty() {
        return array.isEmpty();
    }
    @Override
    public int peek() {
        return array.peek();
    }
    @Override
    public int size() {
        return array.size();
    }
}
