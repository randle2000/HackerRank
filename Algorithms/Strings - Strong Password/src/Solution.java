import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

public class Solution {
	
	private static final String DIGITS_REGEX = ".*\\d.*";
	private static final String SMLET_REGEX = ".*[a-z].*";
	private static final String CAPLET_REGEX = ".*[A-Z].*";
	private static final String SPECIAL_REGEX = ".*[!@#$%\\^&*()\\-+].*";	// these 2 characters are escaped with \\ : ^ and - 
	private static final int MIN_LENGTH = 6;
	
    // Complete the minimumNumber function below.
    static int minimumNumber(int n, String password) {
        // Return the minimum number of characters to make the password strong
    	int charsToAdd = 0;
    	if (!password.matches(DIGITS_REGEX))
    		charsToAdd++;
    	if (!password.matches(SMLET_REGEX))
    		charsToAdd++;
    	if (!password.matches(CAPLET_REGEX))
    		charsToAdd++;
    	if (!password.matches(SPECIAL_REGEX))
    		charsToAdd++;
    	
    	if (charsToAdd + password.length() < 6)
    		charsToAdd = 6 - password.length();
    	
    	return charsToAdd;
    }
    
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        //BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));

        int n = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        String password = scanner.nextLine();

        int answer = minimumNumber(n, password);

        bufferedWriter.write(String.valueOf(answer));
        bufferedWriter.newLine();

        bufferedWriter.close();

        scanner.close();
    }
}
