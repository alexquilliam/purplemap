package parser;

import java.util.ArrayList;

import org.locationtech.jts.geom.MultiPolygon;

public class SpatialCensusBlock {
	private long geoid;
	private MultiPolygon geometry;
	private ArrayList<String> otherFields;
	
	public SpatialCensusBlock(long geoid, MultiPolygon geometry, ArrayList<String> otherFields) {
		super();
		this.geoid = geoid;
		this.geometry = geometry;
		this.otherFields = otherFields;
	}

	public long getGeoid() {
		return geoid;
	}

	public MultiPolygon getGeometry() {
		return geometry;
	}

	public ArrayList<String> getOtherFields() {
		return otherFields;
	}

	@Override
	public String toString() {
		return "SpatialCensusBlock [geoid=" + geoid + ", otherFields=" + otherFields + "]";
	}
}
