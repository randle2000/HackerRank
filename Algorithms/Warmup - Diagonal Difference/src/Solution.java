import java.util.*;

public class Solution {

    static int diagonalDifference(int[][] a) {
    	int n = a.length;

    	int sum1 = 0;
        for(int i = 0; i < n; i++)
        	sum1 += a[i][i];
        
    	int sum2 = 0;
        for(int i = 0; i < n; i++)
        	sum2 += a[i][n-1-i];
        
        return Math.abs(sum2 - sum1);
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[][] a = new int[n][n];
        for(int a_i = 0; a_i < n; a_i++){
            for(int a_j = 0; a_j < n; a_j++){
                a[a_i][a_j] = in.nextInt();
            }
        }
        int result = diagonalDifference(a);
        System.out.println(result);
        in.close();
    }
}