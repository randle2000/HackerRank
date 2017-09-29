import java.util.*;
import java.util.stream.IntStream;

class Node implements Comparable<Node> {
	private int id;
	private int depth = -1;	// depth from the starting node
	private Set<Node> adjacents = new HashSet<>();
	
	public Node(int id) {
		this.id = id;
	}
	
	public void addAdjacent(Node node) {
		Objects.requireNonNull(node);
		if (node.id != id) 
			adjacents.add(node);
	}
	
	public int getId() {
		return id;
	}
	
	public int getDepth() {
		return depth;
	}
	
	public void setDepth(int depth) {
		this.depth = depth;
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
		firstNode.setDepth(0);
		while (!nextToVisit.isEmpty()) {
			Node node = nextToVisit.remove();
			Integer id = node.getId();
			if (visited.contains(id))
				continue;
			visited.add(id);
			
			int depth = node.getDepth();	// depth from the starting node
			for (Node child : node.getAdjacents()) {
				if (child.getDepth() == -1 || child.getDepth() > depth + 1)
					child.setDepth(depth + 1);
				nextToVisit.add(child);
			}
		}		
		return visited;
	}
}

public class Solution {
	
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int q = in.nextInt();
        for (int i = 0; i < q; i++) {
            int n = in.nextInt();
            int m = in.nextInt();
            Graph graph = new Graph();
            IntStream.range(1, n + 1).forEach(x -> graph.addNode(x));
            for (int j = 0; j < m; j++) {
                int u = in.nextInt();
                int v = in.nextInt();
                Node uNode = graph.getNode(u);
                Node vNode = graph.getNode(v);
                uNode.addAdjacent(vNode);
                vNode.addAdjacent(uNode);
            }
            int s = in.nextInt();
        	
            graph.visitRegion(s);
            StringBuilder sb = new StringBuilder();
            graph.getNodes().stream()
            	.sorted()
            	.filter(node -> node.getId() != s)
            	.mapToInt(Node::getDepth)
            	.forEach(depth -> {
            		long dp = depth == -1 ? -1 : (long)depth * 6;
            		sb.append(dp + " ");
            	});
            System.out.println(sb);
        }
        in.close();
    }
}