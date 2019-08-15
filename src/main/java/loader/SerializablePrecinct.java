package loader;

import java.util.Arrays;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.precision.GeometryPrecisionReducer;

public class SerializablePrecinct {
	private int gid;
	private String name;
	private String preid;

	private String geometery;

	private int trumpVotes;
	private int clintonVotes;
	private int johnsonVotes;
	private int steinVotes;

	private int population;
	private int whitePopulation;
	private int hispanicPopulation;
	private int blackPopulation;
	private int asianPopulation;

	private int initialDistrict;
	private int district;

	private int county;
	private String countyName;
	private String countyIdentifier;

	private String[] bordering;
	
	public SerializablePrecinct(Precinct precinct) {
		this.setGid(precinct.getGid());
		this.setName(precinct.getName());
		this.setPreid(precinct.getPreid());
		
		this.setGeometery(precinct.getGeometry().toString());
		
		this.setTrumpVotes(precinct.getTrumpVotes());
		this.setClintonVotes(precinct.getClintonVotes());
		this.setJohnsonVotes(precinct.getJohnsonVotes());
		this.setSteinVotes(precinct.getSteinVotes());
		
		this.setPopulation(precinct.getPopulation());
		this.setWhitePopulation(precinct.getWhitePopulation());
		this.setHispanicPopulation(precinct.getHispanicPopulation());
		this.setBlackPopulation(precinct.getBlackPopulation());
		this.setAsianPopulation(precinct.getAsianPopulation());
		
		this.setInitialDistrict(precinct.getInitialDistrict());
		this.setDistrict(precinct.getDistrict());
		
		this.setCounty(precinct.getCounty());
		this.setCountyName(precinct.getCountyName());
		this.setCountyIdentifier(precinct.getCountyIdentifier());
		
		this.setBordering(precinct.getBorderingPreids());
	}
	
	public Precinct precinct() {
		Precinct precinct = new Precinct();
		
		precinct.setGid(this.getGid());
		precinct.setName(this.getName());
		precinct.setPreid(this.getPreid());
		
		PrecisionModel precision = new PrecisionModel(100);
		GeometryPrecisionReducer reducer = new GeometryPrecisionReducer(precision);
		
		MultiPolygon geometry = null;
		try {
			geometry = (MultiPolygon) new WKTReader(JTSFactoryFinder.getGeometryFactory(null)).read(this.getGeometery());
		}catch (Exception e) {
			e.printStackTrace();
		}

		Geometry raw = reducer.reduce(geometry);
		if(raw instanceof Polygon) {
			geometry = new MultiPolygon(new Polygon[]{(Polygon) raw}, JTSFactoryFinder.getGeometryFactory(null));
		}else {
			geometry = (MultiPolygon) reducer.reduce(geometry);
		}
		
		precinct.setGeometry(geometry);
		
		precinct.setTrumpVotes(this.getTrumpVotes());
		precinct.setClintonVotes(this.getClintonVotes());
		precinct.setJohnsonVotes(this.getJohnsonVotes());
		precinct.setSteinVotes(this.getSteinVotes());
		
		precinct.setPopulation(this.getPopulation());
		precinct.setWhitePopulation(this.getWhitePopulation());
		precinct.setHispanicPopulation(this.getHispanicPopulation());
		precinct.setBlackPopulation(this.getBlackPopulation());
		precinct.setAsianPopulation(this.getAsianPopulation());
		
		precinct.setInitialDistrict(this.getInitialDistrict());
		precinct.setDistrict(this.getDistrict());
		
		precinct.setCounty(this.getCounty());
		precinct.setCountyName(this.getCountyName());
		precinct.setCountyIdentifier(this.getCountyIdentifier());
		
		precinct.setBorderingPreids(this.getBordering());
		
		return precinct;
	}

	public int getGid() {
		return gid;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPreid() {
		return preid;
	}

	public void setPreid(String preid) {
		this.preid = preid;
	}

	public String getGeometery() {
		return geometery;
	}

	public void setGeometery(String geometery) {
		this.geometery = geometery;
	}

	public int getTrumpVotes() {
		return trumpVotes;
	}

	public void setTrumpVotes(int trumpVotes) {
		this.trumpVotes = trumpVotes;
	}

	public int getClintonVotes() {
		return clintonVotes;
	}

	public void setClintonVotes(int clintonVotes) {
		this.clintonVotes = clintonVotes;
	}

	public int getJohnsonVotes() {
		return johnsonVotes;
	}

	public void setJohnsonVotes(int johnsonVotes) {
		this.johnsonVotes = johnsonVotes;
	}

	public int getSteinVotes() {
		return steinVotes;
	}

	public void setSteinVotes(int steinVotes) {
		this.steinVotes = steinVotes;
	}

	public int getPopulation() {
		return population;
	}

	public void setPopulation(int population) {
		this.population = population;
	}

	public int getWhitePopulation() {
		return whitePopulation;
	}

	public void setWhitePopulation(int whitePopulation) {
		this.whitePopulation = whitePopulation;
	}

	public int getHispanicPopulation() {
		return hispanicPopulation;
	}

	public void setHispanicPopulation(int hispanicPopulation) {
		this.hispanicPopulation = hispanicPopulation;
	}

	public int getBlackPopulation() {
		return blackPopulation;
	}

	public void setBlackPopulation(int blackPopulation) {
		this.blackPopulation = blackPopulation;
	}

	public int getAsianPopulation() {
		return asianPopulation;
	}

	public void setAsianPopulation(int asianPopulation) {
		this.asianPopulation = asianPopulation;
	}

	public int getInitialDistrict() {
		return initialDistrict;
	}

	public void setInitialDistrict(int initialDistrict) {
		this.initialDistrict = initialDistrict;
	}

	public int getDistrict() {
		return district;
	}

	public void setDistrict(int district) {
		this.district = district;
	}

	public int getCounty() {
		return county;
	}

	public void setCounty(int county) {
		this.county = county;
	}

	public String getCountyName() {
		return countyName;
	}

	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}

	public String getCountyIdentifier() {
		return countyIdentifier;
	}

	public void setCountyIdentifier(String countyIdentifier) {
		this.countyIdentifier = countyIdentifier;
	}

	public String[] getBordering() {
		return bordering;
	}

	public void setBordering(String[] bordering) {
		this.bordering = bordering;
	}

	@Override
	public String toString() {
		return "SerializablePrecinct [gid=" + gid + ", name=" + name + ", preid=" + preid + ", geometery=" + geometery + ", trumpVotes=" + trumpVotes + ", clintonVotes=" + clintonVotes + ", johnsonVotes=" + johnsonVotes + ", steinVotes=" + steinVotes + ", population=" + population + ", whitePopulation=" + whitePopulation + ", hispanicPopulation=" + hispanicPopulation + ", blackPopulation=" + blackPopulation + ", asianPopulation=" + asianPopulation + ", initialDistrict=" + initialDistrict + ", district=" + district + ", county=" + county + ", countyName=" + countyName + ", countyIdentifier=" + countyIdentifier + ", bordering=" + Arrays.toString(bordering) + "]";
	}
}
