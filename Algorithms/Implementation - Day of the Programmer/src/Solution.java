import java.util.*;

public class Solution {
	
	private static final int DAY_OF_YEAR = 256;

    static String solve(int year){
    	GregorianCalendar gc = new GregorianCalendar();
    	
    	if (year < 1918)	// use Julian calendar 
    		gc.setGregorianChange(new Date(Long.MAX_VALUE));
    	else if (year > 1918)	// use Gregorian calendar 
    		gc.setGregorianChange(new Date(Long.MIN_VALUE));
    	else	// year = 1918
    		return "26.09.1918";
    	
        gc.set(GregorianCalendar.YEAR, year);
        gc.set(Calendar.DAY_OF_YEAR, DAY_OF_YEAR);
        
        int _year = gc.get(Calendar.YEAR);
        int month = gc.get(Calendar.MONTH) + 1; 
        int day = gc.get(Calendar.DAY_OF_MONTH);
        String res = String.format("%02d.%02d.%4d", day, month, _year);
        
        return  res;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int year = in.nextInt();
        in.close();
        String result = solve(year);
        System.out.println(result);
    }
}