import java.util.*;

public class Solution {
	
    public static void main(String[] args) {
    	Scanner in = new Scanner(System.in);
    	String s = in.next();
        in.close();
     
        int wordCount = s.split("([A-Z])").length - 1 + 1;
   		System.out.println(wordCount);
    }
}