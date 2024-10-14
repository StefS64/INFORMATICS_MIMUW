package pl.edu.mimuw;

import java.util.Arrays;

public class IntArray {
    private final int size;
    private final int[] table;

    public IntArray(int size, int[] table) {
        this.size = size;
        this.table = table;
    }
    public IntArray(int... args){
        this.size = args.length;
        this.table = args;
    }
    public IntArray pushed(int newElement) {
        int[] newTable = new  int[size+1];
        System.arraycopy(table, 0, newTable, 0, size);
        newTable[size] = newElement;
        return new IntArray(size+1, newTable);
    }
    public IntArray unshifted(int newElement) {
        int[] newTable = new  int[size+1];
        System.arraycopy(table, 0, newTable, 1, size);
        newTable[0] = newElement;
        return new IntArray(size+1, newTable);
    }
    public IntArray popped() {
        if(size == 0){
            return null;
        }
        int[] newTable = new  int[size-1];
        System.arraycopy(table, 0, newTable, 0, size-1);
        return new IntArray(size-1, newTable);
    }

    public IntArray shifted() {
        int[] newTable = new  int[size-1];
        System.arraycopy(table, 1, newTable, 0, size-1);
        return new IntArray(size-1, newTable);
    }
    public IntArray with(int indeks, int newValue) {
        int[] newTable = new  int[size];
        System.arraycopy(table, 0, newTable, 0, size);
        newTable[indeks] = newValue;
        return new IntArray(size, newTable);
    }
    public IntArray concat(IntArray A) {
        int newLength = size+A.size;
        int[] newTable = Arrays.copyOf(table, newLength);
        System.arraycopy(A.table, 0, newTable, size, A.size);
        return new IntArray(newLength, newTable);
    }
    public int at(int indeks) {
        return table[indeks];
    }

    public int getLength() {
        return size;
    }

    public IntArray reversed(){
        int[] newTable = new int[size];
        for(int i  = size-1; i >= 0; i--) {
            newTable[size-1-i] = table[i];
        }
        return new IntArray(size, newTable);
    }
    public boolean includes(int searchedInt){
        for(int i = 0; i < size; i++) {
            if(table[i] == searchedInt){
                return true;
            }
        }
        return false;
    }

    public int indexOf(int searchedInt){
        for(int i = 0; i < size; i++) {
            if(table[i] == searchedInt){
                return i;
            }
        }
        return -1;
    }

    public int lastIndexOf(int searchedInt){
        for(int i = size-1; i >= 0; i--) {
            if(table[i] == searchedInt){
                return i;
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        return Arrays.toString(table);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(table);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntArray intArray = (IntArray) o;
        return size == intArray.size && Arrays.equals(table, intArray.table);
    }

    public void printArray() {
        //System.out.println(table.toString());
        for (int i = 0 ; i < size; i++ ) {
            System.out.print(STR."\{table[i]} ");
        }
        System.out.println();
    }
}
