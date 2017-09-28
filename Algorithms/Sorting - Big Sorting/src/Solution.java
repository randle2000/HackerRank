import java.util.*;

public class Solution {
	
    public static void main(String[] args) {
    	Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        String[] unsorted = new String[n];
        for(int unsorted_i=0; unsorted_i < n; unsorted_i++){
            unsorted[unsorted_i] = in.next();
        }
        in.close();

        Comparator<String> myComparator = new Comparator<String>() {
        	@Override
        	public int compare(String s1, String s2) {
        		if (s1.length() < s2.length()) {
        			return -1;
        		} else if (s1.length() > s2.length()) {
        			return 1;
        		} else {	// s1.length() == s2.length()
        			for (int i = 0; i < s1.length(); i++) {
        				if (s1.charAt(i) < s2.charAt(i))
        					return -1;
        				else if (s1.charAt(i) > s2.charAt(i))
        					return 1;
        			}
        		} 
    			return 0;
        	}
        };
        Arrays.sort(unsorted, myComparator);
    	for (String str : unsorted) 
    		System.out.println(str);
    }
}