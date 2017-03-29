/**
 * CorrelationBin contains a network G, a scanner fin, and a T_Value (used to be a defined constant, but it can be altered each time the G is constructed, now)
 * 
 * When the constructor is called, populateNetwork() is called, which scans the specified file for edge information and fills the network G with those edges and nodes,
 * but only if those edges satisfy the particular T_Value chosen 
 * 
 * @author Matthew Bald
 * 
 */

import java.util.*;

public class CorrelationBin {

	static double T_VALUE = 0.995;
	Network G;
	Scanner fin;
	
	//Will scan in the file upon construction
	public CorrelationBin(Scanner fin, Double t_val) {
		
		T_VALUE = t_val;
		G = new Network();
		this.fin = fin;
		
		populateNetwork();
		
	}
	
	public void populateNetwork() {
		
		while (fin.hasNextInt()) {
			
			int source = fin.nextInt();
			int dest = fin.nextInt();
			
			Double weight = fin.nextDouble();
			
			Vertex a = G.getVertex(source);
			Vertex b = G.getVertex(dest);
			
			if (a != null) a.connections.add(dest);
			
			else if (a == null) {
				
				a = new Vertex(source);
				a.connections.add(dest);
				G.vertices.add(a);
				
			}
			
			if (b == null) {
				
				b = new Vertex(dest);
				G.vertices.add(b);
				
			}
			
			//FOR ALL EDGES (X, Y) IN |E|, IFF |C(X,Y)| > T (CHOOSE A VALUE T SUCH THAT THE EDGE DENSITY OF G IS BETWEEN 0.05 AND 0.50)
			if (Math.abs(weight) < T_VALUE) 
				continue; //omit this edge
			
			else 
				G.insertDirectedEdge(a, b, weight);
			
		}
	}
}
