import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

enum Color {
    RED, GREEN
}

abstract class Tree {

    private int value;
    private Color color;
    private int depth;

    public Tree(int value, Color color, int depth) {
        this.value = value;
        this.color = color;
        this.depth = depth;
    }

    public int getValue() {
        return value;
    }

    public Color getColor() {
        return color;
    }

    public int getDepth() {
        return depth;
    }

    public abstract void accept(TreeVis visitor);
}

class TreeNode extends Tree {

    private ArrayList<Tree> children = new ArrayList<>();

    public TreeNode(int value, Color color, int depth) {
        super(value, color, depth);
    }

    public void accept(TreeVis visitor) {
        visitor.visitNode(this);

        for (Tree child : children) {
            child.accept(visitor);
        }
    }

    public void addChild(Tree child) {
        children.add(child);
    }
}

class TreeLeaf extends Tree {

    public TreeLeaf(int value, Color color, int depth) {
        super(value, color, depth);
    }

    public void accept(TreeVis visitor) {
        visitor.visitLeaf(this);
    }
}

abstract class TreeVis
{
    public abstract int getResult();
    public abstract void visitNode(TreeNode node);
    public abstract void visitLeaf(TreeLeaf leaf);

}


class SumInLeavesVisitor extends TreeVis {
	private int sum = 0;
	
    public int getResult() {
        return sum;
    }

    public void visitNode(TreeNode node) {
    }

    public void visitLeaf(TreeLeaf leaf) {
      	sum += leaf.getValue();
    }
}

class ProductOfRedNodesVisitor extends TreeVis {
	private static final int MOD = 1000000007;
	int prod = 1;
	
    public int getResult() {
        return prod;
    }

    public void visitNode(TreeNode node) {
    	calcProd(node);
    }

    public void visitLeaf(TreeLeaf leaf) {
    	calcProd(leaf);
    }
    
    private void calcProd(Tree tree) { 
    	if (tree.getColor() == Color.RED)
    		prod = (int) ((long) prod * tree.getValue() % MOD);
    }
}

class FancyVisitor extends TreeVis {
	private int sum1 = 0;	// the sum of values stored in the tree's non-leaf nodes at even depth
	private int sum2 = 0;	// the sum of values stored in the tree's green leaf nodes
	
    public int getResult() {
        return Math.abs(sum1 - sum2);
    }

    public void visitNode(TreeNode node) {
    	if (node.getDepth() % 2 == 0)
    		sum1 += node.getValue();
    }

    public void visitLeaf(TreeLeaf leaf) {
    	if (leaf.getColor() == Color.GREEN)
    		sum2 += leaf.getValue();
    }
}

public class Solution {
  
	/**
	 * 5 key arrays here (each one is indexed 0 to n-1):
	 * <p>
	 * int[] xArray - all Xi values <p>
	 * Color[] colorArray - all Color values <p>
	 * List<Integer>[] connectionsArray - each item is a list of indexes this node is connected to <p>
	 * List<Integer>[] childrenArray - each item is a list of indexes of children of this node <p>
	 * int[] depthArray - depth of each node <p>
	 * Tree[] nodeArray - all nodes of the tree <p>
	 * 
	 * @return
	 */
    public static Tree solve() {
        //read the tree from STDIN and return its root as a return value of this function
    	Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[] xArray = new int[n];
        Color[] colorArray = new Color[n];
        for (int i = 0; i < n; i++)
        	xArray[i] = in.nextInt();
        for (int i = 0; i < n; i++) {
        	switch (in.nextInt()) {
        		case 0: colorArray[i] = Color.RED;
        				break;
        		case 1: colorArray[i] = Color.GREEN;	
        				break;
        	}
        }
        
        @SuppressWarnings("unchecked")
		List<Integer>[] connectionsArray = new List[n];
        for (int i = 0; i < n; i++)
        	connectionsArray[i] = new ArrayList<Integer>();
        for (int i = 0; i < n-1; i++) {
        	int u = in.nextInt();
        	int v = in.nextInt();
        	connectionsArray[u - 1].add(v - 1); 
        	connectionsArray[v - 1].add(u - 1); 
        }
        in.close();
        
        @SuppressWarnings("unchecked")
		List<Integer>[] childrenArray = new List[n];
        for (int i = 0; i < n; i++)
        	childrenArray[i] = new ArrayList<Integer>();
        
        boolean[] visitedArray = new boolean[n];
        int[] depthArray = new int[n];
        depthArray[0] = 0;
        
        Queue<Integer> visitQueue = new LinkedList<>();
        visitQueue.add(0);
        while (!visitQueue.isEmpty()) {
        	int pos = visitQueue.remove();
        	if (!visitedArray[pos]) {
        		visitedArray[pos] = true;
        		for (int connectedNode : connectionsArray[pos])
        			if (!visitedArray[connectedNode]) {
        				childrenArray[pos].add(connectedNode);
        				depthArray[connectedNode] = depthArray[pos] + 1; 
        				visitQueue.add(connectedNode);
        			}
        	}
        }
        
        Tree[] nodeArray = new Tree[n];
        for (int i = 0; i < n; i++) {
        	if (childrenArray[i].isEmpty())
        		nodeArray[i] = new TreeLeaf(xArray[i], colorArray[i], depthArray[i]);
        	else 
        		nodeArray[i] = new TreeNode(xArray[i], colorArray[i], depthArray[i]);
        }
        
        for (int i = 0; i < n; i++) {
        	for (Integer childIndex : childrenArray[i])
        		((TreeNode)nodeArray[i]).addChild(nodeArray[childIndex]);
        }
        
        return nodeArray[0];
    }   

    public static void main(String[] args) {
      	Tree root = solve();
		SumInLeavesVisitor vis1 = new SumInLeavesVisitor();
      	ProductOfRedNodesVisitor vis2 = new ProductOfRedNodesVisitor();
      	FancyVisitor vis3 = new FancyVisitor();

      	root.accept(vis1);
      	root.accept(vis2);
      	root.accept(vis3);

      	int res1 = vis1.getResult();
      	int res2 = vis2.getResult();
      	int res3 = vis3.getResult();

      	System.out.println(res1);
     	System.out.println(res2);
    	System.out.println(res3);
	}
}