import java.util.*;

public class Solution {
	
    static String[] countingFullSort(int[] intArr, String[] strArr) {
    	// generate countArr array
		int maxValue = Arrays.stream(intArr).max().getAsInt();
        int[] countArr = new int[maxValue + 1];	// count of each element
        for (int e : intArr)
        	countArr[e]++;
        
        // generate startingPos array
        int[] startingPos = new int[maxValue + 1];	// starting index of each element in a sorted array
        int resIndex = 0;
        for (int i = 0; i < countArr.length; i++) {
        	startingPos[i] = countArr[i] == 0 ? -1 : resIndex;
        	resIndex += countArr[i];
        }
        
        // generate result 
        String[] result = new String[strArr.length];
        int[] timesEncountered = new int[maxValue + 1];
        for (int i = 0; i < intArr.length; i++) {
        	int x = intArr[i];
        	String s = (i < intArr.length / 2) ? "-" : strArr[i];
        	int sortedIndex = startingPos[x] + timesEncountered[x]; 
        	result[sortedIndex] = s;
        	timesEncountered[x]++;
        }
        return result;
    }

    public static void main(String[] args) {
    	Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[] intArr = new int[n];
        String[] strArr = new String[n];
        for(int i = 0; i < n; i++){
            intArr[i] = in.nextInt();
            strArr[i] = in.next();
        }
        in.close();
        
        String[] result = countingFullSort(intArr, strArr);
        StringBuilder sb = new StringBuilder(result.length * 2);
        for (int i = 0; i < result.length; i++) {
        	sb.append(result[i]);
        	if (i != result.length - 1)
        		sb.append(" ");
        }
        System.out.println(sb.toString());
    }
}