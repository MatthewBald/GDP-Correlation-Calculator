/**
 * 
 * GDPScanner acts as a sort of multi-purpose launcher for methods used to analyze economies. It first scans
 * in a file containing the National Gross Domestic Product Per Capita, Current Prices for every country 
 * of which the IMF has data.
 * 
 * After cleaning up the input, it computes the pearson correlation coefficient for each pair of countries and
 * outputs this information to correlations.txt.
 * 
 * User input begins after being prompted for a T value, which means an edge will be drawn between Vertex A and
 * Vertex B if and only if the absolute value of the correlation coefficient is larger than the chosen T Value.
 * Testing different T Values will result in different graph densities and different numbers of connected components.
 * 
 * The Network, Edge, and Vertex classes are recycled and modified from assignment 3 and are used to store each
 * country by WEO Code and the weight each edge is the correlation coefficient between the country vertices incident
 * on that edge. 
 * 
 * @author Matthew Bald
 *
 */

import java.util.*;
import java.io.*;

public class GDPScanner {
	
	ArrayList<CountryBin> countrydata; //Stores all the information contained in 'weoreptc.aspx'
	ArrayList<CountryCorrelation> countrycorrelations; //Stores the undirected graph of countries and weighted edges
	
	public static void main(String args[]) {
		
		GDPScanner newStudy = new GDPScanner(); //Start
	}
	
	public GDPScanner() {
		
		try {
			
			File file = new File("weoreptc.aspx");
			Scanner fin = new Scanner(file); // Try to find 'weoreptc.aspx'
			
			countrydata = new ArrayList<CountryBin>();
			countrycorrelations = new ArrayList<CountryCorrelation>();
			
			//Scan through the first line (Garbage) 
			fin.nextLine();
			
			//For the next x lines, int, str, str, str, int, int, int, int, int, int, (~Garbage int)
			//						weo, cty, typ, scl, '06, '07, '08, '09, '10, '11, (Estimates After yr. ####)
			
			
			//Stop when next != an integer
			// -- accounted for countries with compound names "Republics of ' ',' ' and ' ', United ' ', etc"
			// -- accounted for 'n/a' data vals, 
			
			while (fin.hasNext()) {
				
				CountryBin country = new CountryBin();
				
				if (!fin.hasNextInt()) //'Better' Terminating condition than the while-- the while loop will continue to run & will error without this condition
					break;
					
				country.setCode(fin.nextInt());
				
				String name = "";
						
				while (!fin.hasNextInt()) {
					
					String name2 = fin.next();
					
					if (!name2.equals("National") && !name2.equals("currency") && !name2.equals("Billions"))
						name += (" " + name2);
					
					if (name2.equals("Billions"))
						break;
				}
				
				country.setCurrency("$");
				country.setScale("Billions");
				country.setName(name);
				
				//Scans in an integer expressed as a string with periods and commas and removes the punctuation to convert the string into a proper integer
				for (int x = 1; x <= 6; x++) {
				
					String rawdata = fin.next();
					String strdata = "";
					double procdata = 0;
					
					for (char y : rawdata.toCharArray()) {
						
						if (y-'0' < 0 || y-'0' > 9)
							continue;
						
						
						strdata += y;
					}
	
					if (strdata.length() > 0) { //Make sure that we've got data
					
						procdata = Long.parseLong(strdata);
						country.add(procdata);
									
					}
				}
				
				if (country.getGdplist() != null)
					countrydata.add(country);
				
				fin.nextLine(); //skip the rest
			}
			
			//Calculate the standard deviations of each countries' GDPs over the time period
			for (CountryBin b : countrydata) {
				b.calcSigma();				
			}
			
			//Compute the Pearson correlation coefficient (r) for each pair of countries
			for (CountryBin a : countrydata)
				for (CountryBin b : countrydata) {
					
					if (a == b)
						continue;
					
					countrycorrelations.add(new CountryCorrelation(a.getWeocode(), b.getWeocode(), a.calcCorrelation(b)));
				}
			
			PrintStream corfile = new PrintStream(new File("correlations.txt"));
			
			//Output formatted to be easily scanned in for network creation ("Node1 Node2 EdgeWeight")
			for (CountryCorrelation c : countrycorrelations)
				corfile.println(c.weo1 + " " + c.weo2 + " " + c.correlation);
			
			Scanner system = new Scanner(System.in);
			
			System.out.printf("Enter a T value for G: ");
			
			CorrelationBin rBin = new CorrelationBin(new Scanner(new File("correlations.txt")), system.nextDouble());
			
			System.out.println(rBin.G.BFS(rBin.G) + " connected components found. Network ready.\n");
			
			while (true) {	
				
				System.out.printf("Enter the WEO code of country to query: ");
				
				int weo = system.nextInt();
				
				//Get the incident edges on the queried country
				ArrayList<Edge> edges = rBin.G.incidentEdges(rBin.G.getVertex(weo));
				
				System.out.printf("Choose a distance d to compute: ");
				
				Double d = system.nextDouble();
				
				for (Edge edge : edges) { //Run through each edge & save the edges that are within distance d of the queried country
					
					if (edge.calcDistance() < d) {
						
						CountryBin country = fetch(edge.b.id);
						System.out.println(country.countryname);
					}
				}
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Iterates through countrydata and grabs the country with a matching weo code
	public CountryBin fetch(int weo) {
		
		for (CountryBin a : countrydata)
			if (a.getWeocode() == weo)
				return a;
		
		return null;
	}
	
	//Prints countrydata to a specified file
	public void printData(PrintStream outfile) {
		
		for (CountryBin b : countrydata)
			b.Print(outfile);
		
	}
}
