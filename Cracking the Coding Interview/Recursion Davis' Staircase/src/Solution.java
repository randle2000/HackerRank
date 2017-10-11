import java.util.*;

public class Solution {
	
	private static Map<Integer, Integer> visitedStairs = new HashMap<>();
	
	public static Integer waysToWalkStaircase(int n) {
		if (n < 0) return 0;
		if (n == 0) return 1;
		
		if (!visitedStairs.containsKey(n)) {
			int ways = waysToWalkStaircase(n - 1) + waysToWalkStaircase(n - 2) + waysToWalkStaircase(n - 3);
			visitedStairs.put(n, ways);
		}
		
		return visitedStairs.get(n);
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int s = in.nextInt();
        for(int i = 0; i < s; i++){
            int n = in.nextInt();
            System.out.println(waysToWalkStaircase(n));
        }
        in.close();
    }
}