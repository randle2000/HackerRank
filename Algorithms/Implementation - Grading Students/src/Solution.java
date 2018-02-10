import java.util.*;

public class Solution {

    static int[] solve(int[] grades){
        return Arrays.stream(grades)
        	.map(e -> {
        		if (e < 38) return e;
        		if (e % 5 >= 3) return (e / 5 + 1) * 5;
        		return e;
        	})
        	.toArray();
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[] grades = new int[n];
        for(int grades_i=0; grades_i < n; grades_i++){
            grades[grades_i] = in.nextInt();
        }
        in.close();
        int[] result = solve(grades);
        for (int i = 0; i < result.length; i++) {
            System.out.print(result[i] + (i != result.length - 1 ? "\n" : ""));
        }
        System.out.println("");
        

    }
}