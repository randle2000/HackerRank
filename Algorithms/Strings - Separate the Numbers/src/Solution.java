import java.util.*;

public class Solution {
	
	/** 
	 * Calc number of digits in a number
	 * ugly code but very fast
	 * 
	 * @param number
	 * @return number of digits
	 */
	private static int numberOfDigits(long number) {
		int length = 1;
		if (number >= 10000000000000000L) {
		    length += 16;
		    number /= 10000000000000000L;
		}
		if (number >= 100000000) {
		    length += 8;
		    number /= 100000000;
		}
		if (number >= 10000) {
		    length += 4;
		    number /= 10000;
		}
		if (number >= 100) {
		    length += 2;
		    number /= 100;
		}
		if (number >= 10) {
		    length += 1;
		}
		return length;		
	}
	
	private static boolean isBeautiful(String s, int startPos, long expected) {
		int width = numberOfDigits(expected);
		if (s.charAt(startPos) == '0') return false;
		if (startPos + width > s.length()) return false;
		long num = Long.parseLong(s.substring(startPos, startPos + width));
		if (num != expected) return false;
		if (startPos + width == s.length())
			return true;
		else
			return isBeautiful(s, startPos + width, expected + 1);
	}

    static void separateNumbers(String s) {
    	int i;
    	boolean beautiful = false;
    	for (i = 1; i <= s.length() / 2; i++) {
    		if (isBeautiful(s, 0, Long.parseLong(s.substring(0, i) ))) {
    			beautiful = true;
    			break;
    		}
    	}
    	if (beautiful)
    		System.out.println("YES " + s.substring(0, i));
    	else
    		System.out.println("NO");
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int q = in.nextInt();
        for(int a0 = 0; a0 < q; a0++){
            String s = in.next();
            separateNumbers(s);
        }
        in.close();
    }
}