package pl.edu.mimuw;

public class DynamicArray {
    private int[] array = new int[1];
    private int arraySize = 0;
    private int allocSize = 1;

    public void pop() {
        arraySize--;
        if (arraySize == 0 || arraySize == -1) {
            allocSize = 1;
            arraySize = 0;
            array = new int[1];
        } else if (arraySize < allocSize / 2) {
            int[] newArray = new int[allocSize / 2];
            System.arraycopy(array, 0, newArray, 0, arraySize);
            allocSize /= 2;
            array = newArray;
        }
    }

    public int peek() {
        return array[arraySize - 1];
    }

    public void push(int value) {
        arraySize++;
        if (allocSize == arraySize) {
            allocSize = allocSize * 2;
            int[] newArray = new int[allocSize];
            System.arraycopy(array, 0, newArray, 0, arraySize);
            array = newArray;
        }
        array[arraySize - 1] = value;
    }

    public int get(int index) {
        return array[index];
    }

    public int size() {
        return arraySize;
    }

    public boolean isEmpty() {
        return arraySize == 0;
    }
}
