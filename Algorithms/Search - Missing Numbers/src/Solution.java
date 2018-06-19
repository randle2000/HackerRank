import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;
import java.util.stream.Collectors;


public class Solution {
	
	// key - Integer
	// values - number of times this Integer occurs in the array
	private static Map<Integer, Integer> freqMap(int[] arr) {
		Map<Integer, Integer> map = new HashMap<>();
		Arrays.stream(arr)
			.forEach(i -> {
				Integer oldValue = map.get(i);
				Integer newValue = oldValue == null ? 1 : oldValue + 1;
				map.put(i, newValue);
			});
		return map;
	}
	
	// Complete the missingNumbers function below.
    static int[] missingNumbers(int[] arr, int[] brr) {
    	Map<Integer, Integer> aMap = freqMap(arr);
    	Map<Integer, Integer> bMap = freqMap(brr);
    	SortedSet<Integer> missing = new TreeSet<>();
    	
    	bMap.entrySet().stream()
    		.forEach(entry -> {
    			Integer number = entry.getKey();
    			int aFreq = aMap.get(number);
    			int bFreq = entry.getValue();
    			if (aFreq < bFreq)
    				missing.add(number);
    		});
    	
    	return missing.stream().mapToInt(Number::intValue).toArray();
    } 
    
    private static final Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) throws IOException {
        //BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));

        int n = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        int[] arr = new int[n];

        String[] arrItems = scanner.nextLine().split(" ");
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        for (int i = 0; i < n; i++) {
            int arrItem = Integer.parseInt(arrItems[i]);
            arr[i] = arrItem;
        }

        int m = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        int[] brr = new int[m];

        String[] brrItems = scanner.nextLine().split(" ");
        //scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        for (int i = 0; i < m; i++) {
            int brrItem = Integer.parseInt(brrItems[i]);
            brr[i] = brrItem;
        }

        int[] result = missingNumbers(arr, brr);

        for (int i = 0; i < result.length; i++) {
            bufferedWriter.write(String.valueOf(result[i]));

            if (i != result.length - 1) {
                bufferedWriter.write(" ");
            }
        }

        bufferedWriter.newLine();
        bufferedWriter.close();

        scanner.close();    }
}
