/*
 * The code in this file is messy but that's partly because of the need for performance
 */

import java.io.IOException;
import java.util.*;

interface TreeVis<T> {
	void visitTreeNode(TreeNode<T> treeNode);
}

/**
 * This class will create a binary search tree (left < parent < right for each node, no duplicates values)
 * from a range of integers.
 * If there are only 2 values in a range, then only root and left leaf are created  
 */
class TreeNode<T> {
	private final int from, to;
	private final int data;
	private final TreeNode<T> left, right;
	private T obj;
	
	/**
	 * @param from inclusive
	 * @param to exclusive
	 */
	public TreeNode(int from, int to) {
		if (to - from < 1)
			throw new IllegalArgumentException();
		
		this.from = from;
		this.to = to;
		data = (from + to) / 2;
		
		left = (data - from >= 1) ? new TreeNode<T>(from, data) : null;  
		right = (to - data > 1) ? new TreeNode<T>(data + 1, to) : null;
	}
	
	public TreeNode<T> find(int value) {
		if (data == value) {
			return this;
		} else if (value < data) {
			return (left == null) ? null : left.find(value);
		} else {
			return (right == null) ? null : right.find(value);
		}
	}
	
	/** 
	 * root, then left, then right
	 * 
	 * @param visitor
	 */
	public void traversePreOrder(TreeVis<T> visitor) {
		visitor.visitTreeNode(this);
		if (left != null)
			left.traversePreOrder(visitor);
		if (right != null)
			right.traversePreOrder(visitor);
	}
	
	public int getFrom() {
		return from;
	}

	public int getTo() {
		return to;
	}

	public int getData() {
		return data;
	}

	public TreeNode<T> getLeft() {
		return left;
	}

	public TreeNode<T> getRight() {
		return right;
	}

	public T getObj() {
		return obj;
	}

	public void setObj(T obj) {
		this.obj = obj;
	}

	public static <T> TreeNode<T> lowestCommonAncestor(TreeNode<T> root, TreeNode<T> n1, TreeNode<T> n2) {
		Objects.requireNonNull(root);
		Objects.requireNonNull(n1);
		Objects.requireNonNull(n2);
		if (root.data > Math.max(n1.data, n2.data)) {
			return lowestCommonAncestor(root.left, n1, n2);
		} else if (root.data < Math.min(n1.data, n2.data)) {
			return lowestCommonAncestor(root.right, n1, n2);
		} else {
			return root; 
		}
	}
}

class Stats {
	Node startNode, targetNode;
	int nodesTraveled;
	int shortcutCycles;
	int adjCycles;
	long microsShortcutCycles;
	long microsAdjCycles;
	
	public Stats(Node startNode, Node targetNode) {
		this.startNode = startNode;
		this.targetNode = targetNode;
	}
	
	@Override
	public String toString() {
		return "Stats [startNode=" + startNode + ", targetNode=" + targetNode + ", nodesTraveled=" + nodesTraveled
				+ ", shortcutCycles=" + shortcutCycles + ", adjCycles=" + adjCycles + ", microsShortcutCycles="
				+ microsShortcutCycles + ", microsAdjCycles=" + microsAdjCycles + "]";
	}
}

class Node {
	public Stats stats;
	
	public final int id;
	public final int row;
	public final int column;
	public final int value;

	private Node[] adjacents;
	private Map<Node, Shortcut> shortcuts = new HashMap<>();

	private boolean visited = false;
	private int distance = Integer.MAX_VALUE;	// distance from the starting node
	
	public Node(int id, int row, int column, int value) {
		this.id = id;
		this.row = row;
		this.column = column;
		this.value = value;
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
	
	public Node[] getAdjacents() {
		return adjacents;
	}
	
	public void setAdjacents(Node[] adjacents) {
		this.adjacents = Objects.requireNonNull(adjacents);
	}
	
	public Collection<Shortcut> getShortcuts() {
		return shortcuts.values();
	}
	
	public Shortcut getShortcut(Node node) {
		return shortcuts.get(node);
	}
	
	public void addShortcut(Shortcut shortcut) {
		shortcuts.put(shortcut.node, shortcut);
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
		return "Node [id=" + id + ", row=" + row + ", column=" + column + ", value=" + value + ", distance=" + distance + "]";
	}
}

class Shortcut {
	public final Node node;
	public final int distance;	// pay attention whether you include originating node's value into here
	
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

interface SearchFilter {

