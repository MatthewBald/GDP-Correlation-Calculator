/**
 * Network has been taken from assignment 3 and modified to work with weighted edges. I removed unnecessary algorithms like DFS and pathFinding with BFS. 
 * 
 * Network stores a network of nodes with weighted edges & is typically used for undirected graphs, but has support for directed graphs. Network is also
 * used to update and access the information contained in the network. BFS is used to determine the number of connected components in G.
 * 
 * @author Matthew Bald
 * 
 */

import java.util.*;
import java.io.*;

public class Network {
	
	ArrayList<Vertex> vertices;
	ArrayList<Edge> edges;
	
	public Network() {
		
		vertices = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();
	}
	
	
	/*
	 * Path finding & mapping algorithms
	 */

	/**
	 * Breadth First Search: sets labels of vertices and edges
	 * @return the number of connected components
	 */
	public int BFS(Network G) {
		
		if (G == null || vertices == null || edges == null)
			return 0;
		
		for (Vertex u : vertices) {
			u.setLabel("UNEXPLORED");
		}
		
		for (Edge e: edges) {
			e.setLabel("UNEXPLORED");
		}
		
		int connectedcomps = 0;
		
		for (Vertex v : vertices) {
			if (v.getLabel().equals("UNEXPLORED")) {
				++connectedcomps;
				BFS(G, v);		
			}
		}
		
		return connectedcomps;
	}
	
	public void BFS(Network G, Vertex s) {
		
		Queue<Vertex> Q = new LinkedList<Vertex>();
		
		Q.add(s);
		
		s.setLabel("VISITED");
		
		while (!Q.isEmpty()) {
			
			Vertex v = Q.remove();
			
			if (v.id%1000 == 0) 
				System.out.print(".");

			if (!v.connections.isEmpty()) {
					
				for (Edge e : G.incidentEdges(v)) {

					if (e.getLabel().equals("UNEXPLORED")) {
						
						Vertex w = opposite(v, e);
						
						if (w.getLabel().equals("UNEXPLORED")) {
							
							e.setLabel("DISCOVERY");
							w.setLabel("VISITED");
							
							Q.add(w);
						}
						
						else 
							e.setLabel("CROSS");
					}
				}
			}
		}
	}
	
	/*
	 * Generic methods of the Graph
	 */
	
	public int numVertices() {	
		return vertices.size();
	}
	
	public int numEdges() {
		return edges.size();
	}
	
	public ArrayList<Vertex> vertices() {
		return vertices;	
	}
	
	public ArrayList<Edge> edges() {
		return edges;
	}
	
	public void printEdges() {
		
		if (edges == null) return;
		
		for (Edge e : edges) System.out.print(e.label + " edge: " + e.a.id + " to " + e.b.id + "\n");
		
		System.out.println();
		
	}
	
	public void printEdges(PrintStream fout) {
		
		if (edges == null) return;
		
		for (Edge e : edges) fout.print(e.label + " edge: " + e.a.id + " to " + e.b.id + "\n");
		
		System.out.println();
		
	}
	
	public void printWeighted(PrintStream fout) {
		
		if (edges == null) return;
		
		for (Edge e : edges) fout.print(e.label + " edge: " + e.a.id + " to " + e.b.id + "(Weighs: "+ e.weight +")\n");
		
		System.out.println();
		
	}
	
	
	public void printVertices() {
		
		if (vertices == null) return;
		
		for (Vertex v : vertices) System.out.print(v.label + " " + v.id + "\n");	
		
		System.out.println();
	}
	
	
	/*
	 * Access Methods
	 */
	
	/**
	 * Searches the network for a specified edge
	 * 
	 * @param e The edge
	 * @return an integer representing the index of the edge, -1 if the edge is not found
	 */
	public int getEdgeIndex(Edge e) {
		
		int index = 0;
		
		for (Edge x : edges) {
			
			if ((x.a.id == e.a.id) && (x.b.id == e.b.id))
				return index;
		
			++index;
		}
		
		return -1;
	}
	
	/**
	 * Searches vertices for an ID match and returns the match
	 *
	 * @param id The identifying label of the vertex
	 * @return The matching vertex, null if not found
	 */
	public Vertex getVertex(int id) {
		
		for (Vertex v : vertices) {
			
			if (v.id == id) return v;
			
		}
		
		return null;
	}
	
