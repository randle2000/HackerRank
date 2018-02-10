import java.util.*;

public class Solution {
	
	static boolean isScored(int min, int max, int throwPoint, int distance) {
		int landPoint = throwPoint + distance;
		return landPoint >= min && landPoint <= max; 
	}

    static void countApplesAndOranges(int s, int t, int a, int b, int[] apples, int[] oranges) {
        int appleScore = 0, orangeScore = 0;
        
        for (int d : apples)
        	if (isScored(s, t, a, d))
        		appleScore++;
        
        for (int d : oranges)
        	if (isScored(s, t, b, d))
        		orangeScore++;
        
        System.out.println(appleScore);
        System.out.println(orangeScore);
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int s = in.nextInt();
        int t = in.nextInt();
        int a = in.nextInt();
        int b = in.nextInt();
        int m = in.nextInt();
        int n = in.nextInt();
        int[] apple = new int[m];
        for(int apple_i = 0; apple_i < m; apple_i++){
            apple[apple_i] = in.nextInt();
        }
        int[] orange = new int[n];
        for(int orange_i = 0; orange_i < n; orange_i++){
            orange[orange_i] = in.nextInt();
        }
        countApplesAndOranges(s, t, a, b, apple, orange);
        in.close();
    }
}