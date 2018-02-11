import java.util.*;

public class Solution {
	
	// key = Character
	// value = number of occurences of that Character in str
	public static Map<Character, Integer> charFreqMap(String str) {
		Map<Character, Integer> map = new HashMap<>();
		// String.chars() produces IntStream which is stream of int
		str.chars().forEach(i -> {
			Character key = new Character((char)i);
			Integer oldValue = map.get(key);
			Integer newValue = (oldValue == null) ? 1 : oldValue + 1;
			map.put(key,  newValue);
		});
		return map;
	}
	
    private static int anagram(String s) {
    	if (s.length() % 2 != 0)
    		return -1;
    	int d = s.length() / 2;
    	String first = s.substring(0, d);
    	String second = s.substring(d);
    	
    	Map<Character, Integer> firstMap = charFreqMap(first);
    	Map<Character, Integer> secondMap = charFreqMap(second);
    	
    	Set<Character> intersection = new HashSet<>(firstMap.keySet());
    	intersection.retainAll(secondMap.keySet());
    	
    	int result = 0;
    	
    	result += intersection.stream()
    		.mapToInt(ch -> Math.min(firstMap.get(ch), secondMap.get(ch)))
			.sum();
    	
    	result = first.length() - result; 
    	
    	return result;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int q = in.nextInt();
        for(int a0 = 0; a0 < q; a0++){
            String s = in.next();
            int result = anagram(s);
            System.out.println(result);
        }
        in.close();
    }
}