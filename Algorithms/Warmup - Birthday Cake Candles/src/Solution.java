import java.util.*;

public class Solution {

    static int birthdayCakeCandles(int n, int[] ar) {
        int max = Arrays.stream(ar).max().getAsInt();
        long candles = Arrays.stream(ar)
        	.filter(e -> e == max)
        	.count();
        return (int)candles;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[] ar = new int[n];
        for(int ar_i = 0; ar_i < n; ar_i++){
            ar[ar_i] = in.nextInt();
        }
        in.close();
        int result = birthdayCakeCandles(n, ar);
        System.out.println(result);
    }
}