package pl.edu.mimuw;
//presentation mode menu- view - appearance - presentation mode
//import java.util.Arrays;

public class Main {
    static long sumOfDigits(long num){
        long sum = 0L;
        while(num > 0){
            sum += num % 10;
            num/=10;
        }
        return sum;
    }
    private static boolean isNiven(Long num){
        return (num % sumOfDigits(num) == 0);
    }
    private static Long nthNiven(Long n){
        long count = 0L;
        Long answer = 0L;
        while(count < n) {
            answer++;
            if (isNiven(answer)) {
                count++;
            }
        }
        return answer;
    }
    public static void main(String[] args) {
        Long n = 100000000L;
        Long niven = nthNiven(n);
        System.out.println(niven);

    }
}
//presentation mode menu- view - appearance - presentation mode
//import java.util.Arrays;




