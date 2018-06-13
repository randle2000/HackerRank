import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

public class Solution {

	static int countingValleys(int n, String s) {
		
		char[] chars = s.toCharArray();
		int level = 0;
		int valleys = 0;
		for (char ch : chars) {
			switch (ch) {
			case 'U': 
				level++;
				break;
			case 'D':
				if (level == 0)			// valley is when level is 0 and next step is down
					valleys++;
				level--;
				break;
			}
		}
		
		return valleys;
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
    	
        int n = scanner.nextInt();
        scanner.nextLine();
        String s = scanner.nextLine();
        scanner.close();
        
        int result = countingValleys(n, s);
        
        System.out.println(result);
        
    }
}
