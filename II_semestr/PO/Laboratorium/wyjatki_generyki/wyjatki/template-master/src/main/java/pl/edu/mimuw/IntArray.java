package pl.edu.mimuw;

public class IntArray {
    private final int size;
    private final int[] table;
    public IntArray(int size, int[] table) {
        this.size = size;
        this.table = table;
    }
    public IntArray addElement(int newElement) {
        int[] newTable = new  int[size+1];
        for(int i = 0 ; i < size; i++){
            newTable[i] = table[i];
        }
        //System.arraycopy(table, 0, newTable, 0, size);
        newTable[size] = newElement;
        return new IntArray(size+1, newTable);
    }
    public IntArray deleteElement() {
        int[] newTable = new  int[size-1];
        System.arraycopy(table, 0, newTable, 0, size-1);
        return new IntArray(size-1, newTable);
    }
    public IntArray changeElement(int indeks, int newValue) {
        int[] newTable = new  int[size];
        System.arraycopy(table, 0, newTable, 0, size);
        newTable[indeks] = newValue;
        return new IntArray(size, newTable);
    }

    public int readElement(int indeks) {
        return table[indeks];
    }

    public int getSize() {
        return size;
    }

    public void printArray() {
        //System.out.println(table.toString());
        for (int i = 0 ; i < size; i++ ){
            System.out.print(table[i] + " ");
        }
        System.out.println();
    }
}
