import java.util.*;
import java.util.stream.Collectors;

public class Solution {

    static int stringConstruction(String s) {
        Set<Character> chSet = s.chars().mapToObj(e -> (char)e).collect(Collectors.toSet());
        return chSet.size();
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int q = in.nextInt();
        for(int a0 = 0; a0 < q; a0++){
            String s = in.next();
            int result = stringConstruction(s);
            System.out.println(result);
        }
        in.close();
    }
}