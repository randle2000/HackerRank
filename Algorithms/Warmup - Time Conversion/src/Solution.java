import java.util.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Solution {

    static String timeConversion(String s) {
    	DateTimeFormatter fmtIn = DateTimeFormatter.ofPattern("hh:mm:ssa"); 
    	DateTimeFormatter fmtOut = DateTimeFormatter.ISO_LOCAL_TIME;
        LocalTime lt = LocalTime.parse(s, fmtIn);
        return lt.format(fmtOut);
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String s = in.next();
        in.close();
        String result = timeConversion(s);
        System.out.println(result);
    }
}