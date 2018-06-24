import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// this class is used for performance
// so you don't to have to sort the same subarrays multiple times 
class CachedArrays {
	private final char[] chars;
	// key is 'from'
	// key of the 2nd map is 'to'
	private Map<Integer, Map<Integer, char[]>> mapFrom = new HashMap<>();
	
	public CachedArrays(String s) {
		this.chars = s.toCharArray();
	}
	
	// from inclusive, to exclusive
	public char[] getSortedSubarray(int from, int to) {
		Map<Integer, char[]> mapTo = mapFrom.get(from);
		if (mapTo == null) {
			mapTo = new HashMap<>();
			mapFrom.put(from, mapTo);
		}
		char[] chArr = mapTo.get(to);
		if (chArr == null) {
			chArr = Arrays.copyOfRange(chars, from, to);
			Arrays.sort(chArr);
			mapTo.put(to, chArr);
		}
		return chArr;
	}
}

public class Solution {
	
	private static boolean isAnagram(CachedArrays cachedArrays, int from1, int from2, int size) {
		// from1 and from2 - inclusive 
		// to1 and to2 - exclusive
		int to1 = from1 + size;
		int to2 = from2 + size;
		if (from2 < to1) {	// only need to compare non-intersecting parts
			to1 = from2;
			from2 = from1 + size;
		}
		
		char[] arr1 = cachedArrays.getSortedSubarray(from1, to1);
		char[] arr2 = cachedArrays.getSortedSubarray(from2, to2);
		
		return Arrays.equals(arr1, arr2);
	}
	
	// Complete the sherlockAndAnagrams function below.
    static int sherlockAndAnagrams(String s) {
    	int length = s.length();
    	int anagramCount = 0;
    	CachedArrays cachedArrays = new CachedArrays(s);
    	
    	for (int from1 = 0; from1 < length - 1; from1++) {
    		for (int size = 1; size <= length - from1 - 1; size++) {
    			for (int from2 = from1 + 1; from2 < length - size + 1; from2++) {
    				if (isAnagram(cachedArrays, from1, from2, size)) 
    					anagramCount++;
    			}
    		}
    	}
    	
    	return anagramCount;
    }
    
    private static final Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) throws IOException {
    	//BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
    	BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));

        int q = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        for (int qItr = 0; qItr < q; qItr++) {
            String s = scanner.nextLine();

            int result = sherlockAndAnagrams(s);

            bufferedWriter.write(String.valueOf(result));
            bufferedWriter.newLine();
        }

        bufferedWriter.close();

        scanner.close();
    }
}
