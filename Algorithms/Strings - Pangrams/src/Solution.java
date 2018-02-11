import java.util.*;
import java.util.stream.Collectors;

public class Solution {
	
	private static final int NUMBER_OF_LETTERS_IN_ALPHABET = 26;
	
	private static boolean isPangram(String s) {
		String s2 = s.toLowerCase().replaceAll("[^a-z]", "");
        Set<Character> chSet = s2.chars().mapToObj(e -> (char)e).collect(Collectors.toSet());
        return chSet.size() == NUMBER_OF_LETTERS_IN_ALPHABET;
	}
	
    public static void main(String[] args) {
    	Scanner in = new Scanner(System.in);
    	String s = in.nextLine();
        in.close();
     
        if (isPangram(s))
        	System.out.println("pangram");
        else
        	System.out.println("not pangram");
    }
}