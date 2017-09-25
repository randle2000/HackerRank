import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Solution {
    Map<String, Integer> magazineMap;
    Map<String, Integer> noteMap;
    
    private Map<String, Integer> stringToMap(String longString) {
    	return Arrays.stream(longString.split(" ")).collect(
        	Collectors.toMap(
        		str -> str,
        		str -> 1,
        		(oldValue, newValue) -> oldValue + newValue
        	)
        );
    	
    }
    
    public Solution(String magazine, String note) {
    	magazineMap = stringToMap(magazine);
    	noteMap = stringToMap(note);
    }
    
    public boolean solve() {
        for (Entry<String, Integer> entry : noteMap.entrySet()) {
        	int noteCount = entry.getValue();
        	int magCount = magazineMap.get(entry.getKey());
        	if (magCount < noteCount)
        		return false;
        }
        return true;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int m = scanner.nextInt();
        int n = scanner.nextInt();
        
        // Eat whitespace to beginning of next line
        scanner.nextLine();
        
        Solution s = new Solution(scanner.nextLine(), scanner.nextLine());
        scanner.close();
        
        boolean answer = s.solve();
        if(answer)
            System.out.println("Yes");
        else System.out.println("No");
      
    }
}
