import java.util.*;

class IndexPair {
	short i, j;
	
	IndexPair(short i, short j) {
		this.i = i;
		this.j = j;
	}
	
	@Override
	public String toString() {
		return i < j ? i + 1 + " " + (j + 1) : j + 1 + " " + (i + 1);
	}
}

public class Solution {
	
	public static Map<Short, IndexPair> generateIdPairs(short money, short[] costs) {
		Map<Short, IndexPair> map = new HashMap<>();
		for (short i = 0; i < costs.length - 1; i++) 
			for (short j = (short) (i + 1); j < costs.length; j++) 
				if (costs[i] < money && costs[j] < money)
					map.putIfAbsent((short)(costs[i] + costs[j]), new IndexPair(i, j));
		return map;
		
	}

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int t = in.nextInt();
        for (int a0 = 0; a0 < t; a0++) {
            short m = in.nextShort();
            int n = in.nextInt();
            short a[] = new short[n];
            for (int a_i=0; a_i < n; a_i++) {
                a[a_i] = in.nextShort();
            }
            
            Map<Short, IndexPair> map = generateIdPairs(m, a);
            System.out.println(map.get(m));
        }
        in.close();
    }
}