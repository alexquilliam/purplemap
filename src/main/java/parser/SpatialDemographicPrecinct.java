package parser;

import java.util.ArrayList;

import org.locationtech.jts.geom.MultiPolygon;

public class SpatialDemographicPrecinct implements Comparable<SpatialDemographicPrecinct> {
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
	
	public SpatialDemographicPrecinct(int gid, String name, String preid, MultiPolygon geometry, ArrayList<Integer> precinctAttributes, ArrayList<Integer> censusBlockGroupAttributes) {
		this.gid = gid;
		this.name = name;
		this.preid = preid;
		
		this.geometry = geometry;
		
		this.trumpVotes = precinctAttributes.get(0);
		this.clintonVotes = precinctAttributes.get(1);
		this.johnsonVotes = precinctAttributes.get(2);
		this.steinVotes = precinctAttributes.get(3);
		
		this.population = censusBlockGroupAttributes.get(0);
		this.hispanicPopulation = censusBlockGroupAttributes.get(1);
		this.whitePopulation = censusBlockGroupAttributes.get(4);
		this.blackPopulation = censusBlockGroupAttributes.get(5) + censusBlockGroupAttributes.get(12) + censusBlockGroupAttributes.get(17) + censusBlockGroupAttributes.get(18) + censusBlockGroupAttributes.get(19) + censusBlockGroupAttributes.get(20);
		this.asianPopulation = censusBlockGroupAttributes.get(7) + censusBlockGroupAttributes.get(14) + censusBlockGroupAttributes.get(24) + censusBlockGroupAttributes.get(25);
		
		int americanIndianPopulation = censusBlockGroupAttributes.get(6) + censusBlockGroupAttributes.get(13) + censusBlockGroupAttributes.get(21) + censusBlockGroupAttributes.get(22) + censusBlockGroupAttributes.get(23);
		int pacificIslanderPopulation = censusBlockGroupAttributes.get(8) + censusBlockGroupAttributes.get(26);
		
		this.asianPopulation += americanIndianPopulation;
		this.asianPopulation += pacificIslanderPopulation;
		
		this.asianPopulation += censusBlockGroupAttributes.get(9) + censusBlockGroupAttributes.get(16) + censusBlockGroupAttributes.get(26);
	}

	public int getGid() {
		return gid;
	}

	public String getName() {
		return name;
	}

	public String getPreid() {
		return preid;
	}

	public MultiPolygon getGeometry() {
		return geometry;
	}

	public int getTrumpVotes() {
		return trumpVotes;
	}
	
	public int getClintonVotes() {
		return clintonVotes;
	}

	public int getJohnsonVotes() {
		return johnsonVotes;
	}

	public int getSteinVotes() {
		return steinVotes;
	}

	public int getPopulation() {
		return population;
	}

	public int getWhitePopulation() {
		return whitePopulation;
	}

	public int getHispanicPopulation() {
		return hispanicPopulation;
	}

	public int getBlackPopulation() {
		return blackPopulation;
	}

	public int getAsianPopulation() {
		return asianPopulation;
	}
	
	@Override
	public String toString() {
		return "SpatialDemographicPrecinct [gid=" + gid + ", name=" + name + ", preid=" + preid + ", geometry=" + geometry + ", trumpVotes=" + trumpVotes + ", clintonVotes=" + clintonVotes + ", johnsonVotes=" + johnsonVotes + ", steinVotes=" + steinVotes + ", population=" + population + ", whitePopulation=" + whitePopulation + ", hispanicPopulation=" + hispanicPopulation + ", blackPopulation=" + blackPopulation + ", asianPopulation=" + asianPopulation + "]";
	}

	public int compareTo(SpatialDemographicPrecinct other) {
		SpatialDemographicPrecinct precinct = (SpatialDemographicPrecinct) other;
		if(this.gid > precinct.getGid()) {
			return 1;
		}else if(this.gid < precinct.getGid()) {
			return -1;
		}
		
		return 0;
	}
}
