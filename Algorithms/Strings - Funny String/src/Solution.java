import java.util.*;

public class Solution {

    static String funnyString(String s){
        char[] chArr = s.toCharArray();
        for (int i = 0; i <= chArr.length / 2; i++) {
        	if (Math.abs(chArr[i+1] - chArr[i]) != Math.abs(chArr[chArr.length - i - 1] - chArr[chArr.length - i - 2])) {
        		return "Not Funny";
        	}
        }
        return "Funny";
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int q = in.nextInt();
        for(int a0 = 0; a0 < q; a0++){
            String s = in.next();
            String result = funnyString(s);
            System.out.println(result);
        }
        in.close();
    }
}