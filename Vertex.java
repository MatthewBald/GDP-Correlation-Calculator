/**
 * A Vertex stores the weo code for one country along with it's outgoing connections and labels used for running BFS and DFS algorithms
 * 
 * @author Matthew Bald
 */

import java.util.*;

public class Vertex {

	int id; 
	ArrayList<Integer> connections;
	String label; //used for BFS/DFS
	Vertex back; //used for pathfinding
	
	public Vertex(int n) {
		
		this.connections = new ArrayList<Integer>();
		this.id = n;
		this.label = "UNEXPLORED";
		this.back = null;
	}
	
	public void setLabel(String label) {
		this.label = label;		
	}
	
	public String getLabel() {
		return label;
	}
	
	public void vPrint() {
		
		System.out.println("Node: " + id);
		System.out.println("Adjacent Nodes: " + this.connections.toString());
		
	}
}