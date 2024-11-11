package lab01.assignments;

import java.util.ArrayList; // Concrete implementation of the List interface.
import java.util.List;

@SuppressWarnings("unused")
public class Primes {
    private static final int NO_DIVISOR = 0;
    private static volatile int foundDivisor;

    /**
     * Searches for a divisor of `n` of the form `start + i * step`, up to at most
     * `end` (inclusive). If any divisor is found, sets `foundDivisor` and stops.
     */
    private static class FindDivisor implements Runnable {
        private final int n;
        private final int start;
        private final int step;
        private final int end;

        public FindDivisor(int n, int start, int end, int step) {
            this.n = n;
            this.start = start;
            this.end = end;
            this.step = step;
        }

        @Override
        public void run() {
            for (int divisor = start; divisor <= end; divisor += step ){
                if(n % divisor == 0){
                    foundDivisor = 1;
                    break;
                }
            }
        }
    }

    public static boolean isPrime(int n) {
        if (n < 2) {
            return false;
        }

        final int STEP = 30;
        // Primes less than or equal to STEP.
        int[] smallPrimes = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29 };
        // Remainders mod 30 that don't have common factors with 30.
        int[] starts = { 31, 37, 41, 43, 47, 49, 53, 59 };

        for(int prime : smallPrimes){
            if(prime == n){
                return true;
            }
            if(n % prime == 0){
                return  false;          
            }
        }

        List<Thread> threads = new ArrayList<>();
        for (int start : starts) {
            threads.add(new Thread(new FindDivisor(n, start, (int)Math.sqrt(n), STEP)));
        }

        for (Thread thread : threads){
            thread.start();
        }

        for (Thread thread : threads){
            while(thread.isAlive()){
                //Busy wait
            }
        }
        if(foundDivisor == 0){
            return true;
        }

        foundDivisor = 0;
        
        return false;
        
    }

    public static void main(String[] args) {
        // A simple test: count how many numbers are prime, up to i=MAX_VALUE_TO_CHECK.
        final int MAX_VALUE_TO_CHECK =  10000;
        final int EXPECTED_N_PRIMES =  1229;

        int primesCount = 0;
        for (int i = 0; i <= MAX_VALUE_TO_CHECK; ++i) {
            if (isPrime(i)) {
                System.out.println(i);
                ++primesCount;
            }
        }

        if (primesCount == EXPECTED_N_PRIMES) {
            System.out.println("OK! Found exactly " + primesCount + " primes.");
        } else {
            System.out.println("Expected " + EXPECTED_N_PRIMES + " primes, but got " + primesCount + ".");
        }
    }
}
