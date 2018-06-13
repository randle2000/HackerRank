import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

public class Solution {

    static int sockMerchant(int[] ar) {
    	
    	// key - color
    	// value - number of socks of this color
    	Map<Integer, Integer> map = new HashMap<>();
    	Arrays.stream(ar).forEach(i -> {
    		Integer oldValue = map.get(i);
    		Integer newValue = (oldValue == null) ? 1 : oldValue + 1;
    		map.put(i, newValue);
    	});
    	
    	int result = map.values().stream()
    		.mapToInt(v -> v / 2)
    		.sum();
    	
    	return result;
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
    	
        int arCount = scanner.nextInt();
        scanner.nextLine();
        String[] arItems = scanner.nextLine().split(" ");
        scanner.close();
        
        int[] ar = Arrays.stream(arItems)
        	.mapToInt(Integer::parseInt)
        	.toArray();
        
        int result = sockMerchant(ar);
        
        System.out.println(result);
        
    }
}
