import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Solution {
	
	// Complete the luckBalance function below.
    static int luckBalance(int n, int k, int[][] contests) {
    	int importantCount = Arrays.stream(contests)
    			.mapToInt(arr -> arr[1])
    			.sum();
    	int haveToWin = importantCount - k;
    	
    	Comparator<int[]> byImportance = (arr1, arr2) -> arr2[1] - arr1[1];
    	Comparator<int[]> byLuckAscending = (arr1, arr2) -> arr1[0] - arr2[0];
    	Comparator<int[]> cmp = byImportance.thenComparing(byLuckAscending);
    	Arrays.sort(contests, cmp);
    	
    	int luck = 0;
    	for (int[] arr : contests) {
    		if (haveToWin > 0 && arr[1] == 1) {
    			haveToWin--;
    			luck -= arr[0];
    		} else
    			luck += arr[0];
    	}
    	
    	return luck;
    }
    
    private static final Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) throws IOException {
    	//BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
    	BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));

        String[] nk = scanner.nextLine().split(" ");

        int n = Integer.parseInt(nk[0]);
        int k = Integer.parseInt(nk[1]);
        int[][] contests = new int[n][2];

        for (int i = 0; i < n; i++) {
            String[] contestsRowItems = scanner.nextLine().split(" ");
            if (i != n-1) 
            	scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");
            
            for (int j = 0; j < 2; j++) {
                int contestsItem = Integer.parseInt(contestsRowItems[j]);
                contests[i][j] = contestsItem;
            }
        }

        int result = luckBalance(n, k, contests);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedWriter.close();

        scanner.close();
    }
}
