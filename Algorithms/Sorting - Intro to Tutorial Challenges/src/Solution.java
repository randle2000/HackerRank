import java.util.*;

public class Solution {
	
    public static void main(String[] args) {
    	Scanner in = new Scanner(System.in);
    	int v = in.nextInt();
        int n = in.nextInt();
        int[] arr = new int[n];
        for(int i=0; i < n; i++)
            arr[i] = in.nextInt();
        in.close();
        
        // our array is already sorted
        int index = Arrays.binarySearch(arr, v);
   		System.out.println(index);
    }
}