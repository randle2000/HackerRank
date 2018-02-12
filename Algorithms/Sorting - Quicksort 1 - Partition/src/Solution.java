import java.util.*;

public class Solution {
	
    static int[] quickSort(int[] arr) {
        int p = arr[0];
        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();
        for (int i = 1; i < arr.length; i++) {
        	if (arr[i] < p)
        		left.add(arr[i]);
        	else
        		right.add(arr[i]);
        }
        
        int[] res = new int[arr.length];
        int[] leftArr = left.stream().mapToInt(Integer::intValue).toArray();
        int[] rightArr = right.stream().mapToInt(Integer::intValue).toArray();
        System.arraycopy(leftArr, 0, res, 0, leftArr.length);
        res[leftArr.length] = p;
        System.arraycopy(rightArr, 0, res, leftArr.length + 1, rightArr.length);
        return res;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[] arr = new int[n];
        for(int arr_i = 0; arr_i < n; arr_i++){
            arr[arr_i] = in.nextInt();
        }
        int[] result = quickSort(arr);
        for (int i = 0; i < result.length; i++) {
            System.out.print(result[i] + (i != result.length - 1 ? " " : ""));
        }
        System.out.println("");


        in.close();
    }
}