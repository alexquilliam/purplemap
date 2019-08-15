package algorithm;

import java.util.ArrayList;
import java.util.TreeMap;

import loader.Precinct;

public class DistrictGraph extends ArrayList<District> {
	private static final long serialVersionUID = -6445603330184845681L;
	
	public DistrictGraph(PrecinctGraph precincts) {
		TreeMap<Integer, District> districts = new TreeMap<Integer, District>();
		for(Precinct precinct : precincts) {
			if(!districts.containsKey(precinct.getDistrict())) {
				PrecinctGraph constituents = new PrecinctGraph();
				constituents.add(precinct);
				
				districts.put(precinct.getDistrict(), new District(precinct.getDistrict(), constituents));
			}else {
				District district = districts.get(precinct.getDistrict());
				PrecinctGraph constituents = district.getConstituents();
				
				constituents.add(precinct);
				
				district.setConstituents(constituents);
				districts.put(precinct.getDistrict(), district);
			}
		}
		
		this.addAll(districts.values());
		
		for(District district : this) {
			district.updateGeometry();
		}
	}
}
