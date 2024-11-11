package lab02.examples;

import java.time.Duration;

public class Interrupts {
    private static long startTime = System.currentTimeMillis();

    private static void printTime() {
        System.out.println("Time since start: " + (System.currentTimeMillis() - startTime) + " ms");
    }

    private static void doUninterruptibleWork(Duration workTime) {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() < start + workTime.toMillis()) {
            Math.random();
        }
    }

    private static void doBusyWork(Duration workTime) throws InterruptedException {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() < start + workTime.toMillis()) {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            doUninterruptibleWork(Duration.ofMillis(200));
        }
    }

    private static class Helper implements Runnable {
        private final Duration sleepTime;
        private final Duration workTime;

        public Helper(Duration sleepTime, Duration workTime) {
            this.sleepTime = sleepTime;
            this.workTime = workTime;
        }

        @Override
        public void run() {
            String phase = "sleep";
            try {
                Thread.sleep(sleepTime.toMillis());

                phase = "computations";
                doBusyWork(workTime);
                // doUninterruptibleWork(workTime);

                phase = "finished";
                System.err.println(Thread.currentThread().getName() + " finished uninterrupted.");

            } catch (InterruptedException e) {
                Thread current = Thread.currentThread();
                System.err.print(current.getName() + " was interrupted during " + phase);
                System.err.println(", isInterrupted()=" + current.isInterrupted());
            }
        }
    }

    public static void main(String args[]) {
        try {

            Thread first = new Thread(new Helper(Duration.ofSeconds(1), Duration.ofSeconds(1)), "First");
            Thread sleeper = new Thread(new Helper(Duration.ofSeconds(10), Duration.ZERO), "Sleeper");
            Thread worker = new Thread(new Helper(Duration.ZERO, Duration.ofSeconds(10)), "Worker");
            first.start();
            sleeper.start();
            worker.start();
            printTime();

            first.join();
            printTime();

            sleeper.interrupt();
            sleeper.join();
            printTime();

            worker.interrupt();
            worker.join();
            printTime();

        } catch (InterruptedException e) {
            System.out.println("Main interrupted.");
        }
    }
}
