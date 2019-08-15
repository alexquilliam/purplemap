package loader;

import java.util.ArrayList;
import java.util.Arrays;

import org.locationtech.jts.geom.MultiPolygon;

public class Precinct {
	private int gid;
	private String name;
	private String preid;

	private MultiPolygon geometry;

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

	private String[] borderingPreids;
	private Precinct[] bordering = null;
	
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

	public MultiPolygon getGeometry() {
		return geometry;
	}

	public void setGeometry(MultiPolygon geometry) {
		this.geometry = geometry;
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

	public String[] getBorderingPreids() {
		return borderingPreids;
	}

	public void setBorderingPreids(String[] borderingPreids) {
		this.borderingPreids = borderingPreids;
	}

	public Precinct[] getBordering() {
		return bordering;
	}

	public void setBordering(Precinct[] bordering) {
		this.bordering = bordering;
	}

	@Override
	public String toString() {
		return "Precinct [gid=" + gid + ", name=" + name + ", preid=" + preid + ", clintonVotes=" + clintonVotes + ", trumpVotes=" + trumpVotes + ", johnsonVotes=" + johnsonVotes + ", steinVotes=" + steinVotes + ", population=" + population + ", whitePopulation=" + whitePopulation + ", hispanicPopulation=" + hispanicPopulation + ", blackPopulation=" + blackPopulation + ", asianPopulation=" + asianPopulation + ", initialDistrict=" + initialDistrict + ", district=" + district + ", county=" + county + ", countyName=" + countyName + ", countyIdentifier=" + countyIdentifier + ", borderingPreids=" + Arrays.toString(borderingPreids) + ", bordering=" + formatBordering(bordering) + /*", geometery=" + geometery +*/ "]";
	}
	
	private String formatBordering(Precinct[] bordering) {
		if(bordering == null) {
			return null;
		}
		
		ArrayList<String> formatted = new ArrayList<String>();
		for(Precinct precinct : bordering) {
			formatted.add(precinct.getPreid());
		}
		
		return "[" + String.join(", ", formatted) + "]";
	}
}
