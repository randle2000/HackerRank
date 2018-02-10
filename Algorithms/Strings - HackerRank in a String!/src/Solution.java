import java.util.*;

public class Solution {
	
	private static final char[] HR = "hackerrank".toCharArray();
	
    static String hackerrankInString(String s) {
    	int i = 0, j = 0;
    	while (i < HR.length && j < s.length()) {
    		int idx = s.indexOf(HR[i], j);
    		if (idx == -1)
    			return "NO";
    		i++;
    		j = idx + 1;
    	}
    	if (i != HR.length)
    		return "NO";
    	return "YES";
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int q = in.nextInt();
        for(int a0 = 0; a0 < q; a0++){
            String s = in.next();
            String result = hackerrankInString(s);
            System.out.println(result);
        }
        in.close();
    }
}