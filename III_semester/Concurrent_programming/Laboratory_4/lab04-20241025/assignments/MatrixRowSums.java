package lab04.assignments;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Map;
import java.util.HashMap;       
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntBinaryOperator;

public class MatrixRowSums {
    private static final int N_ROWS = 1000000;
    private static final int N_COLUMNS = 100;

    private static IntBinaryOperator matrixDefinition = (row, col) -> {
        int a = 2 * col + 1;
        return (row + 1) * (a % 4 - 2) * a;
    };

    private static void printRowSumsSequentially() {
        for (int r = 0; r < N_ROWS; ++r) {
            int sum = 0;
            for (int c = 0; c < N_COLUMNS; ++c) {
                sum += matrixDefinition.applyAsInt(r, c);
            }
            System.out.println(r + " ->- " + sum);
        }
    }

    private static void printRowSumsInParallel() throws InterruptedException {
        LinkedBlockingQueue<int[]> calculated = new LinkedBlockingQueue<>();
        Map<Integer,int[]> calculated_rows = new HashMap<>();
        List<Thread> threads = new ArrayList<>();

        for (int c = 0; c < N_COLUMNS; ++c) {
            final int myColumn = c;
            threads.add(new Thread(() -> {
                for (int i = 0; i < N_ROWS; i++){
                    calculated.add(new int[]{i, matrixDefinition.applyAsInt(i, myColumn)});
                }
                
            }));
        }
        for (Thread t : threads) {
            t.start();
        }
        int row_num = 0;
        while (row_num < N_ROWS){
            int[] value = calculated.take();
            int row = value[0];
            int val = value[1];
            calculated_rows.compute(
                row,
                (k, v) -> (v == null ? new int[] {1, val} : new int[] {v[0] + 1, v[1] + val})
            );
            
            if (calculated_rows.get(row_num)[0] == N_COLUMNS) {
                System.out.println(row_num + " -> " + calculated_rows.get(row_num)[1]);
                calculated_rows.remove(row_num);
                row_num++;
            }
        }
        try {
            for (Thread t : threads) {
                t.join();
            }
        } catch (InterruptedException e) {
            for (Thread t : threads) {
                t.interrupt();
            }
            throw e;
        }
    }

    public static void main(String[] args) {
        try {
            System.out.println("-- Sequentially --");
            printRowSumsSequentially();
            System.out.println("-- In parallel --");
            printRowSumsInParallel();
            System.out.println("-- End --");
        } catch (InterruptedException e) {
            System.err.println("Main interrupted.");
        }
    }
}
