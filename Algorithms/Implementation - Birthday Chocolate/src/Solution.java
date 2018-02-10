import java.util.*;
import java.util.stream.IntStream;

public class Solution {

    static int solve(int n, int[] s, int d, int m){
    	if (m > n)
    		return 0;
        int sum = IntStream.of(Arrays.copyOf(s, m)).sum();
        int count = 0;
    	if (sum == d)
    		count++;
        for (int i = 0; i < (n - m); i++) {
        	sum -= s[i];	
        	sum += s[i + m];
        	if (sum == d)
        		count++;
        }
        return count;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[] s = new int[n];
        for(int s_i=0; s_i < n; s_i++){
            s[s_i] = in.nextInt();
        }
        int d = in.nextInt();
        int m = in.nextInt();
        in.close();
        int result = solve(n, s, d, m);
        System.out.println(result);
    }
}