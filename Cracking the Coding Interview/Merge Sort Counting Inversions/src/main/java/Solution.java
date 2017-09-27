import java.util.*;

public class Solution {

    static long countInversions(int[] arr) {
        // Complete this function
    	Mergesort sorter = new Mergesort();
    	sorter.sort(arr);
    	return sorter.getInvCount();
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int t = in.nextInt();
        for(int a0 = 0; a0 < t; a0++){
            int n = in.nextInt();
            int[] arr = new int[n];
            for(int arr_i = 0; arr_i < n; arr_i++){
                arr[arr_i] = in.nextInt();
            }
            long result = countInversions(arr);
            System.out.println(result);
        }
        in.close();
    }
}
