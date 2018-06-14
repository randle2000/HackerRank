import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

public class Solution {
	
	private static final int LETTERS_IN_ALPAHBET = 26;
	
	private static char shiftIfLetter(char ch, int by, char a, char z) {
		if (ch >= a && ch <= z)
			if (ch + by > z)
				ch = (char)(a + (by - (z - ch)) - 1);
			else
				ch = (char)(ch + by);
		
		return ch;
	}
	
	private static char shiftBy(char ch, int by) {
		by = (by + LETTERS_IN_ALPAHBET) % LETTERS_IN_ALPAHBET;
		
		ch = shiftIfLetter(ch, by, 'a', 'z');
		ch = shiftIfLetter(ch, by, 'A', 'Z');
		
		return ch;
	}
	
	// Complete the caesarCipher function below.
    static String caesarCipher(String s, int k) {
    	char[] chArr = s.toCharArray();
    	
    	for (int i = 0; i < chArr.length; i++)
    		chArr[i] = shiftBy(chArr[i], k);
    	
    	return new String(chArr);

    }
    
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        //BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));

        int n = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        String s = scanner.nextLine();

        int k = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        String result = caesarCipher(s, k);

        bufferedWriter.write(result);
        bufferedWriter.newLine();

        bufferedWriter.close();

        scanner.close();
    }
}
