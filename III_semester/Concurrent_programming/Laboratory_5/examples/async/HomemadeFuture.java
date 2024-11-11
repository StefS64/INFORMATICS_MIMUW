package lab05.examples.async;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class HomemadeFuture {

    public static class Future<T> {
        // The worker will notify us using this single-element queue, once the
        // computation is done.
        private final LinkedBlockingQueue<T> channel;
        private T result = null;

        public Future(LinkedBlockingQueue<T> channel) {
            this.channel = channel;
        }

        public T get() throws InterruptedException {
            if (result == null) {
                result = channel.take();
            }
            return result;
        }
    }

    // Stores the task to be done and the channel used to notify when it's done.
    private static class PromisedTask<T> {
        private final Callable<T> computation;
        private final LinkedBlockingQueue<T> channel;

        public PromisedTask(Callable<T> computation, LinkedBlockingQueue<T> channel) {
            this.computation = computation;
            this.channel = channel;
        }

        public Callable<T> getComputation() {
            return computation;
        }

        public void returnValue(T value) throws InterruptedException {
            channel.put(value);
        }
    }

    public static class ThreadPool {
        private final List<Thread> threads; // Worker threads.
        private final BlockingQueue<PromisedTask<Object>> taskQueue;

        public ThreadPool(int size) {
            this.taskQueue = new LinkedBlockingQueue<>();
            this.threads = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                threads.add(new Thread(() -> {
                    try {
                        while (!Thread.interrupted()) {
                            PromisedTask<Object> task = taskQueue.take();
                            Object result = task.getComputation().call();
                            task.returnValue(result);
                        }
                    } catch (InterruptedException e) {
                        // We simply finish.
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }));
            }
            for (Thread thread : threads) {
                thread.start();
            }
        }

        public void shutdown() {
            for (Thread thread : threads) {
                thread.interrupt();
            }
        }

        // Submits a computation to be computed and returns a future value.
        @SuppressWarnings("unchecked")
        public <T> Future<T> submit(Callable<T> computation) {
            LinkedBlockingQueue<T> channel = new LinkedBlockingQueue<>();
            PromisedTask<T> promise = new PromisedTask<>(computation, channel);
            Future<T> future = new Future<>(channel);
            try {
                taskQueue.put((PromisedTask<Object>) promise);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return future;
        }
    }

    public static void main(String[] args) {
        ThreadPool threadPool = new ThreadPool(9);
        Duration totalTasksDuration = Duration.ZERO;
        List<Future<Duration>> futures = new ArrayList<>();
        Instant start = Instant.now();
        for (int i = 0; i < 20; i++) {
            Future<Duration> future = threadPool.submit(() -> {
                Instant thisTaskStart = Instant.now();
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    System.err.println(Thread.currentThread().getName() + " interrupted");
                }
                return Duration.between(thisTaskStart, Instant.now());

            });
            futures.add(future);
        }

        for (Future<Duration> future : futures) {
            try {
                totalTasksDuration = totalTasksDuration.plus(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Total work time: " + totalTasksDuration.toMillis() / 1000.0 + " s");
        System.out.println(
                "But in the real world only " + Duration.between(start, Instant.now()).toMillis() / 1000.0
                        + " s have passed.");

        threadPool.shutdown();
    }
}
