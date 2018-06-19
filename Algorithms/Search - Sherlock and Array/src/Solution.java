import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;
import java.util.stream.Collectors;


public class Solution {
	
	static String solve(int[] a) {
		int leftSum = 0;
		int rightSum = Arrays.stream(a).sum();
		
		int prevEl = 0;
		for (int el : a) {
			leftSum += prevEl;
			rightSum -= el;
			prevEl = el;
			if (leftSum == rightSum)
				return "YES";
			if (leftSum > rightSum)
				return "NO";
		}
		
        return "NO";
    }
    
    private static final Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) throws IOException {
    	Scanner in = new Scanner(System.in);
        int T = in.nextInt();
        for(int a0 = 0; a0 < T; a0++){
            int n = in.nextInt();
            int[] a = new int[n];
            for(int a_i=0; a_i < n; a_i++){
                a[a_i] = in.nextInt();
            }
            String result = solve(a);
            System.out.println(result);
        }
    }
}
