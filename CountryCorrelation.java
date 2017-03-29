/**
 * Simple object used to store the bare minimum information to create a network & may output to a file for use in creating a network later
 * 
 * @author Matthew Bald
 */

import java.io.*;

public class CountryCorrelation {
	
	int weo1;
	int weo2;
	double correlation;
	
	public CountryCorrelation(int a, int b, double r) {
		
		weo1 = a;
		weo2 = b;
		correlation = r;
	}
	
	public void Print(PrintStream outfile) {
		
		outfile.println(weo1 + " " + weo2 + " " + correlation);
		
		
	}
	
}
