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
	
	// Complete the minimumBribes function below.
    static void minimumBribes(int[] q) {
    	int wasMoved = 0;
    	for (int i = 0; i < q.length; i++) {
    		
    		if (q[i] - i - 1 > 2) {
    			System.out.println("Too chaotic");
    			return;
    		}
    		
    		int fromPos = q[i] - 2;
    		if (fromPos < 0)
    			fromPos = 0;
    		for (int j = fromPos; j < i; j++)
    			if (q[i] < q[j])
    				wasMoved++;
    	}
    	
    	System.out.println(wasMoved);
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int t = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        for (int tItr = 0; tItr < t; tItr++) {
            int n = scanner.nextInt();
            scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

            int[] q = new int[n];

            String[] qItems = scanner.nextLine().split(" ");
            if (tItr != t-1) 
            	scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

            for (int i = 0; i < n; i++) {
                int qItem = Integer.parseInt(qItems[i]);
                q[i] = qItem;
            }

            minimumBribes(q);
        }

        scanner.close();
    }
}