	boolean isVisitAllowed(Node from, Node to);
	
	void resetDistances();
	
	boolean isInRange(Node node);
	
	List<Shortcut> getShortcuts(Node node);
}

class Graph implements TreeVis<Node[]> {
	private final int rows;
	private final int columns;
	private final Node[] nodes;
	private final TreeNode<Node[]> rootBar;
	
	/**
	 * This filter will limit search area when searching with searchDijkstra()
	 * allow search from column from-1 to column to+1
	 * that's because bordering columns from the left and from the right have shortcuts to itself 
	 * from - inclusive, to - exclusive
	 */
	private class SearchFilterBar implements SearchFilter {
		private final int allowFrom, allowTo;
		public final int from, to;
	
		// from inclusive, to exclusive
		public SearchFilterBar(int from, int to) {
			this.from = from;
			this.to = to;
			this.allowFrom = from == 0 ? from : from - 1 ;
			this.allowTo = to == columns ? to : to + 1;
		}
		
		@Override
		public boolean isVisitAllowed(Node from, Node to) {
			return to.column >= allowFrom && to.column < allowTo; 
		}
		
		@Override
		public void resetDistances() {
			for (int r = 0; r < rows; r++) {
				for (int c = allowFrom; c < allowTo; c++) {
					int index = rcToIndex(r, c);
					Node n = nodes[index];
					n.setDistance(Integer.MAX_VALUE);
					n.setVisited(false);
				}
			}
		}
		
		// 1 column to the left and 1 column to the right of from-to zone will contain shortcuts to itself 
		@Override
		public List<Shortcut> getShortcuts(Node node) {
			int column = node.column; 
			if (column == from - 1 || column == to) {
				List<Shortcut> list = new ArrayList<>(rows);
				for (int i = 0; i < rows; i++) {
					if (i != node.row) {	// node does not have shortcut to itself
						int index = rcToIndex(i, column);
						list.add(node.getShortcut(nodes[index]));
					}
				}
				return list;
			} else 
				return null;
		}
		
		@Override
		public boolean isInRange(Node node) {
			return node.column >= from && node.column < to;
		}
	}
	
	public Node getNode(int id) {
		return nodes[id];
	}
	
	public Node[] getNodes() {
		return nodes;
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
		return destNode;
	}

	/**
	 * 
	 * @param arr		indexing starts with 0 and goes from top left corner to the right, should be a representation of a "rows X columns" array  
	 * @param rows
	 * @param columns
	 */
	public Graph(int[] arr, final int rows, final int columns) {
		if (arr.length != rows * columns)
			throw new IllegalArgumentException();
		this.rows = rows;
		this.columns = columns;
		
		// create nodes
		nodes = new Node[arr.length];
        for (int i = 0; i < arr.length; i++) {
        	int value = arr[i];
        	int r = i / columns;
        	int c = i % columns;
        	//int c = i - r * columns;
        	Node node = new Node(i, r, c, value);
        	nodes[i] = node;
        }
        
        // add adjacents
        for (Node node : nodes) {
        	int r = node.row;
        	int c = node.column;
        	int cnt = 0;
        	Node n1 = getAdjacent(r - 1, c);
        	if (n1 != null) cnt++;
        	Node n2 = getAdjacent(r + 1, c);
        	if (n2 != null) cnt++;
        	Node n3 = getAdjacent(r, c - 1);
        	if (n3 != null) cnt++;
        	Node n4 = getAdjacent(r, c + 1);
        	if (n4 != null) cnt++;
        	
        	Node[] adjacents = new Node[cnt];
        	int i = 0;
        	if (n1 != null) adjacents[i++] = n1;
        	if (n2 != null) adjacents[i++] = n2;
        	if (n3 != null) adjacents[i++] = n3;
        	if (n4 != null) adjacents[i++] = n4;
        	node.setAdjacents(adjacents);
        }
        
        // this will create binary search tree, each TreeNode.data is equal to index of column that splits zone from-to in 2 halfs  
        rootBar = new TreeNode<>(0, this.columns);
        // this will traverse tree and run searchDijkstra() for each zone
        // so that the vertical bar that splits will contain shortcuts to every node of the the zone it splits  
        rootBar.traversePreOrder(this);
	}
	
