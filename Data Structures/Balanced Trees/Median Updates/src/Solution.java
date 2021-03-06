import java.text.DecimalFormat;
import java.util.*;


public class Solution {

    //stores all the numbers less than the current median in a maxheap, i.e median is the maximum, at the root
    private PriorityQueue<Integer> maxheap;
    //stores all the numbers greater than the current median in a minheap, i.e median is the minimum, at the root
    private PriorityQueue<Integer> minheap;

    //comparators for PriorityQueue
    private static final maxHeapComparator myMaxHeapComparator = new maxHeapComparator();
    private static final minHeapComparator myMinHeapComparator = new minHeapComparator();

    /**
     * Comparator for the minHeap, smallest number has the highest priority, natural ordering
     */
    private static class minHeapComparator implements Comparator<Integer>{
        @Override
        public int compare(Integer i, Integer j) {
            return i > j ? 1 : i == j ? 0 : -1;
        }
    }

    /**
     * Comparator for the maxHeap, largest number has the highest priority
     */
    private static  class maxHeapComparator implements Comparator<Integer>{
        // opposite to minHeapComparator, invert the return values
        @Override
        public int compare(Integer i, Integer j) {
            return i > j ? -1 : i == j ? 0 : 1;
        }
    }

    /**
     * Constructor for a MedianHeap, to dynamically generate median.
     */
    public Solution() {
        // initialize maxheap and minheap with appropriate comparators
        maxheap = new PriorityQueue<Integer>(11, myMaxHeapComparator);
        minheap = new PriorityQueue<Integer>(11, myMinHeapComparator);
    }

    /**
     * Returns empty if no median i.e, no input
     * @return
     */
    private boolean isEmpty(){
        return maxheap.size() == 0 && minheap.size() == 0 ;
    }

    /**
     * Inserts into MedianHeap to update the median accordingly
     * @param n
     */
    public void insert(int n) {
        // initialize if empty
        if (isEmpty())
        	minheap.add(n);
        else {
            //add to the appropriate heap
            // if n is less than or equal to current median, add to maxheap
            if (Double.compare(n, median()) <= 0)
            	maxheap.add(n);
            else	// if n is greater than current median, add to min heap
            	minheap.add(n);
        }
        // fix the chaos, if any imbalance occurs in the heap sizes
        // i.e, absolute difference of sizes is greater than one.
        fixChaos();
    }
    
    public boolean remove(int n) {
        // return if empty
        if (isEmpty()) 
        	return false;
        
        boolean success;
        int cmp = Double.compare(n, median());
        // remove from appropriate heap
        // if n is less than current median, remove from maxheap
        if (cmp < 0) {
        	success = maxheap.remove(n);
        } else if (cmp > 0) {	// if n is greater than current median, remove from min heap
        	success = minheap.remove(n);
        } else {	// if n is equal to current median, remove from heap with bigger size
        	if (minheap.size() > maxheap.size())
        		success = minheap.remove(n);
        	else 
        		success = maxheap.remove(n);
        }
        
        // fix the chaos, if any imbalance occurs in the heap sizes
        //i.e, absolute difference of sizes is greater than one.
        if (success)
        	fixChaos();
        
        return success;
    }

    /**
     * Re-balances the heap sizes
     */
    private void fixChaos(){
        //if sizes of heaps differ by 2, then it's a chaos, since median must be the middle element
        if (Math.abs(maxheap.size() - minheap.size()) > 1) {
            //check which one is the culprit and take action by kicking out the root from culprit into victim
            if(maxheap.size() > minheap.size())
                minheap.add(maxheap.poll());
            else 
            	maxheap.add(minheap.poll());
        }
    }
    
    /**
     * returns the median of the numbers encountered so far
     * @return
     */
    public double median(){
        //if total size(no. of elements entered) is even, then median iss the average of the 2 middle elements
        //i.e, average of the root's of the heaps.
        if (maxheap.size() == minheap.size()) {
            return ((double)maxheap.peek() + (double)minheap.peek()) / 2 ;
    	} else if (maxheap.size() > minheap.size()) {	//else median is middle element, i.e, root of the heap with one element more
        	return (double)maxheap.peek();
        } else {
        	return (double)minheap.peek();
        }
    }
    

    public static void main(String[] args) {
    	Solution medianHeap = new Solution();
    	DecimalFormat df = new DecimalFormat("#.#");
    	
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        for(int i=0; i < n; i++) {
        	char ch = in.next().charAt(0);
            int x = in.nextInt();
            switch (ch) {
            case 'a':
            	medianHeap.insert(x);
            	System.out.println(df.format(medianHeap.median()));
            	break;
            case 'r':
            	if (medianHeap.remove(x) && !medianHeap.isEmpty())
            		System.out.println(df.format(medianHeap.median()));
            	else
            		System.out.println("Wrong!");
            	break;
            }
        }
        in.close();
    }
}