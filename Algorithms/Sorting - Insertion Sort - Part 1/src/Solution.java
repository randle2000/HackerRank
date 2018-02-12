import java.util.*;
import java.util.stream.Collectors;

public class Solution {
	
	private static void printArray(int[] arr) {
    	String out = Arrays.stream(arr).mapToObj(Integer::toString).collect(Collectors.joining(" "));
    	System.out.println(out);
	}

    static void insertionSort1(int n, int[] arr) {
        int i = n - 1;
        int e = arr[n - 1];
        while (i > 0 && e < arr[i - 1]) {
        	arr[i] = arr[i - 1];
        	i--;
        	printArray(arr);
        }
        arr[i] = e;
        printArray(arr);
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[] arr = new int[n];
        for(int arr_i = 0; arr_i < n; arr_i++){
            arr[arr_i] = in.nextInt();
        }
        insertionSort1(n, arr);
        in.close();
    }
}