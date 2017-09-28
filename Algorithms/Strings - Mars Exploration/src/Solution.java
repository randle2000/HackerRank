import java.util.*;

public class Solution {
	
    public static void main(String[] args) {
    	Scanner in = new Scanner(System.in);
    	String s = in.next();
        in.close();
     
        char[] ch = s.toCharArray();
        int bad = 0;
        for (int i = 0; i < ch.length; i+=3) {
        	if (ch[i] != 'S')
        		bad++;
        	if (ch[i + 1] != 'O')
        		bad++;
        	if (ch[i + 2] != 'S')
        		bad++;
        }
   		System.out.println(bad);
    }
}