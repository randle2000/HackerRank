import java.util.*;

public class Solution {
	
    static int[] countingSort(int[] arr) {
		int maxValue = Arrays.stream(arr).max().getAsInt();
        int[] countArr = new int[maxValue + 1];
        for (int e : arr)
        	countArr[e]++;
        return countArr;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[] arr = new int[n];
        for(int arr_i = 0; arr_i < n; arr_i++){
            arr[arr_i] = in.nextInt();
        }
        int[] result = countingSort(arr);
        for (int i = 0; i < result.length; i++) {
            System.out.print(result[i] + (i != result.length - 1 ? " " : ""));
        }
        System.out.println("");


        in.close();
    }
}