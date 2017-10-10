import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.*;

class FastReader {
	BufferedReader br;
	StringTokenizer st;

	public FastReader() {
		br = new BufferedReader(new	InputStreamReader(System.in));
	}

	String next() {
		while (st == null || !st.hasMoreElements()) {
			try	{
				st = new StringTokenizer(br.readLine());
			}
			catch (IOException  e) {
				e.printStackTrace();
			}
		}
		return st.nextToken();
	}

	int nextInt() {
		return Integer.parseInt(next());
	}

	long nextLong() {
		return Long.parseLong(next());
	}

	double nextDouble() {
		return Double.parseDouble(next());
	}

	String nextLine() {
		String str = "";
		try {
			str = br.readLine();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}
}

class Edge {
	private final int x, y;
	private final long r;
	Edge(int x, int y, long r) {
		this.x = x; this.y = y; this.r = r;
	}
	int getX() { return x; }
	int getY() { return y; }
	long getR() { return r; }
}

class Tuple {
	int x, y;
	Tuple(int x, int y) {
		this.x = x; this.y = y;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = prime * (x + y);
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
		Tuple other = (Tuple) obj;
		if ((x == other.x && y == other.y) || (x == other.y && y == other.x))
			return true;
		else 
			return false;
	}
}

class Node {
	public final int id;

	private List<Shortcut> shortcuts = new ArrayList<>();

	private boolean visited = false;
	private long distance = -1;	// distance from the starting node
	
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
	
	public long getDistance() {
		return distance;
	}
	
	public void setDistance(long distance) {
		this.distance = distance;
	}
	
	public List<Shortcut> getShortcuts() {
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
}

class Shortcut {
	public final Node node;
	public final long distance;	// pay attention whether you include originating node's value into here
	
	public Shortcut(Node node, long distance) {
		this.node = Objects.requireNonNull(node);
		this.distance = distance;
	}

	public long getDistance() {
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
		Comparator<Shortcut> byDistance = Comparator.comparingLong(Shortcut::getDistance);
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

			long distance = node.getDistance();	// distance from the starting node
			
			// queue shortcuts
			Collection<Shortcut> shortcuts = node.getShortcuts();
			if (shortcuts != null)
				for (Shortcut shortcut : shortcuts) {
					Node child = shortcut.node;
					if (!child.isVisited()) {	 
						long newChildDistance = distance + shortcut.distance;
						long childDistance = child.getDistance();
						if (childDistance == -1 || newChildDistance < childDistance) {
							child.setDistance(newChildDistance);
							nextToVisit.add(new Shortcut(child, newChildDistance));
						}
					}
				}
		}
	}
    
    public static void main(String[] args) {
    	//long startTime = System.nanoTime();
    	FastReader in=new FastReader();

		int t = in.nextInt();
        for(int i = 0; i < t; i++){
    		int n = in.nextInt();
    		int m = in.nextInt();
    		
            Node[] nodes = IntStream.range(1, n + 1)
            	.mapToObj(Node::new)
            	.toArray(Node[]::new);
            
            List<Edge> edges = new ArrayList<>(m);
            for(int j = 0; j < m; j++) {
        		edges.add(new Edge(in.nextInt(), in.nextInt(), in.nextLong()));
            }
            
            Map<Tuple, Optional<Edge>> mapEdges = edges.stream()
          		  .collect(
          				  groupingBy(
          						  edge -> new Tuple(edge.getX(), edge.getY()), 
          						  minBy(Comparator.comparingLong(Edge::getR)) 
          			));
            
            
            //ConcurrentMap<Integer, ConcurrentMap<Integer, Optional<Edge>>> map = edges.parallelStream()
          	//	  .collect(groupingByConcurrent(Edge::getX, groupingByConcurrent(Edge::getY, minBy(Comparator.comparingInt(Edge::getR)) )));
            
            mapEdges.entrySet().stream().forEach(mapEntry -> {
            	Tuple tuple = mapEntry.getKey();
            	long r = mapEntry.getValue().get().getR();
        		nodes[tuple.x - 1].addShortcut(new Shortcut(nodes[tuple.y - 1], r));
                nodes[tuple.y - 1].addShortcut(new Shortcut(nodes[tuple.x - 1], r));
            });
            
    		int s = in.nextInt();
            
            searchDijkstra(nodes, nodes[s - 1]);
            for (Node node : nodes)
            	if (node.id != s)
            		System.out.print(node.getDistance()+" ");
            System.out.println();
            /*
            String resStr = Arrays.stream(nodes)
            	.filter(nd -> nd.id != s)
            	.sorted(Comparator.comparingInt(Node::getId))
            	.mapToLong(Node::getDistance)
            	.mapToObj(Long::toString)
            	.collect(Collectors.joining(" "));
            */
            //System.out.println("Total time (ms): " + TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS));
        }
    }
}