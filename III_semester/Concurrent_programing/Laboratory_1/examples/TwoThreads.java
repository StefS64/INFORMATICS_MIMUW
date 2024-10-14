package lab01.examples;

public class TwoThreads {
    private static void printName() {
        System.out.println(Thread.currentThread());
    }

    private static class Helper implements Runnable {
        @Override
        public void run() {
            printName();
        }
    }

    public static void main(String[] args) {
        Thread.currentThread().setName("Main");
        Runnable r = new Helper();
        Thread thread = new Thread(r, "Helper");
        thread.start();
        printName();
    }
}