	/**
	 * Returns whether or not v and w are adjacent edges in a graph
	 * 
	 * @param v and w
	 * @return true if v and w are adjacent, false otherwise
	 */
	public boolean areAdjacent(Vertex v, Vertex w) {
		if (v == null || w == null) return false;
		
		for (int e : v.connections) 
			if (e == w.id) return true;
		
		for (int e : w.connections) 
			if (e == v.id) return true;
		
		
		return false;
	}
	
	/**
	 * Looks for the other node in an edge
	 * 
	 * @param v the node we don't want to return
	 * @param e the edge we are looking at
	 * @return the node on the 'other side' of the edge
	 */
	public Vertex opposite(Vertex v, Edge e) {
		if (v == null || e == null) return null;
		
		if (e.a.id == v.id) return getVertex(e.b.id);
		if (e.b.id == v.id) return getVertex(e.a.id);
		
		return null;
	}
	
	/**
	 * Determines the origin of a directed edge
	 * 
	 * @param e
	 * @return the source of edge e
	 */
	public Vertex origin(Edge e) {
		if (e == null)
			return null;
					
		return e.a;
	}
	
	/**
	 * Determines the destination of a directed edge
	 * 
	 * @param e
	 * @return the destination of e
	 */
	public Vertex destination(Edge e) {
		if (e == null)
			return null;
		
		return e.b;		
	}
	
	public boolean isDirected(Edge e) {
		
		if (e == null)
			return false;
		
		for (int iter : e.a.connections) {
			
			if (iter == e.b.id) {
				
				Edge conjugate = e.conjugate();
				
				for (int iter2 : e.a.connections) {
					if (iter2 == conjugate.b.id)
						return false;
				}

			return true;
				
			}
		}
		
		return false;
	}
	
	/**
	 * Gets the vertices that edge e spans
	 * 
	 * @param e
	 * @return an ArrayList of two vertices, null if no edge exists
	 */
	public ArrayList<Vertex> endVertices(Edge e) {
		if (e == null)
			return null;
		
		if (e.a == null)
			if (e.b == null)
				return null;
		
		ArrayList<Vertex> endpoints = new ArrayList<Vertex>();
		
		endpoints.add(getVertex(e.a.id));
		endpoints.add(getVertex(e.b.id));
		
		return endpoints;
	}
	
	public boolean aVertex(Object o) {
		
		if (o instanceof Vertex)
			return true;
		
		else
			return false;	
	}
	
	public Edge getEdge(int index) {
		
		Edge e = edges.get(index);
		
		return e;
	}
	
	/**
	 * Takes in a vertex and returns the edges associated with that edge
	 * 
	 * @param v The vertex to look at
	 * @return an ArrayList of edges incident on v
	 */
	public ArrayList<Edge> incidentEdges(Vertex v) {
		
		if (v.connections == null) return null;
		
		if (v.connections.isEmpty()) return null;
		
		ArrayList<Edge> incidents = new ArrayList<Edge>();
		
		for (Edge e : edges) {
			
			if (e.a.id == v.id) incidents.add(e);
			//if (e.b.id == v.id) incidents.add(getEdgeIndex(e));
		}
		
		return incidents;	
	}
	
	/* 
	 * Update Methods
	 */
	
	/**
	 * Adds a directed edge between two vertices in a graph
	 * 
	 * @param v The source vertex
	 * @param w The destination vertex
	 */
	public void insertDirectedEdge(Vertex v, Vertex w, Double weight) {
		if (w == null || v == null) return;
		
		Edge e = new Edge(v,w, weight);

		edges.add(e);
	}
	
	/**
	 * Adds a directed edge between two vertices in a graph
	 * 
	 * @param v The source vertex
	 * @param w The destination vertex
	 */
	public void insertDirectedEdge(Vertex v, Vertex w) {
		if (w == null || v == null) return;
		
		Edge e = new Edge(v,w);

		edges.add(e);
	}
	
	public void insertEdge(Vertex v, Vertex w) {
		if (w == null || v == null) return;
		
		insertDirectedEdge(v, w);
		insertDirectedEdge(w, v);
	}
	
	public void insertVertex(Vertex o) {
		vertices.add(o);
	}

	/**
	 * Removes a directed edge from the network
	 * 
	 * @param e The edge to be removed
	 */
	public void removeDirectedEdge(Edge e) {
		int index = getEdgeIndex(e);
		
		if (index != -1)
			edges.remove(index);		
	}
}
