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
	
	// these 2 arrays must be sorted, otherwise binarySearch will not work
	private static char[] opening = {'(', '[', '{'};
	private static char[] closing = {')', ']', '}'};
	
	private static boolean isOpening(char ch) {
		return Arrays.binarySearch(opening, ch) >= 0;
	}
	
	private static boolean isClosingFor(char closingBr, char openingBr) {
		int index = Arrays.binarySearch(closing, closingBr);
		if (index < 0)	// not a closing bracket
			return false;
		return opening[index] == openingBr;
	}
	
	// Complete the isBalanced function below.
    static String isBalanced(String s) {
    	// Stack is a synchronized implementation.
    	// However, synchronization isn’t always needed, in such cases, it’s advised to use ArrayDeque.
    	Deque<Character> stack = new ArrayDeque<>();
    	for (char ch : s.toCharArray()) {
    		if (isOpening(ch)) {	// opening bracket
    			stack.push(ch);
    		} else {				// closing bracket
    			if (stack.isEmpty() || !isClosingFor(ch, stack.pop()))
    				return "NO";
    		}
    	}
    	
    	if (stack.isEmpty())
    		return "YES";
    	else
    		return "NO";
    }
    
    private static final Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) throws IOException {
    	//BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
    	BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));

        int t = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        for (int tItr = 0; tItr < t; tItr++) {
            String s = scanner.nextLine();

            String result = isBalanced(s);

            bufferedWriter.write(result);
            bufferedWriter.newLine();
        }

        bufferedWriter.close();

        scanner.close();
    }
}
