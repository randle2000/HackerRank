import java.util.*;

public class Solution {

    static String kangaroo(int x1, int v1, int x2, int v2) {
    	String met = "NO";
        while (true) {
        	x1 += v1;
        	x2 += v2;
        	if (x1 == x2) {
        		met = "YES";
        		break;
        	} else if (v1 > v2) {
        		if (x1 > x2)
        			break;
        	} else if (v1 < v2) {
        		if (x2 > x1)
        			break;
        	} else	// v1 = v2
        		break;
        }
        return met;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int x1 = in.nextInt();
        int v1 = in.nextInt();
        int x2 = in.nextInt();
        int v2 = in.nextInt();
        in.close();
        String result = kangaroo(x1, v1, x2, v2);
        System.out.println(result);
    }
}