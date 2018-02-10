import java.util.*;

public class Solution {
	
	static List<Integer> getAllDivisors(int num) {
		List<Integer> res = new ArrayList<>();
		res.add(1);
		res.add(num);
		for (int i = 2; i <= num / 2; i++)
            if (num % i == 0)
                res.add(i);
        return res;
	}

    static int getTotalX(int[] a, int[] b) {
    	int minB = Arrays.stream(b).min().getAsInt();
    	List<Integer> divisors = getAllDivisors(minB);
    	for (int e : b) {
    		Iterator<Integer> it = divisors.iterator();
    		while (it.hasNext()) {
    			if (e % it.next() != 0) {
    				it.remove();
    			}
    		}
    	}
    	
    	for (int e : a) {
    		Iterator<Integer> it = divisors.iterator();
    		while (it.hasNext()) {
    			if (it.next() % e != 0) {
    				it.remove();
    			}
    		}
    	}
    	
    	return divisors.size();
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();
        int[] a = new int[n];
        for(int a_i = 0; a_i < n; a_i++){
            a[a_i] = in.nextInt();
        }
        int[] b = new int[m];
        for(int b_i = 0; b_i < m; b_i++){
            b[b_i] = in.nextInt();
        }
        int total = getTotalX(a, b);
        System.out.println(total);
        in.close();
    }
}