	@Override
	public void visitTreeNode(TreeNode<Node[]> treeNode) {
		int columnFrom = treeNode.getFrom();
		int columnTo = treeNode.getTo();
		SearchFilter sfBar = new SearchFilterBar(columnFrom, columnTo);
		Node[] barNodes = new Node[rows];
		int column = treeNode.getData();
		// for each node of the bar, find shortest path to each other node in the zone
		for (int i = 0; i < rows; i++) {
			int index = rcToIndex(i, column);
			Node startNode = nodes[index];
			// add shortcuts to startNode going to all other nodes in the area limited by sfBar
			searchDijkstra(startNode, null, sfBar);
			barNodes[i] = startNode; 
		}
		treeNode.setObj(barNodes);
	}
	
	public int searchMinPath(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
		int sourceIndex = rcToIndex(sourceRow, sourceColumn);
		int targetIndex = rcToIndex(targetRow, targetColumn);
		if (sourceIndex < 0 || sourceIndex >= nodes.length || targetIndex < 0 || targetIndex >= nodes.length)
			throw new IllegalArgumentException();
		Node sourceNode = nodes[sourceIndex];
		Node targetNode = nodes[targetIndex];
		
		if (sourceNode.equals(targetNode)) 
			return sourceNode.value;
		
		TreeNode<Node[]> sourceBar = rootBar.find(sourceColumn);
		TreeNode<Node[]> targetBar = rootBar.find(targetColumn);
		TreeNode<Node[]> tnBar = TreeNode.<Node[]>lowestCommonAncestor(rootBar, sourceBar, targetBar);
		Node[] barNodes = tnBar.getObj();
		int minDistance = Integer.MAX_VALUE;
		if (tnBar.getData() == targetColumn || tnBar.getData() == sourceColumn) {	// if the source or target is on the bar
			Node n1; 
			Node n2;
			if (tnBar.getData() == sourceColumn) {
				n1 = sourceNode;
				n2 = targetNode;
			} else {
				n1 = targetNode;
				n2 = sourceNode;
			}
			minDistance = n1.getShortcut(n2).distance;
		} else {	// select shortcuts from every node of the splitting bar to source and target, sum then and select min(sum)
			for (Node n : barNodes) {
				Shortcut sc1 = n.getShortcut(sourceNode);
				Shortcut sc2 = n.getShortcut(targetNode);
				int distance = sc1.distance + sc2.distance - n.value;	// -value because both shortcuts include it
				minDistance = Math.min(distance, minDistance);
			}
		}

		targetNode.setDistance(minDistance);
		return minDistance; 
	}

	/**
	 * searchDijkstra() helper 
	 */
	private void enqueueChild(Node parent, Node child, int newChildDistance, PriorityQueue<Shortcut> nextToVisit, SearchFilter searchFilter) {
		if (searchFilter.isVisitAllowed(parent, child)) {
			if (!child.isVisited()) {	 
				if (newChildDistance < child.getDistance()) {
					child.setDistance(newChildDistance);
					nextToVisit.add(new Shortcut(child, newChildDistance));
				}
			}
		}
	}
	
