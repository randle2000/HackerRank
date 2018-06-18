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
	
    static String splitBill(int[] bills, int k, int b) {
    	int annasBill = IntStream.range(0, bills.length)
    		.filter(i -> i != k)
    		.map(i -> bills[i])
    		.sum();
    	annasBill = annasBill / 2;
    	
    	return annasBill == b ? "Bon Appetit" : String.valueOf(b - annasBill);
    }
    
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        //BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));

        int n = scanner.nextInt();
        int k = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        String[] str = scanner.nextLine().split(" ");
       	scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");
        int bills[] = new int[n];
        for (int i = 0; i < n; i++) {
        	bills[i] = Integer.parseInt(str[i]);
        }

        int b = scanner.nextInt();

        String result = splitBill(bills, k, b);

        bufferedWriter.write(result);
        bufferedWriter.newLine();
        bufferedWriter.close();

        scanner.close();
    }
}
