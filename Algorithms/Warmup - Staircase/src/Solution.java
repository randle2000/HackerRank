import java.util.*;

public class Solution {

    static void staircase(int n) {
    	for (int i = 0; i < n; i++) 
    		System.out.println( String.join("", Collections.nCopies(n - i - 1, " ")) + String.join("", Collections.nCopies(i + 1, "#")) );
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        staircase(n);
        in.close();
    }
}