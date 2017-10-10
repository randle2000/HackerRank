import java.util.*;

public class Solution {
	
	private static final String PRIME = "Prime";
	private static final String NOT_PRIME = "Not prime";
	
	// checks whether an int is prime or not.
	public static boolean isPrime(int n) {
		if (n == 1)
			return false;
		if (n == 2)
			return true;
	    // check if n is a multiple of 2
	    if (n % 2 == 0)
	    	return false;
	    int sqrtn = (int)Math.sqrt(n);
	    // if not, then just check the odds
	    for (int i = 3; i <= sqrtn; i += 2) {
	        if(n % i == 0)
	            return false;
	    }
	    return true;
	}

	public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        
        in.close();
        
        int p = in.nextInt();
        for(int a0 = 0; a0 < p; a0++){
            //BigInteger n = in.nextBigInteger();
            //System.out.println(n.isProbablePrime(7) ? PRIME : NOT_PRIME);
            
            int n = in.nextInt();
            if (isPrime(n))
            	System.out.println(PRIME);
            else
            	System.out.println(NOT_PRIME);
        }
        in.close();
    }
}