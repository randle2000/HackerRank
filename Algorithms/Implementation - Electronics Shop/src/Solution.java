import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

public class Solution {

	static int getMoneySpent(int[] keyboards, int[] drives, int b) {
		
		List<Integer> allPairs = new ArrayList<>(keyboards.length * drives.length);
		for (int k : keyboards)
			for (int d : drives)
				allPairs.add(k + d);
		
		Optional<Integer> maxSpent = allPairs.stream()
			.filter(sum -> sum <= b)
			.max(Comparator.naturalOrder());
		
		return maxSpent.isPresent() ? maxSpent.get() : -1;
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
    	

        int b = scanner.nextInt();
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        
        int[] keyboards = new int[n];
        int[] drives = new int[m];
        
        for (int i = 0; i < n; i++)
        	keyboards[i] = scanner.nextInt();
        
        for (int i = 0; i < m; i++)
        	drives[i] = scanner.nextInt();

        scanner.close();
        
        int moneySpent = getMoneySpent(keyboards, drives, b);
        
        System.out.println(moneySpent);
        
    }
}
