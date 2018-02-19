import java.util.*;
import java.util.stream.Collectors;

public class Solution {
	
    static int insertionSort1(int n, int[] arr) {
        int i = n - 1;
        int e = arr[n - 1];
        int shiftCount = 0;
        while (i > 0 && e < arr[i - 1]) {
        	arr[i] = arr[i - 1];
        	i--;
        	shiftCount++;
        }
        arr[i] = e;
        return shiftCount;
    }
    
    static void insertionSort2(int n, int[] arr) {
    	int i = 1;
    	int shiftCount = 0;
    	while (i < n) {
    		shiftCount += insertionSort1(i + 1, arr);
    		i++;
    	}
    	System.out.println(shiftCount);
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[] arr = new int[n];
        for(int arr_i = 0; arr_i < n; arr_i++){
            arr[arr_i] = in.nextInt();
        }
        insertionSort2(n, arr);
        in.close();
    }
}