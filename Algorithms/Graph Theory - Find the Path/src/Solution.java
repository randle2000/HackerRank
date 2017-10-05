/*
 * The code in this file is messy but that's partly because of the need for performance
 */


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

class Node {
	public final int id;
	public final int row;
	public final int column;
	public final int value;

	public final int zoneId;		// which zone does this node belong to
	public final boolean isZoneBar; // left bar
	
	private Node[] adjacents;
	private Set<Shortcut> shortcuts = new HashSet<>();

	private boolean visited = false;
	private int distance = Integer.MAX_VALUE;	// distance from the starting node
	
	public Node(int id, int row, int column, int value, int zoneId, boolean isZoneBar) {
		this.id = id;
		this.row = row;
		this.column = column;
		this.value = value;
		this.zoneId = zoneId;
		this.isZoneBar = isZoneBar;
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
	
	public Set<Shortcut> getShortcuts() {
		return shortcuts;
	}
	
	public void addShortcut(Shortcut shortcut) {
		this.shortcuts.add(shortcut);
	}
	
	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)	// for performance. there will be only 1 instance of each node 
			return true;
		else
			return false;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		Node other = (Node) obj;
//		if (id != other.id)
//			return false;
//		return true;
	}

	@Override
	public String toString() {
		return "Node [id=" + id + ", row=" + row + ", column=" + column + ", value=" + value + ", zoneId=" + zoneId
				+ ", isZoneBar=" + isZoneBar + ", distance=" + distance + "]";
	}
}

class Shortcut {
	public final Node node;
	public final int distance;	// pay attention whether you include originating node's value into here
	
