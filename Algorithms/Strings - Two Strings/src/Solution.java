import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;
import java.util.stream.Collectors;

public class Solution {
	
	// Complete the twoStrings function below.
    static String twoStrings(String s1, String s2) {
    	Set<Character> set1 = s1.chars()	// IntStream
    			.mapToObj(e -> (char)e)		// Stream<Character>
    			.collect(Collectors.toSet());
    	Set<Character> set2 = s2.chars()	// IntStream
    			.mapToObj(e -> (char)e)		// Stream<Character>
    			.collect(Collectors.toSet());
    	set1.retainAll(set2);
    	
    	return set1.size() > 0 ? "YES" : "NO";
    }
    
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        //BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));

        int q = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        for (int qItr = 0; qItr < q; qItr++) {
            String s1 = scanner.nextLine();

            String s2 = scanner.nextLine();

            String result = twoStrings(s1, s2);

            bufferedWriter.write(result);
            bufferedWriter.newLine();
        }

        bufferedWriter.close();

        scanner.close();
    }
}
