import java.util.*;

public class Solution {

    static void plusMinus(int[] arr) {
    	int p = 0, n = 0, z = 0;
    	int nn = arr.length;
    	
    	for (int e : arr) {
    		if (e > 0)
    			p++;
    		else if (e < 0)
    			n++;
    		else
    			z++;
    	}
    	System.out.format("%.6f%n", (double)p/nn); 
    	System.out.format("%.6f%n", (double)n/nn); 
    	System.out.format("%.6f%n", (double)z/nn); 
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[] arr = new int[n];
        for(int arr_i = 0; arr_i < n; arr_i++){
            arr[arr_i] = in.nextInt();
        }
        plusMinus(arr);
        in.close();
    }
}