	public Shortcut(Node node, int distance) {
		//this.node = Objects.requireNonNull(node);
		this.node = node;
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

// zone always includes a left bar, except for zone 0
// last zone may consist of a bar only
class Zone {
	public final int id;		// number of zone
	
	public final Node[] barNodes;
	public final Node[] bodyNodes;
	
	public Zone(int id, Node[] barNodes, Node[] bodyNodes) {
		this.id = id;
		this.barNodes = Objects.requireNonNull(barNodes);
		this.bodyNodes = Objects.requireNonNull(bodyNodes);
	}
}

interface SearchFilter {

	boolean isVisitAllowed(Node from, Node to);
	
	void resetDistances();
}

class Graph {
	// this filter is used when initially searching for shortcuts
	// it will allow search to continue to any zone on the right (id >= sourceZoneId)
	// nodes to the left of the sourceZone should not be searched as they are already covered by target zone's bar
	private class SearchFilterShortcuts implements SearchFilter {	
		protected final int sourceZoneId;
		protected final Zone[] zones;
		public SearchFilterShortcuts(int sourceZoneId, Zone[] zones) {
			this.sourceZoneId = sourceZoneId;
			this.zones = zones;
		}
		
		@Override
		public boolean isVisitAllowed(Node from, Node to) {
			if (to.zoneId >= sourceZoneId)
				return true;
			return false;
		}
		
		@Override
		public void resetDistances() {
			for (int i = sourceZoneId; i < zones.length; i++) {
				for (Node n : zones[i].barNodes) {
					n.setDistance(Integer.MAX_VALUE);
					n.setVisited(false);
				}
				for (Node n : zones[i].bodyNodes) {
					n.setDistance(Integer.MAX_VALUE);
					n.setVisited(false);
				}
			}
		}
	}
	
	// this filter is used for 2nd pass when initially searching for shortcuts
	// it will allow search to continue to any zone on the right (id > sourceZoneId)
	// only search within bar nodes
	private class SearchFilterShortcutsBarsOnly extends SearchFilterShortcuts  {
		public SearchFilterShortcutsBarsOnly(int sourceZoneId, Zone[] zones) {
			super(sourceZoneId, zones);
		}
		
		@Override
		public boolean isVisitAllowed(Node from, Node to) {
			if (from.isZoneBar && to.isZoneBar && to.zoneId > sourceZoneId)
				return true;
			return false;
		}
		
		@Override
		public void resetDistances() {
			for (int i = sourceZoneId; i < zones.length; i++) {
				for (Node n : zones[i].barNodes) {
					n.setDistance(Integer.MAX_VALUE);
					n.setVisited(false);
				}
			}
		}
	}
	
	/**
	 * source and target are within one zone
	 * source and/or target is on the bar
	 * source and/or target is in zone 0
	 * source and target are in bordering zones
	 * last zone consists of bar only

	 * (not optimal if target is on a bar - should only follow current bar)
	 * Scenario 1:
	 * 	allow travel 
	 *		from: zone1 body
	 *		to: zone1 (including 2 bars), all bars between, and zone2 (including 2 bars)
	 *	allow travel
	 *		from: zone2 (including 2 bars)
	 *		to: zone2 (including 2 bars)
	 *	allow travel
	 *		from: any bar of mid-zone and the bar opposite of target
	 *		to: within same bar or any bar between 2 zones TOWARD zone2, zone2 (including 2 bars)
	 * 
	 */
	private class SearchFilterMain implements SearchFilter {
		private final Zone[] zones;
		private final int sZoneId;
		private final int tZoneId;
		private final int minZoneId;
		private final int maxZoneId;
		
		public SearchFilterMain(Zone[] zones, Node sourceNode, Node targetNode) {
			this.zones = zones;
			this.sZoneId = sourceNode.zoneId;
			this.tZoneId = targetNode.zoneId;
			if (sZoneId <= tZoneId) {
				minZoneId = sZoneId;
				maxZoneId = tZoneId;
			} else {
				minZoneId = tZoneId;
				maxZoneId = sZoneId;
			}
		}

		@Override
		public boolean isVisitAllowed(Node from, Node to) {
			if (from.zoneId == sZoneId || (from.zoneId == sZoneId+1 && from.isZoneBar)) {	// FROM: zone1 (including 2 bars) 
				if (to.zoneId == sZoneId || (to.zoneId == sZoneId+1 && to.isZoneBar))		// TO: zone1 (including 2 bars)
					return true;
				if (to.zoneId == tZoneId || (to.zoneId == tZoneId+1 && to.isZoneBar))		// TO: zone2 (including 2 bars)
					return true;
			}

			if (from.zoneId == tZoneId || (from.zoneId == tZoneId+1 && from.isZoneBar)) {	// FROM: zone2 (including 2 bars)
				if (to.zoneId == tZoneId || (to.zoneId == tZoneId+1 && to.isZoneBar))		// TO: zone2 (including 2 bars)
					return true;
			}
			
			
			
//			if (from.zoneId == sZoneId && !from.isZoneBar) {								// FROM: zone1 body
//				if (to.zoneId == sZoneId || (to.zoneId == sZoneId+1 && to.isZoneBar))		// TO: zone1 (including 2 bars)
//					return true;
//				if (to.zoneId == tZoneId || (to.zoneId == tZoneId+1 && to.isZoneBar))		// TO: zone2 (including 2 bars)
//					return true;
//				if (to.isZoneBar && to.zoneId > minZoneId && to.zoneId <= maxZoneId)		// TO: all bars between zones
//					return true;
//			}
//			
//			if (from.zoneId == tZoneId || (from.zoneId == tZoneId+1 && from.isZoneBar)) {	// FROM: zone2 (including 2 bars)
//				if (to.zoneId == tZoneId || (to.zoneId == tZoneId+1 && to.isZoneBar))		// TO: zone2 (including 2 bars)
//					return true;
//			}
//			
//			int minId = minZoneId;
//			int maxId = maxZoneId;
//			if (sZoneId <= tZoneId)
//				minId--;
//			else 
//				maxId++;
//			if (from.isZoneBar && from.zoneId > minId && from.zoneId <= maxId) {			// FROM: any bar of mid-zone and the bar opposite of target
//				if (to.isZoneBar && to.zoneId == from.zoneId)								// TO: within same bar
//					return true;
//				if (to.isZoneBar && to.zoneId > minZoneId && to.zoneId <= maxZoneId &&		// TO: any bar between 2 zones
//					Math.abs(tZoneId - to.zoneId) < Math.abs(tZoneId - from.zoneId)) 		//		TOWARD zone2
//					return true;
//				if (to.zoneId == tZoneId || (to.zoneId == tZoneId+1 && to.isZoneBar))		// TO: zone2 (including 2 bars)
//					return true;
//			}
			
			return false;																	// PROHIBIT everything else
		}
		
		@Override
		public void resetDistances() {
			// reset source zone with 2 bars
			int i = sZoneId;
			for (Node n : zones[i].barNodes) {
				n.setDistance(Integer.MAX_VALUE);
				n.setVisited(false);
			}
			for (Node n : zones[i].bodyNodes) {
				n.setDistance(Integer.MAX_VALUE);
				n.setVisited(false);
			}
			if (i + 1 < zones.length)
				for (Node n : zones[i + 1].barNodes) {
					n.setDistance(Integer.MAX_VALUE);
					n.setVisited(false);
				}
			
			// reset target zone with 2 bars
			if (tZoneId != sZoneId) {
				i = tZoneId;
				for (Node n : zones[i].barNodes) {
					n.setDistance(Integer.MAX_VALUE);
					n.setVisited(false);
				}
				for (Node n : zones[i].bodyNodes) {
					n.setDistance(Integer.MAX_VALUE);
					n.setVisited(false);
				}
				if (i + 1 < zones.length)
					for (Node n : zones[i + 1].barNodes) {
						n.setDistance(Integer.MAX_VALUE);
						n.setVisited(false);
					}
			}
			

			
//			for (int i = minZoneId; i <= maxZoneId; i++) {
//				// reset all bars of source zone, target zone and all bars in between
//				for (Node n : zones[i].barNodes) {
//					n.setDistance(Integer.MAX_VALUE);
//					n.setVisited(false);
//				}
//				// reset body of source zone and target
//				if (i == minZoneId || i == maxZoneId)
//					for (Node n : zones[i].bodyNodes) {
//						n.setDistance(Integer.MAX_VALUE);
//						n.setVisited(false);
//					}
//				// reset the most right bar if it exists
//				if (i == maxZoneId)
//					if (maxZoneId + 1 < zones.length)
//						for (Node n : zones[maxZoneId + 1].barNodes) {
//							n.setDistance(Integer.MAX_VALUE);
//							n.setVisited(false);
//						}
//			}
		}
	}
	
	public final int rows;
	public final int columns;
	
	private final Node[] nodes;
	private final Zone[] zones;
	
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
	 * @param maxColumnsPerZone	indicates how to split to zones
	 * @throws IOException 
	 */
	public Graph(int[] arr, final int rows, final int columns, final int maxColumnsPerZone) {
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
        	int zoneId = c / maxColumnsPerZone;
        	boolean isZoneBar = (c - zoneId * maxColumnsPerZone) == 0 && c != 0;	// zone0 does not have zonebar
        	Node node = new Node(i, r, c, value, zoneId, isZoneBar);
        	nodes[i] = node;
        }
        
        // create zones
        int zoneCount = 1 + (columns - 1) / maxColumnsPerZone;
        zones = new Zone[zoneCount];
        for (int i = 0; i < zoneCount; i++) {
        	// number of columns in this zone (including 1 bar column)
        	int colCnt = (i == zoneCount - 1)  ?  columns - i * maxColumnsPerZone  :  maxColumnsPerZone;
        	int nodesInBar;
        	int nodesInBody;
        	if (i == 0) {
        		nodesInBar = 0;					// zone0 does not have a bar				
        		nodesInBody = rows * colCnt;	// zone0 consists of just body w/out bar
        	} else {
        		nodesInBar = rows;
        		nodesInBody = rows * (colCnt - 1);	// body, excluding 1 bar column
        	}
    		Node[] barNodes = new Node[nodesInBar];							
    		Node[] bodyNodes = new Node[nodesInBody];	
    		int bodyTmp = 0;
        	for (int j = 0; j < rows; j++) {
        		for (int k = 0; k < colCnt; k++) {
        			int r = j;
        			int c = i * maxColumnsPerZone + k;
        			int index = rcToIndex(r, c);
        			Node node = nodes[index];
        			if (k == 0 && i != 0)	// add first column of each zone to bar, except for zone0 (where whole zone is a body)  
        				barNodes[j] = node;
        			else
        				bodyNodes[bodyTmp++] = node;
        		}
        		
        	}
        	Zone zone = new Zone(i, barNodes, bodyNodes);
        	zones[i] = zone;
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
        
        // generate shortcuts on bar nodes
        // starting from zone1 because zone0 does not have a bar
        for (int z = 1; z < zones.length; z++) {	// process each zone from 1 to the right
        	SearchFilter searchFilterSameBar = new SearchFilterShortcuts(z - 1, zones);  
        	SearchFilter searchFilterNextBar = new SearchFilterShortcuts(z, zones);
        	
        	Node[] barNodes = zones[z].barNodes;
        	// for each node in this bar, find shortcuts to each other node within this same bar
        	for (int i = 0; i < barNodes.length; i++) 	
        		// start from 2nd bar below because to the bar right below, there already is an adjacent
        		for (int j = i + 2; j < barNodes.length; j++) 
        			linkNodesWithShortcut(barNodes[i], barNodes[j], searchFilterSameBar);
        	
        	// now search from each node of this bar to each node of the next zone's bar (bar to the right)
        	if (z != zones.length - 1) {	// not the last zone
        		Node[] nextBarNodes = zones[z + 1].barNodes;
            	// for each node in this bar, find shortcuts to each node within this same bar
            	for (Node bn : barNodes) 			// for each node in this bar, 
            		for (Node nbn : nextBarNodes) 	// find shortcut to each node of the next bar
            			linkNodesWithShortcut(bn, nbn, searchFilterNextBar);
        	}
        }
        
        // generate shortcuts from every bar node to EACH other bar node
        // use already generated shortcuts
        // starting from zone1 because zone0 does not have a bar
        // ending 3rd zone from the right (i.e. not doing this for last 2 zones)        
        for (int z = 1; z < zones.length - 2; z++) {	// process each zone from 1 to the right
        	SearchFilter searchFilterBarsOnly = new SearchFilterShortcutsBarsOnly(z, zones);
        	
        	Node[] barNodes = zones[z].barNodes;
        	for (Node bn : barNodes) {		// for each node in this bar,
        		// starting from 2nd zone to the right of current because there are shortcuts already to the next zone
        		for (int z2 = z + 2; z2 < zones.length; z2++) {
        			Node[] nextBarNodes = zones[z2].barNodes;
        			for (Node nbn : nextBarNodes) 	// find shortcut to each node of the next bar
        				linkNodesWithShortcut(bn, nbn, searchFilterBarsOnly);
        		}
        	}
        }
	}
	
	private void linkNodesWithShortcut(Node from, Node to, SearchFilter searchFilter) {
		searchDijkstra(from, to, searchFilter);
		int distance = to.getDistance();
		// distance is the shortest path between these 2 nodes, add shortcut to both of them 
		// be careful: this distance includes values from both nodes
		from.addShortcut(new Shortcut(to, distance));
		to.addShortcut(new Shortcut(from, distance));
		
	}
	
	public Node searchPath(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
		int sourceIndex = rcToIndex(sourceRow, sourceColumn);
		int targetIndex = rcToIndex(targetRow, targetColumn);
		if (sourceIndex < 0 || sourceIndex >= nodes.length || targetIndex < 0 || targetIndex >= nodes.length)
			throw new IllegalArgumentException();
		Node sourceNode = nodes[sourceIndex];
		Node targetNode = nodes[targetIndex];
		
		SearchFilter searchFilterMain = new SearchFilterMain(zones, sourceNode, targetNode); 
		searchDijkstra(sourceNode, targetNode, searchFilterMain);
		return targetNode; 
	}
	
	/**
	 * Will visit all nodes and set depth of each node to min path from starting node
	 * @param firstId of first node to visit
	 * @throws IOException 
	 */
	public void searchDijkstra(Node startNode, Node targetNode, SearchFilter searchFilter) {
		searchFilter.resetDistances();
			
		final int r = targetNode.row;
		final int c = targetNode.column;
		Comparator<Shortcut> byDistance = Comparator.comparingInt(Shortcut::getDistance);
		Comparator<Shortcut> byProximity = (s1, s2) -> {
			int proximity1 = Math.abs(c - s1.node.column) + Math.abs(r - s1.node.row);
			int proximity2 = Math.abs(c - s2.node.column) + Math.abs(r - s2.node.row);
			return proximity1 - proximity2;
		};
		Comparator<Shortcut> myComparator = byDistance.thenComparing(byProximity);
		//Comparator<Shortcut> myComparator = byDistance;
		PriorityQueue<Shortcut> nextToVisit = new PriorityQueue<>(11, myComparator);
		
		nextToVisit.add(new Shortcut(startNode, startNode.value));
		startNode.setDistance(startNode.value);
		
		int mm = 0;
		
		while (!nextToVisit.isEmpty()) {
			// find node in nextToVisit with the lowest distance
			Node node = nextToVisit.poll().node;
			
			if (node.isVisited())
				continue;
			node.setVisited(true);
			
			if (node.getDistance() < mm)
				System.out.println("mmmmmmmmmmmmmmmmmmmmmmmmmm");
			if (mm < node.getDistance())
				mm = node.getDistance();

			
			if (node.id == targetNode.id)
				return;
			
			int distance = node.getDistance();	// depth from the starting node
			// queue shortcuts
			for (Shortcut shortcut : node.getShortcuts()) {
				Node child = shortcut.node;
				if (!child.isVisited()) {	 
					if (searchFilter.isVisitAllowed(node, child)) {
						int childDistance = child.getDistance();
						// need to subtract node.value because shortcut.distance already includes it  
						int newChildDistance = distance + shortcut.distance - node.value;
						if (newChildDistance < childDistance) {
							child.setDistance(newChildDistance);
							nextToVisit.add(new Shortcut(child, newChildDistance));
						}
					}
				}
			}
			// queue adjacents
			for (Node child : node.getAdjacents()) {
				if (!child.isVisited()) {	 
					if (searchFilter.isVisitAllowed(node, child)) {
						int childDistance = child.getDistance();
						int newChildDistance = distance + child.value;
						if (newChildDistance < childDistance) {
							child.setDistance(newChildDistance);
							nextToVisit.add(new Shortcut(child, newChildDistance));
						}
					}
				}
			}
		}
	}
}

public class Solution {
    
    public static void main(String[] args) throws IOException {
    	long startTime = System.nanoTime(); 
    	
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();
        int[] arr = new int[n * m];
        for (int i = 0; i < n * m; i++)
        	arr[i] = in.nextInt();
        
        long estimatedTime = System.nanoTime() - startTime;
        System.out.println("Readout: " + TimeUnit.MILLISECONDS.convert(estimatedTime, TimeUnit.NANOSECONDS));
        //System.out.println("------------");
        
    	startTime = System.nanoTime(); 
    	Graph graph = new Graph(arr, n, m, 70);
    	estimatedTime = System.nanoTime() - startTime;
        System.out.println("Graph creation: " + TimeUnit.MILLISECONDS.convert(estimatedTime, TimeUnit.NANOSECONDS));
        //System.out.println("------------");
        
//		BufferedWriter out = new BufferedWriter(new FileWriter(new File("aaaa.txt")));
		BufferedReader eo = new BufferedReader(new FileReader(new File("expectedOutput.txt")));

		
		startTime = System.nanoTime();
		List<Long> execTimes = new ArrayList<>();
        int q = in.nextInt();
    	
        int good = 0;
    	int bad = 0;

    	for (int i = 0; i < q; i++) {
            int r1 = in.nextInt();
            int c1 = in.nextInt();
            int r2 = in.nextInt();
            int c2 = in.nextInt();
            
        	long taskStartTime = System.nanoTime(); 
            Node targetNode = graph.searchPath(r1, c1, r2, c2);
            long taskExecTime = TimeUnit.MICROSECONDS.convert(System.nanoTime() - taskStartTime, TimeUnit.NANOSECONDS);
        	execTimes.add(taskExecTime);
//    		out.write(String.valueOf(taskExecTime));
//    		out.newLine();


        	int minDepth = targetNode.getDistance();
        	String es = eo.readLine();
        	if (!es.equals(String.valueOf(minDepth)) ) {
        		bad++;
        		Node nn = graph.getNode(graph.rcToIndex(r1,  c1));
        		System.out.println(nn);
        		System.out.println(targetNode);
        		System.out.println("actual="+minDepth);
        		System.out.println("expected="+es);
        		System.out.println("-------------");
        	} else good++;
//    		System.out.println(minDepth);
//    		out.write(String.valueOf(minDepth));
//    		out.newLine();
        }
//		out.close();
        eo.close();
        in.close();
        System.out.println("good: " + good);
        System.out.println("good: " + bad);
        
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
        System.out.println("1000 micros = (ms): " + TimeUnit.MILLISECONDS.convert(1000, TimeUnit.MICROSECONDS));
    }
}