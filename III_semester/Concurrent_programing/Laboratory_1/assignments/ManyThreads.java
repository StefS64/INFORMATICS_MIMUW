package lab01.assignments;

import java.util.concurrent.ThreadLocalRandom;

public class ManyThreads {
    private static final int THREAD_COUNT = 5; // If it works for 5, you can increase it to 100.

    private static class MyRunnable implements Runnable {
        private final int nSubthreads;

        public MyRunnable(int nSubthreads) {
            this.nSubthreads = nSubthreads;
        }

        @Override
        public void run() {
            if (nSubthreads > 0){
                Runnable r = new MyRunnable(nSubthreads - 1);
                Thread thread = new Thread(r, "Father to "+ nSubthreads);
                thread.start();
            }

            System.out.println(Thread.currentThread().getName());
        }
    }

    public static void main(String[] args) {
        Runnable r = new MyRunnable(THREAD_COUNT);
        Thread thread = new Thread(r,"start thread");
        thread.start();
    }
}
