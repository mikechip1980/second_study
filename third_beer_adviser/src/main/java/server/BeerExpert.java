package server;

import java.util.ArrayList;

public class BeerExpert {
	public ArrayList getBrands(String color ) {
		ArrayList brands =  new ArrayList();
		if (color.equals("amber")) {
			brands.add("Jack Amber");
			brands.add("Red Moose");
		}
		else {
			brands.add("Guiness");
			brands.add("Gout Stout");

		}
	return (brands);		
	}

}
