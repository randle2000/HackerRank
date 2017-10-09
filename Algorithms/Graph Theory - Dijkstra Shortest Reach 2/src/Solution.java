import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

class Node {
	public final int id;

	private Map<Node, Shortcut> shortcuts = new HashMap<>();

	private boolean visited = false;
	private int distance = -1;	// distance from the starting node
	
	public Node(int id) {
		this.id = id;
	}
	
	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public int getId() {
		return id;
	}
	
	public int getDistance() {
		return distance;
	}
	
	public void setDistance(int distance) {
		this.distance = distance;
	}
	
	public Collection<Shortcut> getShortcuts() {
		return shortcuts.values();
	}
	
	public void addShortcut(Shortcut shortcut) {
		Shortcut sc = shortcuts.get(shortcut.node);
		if (sc == null || sc.getDistance() > shortcut.getDistance())
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

public class Solution {
	
	/**
	 * Will visit all nodes and set distance of each node to min path from starting node
	 * 
	 * @param startNode a node where to start search from
	 */
	public static void searchDijkstra(Node[] nodes, Node startNode) {
		Comparator<Shortcut> byDistance = Comparator.comparingInt(Shortcut::getDistance);
		// It is not recommended to change fields by which PriorityQueue sorts after it was added to queue
		// that's why using PriorityQueue<Shortcut> instead of PriorityQueue<Node>
		// (node.distance will be changed)
		PriorityQueue<Shortcut> nextToVisit = new PriorityQueue<>(byDistance);
		
		nextToVisit.add(new Shortcut(startNode, 0));
		startNode.setDistance(0);
		
		while (!nextToVisit.isEmpty()) {
			// find node in nextToVisit with the lowest distance
			Node node = nextToVisit.poll().node;
			
			if (node.isVisited()) 
				continue;
			node.setVisited(true);

			int distance = node.getDistance();	// distance from the starting node
			
			// queue shortcuts
			Collection<Shortcut> shortcuts = node.getShortcuts();
			if (shortcuts != null)
				for (Shortcut shortcut : shortcuts) {
					Node child = shortcut.node;
					if (!child.isVisited()) {	 
						int newChildDistance = distance + shortcut.distance;
						int childDistance = child.getDistance();
						if (childDistance == -1 || newChildDistance < childDistance) {
							child.setDistance(newChildDistance);
							nextToVisit.add(new Shortcut(child, newChildDistance));
						}
					}
				}
		}
	}
    
    public static void main(String[] args) throws IOException {
    	
        Scanner in = new Scanner(System.in);
        int t = in.nextInt();
        for(int a0 = 0; a0 < t; a0++){
            int n = in.nextInt();
            
            Node[] nodes = IntStream.range(1, n + 1)
            	.mapToObj(Node::new)
            	.toArray(Node[]::new);
            
            int m = in.nextInt();
            for(int a1 = 0; a1 < m; a1++){
                int x = in.nextInt();
                int y = in.nextInt();
                int r = in.nextInt();
                
                // nodes are 1-based while arrays of nodes is 0-based
                nodes[x - 1].addShortcut(new Shortcut(nodes[y - 1], r));
                nodes[y - 1].addShortcut(new Shortcut(nodes[x - 1], r));
            }
            int s = in.nextInt();
            
            searchDijkstra(nodes, nodes[s - 1]);
            for (Node node : nodes)
            	if (node.id != s)
            		System.out.print(node.getDistance()+" ");
            /*
            String resStr = Arrays.stream(nodes)
            	.filter(nd -> nd.id != s)
            	.sorted(Comparator.comparingInt(Node::getId))
            	.mapToInt(Node::getDistance)
            	.mapToObj(Integer::toString)
            	.collect(Collectors.joining(" "));
            */
            System.out.println();
        }
        in.close();
    }
}