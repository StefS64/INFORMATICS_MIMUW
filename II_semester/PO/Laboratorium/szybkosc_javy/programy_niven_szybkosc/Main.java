public class Main {
    static long sumOfDigits(long num){
        long sum = 0L;
        while(num > 0){
            sum += num % 10;
            num/=10;
        }
        return sum;
    }
    private static boolean isNiven(long num){
        return (num % sumOfDigits(num) == 0);
    }
    private static long nthNiven(long n){
        long count = 0L;
        long answer = 0L;
        while(count < n) {
            answer++;
            if (isNiven(answer)) {
                count++;
            }
        }
        return answer;
    }
    public static void main(String[] args) {
        long n = 1000000000L;
        long niven = nthNiven(n);
        System.out.println(niven);

    }
}