	/**
	 * In scanMode will visit all nodes and set distance of each node to min path from starting node
	 * To activate scanMode pass null as targetNode
	 * 
	 * @param startNode a node where to start search from
	 * @param targetNode if this is null, then it will traverse all nodes and startNode will have shortcuts set to every one of them (scanMode).
	 * 			If this is not null then shortest path to only one node it searched for.
	 * @param searchFilter use this filter to limit search area
	 */
	public void searchDijkstra(Node startNode, Node targetNode, SearchFilter searchFilter) {
		searchFilter.resetDistances();
		boolean scanMode = targetNode == null;
		
		/*
		final int r = targetNode.row;
		final int c = targetNode.column;
		Comparator<Shortcut> byDistance = Comparator.comparingInt(Shortcut::getDistance);
		Comparator<Shortcut> byProximity = (s1, s2) -> {
			int proximity1 = Math.abs(c - s1.node.column) + Math.abs(r - s1.node.row);
			int proximity2 = Math.abs(c - s2.node.column) + Math.abs(r - s2.node.row);
			return proximity1 - proximity2;
		};
		Comparator<Shortcut> myComparator = byDistance.thenComparing(byProximity);
		*/
		Comparator<Shortcut> byDistance = Comparator.comparingInt(Shortcut::getDistance);
		// It is not recommended to change fields by which PriorityQueue sorts after it was added to queue
		// that's why using PriorityQueue<Shortcut> instead of PriorityQueue<Node>
		// (node.distance will be changed)
		PriorityQueue<Shortcut> nextToVisit = new PriorityQueue<>(byDistance);
		
		nextToVisit.add(new Shortcut(startNode, startNode.value));
		startNode.setDistance(startNode.value);
		
		//Stats stats = new Stats(startNode, targetNode);
		//long startTime;
		
		while (!nextToVisit.isEmpty()) {
			// find node in nextToVisit with the lowest distance
			Node node = nextToVisit.poll().node;
			
			if (node.isVisited()) 
				continue;
				
			node.setVisited(true);
			
			//stats.nodesTraveled++;

			int distance = node.getDistance();	// distance from the starting node
			
			// isInRange() check is used not to add shortcuts to 2 bordering bar columns: left and right 
			if (scanMode) {
				if (searchFilter.isInRange(node) && node != startNode)
					startNode.addShortcut(new Shortcut(node, distance));
			} else if (node.id == targetNode.id) {
				//targetNode.stats = stats;
				return;
			}
			
			// queue shortcuts
			//startTime = System.nanoTime();
			List<Shortcut> shortcuts = searchFilter.getShortcuts(node);
			//Set<Shortcut> shortcuts = node.getShortcuts();
			if (shortcuts != null)
				for (Shortcut shortcut : shortcuts) {
					//stats.shortcutCycles++;
					Node child = shortcut.node;
					// need to subtract node.value because shortcut.distance already includes it  
					int newChildDistance = distance + shortcut.distance - node.value;
					enqueueChild(node, child, newChildDistance, nextToVisit, searchFilter);
				}
			//stats.microsShortcutCycles += TimeUnit.MICROSECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
			
			//startTime = System.nanoTime();
			// queue adjacents
			for (Node child : node.getAdjacents()) {
				//stats.adjCycles++;
				int newChildDistance = distance + child.value;
				enqueueChild(node, child, newChildDistance, nextToVisit, searchFilter);
			}
			//stats.microsAdjCycles += TimeUnit.MICROSECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
		}
	}
}

public class Solution {
    
