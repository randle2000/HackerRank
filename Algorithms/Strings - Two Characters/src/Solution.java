import java.util.*;
import java.util.stream.Collectors;

public class Solution {
	
	private static int getSLen(String s, char ch1, char ch2) {
		String s2 = s.replaceAll("[^" + ch1 + ch2 + "]", "");
		char[] chArr = s2.toCharArray();
		 
		for (int i = 0; i < chArr.length - 1; i++) {
			char nextExpected;
			if (chArr[i] == ch1)
				nextExpected = ch2;
			else
				nextExpected = ch1;
			if (chArr[i+1] != nextExpected)
				return 0;
		}
		return s2.length();
	}

    static int twoCharaters(String s) {
    	Set<Character> chSet = s.chars().mapToObj(e -> (char)e).collect(Collectors.toSet());
    	int[] chArr  = chSet.stream().mapToInt(e -> (char)e).toArray();
    	int maxLen = 0;
    	for (int i = 0; i < chSet.size() - 1; i++)
    		for (int j = i + 1; j < chSet.size(); j++) {
    			int len = getSLen(s, (char)chArr[i], (char)chArr[j]); 
    		    if (len > maxLen)
    		    	maxLen = len;
    		}
    	return maxLen;
    			
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int l = in.nextInt();
        String s = in.next();
        int result = twoCharaters(s);
        System.out.println(result);
        in.close();
    }
}