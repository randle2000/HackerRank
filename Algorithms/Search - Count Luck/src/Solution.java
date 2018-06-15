import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;
import java.util.stream.Collectors;

enum NodeType {
	START('M'), TARGET('*'), FREE('.'), BLOCK('X');
	
	public final char ch;

    private NodeType(char ch) {
        this.ch = ch;
    }

    public static NodeType fromChar(char ch) {
        for (NodeType nt : values())
            if (nt.ch == ch) 
                return nt;
        throw new IllegalArgumentException("Invalid char: " + ch);
    }
}

class Node {
	public final int id;
	public final int row;
	public final int column;
	public final NodeType nodeType;

	private List<Shortcut> shortcuts = new ArrayList<>();

	private boolean visited = false;
	private int distance = Integer.MAX_VALUE;	// distance from the starting node; but in this case it will be the number of decisions
	
	public Node(int id, int row, int column, NodeType nodeType) {
		this.id = id;
		this.row = row;
		this.column = column;
		this.nodeType = nodeType;
	}
	
	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public int getDistance() {
		return distance;
	}
	
	public void setDistance(int distance) {
		this.distance = distance;
	}
	
	public Collection<Shortcut> getShortcuts() {
		return shortcuts;
	}
	
	public void addShortcut(Shortcut shortcut) {
		shortcuts.add(shortcut);
	}
	
	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		return (this == obj);	// for performance. there will be only 1 instance of each node 
	}

	@Override
	public String toString() {
		return "Node [id=" + id + ", row=" + row + ", column=" + column + ", nodeType=" + nodeType + ", distance=" + distance + "]";
	}
}

class Shortcut {
	public final Node node;
	public final int distance;	// pay attention whether you include originating node's value into here; but in this case it will always be 0 = NOT USED
	
	public Shortcut(Node node, int distance) {
		this.node = Objects.requireNonNull(node);
		this.distance = distance;
	}

	public int getDistance() {
		return distance;
	}

	@Override
	public int hashCode() {
		return node.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
		Shortcut other = (Shortcut) obj;
		return node.equals(other.node);
	}
}

class Graph {
	private final int rows;
	private final int columns;
	private final Node[] nodes;
	
	private Node startNode;
	private Node targetNode;
	
	public Node getNode(int id) {
		return nodes[id];
	}
	
	public Node[] getNodes() {
		return nodes;
	}
	
    public Node getStartNode() {
		return startNode;
	}

	public Node getTargetNode() {
		return targetNode;
	}

	/**
     * Converts row and column to Index
     */
    public int rcToIndex(int row, int column) {
    	return row * this.columns + column;
    }
    
	private Node getAdjacent(int r, int c) {
		if (r < 0 || c < 0 || c > (columns - 1) || r > (rows - 1))	// out of bounds
			return null;
		// convert r and c to index
		int index = rcToIndex(r, c);
		Node destNode = nodes[index];
		if (destNode.nodeType == NodeType.BLOCK)	// do not create shortcuts for cells marked with X
			return null;
		return destNode;
	}

	/**
	 * 
	 * @param arr		each String in arr is 1 row. Number of rows equals to number of Strings in arr. Each String must be of the same length
	 */
	public Graph(String[] arr) {
		this.rows = arr.length;
		this.columns = arr[0].length();
		
		// create nodes
		nodes = new Node[rows * columns];
		
        for (int r = 0; r < arr.length; r++) {
        	String s = arr[r];
        	if (s.length() != columns)
        		throw new IllegalArgumentException("Each String must be of the same length");
        	for (int c = 0; c < s.length(); c++) {
        		int id = rcToIndex(r, c);
        		NodeType nodeType = NodeType.fromChar(s.charAt(c));
            	Node node = new Node(id, r, c, nodeType);	// id = index in nodes[]
            	nodes[id] = node;
        		switch (nodeType) {
        		case START:
        			this.startNode = node;
        			break;
        		case TARGET:
        			this.targetNode = node;
        			break;
				default:
					break;
        		}
        	}
        }
        
        // add adjacents
        for (Node node : nodes) {
        	int r = node.row;
        	int c = node.column;
        	Node n;
        	n = getAdjacent(r - 1, c);
        	if (n != null) node.addShortcut(new Shortcut(n, 0));
        	n = getAdjacent(r + 1, c);
        	if (n != null) node.addShortcut(new Shortcut(n, 0));
        	n = getAdjacent(r, c - 1);
        	if (n != null) node.addShortcut(new Shortcut(n, 0));
        	n = getAdjacent(r, c + 1);
        	if (n != null) node.addShortcut(new Shortcut(n, 0));
        }
	}
}

