package display;

import java.awt.Dimension;

import org.geotools.map.MapContent;
import org.geotools.swing.JMapFrame;

import algorithm.DistrictGraph;
import algorithm.PrecinctGraph;

public class MapDisplay {
	private PrecinctGraph precincts;
	private DistrictGraph districts;

	public MapDisplay(PrecinctGraph precincts) {
		this.precincts = precincts;
	}

	public MapDisplay(DistrictGraph districts) {
		this.districts = districts;
	}

	public void display() {
		MapContent map = new MapContent();
		map.setTitle("purplemap");

		JMapFrame mapFrame = new JMapFrame(map);
		mapFrame.enableToolBar(true);

		if(this.getPrecincts() != null) {
			map = BuildTools.draw(map, this.getPrecincts());
		}else if(this.getDistricts() != null) {
			map = BuildTools.draw(map, this.getDistricts());
		}
		
		mapFrame.setExtendedState(JMapFrame.MAXIMIZED_BOTH);
		mapFrame.setMinimumSize(new Dimension(800, 600));
		mapFrame.setVisible(true);
		mapFrame.requestFocus();
	}

	public PrecinctGraph getPrecincts() {
		return precincts;
	}

	public DistrictGraph getDistricts() {
		return districts;
	}
}