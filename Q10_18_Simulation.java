import java.util.*;
import java.io.*;

public class Q10_18_Simulation {

	public static final long[] factorial = {1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800, 479001600, 6227020800L, 87178291200L};
	
	public static void main(String[] args) throws IOException {
		int[] permutation = {0, 1, 2, 3, 4, 5, 6, 7};
		PrintWriter pw = new PrintWriter("output file path");
		HashSet<ArrayList<Integer>> distinctOutputs = new HashSet<ArrayList<Integer>>();
		for (long i = 1; i <= 40320; i++) { // Means 8! permutations are processed in this case
			if (i % 39916800 == 0) { // Prints out progress check every 11! permutations processed.
				System.out.println(i/39916800);
			}
			int n = permutation.length;
			int m = (n+4)/2;
			int[] temp = permutation;
			for (int j = 0; j < n-m; j++) {
				temp = stackSort(temp);
			}
			
			// Output
			if (satStdCond(temp, m) == false) {
				ArrayList<Integer> tempList = new ArrayList<Integer>(); // for better compatibility with HashSet
				for (int k : temp) {
				    tempList.add(k);
				}
				distinctOutputs.add(tempList);
			}
			permutation = nextPerm(permutation);
		}
		pw.println(distinctOutputs.size());
		for (ArrayList<Integer> i : distinctOutputs) {
			for (Integer j : i) {
				pw.print(j + " ");
			}
			pw.println();
		}
		pw.close();
	}
	
	public static int[] stackSort(int[] perm) {
		int[] ans = new int[perm.length];
		int counter = 0; // Keeps track of output array's index.
		Stack<Integer> s = new Stack<>();
		
		for (int i = 0; i < perm.length; i++) {
			while (!(s.isEmpty()) && s.peek() < perm[i]) {
				ans[counter] = s.pop();
				counter++;
			}
			s.add(perm[i]);
		}
		while (!(s.isEmpty())) {
			ans[counter] = s.pop();
			counter++;
		}
		
		return ans;
	}
	
	public static boolean satStdCond(int[] perm, int m) {
		/** Checks if the permutation satisfies the following:
		 * tl(pi) >= n-m
		 * every descent top of pi is a left-to-right max
		 */
		// Calculating tailLength
		int i = perm.length-1;
		while (i >= 0) {
			if (perm[i] == i) {
				i--;
			} else {
				break;
			}
		}
		int tailLength = perm.length-i-1;
		if (tailLength < perm.length-m) {
			return false;
		}
		
		// Checking descent tops
		int curLRMax = perm[0]-1;
		for (int j = 0; j < perm.length-1; j++) {
			if (perm[j] > perm[j+1] && perm[j] < curLRMax) {
				return false;
			}
			curLRMax = Math.max(curLRMax, perm[j]);
		}
		return true;
	}
	
	public static int[] nextPerm(int[] perm) {
		/** Gives the next permutation in logn log(logn) time (on average).
		 * For example, 0312 goes to 0321 and 3210 goes to 01234.
		 */
		// Calculates the rightmost non-decreasing index. For example, in 502143, it is the index 3.
		int firstIndex = perm.length-2;
		while (firstIndex >= 0 && perm[firstIndex] > perm[firstIndex+1]) {
			firstIndex--;
		}
		// If this index is -1, move to the next length.
		if (firstIndex == -1) {
			int[] ans = new int[perm.length+1];
			for (int i = 0; i < perm.length+1; i++) {
				ans[i] = i;
			}
			return ans;
		}
		// Otherwise, 054321 -> 102345 for instance. Cycles too complex, so we will use storage.
		// 1 2 0 -> 2 0 1
		int[] storage = new int[perm.length-firstIndex];
		for (int i = 0; i < storage.length; i++) {
			storage[i] = perm[i+firstIndex];
		}
		int prevLeftEntry = storage[0];
		Arrays.sort(storage);
		int counter = firstIndex+1;
		for (int i = 0; i < storage.length; i++) {
			if (storage[i] > prevLeftEntry) {
				perm[firstIndex] = storage[i];
				prevLeftEntry = Integer.MAX_VALUE;
			} else {
				perm[counter] = storage[i];
				counter++;
			}
		}
		
		return perm;
	}
}
