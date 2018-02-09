import java.util.*;

public class Solution {

    static void miniMaxSum(int[] arr) {
    	
    	long maxSum = Arrays.stream(arr)
    		.sorted()
    		.skip(1)
    		.asLongStream()
    		.sum();
    	
    	long minSum = Arrays.stream(arr)
        	.sorted()
        	.limit(arr.length - 1)
        	.asLongStream()
        	.sum();
    	
    	System.out.println(minSum + " " + maxSum);

    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int[] arr = new int[5];
        for(int arr_i = 0; arr_i < 5; arr_i++){
            arr[arr_i] = in.nextInt();
        }
        miniMaxSum(arr);
        in.close();
    }
}