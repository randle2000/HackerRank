import java.util.*;

class Pair {
	int x, y;
	
	Pair(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString() {
		return x + " " + y;
	}
}


public class Solution {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[] a = new int[n];
        for(int a_i=0; a_i < n; a_i++){
            a[a_i] = in.nextInt();
        }
        in.close();
        
        Arrays.sort(a);
        List<Pair> pairs = new LinkedList<>();
        int minDiff = a[1] - a[0];
        for (int i = 0; i < n - 1; i++) {
        	int diff = a[i + 1] - a[i];
        	if (diff == minDiff) {
        		pairs.add(new Pair(a[i], a[i + 1]));
        	} else if (diff < minDiff) {
        		pairs.clear();
        		pairs.add(new Pair(a[i], a[i + 1]));
        		minDiff = diff;
        	}
        }
        
    	for (Pair pair : pairs) 
    		System.out.print(pair + " ");
    	System.out.println();
    }
}