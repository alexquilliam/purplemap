package algorithm;

import java.util.ArrayList;

import loader.Precinct;

public class PrecinctGraph extends ArrayList<Precinct> {
	private static final long serialVersionUID = 8948930395094799465L;
	
	public PrecinctGraph() {}
	
	public PrecinctGraph(ArrayList<Precinct> precincts) {
		super(precincts);
	}
	
	public ArrayList<Precinct> get() {
		return this;
	}
}
