import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

public class Solution {
	
	// Complete the theLoveLetterMystery function below.
    static int theLoveLetterMystery(String s) {
    	int opCount = 0;
    	
    	char[] chArr = s.toCharArray();
    	for (int i = 0; i < (chArr.length / 2); i++) {
    		char f = chArr[i];
    		char l = chArr[chArr.length - 1 - i];
    		if (l > f)
    			opCount += l - f;
    		else if (f > l)
    			opCount += f - l;
    	}
    	
    	return opCount;
    }
    
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        //BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));

        int q = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        for (int qItr = 0; qItr < q; qItr++) {
            String s = scanner.nextLine();

            int result = theLoveLetterMystery(s);

            bufferedWriter.write(String.valueOf(result));
            bufferedWriter.newLine();
        }

        bufferedWriter.close();

        scanner.close();
    }
}
