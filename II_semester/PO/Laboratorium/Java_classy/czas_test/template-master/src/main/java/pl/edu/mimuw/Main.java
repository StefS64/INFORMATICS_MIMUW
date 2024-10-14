package pl.edu.mimuw;
//presentation mode menu- view - appearance - presentation mode
//import java.util.Arrays;
//import java.util.Scanner;

public class Main {
    private static final Long MOD = 1000000007L;
    public static Long[][] multiplyMatrices2x2(Long[][] matrixA, Long[][] matrixB) {
        Long[][] ans = new Long[2][2];

        ans[0][0] = (matrixA[0][0]*matrixB[0][0] % MOD + matrixA[0][1]*matrixB[1][0] % MOD) % MOD;
        ans[0][1] = (matrixA[0][0]*matrixB[0][1] % MOD + matrixA[0][1]*matrixB[1][1] % MOD) % MOD;
        ans[1][0] = (matrixA[1][0]*matrixB[0][0] % MOD + matrixA[1][1]*matrixB[1][0] % MOD) % MOD;
        ans[1][1] = (matrixA[1][0]*matrixB[0][1] % MOD + matrixA[1][1]*matrixB[1][1] % MOD) % MOD;
        return ans;
    }
    public static Long[][] matrixToTheNthPower(Long[][] matrix, int n) {
        Long[][] ans = new Long[2][2];
        ans[0][0] = 1L;
        ans[1][0] = 0L;
        ans[0][1] = 0L;
        ans[1][1] = 1L;

        while(n > 0) {
            /*if (n%2 == 1) {
                ans = multiplyMatrices2x2(ans, matrix);
            }
            matrix = multiplyMatrices2x2(matrix, matrix);
            n/=2;*/
            ans = multiplyMatrices2x2(ans, matrix);
            n--;
        }
        return ans;
    }
    public static void main(String[] args) {
        Long[][] base = new Long[2][2];
        base[0][0] = 0L;
        base[1][0] = 1L;
        base[0][1] = 1L;
        base[1][1] = 1L;
        int n = 10000000;
        base = matrixToTheNthPower(base, n-1);
        for( int i = 0; i < 2; i ++ ) {
            for (int j = 0; j < 2; j++) {
                System.out.print(STR."\{Long.toString(base[i][j])} ");
            }
            System.out.println();
        }
    }
}
