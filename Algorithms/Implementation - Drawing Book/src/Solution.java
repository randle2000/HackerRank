import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

public class Solution {
	
	static boolean isEven(int n) {
		return n % 2 == 0;
	}

	static int flipCount(int n, int p) {
		if (isEven(n))
			n++;
		if (isEven(p))
			p++;
		
		return n >= p ? 0 : (p - n) / 2;
	}
	
	static int pageCount(int n, int p) {
		
		int flipCountFromBeginning = flipCount(1, p);
		int flipCountFromEnd = flipCount(p, n);
		
		return Math.min(flipCountFromBeginning, flipCountFromEnd);
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
    	
        int n = scanner.nextInt();
        int p = scanner.nextInt();
        scanner.close();
        
        int result = pageCount(n, p);
        
        System.out.println(result);
        
    }
}
