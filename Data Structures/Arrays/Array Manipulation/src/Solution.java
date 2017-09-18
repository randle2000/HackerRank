import java.util.*;

public class Solution {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();
        
        long[] arr = new long[n + 1];
        for(int a0 = 0; a0 < m; a0++){
            int a = in.nextInt();
            int b = in.nextInt();
            int k = in.nextInt();
            
            arr[a-1] += k;
            arr[b] -= k;
        }
        in.close();
        
        long sumValue = 0, maxValue = 0;
        for (int i = 0; i < n; i++) {
        	sumValue += arr[i];
        	if (sumValue > maxValue)
        		maxValue = sumValue;
        }
        
        //Arrays.stream(arr).forEach(System.out::println);
        System.out.println(maxValue);
    }
}