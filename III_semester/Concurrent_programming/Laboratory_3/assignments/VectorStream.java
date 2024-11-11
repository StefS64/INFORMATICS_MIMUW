package lab03.assignments;

import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.function.IntBinaryOperator;

import lab03.examples.ArrayRearrangement;

public class VectorStream {
    private static final int STREAM_LENGTH = 10;
    private static final int VECTOR_LENGTH = 100;

    /**
     * Function that defines how vectors are computed: the i-th element depends on
     * the previous sum and the index i.
     * The sum of elements in the previous vector is initially given as zero.
     */
    private final static IntBinaryOperator vectorDefinition = (previousSum, i) -> {
        int a = 2 * i + 1;
        return (previousSum / VECTOR_LENGTH + 1) * (a % 4 - 2) * a;
    };

    private static void computeVectorStreamSequentially() {
        int[] vector = new int[VECTOR_LENGTH];
        int sum = 0;
        for (int vectorNo = 0; vectorNo < STREAM_LENGTH; ++vectorNo) {
            for (int i = 0; i < VECTOR_LENGTH; ++i) {
                vector[i] = vectorDefinition.applyAsInt(sum, i);
            }
            sum = 0;
            for (int x : vector) {
                sum += x;
            }
            System.out.println(vectorNo + " -> " + sum);
        }
    }
    
    private static  int[] parallelSum = new int[VECTOR_LENGTH];
    private static int[] parallelVector = new int[VECTOR_LENGTH];
    private static int counter = 0;

    private static class vectorElem implements Runnable {
        private final int id;

        public vectorElem(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            try {
               
                
                for(int i = 0; i < STREAM_LENGTH; i++){
                    parallelVector[id] = vectorDefinition.applyAsInt(parallelSum[id], id);
                    barrier.await();
                }
            } catch (InterruptedException | BrokenBarrierException e) {
                System.err.println(Thread.currentThread().getName() + " interrupted.");
            }
        }
    }



    private static final CyclicBarrier barrier = new CyclicBarrier(VECTOR_LENGTH,
        VectorStream::printData);

    private static void printData() {
        // This method is always run by a single thread, with others waiting,
        // so it's safe to use non-atomic operations here.
        int vectorSum = 0;
        for (int i = 0; i < VECTOR_LENGTH; i++ ){
            vectorSum += parallelVector[i];
        }
        for (int i = 0; i < VECTOR_LENGTH; i ++){
            parallelSum[i] = vectorSum;
        }
       System.out.println(counter + " -> " + parallelSum[0]);
        counter++;
    }
    private static void computeVectorStreamInParallel() throws InterruptedException {
        Thread[] threads = new Thread[VECTOR_LENGTH];
        for (int i = 0; i < VECTOR_LENGTH; i++){
            parallelSum[i] = 0;
            parallelVector[i] = 0;
        }        
        for (int i = 0; i < VECTOR_LENGTH; ++i) {
            threads[i] = new Thread(new vectorElem(i), "vectorElem" + i);
        }
        for (int i = 0; i < VECTOR_LENGTH; ++i) {
            threads[i].start();
        }
        try {
            for (int i = 0; i < VECTOR_LENGTH; ++i) {
                threads[i].join();
            }
        } catch (InterruptedException e) {
            System.err.println("Main interrupted");
        }
        
    }

    public static void main(String[] args) {
        try {
            System.out.println("-- Sequentially --");
            computeVectorStreamSequentially();
            System.out.println("-- Parallel --");
            computeVectorStreamInParallel();
            System.out.println("-- End --");
        } catch (InterruptedException e) {
            System.err.println("Main interrupted.");
        }
    }
}
