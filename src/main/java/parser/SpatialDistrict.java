package parser;

import java.util.ArrayList;

import org.locationtech.jts.geom.MultiPolygon;

public class SpatialDistrict implements Comparable<SpatialDistrict> {
	private int id;
	private MultiPolygon geometry;
	private ArrayList<String> otherFields;
	
	public SpatialDistrict(int id, MultiPolygon geometry, ArrayList<String> otherFields) {
		this.id = id;
		this.geometry = geometry;
		this.otherFields = otherFields;
	}

	public int getId() {
		return id;
	}

	public MultiPolygon getGeometry() {
		return geometry;
	}

	public ArrayList<String> getOtherFields() {
		return otherFields;
	}

	@Override
	public String toString() {
		return "SpatialDistrict [id=" + id + ", otherFields=" + otherFields + ", geometry=" + geometry + "]";
	}
	
	public int compareTo(SpatialDistrict other) {
		SpatialDistrict district = (SpatialDistrict) other;
		if(this.id > district.getId()) {
			return 1;
		}else if(this.id < district.getId()) {
			return -1;
		}
		
		return 0;
	}
}
