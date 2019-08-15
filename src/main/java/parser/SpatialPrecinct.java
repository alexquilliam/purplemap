package parser;

import java.util.ArrayList;

import org.locationtech.jts.geom.MultiPolygon;

public class SpatialPrecinct implements Comparable<SpatialPrecinct> {
	private int gid;
	private MultiPolygon geometry;
	private ArrayList<String> otherFields;
	
	public SpatialPrecinct(int gid, MultiPolygon geometry, ArrayList<String> otherFields) {
		this.gid = gid;
		this.geometry = geometry;
		this.otherFields = otherFields;
	}

	public int getGid() {
		return gid;
	}

	public MultiPolygon getGeometry() {
		return geometry;
	}

	public ArrayList<String> getOtherFields() {
		return otherFields;
	}
	
	@Override
	public String toString() {
		return "Precinct [gid=" + gid + ", otherFields=" + otherFields +  ", geometry=" + geometry + "]";
	}

	public int compareTo(SpatialPrecinct other) {
		SpatialPrecinct precinct = (SpatialPrecinct) other;
		if(this.gid > precinct.getGid()) {
			return 1;
		}else if(this.gid < precinct.getGid()) {
			return -1;
		}
		
		return 0;
	}
}
