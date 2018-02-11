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
	
    public static int numberNeeded(String first, String second) {
    	Map<Character, Integer> firstMap = charFreqMap(first);
    	Map<Character, Integer> secondMap = charFreqMap(second);
    	
    	Set<Character> intersection = new HashSet<>(firstMap.keySet());
    	intersection.retainAll(secondMap.keySet());
    	
    	Set<Character> firstRemains = new HashSet<>(firstMap.keySet());
    	Set<Character> secondRemains = new HashSet<>(secondMap.keySet());
    	firstRemains.removeAll(intersection);
    	secondRemains.removeAll(intersection);

    	int result = 0;
    	
    	result += firstRemains.stream()
    		.mapToInt(ch -> firstMap.get(ch))
    		.sum();
    	
    	result += secondRemains.stream()
    		.mapToInt(ch -> secondMap.get(ch))
    		.sum();

    	result += intersection.stream()
    		.mapToInt(ch -> Math.abs(firstMap.get(ch) - secondMap.get(ch)))
			.sum();
    	
    	return result;
    }
  
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String a = in.next();
        String b = in.next();
        in.close();
        System.out.println(numberNeeded(a, b));
    }
}