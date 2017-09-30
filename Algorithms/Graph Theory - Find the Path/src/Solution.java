import java.util.*;

class Node implements Comparable<Node> {
	private int id;
	private int value;
	private int depth = -1;	// depth from the starting node
	private Node prevNode;	// previous node in the shortest (min weight) path from the starting node. Starting node will always have this at null
	private Set<Node> adjacents = new HashSet<>();
	
	public Node(int id, int value) {
		this.id = id;
		this.value = value;
	}
	
	public void addAdjacent(Node node) {
		Objects.requireNonNull(node);
		if (node.id != id) 
			adjacents.add(node);
	}
	
	public int getId() {
		return id;
	}
	
	public int getValue() {
		return value;
	}
	
	public int getDepth() {
		return depth;
	}
	
	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	public Node getPrevNode() {
		return prevNode;
	}

	public void setPrevNode(Node prevNode) {
		this.prevNode = prevNode;
	}

	public Set<Node> getAdjacents() {
		//return Collections.unmodifiableList(adjacents);
		return adjacents;
	}
	
	@Override
	public int compareTo(Node node) {
		return id - node.id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (id != other.id)
			return false;
		return true;
	}
}

class Graph {
	private Map<Integer, Node> nodeLookup = new HashMap<>();
	
	public Node getNode(Integer id) {
		return nodeLookup.get(id);
	}
	
	public void addNode(int id, int value) {
		nodeLookup.put(id, new Node(id, value));
	}
	
	public void addEdge(int source, int dest) {
		Node s = getNode(source);
		Node d = getNode(dest);
		if (s != null && d != null)
			s.addAdjacent(d);
	}
	
	public Collection<Node> getNodes() {
		return Collections.unmodifiableCollection(nodeLookup.values());
	}
	
	public Set<Integer> getIds() {
		return Collections.unmodifiableSet(nodeLookup.keySet());
	}
	
	public void clearDepths() {
		nodeLookup.values().stream()
			.forEach(node -> node.setDepth(-1));
	}
	
	/**
	 * Will visit all nodes and set depth of each node to min path from starting node
	 * @param firstId of first node to visit
	 */
	public void visitRegion(Integer firstId) {
		clearDepths();
		Node firstNode = getNode(firstId);
		if (firstNode == null)
			return;
		Set<Node> nextToVisit = new LinkedHashSet<>();
		nextToVisit.add(firstNode);
		firstNode.setDepth(firstNode.getValue());
		while (!nextToVisit.isEmpty()) {
			Iterator<Node> iterator = nextToVisit.iterator(); 
			Node node = iterator.next(); 
			iterator.remove();
			
			int depth = node.getDepth();	// depth from the starting node
			for (Node child : node.getAdjacents()) { 	// depth=-1 means node was not yet visited, otherwise it was already visited
				int childDepth = child.getDepth();
				int newChildDepth = depth + child.getValue();
				if (childDepth == -1 || newChildDepth < childDepth) {
					child.setDepth(newChildDepth);
					nextToVisit.add(child);
				}
			}
		}		
	}
}

public class Solution {
	private static int n;
    private static int m;
    
    /**
     * Converts row and column to Index
     */
    private static Integer rcToIndex(int row, int column) {
    	return new Integer(row * m + column);
    }
    
	private static void checkAndAddAdjacent(Node node, Graph graph, int r, int c) {
		if (r < 0 || c < 0 || c > (m - 1) || r > (n - 1))	// out of bounds
			return;
		// convert r and c to index
		Integer index = rcToIndex(r, c);
		Node destNode = graph.getNode(index);
		node.addAdjacent(destNode);
	}
	
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        n = in.nextInt();
        m = in.nextInt();
        Graph graph = new Graph();
        for (int i = 0; i < n * m; i++)
        	graph.addNode(i, in.nextInt());
        
        // add adjacents
        for (Node node : graph.getNodes()) {
        	// convert index to i and j
        	int r = node.getId() / m;
        	int c = node.getId() - r * m;
        	checkAndAddAdjacent(node, graph, r - 1, c);
        	checkAndAddAdjacent(node, graph, r + 1, c);
        	checkAndAddAdjacent(node, graph, r, c - 1);
        	checkAndAddAdjacent(node, graph, r, c + 1);
        }

        int q = in.nextInt();
        for (int i = 0; i < q; i++) {
            int r1 = in.nextInt();
            int c1 = in.nextInt();
            int r2 = in.nextInt();
            int c2 = in.nextInt();
            
            Integer start = rcToIndex(r1, c1);
            Integer end = rcToIndex(r2, c2);
            graph.visitRegion(start);
            int minDepth = graph.getNode(end).getDepth();
            System.out.println(minDepth);
        }	
        in.close();
    }
}