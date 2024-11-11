package lab01.examples;

public class TwoWriters {
    private static class Writer implements Runnable {
        private static final int CHARS_COUNT = 5000;

        private final char first;
        private final char last;

        public Writer(char first, char last) {
            this.first = first;
            this.last = last;
        }

        @Override
        public void run() {
            char c = first;
            for (int i = 0; i < CHARS_COUNT; ++i) {
                System.out.print(c);
                ++c;
                if (c > last) {
                    c = first;
                }
            }
        }
    }

    public static void main(String args[]) {
        Thread litery = new Thread(new Writer('a', 'z'));
        Thread cyfry = new Thread(new Writer('0', '9'));
        System.out.println("Started.");
        litery.start();
        cyfry.start();
        while (litery.isAlive()) {
            // Busy wait.
        }
        while (cyfry.isAlive()) {
            // Busy wait.
        }
        System.out.println();
        System.out.println("Finished.");
    }
}
