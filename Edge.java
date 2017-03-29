/**
 * Each edge has a weight, a start vertex and an edge vertex. 
 *
 * @author Matthew Bald
 */
public class Edge {
	Vertex a;
	Vertex b;
	String label;
	Double weight;
	
	public Edge(Vertex a, Vertex b) {
		
		this.a = a;
		this.b = b;		
		this.label = "UNEXPLORED";
	}
	
	public Edge(Vertex a, Vertex b, Double weight) {
		
		this.a = a;
		this.b = b;
		this.weight = weight;
		this.label = "UNEXPLORED";
		
	}
	
	//Finds the correlation distance between Vertex A and Vertex B
	public Double calcDistance() {
		
		return 1-Math.abs(this.weight); // the distance is the difference of the absolute value of the correlation from 1
		
	}
	
	public void printEdge() {
		System.out.println(a.id + " to " + b.id + " Label: " + label);
		
	}

	public void setLabel(String label) {
		this.label = label;		
	}
	
	public String getLabel() {
		return label;
	}
	
	public Edge conjugate() {
		Edge conjugate = new Edge(this.b, this.a);
		return conjugate;
	}
	
	public boolean equals(Edge e) {
		if (e == null) return false;
		
		if (e.a == this.a)
			if (e.b == this.b)
				return true;
		
		return false;
	}
}