    public static void main(String[] args) throws IOException {
    	Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();
        int[] arr = new int[n * m];
        for (int i = 0; i < n * m; i++)
        	arr[i] = in.nextInt();
        
    	//long startTime = System.nanoTime(); 
    	Graph graph = new Graph(arr, n, m);
    	//long estimatedTime = System.nanoTime() - startTime;
        //System.out.println("Graph creation: " + TimeUnit.MILLISECONDS.convert(estimatedTime, TimeUnit.NANOSECONDS));
        
		//BufferedWriter out = new BufferedWriter(new FileWriter(new File("aaaa.txt")));
		//BufferedReader eo = new BufferedReader(new FileReader(new File("expectedOutput.txt")));
		
		//startTime = System.nanoTime();
		//List<Long> execTimes = new LinkedList<>();
		//List<Stats> statList = new LinkedList<>();

		int q = in.nextInt();
    	for (int i = 0; i < q; i++) {
            int r1 = in.nextInt();
            int c1 = in.nextInt();
            int r2 = in.nextInt();
            int c2 = in.nextInt();
            
        	//long taskStartTime = System.nanoTime(); 
        	int minDistance = graph.searchMinPath(r1, c1, r2, c2);
            //long taskExecTime = TimeUnit.MICROSECONDS.convert(System.nanoTime() - taskStartTime, TimeUnit.NANOSECONDS);
        	//execTimes.add(taskExecTime);
        	//statList.add(targetNode.stats);

        	/*
        	String es = eo.readLine();
        	if (!es.equals(String.valueOf(minDistance)) ) {
        		System.out.println(graph.getNode(graph.rcToIndex(r1,  c1)));
        		System.out.println(graph.getNode(graph.rcToIndex(r2,  c2)));
        		System.out.println("actual="+minDistance);
        		System.out.println("expected="+es);
        		System.out.println("-------------");
        	}
        	out.write(String.valueOf(minDistance));
    		out.newLine();
        	*/
        	
    		System.out.println(minDistance);
        }
    	in.close();
    	
    	/*
		out.close();
        eo.close();
        
        estimatedTime = TimeUnit.SECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
        double avgTime = execTimes.stream()
        		.mapToLong(Long::longValue)
        		.average()
        		.getAsDouble();
        long totalTime = execTimes.stream()
        		.mapToLong(t -> TimeUnit.MICROSECONDS.convert(t, TimeUnit.NANOSECONDS))
        		.sum();
        System.out.println("Avg query time (micros): " + avgTime);
        System.out.println("Total query time (s): " + TimeUnit.SECONDS.convert(totalTime, TimeUnit.MICROSECONDS));
        System.out.println("Total time (s): " + estimatedTime);
        
        System.out.println("-------");
        System.out.println("Avg nodes traveled: " + (int)statList.stream().mapToInt(s -> s.nodesTraveled).average().getAsDouble());
        System.out.println("Max nodes traveled: " + statList.stream().mapToInt(s -> s.nodesTraveled).max().getAsInt());
        System.out.println("Min nodes traveled: " + statList.stream().mapToInt(s -> s.nodesTraveled).min().getAsInt());
        System.out.println("-------");
        System.out.println("Avg shortcut cycles: " + (int)statList.stream().mapToInt(s -> s.shortcutCycles).average().getAsDouble());
        System.out.println("Max shortcut cycles: " + statList.stream().mapToInt(s -> s.shortcutCycles).max().getAsInt());
        System.out.println("Min shortcut cycles: " + statList.stream().mapToInt(s -> s.shortcutCycles).min().getAsInt());
        System.out.println("-------");
        System.out.println("Avg adj cycles: " + (int)statList.stream().mapToInt(s -> s.adjCycles).average().getAsDouble());
        System.out.println("Max adj cycles: " + statList.stream().mapToInt(s -> s.adjCycles).max().getAsInt());
        System.out.println("Min adj cycles: " + statList.stream().mapToInt(s -> s.adjCycles).min().getAsInt());
        System.out.println("-------");
        System.out.println("Avg microsecs spent on shortcut cycles: " + (long)statList.stream().mapToLong(s -> s.microsShortcutCycles).average().getAsDouble());
        System.out.println("Max microsecs spent on shortcut cycles: " + statList.stream().mapToLong(s -> s.microsShortcutCycles).max().getAsLong());
        System.out.println("Min microsecs spent on shortcut cycles: " + statList.stream().mapToLong(s -> s.microsShortcutCycles).min().getAsLong());
        System.out.println("-------");
        System.out.println("Avg microsecs spent on adj cycles: " + (long)statList.stream().mapToLong(s -> s.microsAdjCycles).average().getAsDouble());
        System.out.println("Max microsecs spent on adj cycles: " + statList.stream().mapToLong(s -> s.microsAdjCycles).max().getAsLong());
        System.out.println("Min microsecs spent on adj cycles: " + statList.stream().mapToLong(s -> s.microsAdjCycles).min().getAsLong());
        */
    }
}