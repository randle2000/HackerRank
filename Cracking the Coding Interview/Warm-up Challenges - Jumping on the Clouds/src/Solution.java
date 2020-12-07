import java.io.*;
import java.util.*;

public class Solution {

    static int jumpsRemaining(int[] arr, int pos) {
        int stepsToEnd = arr.length - 1 - pos;
        if (stepsToEnd == 0) {
            return 0;
        }
        if (stepsToEnd <= 2) {
            return 1;
        }
        int nextPos = arr[pos + 2] == 0 ? pos + 2 : pos + 1;
        return 1 +  jumpsRemaining(arr, nextPos);
    }

    // Complete the jumpingOnClouds function below.
    static int jumpingOnClouds(int[] c) {
        return jumpsRemaining(c, 0);
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int n = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        int[] c = new int[n];

        String[] cItems = scanner.nextLine().split(" ");
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        for (int i = 0; i < n; i++) {
            int cItem = Integer.parseInt(cItems[i]);
            c[i] = cItem;
        }

        int result = jumpingOnClouds(c);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedWriter.close();

        scanner.close();
    }
}