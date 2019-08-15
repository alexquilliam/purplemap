package algorithm;

import java.util.ArrayList;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.operation.buffer.BufferOp;
import org.locationtech.jts.operation.buffer.BufferParameters;

import loader.Precinct;

public class District {
	private int id;
	private PrecinctGraph constituents;

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
	
	public District(int id, PrecinctGraph constituents) {
		this.id = id;
		this.constituents = constituents;
	}
	
	public ArrayList<ConflictedEdge> getConflictedEdges() {
		ArrayList<ConflictedEdge> conflictedEdges = new ArrayList<ConflictedEdge>();
		
		for(Precinct precinct : this.getConstituents()) {
			for(Precinct bordering : precinct.getBordering()) {
				if(bordering.getDistrict() != this.getId()) {
					conflictedEdges.add(new ConflictedEdge(precinct, bordering));
				}
			}
		}
		
		return conflictedEdges;
	}
	
	public void updateData() {
		consolidateConstituentData(this.getConstituents());
	}
	
	public void updateGeometry() {
		consolidateConstituentGeometries(this.getConstituents());
	}
	
	private void consolidateConstituentData(PrecinctGraph constituents) {
		this.setTrumpVotes(0);
		this.setClintonVotes(0);
		this.setJohnsonVotes(0);
		this.setSteinVotes(0);
		
		this.setPopulation(0);
		this.setWhitePopulation(0);
		this.setHispanicPopulation(0);
		this.setBlackPopulation(0);
		this.setAsianPopulation(0);
		
		for (Precinct precinct : constituents) {
			this.setTrumpVotes(this.getTrumpVotes() + precinct.getTrumpVotes());
			this.setClintonVotes(this.getClintonVotes() + precinct.getClintonVotes());
			this.setJohnsonVotes(this.getJohnsonVotes() + precinct.getJohnsonVotes());
			this.setSteinVotes(this.getSteinVotes() + precinct.getSteinVotes());
			
			this.setPopulation(this.getPopulation() + precinct.getPopulation());
			this.setWhitePopulation(this.getWhitePopulation() + precinct.getWhitePopulation());
			this.setHispanicPopulation(this.getHispanicPopulation() + precinct.getHispanicPopulation());
			this.setBlackPopulation(this.getBlackPopulation() + precinct.getBlackPopulation());
			this.setAsianPopulation(this.getAsianPopulation() + precinct.getAsianPopulation());
		}
	}
	
	private void consolidateConstituentGeometries(PrecinctGraph constituents) {
		ArrayList<MultiPolygon> geometries = new ArrayList<MultiPolygon>();
		GeometryFactory factory = JTSFactoryFinder.getGeometryFactory(null);
		for (Precinct precinct : constituents) {
			Geometry geometry = inflate(precinct.getGeometry(), 10);
			if(geometry instanceof Polygon) {
				geometries.add((factory.createMultiPolygon(new Polygon[] {(Polygon) geometry})));
			}else {
				geometries.add(((MultiPolygon) geometry));
			}
		}

		GeometryCollection geometryCollection = (GeometryCollection) factory.buildGeometry((geometries));

		Geometry geometry = geometryCollection.union();
		if(geometry instanceof Polygon) {
			this.setGeometry(factory.createMultiPolygon(new Polygon[] {(Polygon) geometry}));
		}else {
			this.setGeometry((MultiPolygon) geometry);
		}
	}
	
	private Geometry inflate(Geometry geometry, double buffer) {
		BufferParameters bufferParameters = new BufferParameters();
		bufferParameters.setEndCapStyle(BufferParameters.CAP_ROUND);
		bufferParameters.setJoinStyle(BufferParameters.JOIN_MITRE);
		
		Geometry buffered = BufferOp.bufferOp(geometry, buffer, bufferParameters);
		buffered.setUserData(geometry.getUserData());
		
		return buffered;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public PrecinctGraph getConstituents() {
		return constituents;
	}

	public void setConstituents(PrecinctGraph constituents) {
		this.constituents = constituents;
		
		updateData();
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

	@Override
	public String toString() {
		return "District [id=" + id + ", trumpVotes=" + trumpVotes + ", clintonVotes=" + clintonVotes + ", johnsonVotes=" + johnsonVotes + ", steinVotes=" + steinVotes + ", population=" + population + ", whitePopulation=" + whitePopulation + ", hispanicPopulation=" + hispanicPopulation + ", blackPopulation=" + blackPopulation + ", asianPopulation=" + asianPopulation + "]";
	}
}
