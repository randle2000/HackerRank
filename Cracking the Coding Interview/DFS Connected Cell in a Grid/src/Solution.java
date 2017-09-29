import java.util.*;

class Node {
	private int id;
	private List<Node> adjacents = new LinkedList<>();
	
	public Node(int id) {
		this.id = id;
	}
	
	public void addAdjacent(Node node) {
		Objects.requireNonNull(node);
		adjacents.add(node);
	}
	
	public int getId(){
		return id;
	}
	
	public List<Node> getAdjacents() {
		//return Collections.unmodifiableList(adjacents);
		return adjacents;
	}
}

class Graph {
	private Map<Integer, Node> nodeLookup = new HashMap<>();
	
	public Node getNode(Integer id) {
		return nodeLookup.get(id);
	}
	
	public void addNode(int id) {
		nodeLookup.put(id, new Node(id));
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
	
	/**
	 * @param firstId of first node to visit
	 * @return IDs of all visited nodes 
	 */
	public Set<Integer> visitRegion(Integer firstId) {
		Set<Integer> visited = new HashSet<>();
		Node firstNode = getNode(firstId);
		if (firstNode == null)
			return visited; 
		Queue<Node> nextToVisit = new LinkedList<>();
		nextToVisit.add(firstNode);
		while (!nextToVisit.isEmpty()) {
			Node node = nextToVisit.remove();
			Integer id = node.getId();
			if (visited.contains(id))
				continue;
			visited.add(id);
			
			nextToVisit.addAll(node.getAdjacents());
		}		
		return visited;
	}
}

public class Solution {
    private static int n;
    private static int m;
	
	private static void checkAndAddAdjacent(Node node, Graph graph, int[] arr, int i, int j) {
		if (i < 0 || j < 0 || j > (m - 1) || i > (n - 1))	// out of bounds
			return;
		// convert i and j to index
		Integer index = i * m + j;
		Node destNode = graph.getNode(index);
		if (destNode != null)
			node.addAdjacent(destNode);
	}
	
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        n = in.nextInt();
        m = in.nextInt();
        int arr[] = new int[n * m];
        for (int x = 0; x < n * m; x++)
        	arr[x] = in.nextInt();
        in.close();
        
        Graph graph = new Graph();
        for (int x = 0; x < arr.length; x++) {
        	if (arr[x] == 1)
        		graph.addNode(x);
        }
        
        for (Node node : graph.getNodes()) {
        	// convert index to i and j
        	int i = node.getId() / m;
        	int j = node.getId() - i * m;
        	checkAndAddAdjacent(node, graph, arr, i - 1, j - 1);
        	checkAndAddAdjacent(node, graph, arr, i - 1, j);
        	checkAndAddAdjacent(node, graph, arr, i - 1, j + 1);
        	checkAndAddAdjacent(node, graph, arr, i, j - 1);
        	checkAndAddAdjacent(node, graph, arr, i, j + 1);
        	checkAndAddAdjacent(node, graph, arr, i + 1 , j - 1);
        	checkAndAddAdjacent(node, graph, arr, i + 1 , j);
        	checkAndAddAdjacent(node, graph, arr, i + 1 , j + 1);
        }
        
        // returns IDs of all visited nodes
        Set<Integer> allIds = new HashSet<>(graph.getIds());
        int maxCount = 0; 
        while (!allIds.isEmpty()) {
        	Integer id = allIds.iterator().next();
        	Set<Integer> visited = graph.visitRegion(id);
        	if (visited.size() > maxCount)
        		maxCount = visited.size(); 
        	allIds.removeAll(visited);
        }
        
        System.out.println(maxCount);
         
    }
}