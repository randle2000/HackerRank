import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;
import java.util.stream.Collectors;

public class Solution {
	
	private static Set<Character> strToSet(String s) {
    	return s.chars()					// IntStream
        		.mapToObj(e -> (char)e)		// Stream<Character>
        		.collect(Collectors.toSet());
	}
	
	// Complete the gemstones function below.
    static int gemstones(String[] arr) {
    	if (arr.length == 0)
    		return 0;
    	
    	Set<Character> gems = strToSet(arr[0]);	// 1st string
    	
    	for (int i = 1; i < arr.length; i++) {	// start with 2nd string
    		Set<Character> stones = strToSet(arr[i]);
    		gems.retainAll(stones);
    	}
    	
    	return gems.size();

    }
    
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        //BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));

        int n = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        String[] arr = new String[n];

        for (int i = 0; i < n; i++) {
            String arrItem = scanner.nextLine();
            arr[i] = arrItem;
        }

        int result = gemstones(arr);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedWriter.close();

        scanner.close();
    }
}
