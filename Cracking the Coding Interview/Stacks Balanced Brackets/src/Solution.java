import java.util.*;

public class Solution {
    
    public static boolean isBalanced(String expression) {
    	Stack<Character> brackets = new Stack<>();
    	for (int i = 0; i < expression.length(); i++) {
    		char ch = expression.charAt(i);
    		switch (ch) {
    			case '(':
    			case '{':
    			case '[':
    				brackets.push(ch);
    				break;
    			case ')':
    				if (brackets.empty() || brackets.pop() != '(') return false;
    				break;
    			case '}':
    				if (brackets.empty() || brackets.pop() != '{') return false;
    				break;
    			case ']':
    				if (brackets.empty() || brackets.pop() != '[') return false;
    				break;
    		}
    	}
        return brackets.empty();
    }
  
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int t = in.nextInt();
        for (int a0 = 0; a0 < t; a0++) {
            String expression = in.next();
            System.out.println( (isBalanced(expression)) ? "YES" : "NO" );
        }
        in.close();
    }
}