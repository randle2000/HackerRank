import java.util.*;

public class Solution {

    static int[] breakingRecords(int[] score) {
    	int res[] = new int[2];
    	int lo = score[0];
    	int hi = score[0];
        for (int i = 1; i < score.length; i++) {
        	int s = score[i]; 
        	if (s > hi) {
        		hi = s;
        		res[0]++;
        	}
        	if (s < lo) {
        		lo = s;
        		res[1]++;
        	}
        }
        return res;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[] score = new int[n];
        for(int score_i = 0; score_i < n; score_i++){
            score[score_i] = in.nextInt();
        }
        int[] result = breakingRecords(score);
        for (int i = 0; i < result.length; i++) {
            System.out.print(result[i] + (i != result.length - 1 ? " " : ""));
        }
        System.out.println("");


        in.close();
    }
}