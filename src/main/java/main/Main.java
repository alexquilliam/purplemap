package main;

import algorithm.DistrictGraph;
import algorithm.PrecinctGraph;
import display.MapDisplay;
import loader.Loader;
import loader.State;

public class Main {
	public Main() {
		PrecinctGraph precincts = new PrecinctGraph(Loader.load(State.MD2011));
		DistrictGraph districts = new DistrictGraph(precincts);
		
		MapDisplay map = new MapDisplay(districts);
		map.display();
	}

	public static void main(String[] args) {
		new Main();
	}
}
