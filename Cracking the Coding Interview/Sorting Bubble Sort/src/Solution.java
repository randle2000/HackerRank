import java.util.*;

public class Solution {
	
	private static void swap(int[] a, int i, int j) {
		int tmp = a[i];
		a[i] = a[j];
		a[j] = tmp;
	}
	
	/**
	 * @param arr
	 * @return total number of swaps it took to sort the array
	 */
	private static int bubbleSort(int[] a) {
	    int totalNumberOfSwaps = 0;
	    int n = a.length;
	    int lastUnsorted = n - 1;
		
		for (int i = 0; i < n; i++) {
		    // Track number of elements swapped during a single array traversal
		    int numberOfSwaps = 0;
		    
		    for (int j = 0; j < lastUnsorted; j++) {
		        // Swap adjacent elements if they are in decreasing order
		        if (a[j] > a[j + 1]) {
		            swap(a, j, j + 1);
		            numberOfSwaps++;
		        }
		    }
		    lastUnsorted--;
		    
		    totalNumberOfSwaps += numberOfSwaps;
		    
		    // If no elements were swapped during a traversal, array is sorted
		    if (numberOfSwaps == 0) {
		        break;
		    }
		}
		
		return totalNumberOfSwaps;
	}

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[] values = new int[n];
        for(int i = 0; i < n; i++){
            values[i] = in.nextInt();
        }
        in.close();
        
        int numSwaps = bubbleSort(values); 
        System.out.printf("Array is sorted in %d swaps.\n", numSwaps);
        System.out.printf("First Element: %d\n", values[0]);
        System.out.printf("Last Element: %d\n", values[values.length - 1]);
    }
}