public class Solution {
	
	/**
	 * @return		1 if it is decision node, or 0 otherwise  
	 */
	private static int getDecisionValue(Node node) {
		if (node.nodeType == NodeType.TARGET)
			return 0;
		
		long waysToGoFromThisNode = node.getShortcuts().stream()
			.map(s -> s.node)
			.filter(n -> !n.isVisited())
			.count();
		
		if (waysToGoFromThisNode > 1)
			return 1;
		else
			return 0;
	}
	
	/**
	 * 
	 * @param startNode 	a node where to start search from
	 * @param targetNode 	the shortest path to this node is searched for.
	 */
	public static void searchDijkstra(Node startNode, Node targetNode) {
		// distance of any Shortcut will always be 0 in our case so using PriorityQueue doesn't make much sense
		// just leaving it as is for the sake of integrity of a function
		Comparator<Shortcut> byDistance = Comparator.comparingInt(Shortcut::getDistance);	
		// It is not recommended to change fields by which PriorityQueue sorts after it was added to queue
		// that's why using PriorityQueue<Shortcut> instead of PriorityQueue<Node>
		// (node.distance will be changed)
		PriorityQueue<Shortcut> nextToVisit = new PriorityQueue<>(byDistance);
		
		nextToVisit.add(new Shortcut(startNode, 0));
		startNode.setDistance(getDecisionValue(startNode));
		
		while (!nextToVisit.isEmpty()) {
			// find node in nextToVisit with the lowest distance; in fact distance is always 0 here
			Node node = nextToVisit.poll().node;
			
			if (node.isVisited()) 
				continue;
				
			node.setVisited(true);
			
			int distance = node.getDistance();	// distance from the starting node; i.e. number of decisions from starting nodes
			
			if (node.id == targetNode.id)
				return;
			
			// queue shortcuts
			Collection<Shortcut> shortcuts = node.getShortcuts();
			if (shortcuts != null)
				for (Shortcut shortcut : shortcuts) {
					Node child = shortcut.node;
					// need to subtract node.value because shortcut.distance already includes it  
					int newChildDistance = distance + getDecisionValue(child);
					if (!child.isVisited()) {	 
						if (newChildDistance < child.getDistance()) {
							child.setDistance(newChildDistance);
							nextToVisit.add(new Shortcut(child, 0));
						}
					}
				}
		}
	}
	
	
	// Complete the countLuck function below.
    static String countLuck(String[] matrix, int k) {
    	Graph graph = new Graph(matrix);
    	Node startNode = graph.getStartNode();
    	Node targetNode = graph.getTargetNode();
    	searchDijkstra(startNode, targetNode);
    	int decisionsCount = targetNode.getDistance();
    	
    	return decisionsCount == k ? "Impressed" : "Oops!";
    }
    
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        //BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));

        int t = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        for (int tItr = 0; tItr < t; tItr++) {
            String[] nm = scanner.nextLine().split(" ");

            int n = Integer.parseInt(nm[0]);

            int m = Integer.parseInt(nm[1]);

            String[] matrix = new String[n];

            for (int i = 0; i < n; i++) {
                String matrixItem = scanner.nextLine();
                matrix[i] = matrixItem;
            }

            int k = scanner.nextInt();
           	scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

            String result = countLuck(matrix, k);

            bufferedWriter.write(result);
            bufferedWriter.newLine();
        }

        bufferedWriter.close();

        scanner.close();
    }
}
