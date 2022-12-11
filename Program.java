import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Program {
	//https://open.kattis.com/problems/abandonedanimal
	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		
		int numStores = sc.nextInt();
		sc.nextLine();
		
		int numItems = sc.nextInt();
		sc.nextLine();
		
		HashMap<String, ArrayList<Integer>> itemStores = new HashMap<String, ArrayList<Integer>>(numItems);
		for (int i = 0; i < numItems; i++) {
			String[] line = sc.nextLine().split("\\s+");
			if (!itemStores.containsKey(line[1]))
				itemStores.put(line[1], new ArrayList<Integer>());
			itemStores.get(line[1]).add(Integer.parseInt(line[0]));
		}
		
		int numPurchases = sc.nextInt();
		sc.nextLine();
		
		String[] purchases = new String[numPurchases];
		for (int i = 0; i < numPurchases; i++)
			purchases[i] = sc.nextLine();
		
		HashMap<String, int[]> itemPaths = new HashMap<String, int[]>();
		
		String purchase = purchases[purchases.length - 1];
		Collections.sort(itemStores.get(purchase));
		itemPaths.put(purchase, new int[itemStores.get(purchase).size()]);
		int[] paths = itemPaths.get(purchase);
		paths[paths.length - 1] = 1;
		for (int i = 0; i < paths.length - 1; i++)
			paths[i] = 2;
		
		for (int i = purchases.length - 2; i >= 0; i--) {
			purchase = purchases[i];
			Collections.sort(itemStores.get(purchase));
			itemPaths.put(purchase, new int[itemStores.get(purchase).size()]);
			paths = itemPaths.get(purchase);
			ArrayList<Integer> nextPurchaseStores = itemStores.get(purchases[i + 1]);
			ArrayList<Integer> currStores = itemStores.get(purchase);
			int indxOfNext = binSearch(nextPurchaseStores, currStores.get(currStores.size() - 1), nextPurchaseStores.size() - 1);
			paths[paths.length - 1] = indxOfNext < 0 ? 0 : itemPaths.get(purchases[i + 1])[indxOfNext];
			
			for (int j = paths.length - 2; j >= 0; j--) {
				int rightVal = paths[j + 1];
				indxOfNext = binSearch(nextPurchaseStores, currStores.get(j), indxOfNext > -1 ? indxOfNext : nextPurchaseStores.size() - 1);
				int lowVal = indxOfNext < 0 ? 0 : itemPaths.get(purchases[i + 1])[indxOfNext];
				int sum = rightVal + lowVal;
				paths[j] = sum > 2 ? 2 : sum;
			}
		}
		int result = itemPaths.get(purchases[0])[0];
		if (result > 1)
			System.out.println("ambiguous");
		else if (result == 1)
			System.out.println("unique");
		else
			System.out.println("impossible");
		
		sc.close();
	}
	
	// Writing a custom binary search that will return the smallest value >= the requested value
	public static int binSearch(ArrayList<Integer> array, int val, int lastIndex) {
		if (val > array.get(lastIndex))
			return -1;
		
		int start = 0;
		int end = lastIndex;
		int mid = 0;
		
		while (end >= start) {
			mid = (end - start) / 2 + start;
			if (array.get(mid) >= val && (mid == 0 || array.get(mid - 1) < val))
				return mid;
			
			if (array.get(mid) > val)
				end = mid - 1;
			else
				start = mid + 1;
		}
		
		if (array.get(mid) < val)
			return mid + 1;
		return mid;
	}

}
