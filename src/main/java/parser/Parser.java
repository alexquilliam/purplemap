package parser;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.precision.GeometryPrecisionReducer;
import org.postgis.PGgeometry;
import org.postgresql.PGConnection;
import org.postgresql.util.PGobject;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import loader.Precinct;
import loader.SerializablePrecinct;

public class Parser {
	private static final double PRECISION = 100;
	
	public static ArrayList<Precinct> parse(String username, String password, String database, String precinctTable, String censusBlockTable, String districtTable) {
		ArrayList<SpatialPrecinct> precincts = Parser.readPrecincts(username, password, database, precinctTable);
		System.out.println("Read precincts...");
		ArrayList<SpatialCensusBlock> censusBlocks = Parser.readCensusBlocks(username, password, database, censusBlockTable);
		System.out.println("Read census blocks...");
		ArrayList<SpatialDistrict> districts = Parser.readDistricts(username, password, database, districtTable);
		System.out.println("Read districts...");
		
		ArrayList<SpatialDemographicPrecinct> assignedDemographics = Parser.assignDemographics(precincts, censusBlocks);
		System.out.println("Assigned demographics...");
		
		ArrayList<Precinct> assignedPrecincts = Parser.assignInitialDistrict(assignedDemographics, districts);
		System.out.println("Assigned districts...");
		
		ArrayList<Precinct> assignedCounties = Parser.assignCounty(assignedPrecincts);
		System.out.println("Assigned counties...");
		
		ArrayList<Precinct> assignedBorderingPrecincts = Parser.assignBorderingPrecincts(assignedCounties);
		System.out.println("Assigned bordering precincts...");
		
		return assignedBorderingPrecincts;
	}
	
	public static String serialize(ArrayList<Precinct> precincts) {
		ArrayList<SerializablePrecinct> serializable = new ArrayList<SerializablePrecinct>();
		for(Precinct precinct : precincts) {
			serializable.add(new SerializablePrecinct(precinct));
		}
		
		Type type = new TypeToken<ArrayList<SerializablePrecinct>>(){}.getType();
		return new GsonBuilder().serializeSpecialFloatingPointValues().create().toJson(serializable, type);
	}
	
	private static ArrayList<Precinct> assignBorderingPrecincts(ArrayList<Precinct> precincts) {
		for(int i = 0; i < precincts.size(); i++) {
			Precinct precinct = precincts.get(i);
			
			ArrayList<Precinct> bordering = new ArrayList<Precinct>();
			ArrayList<String> borderingPreids = new ArrayList<String>();
			for(Precinct border : precincts) {
				if(border.equals(precinct)) {
					continue;
				}
				
				if(precinct.getGeometry().intersects(border.getGeometry())) {
					bordering.add(border);
					borderingPreids.add(border.getPreid());
				}
			}
			
			precinct.setBordering(bordering.toArray(new Precinct[0])); 
			precinct.setBorderingPreids(borderingPreids.toArray(new String[0]));
			
			precincts.set(i, precinct);
		}
		
		return precincts;
	}
	
	private static ArrayList<Precinct> assignCounty(ArrayList<Precinct> precincts) {
		TreeMap<String, Integer> names = new TreeMap<String, Integer>();
		int id = 1;
		for(int i = 0; i < precincts.size(); i++) {
			Precinct precinct = precincts.get(i);
			
			if(precinct.getPreid().equals("NO DATA")) {
				precinct.setCounty(0);
				precinct.setCountyName("NO DATA");
				precinct.setCountyIdentifier("NO DATA");
				
				continue;
			}
			
			String[] parts = precinct.getName().split(" ");
			String name = String.join(" ", Arrays.copyOfRange(parts, 0, parts.length - 2));
			if(!names.containsKey(name)) {
				names.put(name, id);
				id++;
			}
			
			String identifier = precinct.getPreid().split("-")[0];
			
			precinct.setCounty(names.get(name));
			precinct.setCountyName(name);
			precinct.setCountyIdentifier(identifier);
			
			precincts.set(i, precinct);
		}
		
		return precincts;
	}
	
	private static ArrayList<Precinct> assignInitialDistrict(ArrayList<SpatialDemographicPrecinct> precincts, ArrayList<SpatialDistrict> districts) {
		ArrayList<Precinct> assignedPrecincts = new ArrayList<Precinct>();
		for(SpatialDemographicPrecinct precinct : precincts) {
			if(precinct == null) {
				continue;
			}
			
			ArrayList<SpatialDistrict> containing = new ArrayList<SpatialDistrict>();
			for(SpatialDistrict district : districts) {
				if(precinct.getGeometry().intersects(district.getGeometry())) {
					containing.add(district);
				}
			}
			
			TreeMap<SpatialDistrict, Double> overlapRatios = new TreeMap<SpatialDistrict, Double>();
			for(SpatialDistrict district : containing) {
				double intersection = precinct.getGeometry().intersection(district.getGeometry()).getArea();
				
				overlapRatios.put(district, intersection);
			}
			
			Map.Entry<SpatialDistrict, Double> max = null;
			for(Map.Entry<SpatialDistrict, Double> pair : overlapRatios.entrySet()) {
				if(max == null || pair.getValue() > max.getValue()) {
					max = pair;
				}
			}
			
			if(max == null) {
				continue;
			}
			
			Precinct assignedPrecinct = new Precinct();
			
			assignedPrecinct.setGid(precinct.getGid());
			assignedPrecinct.setName(precinct.getName());
			assignedPrecinct.setPreid(precinct.getPreid());
			
			assignedPrecinct.setGeometry(precinct.getGeometry());
			assignedPrecinct.setTrumpVotes(precinct.getTrumpVotes());
			assignedPrecinct.setClintonVotes(precinct.getClintonVotes());
			assignedPrecinct.setJohnsonVotes(precinct.getJohnsonVotes());
			assignedPrecinct.setSteinVotes(precinct.getSteinVotes());
			
			assignedPrecinct.setPopulation(precinct.getPopulation());
			assignedPrecinct.setWhitePopulation(precinct.getWhitePopulation());
			assignedPrecinct.setHispanicPopulation(precinct.getHispanicPopulation());
			assignedPrecinct.setBlackPopulation(precinct.getBlackPopulation());
			assignedPrecinct.setAsianPopulation(precinct.getAsianPopulation());
			
			assignedPrecinct.setInitialDistrict(max.getKey().getId());
			assignedPrecinct.setDistrict((max.getKey().getId()));
			
			assignedPrecincts.add(assignedPrecinct);
		}
		
		return assignedPrecincts;
	}
	
	private static ArrayList<SpatialDemographicPrecinct> assignDemographics(ArrayList<SpatialPrecinct> precincts, ArrayList<SpatialCensusBlock> censusBlocks) {
		ArrayList<SpatialDemographicPrecinct> assignedDemographics = new ArrayList<SpatialDemographicPrecinct>();
		for(SpatialPrecinct precinct : precincts) {
			ArrayList<SpatialCensusBlock> intersecting = new ArrayList<SpatialCensusBlock>();
			for(SpatialCensusBlock censusBlock : censusBlocks) {
				if(precinct.getGeometry().intersects(censusBlock.getGeometry())) {
					intersecting.add(censusBlock);
				}
			}
			
			int n = 0;
			ArrayList<Integer> demographics = new ArrayList<Integer>();
			for(SpatialCensusBlock censusBlock : intersecting) {
				double intersectionProportion = precinct.getGeometry().intersection(censusBlock.getGeometry()).getArea() / (censusBlock.getGeometry().getArea());
				
				if(intersectionProportion > .0001) {
					for(int j = 17; j < censusBlock.getOtherFields().size(); j++) {
						int demographic = (int) Math.round(Integer.parseInt(censusBlock.getOtherFields().get(j)) * intersectionProportion);
						
						if(n == 0) {
							demographics.add(demographic);
						}else {
							demographics.set(j - 17, demographics.get(j - 17) + demographic);
						}
					}
					
					n++;
				}
			}
			
			int gid = precinct.getGid();
			String name = precinct.getOtherFields().get(1);
			String preid = precinct.getOtherFields().get(3);
			if(preid == null) {
				preid = "NO DATA";
			}
			
			MultiPolygon geometry = precinct.getGeometry();
			
			ArrayList<Integer> votes = new ArrayList<Integer>();
			for(String count : precinct.getOtherFields().subList(4, 8)) {
				votes.add(Integer.parseInt(count));
			}
			
			SpatialDemographicPrecinct demographicPrecinct = new SpatialDemographicPrecinct(gid, name, preid, geometry, votes, demographics);
			
			assignedDemographics.add(demographicPrecinct);
		}
		
		return assignedDemographics;
	}
	
	@SuppressWarnings("unchecked")
	private static ArrayList<SpatialDistrict> readDistricts(String username, String password, String database, String table) {
		ArrayList<SpatialDistrict> districts = new ArrayList<SpatialDistrict>();
		
		try {
			Class.forName("org.postgresql.Driver");
			Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + database, "postgres", password);

			((PGConnection) connection).addDataType("geometry", (Class<? extends PGobject>) Class.forName("org.postgis.PGgeometry"));

			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery("SELECT * FROM " + table);
			
			while (results.next()) {
				PrecisionModel precision = new PrecisionModel(Parser.PRECISION);
				GeometryPrecisionReducer reducer = new GeometryPrecisionReducer(precision);
				
				MultiPolygon geometry = (MultiPolygon) new WKTReader(JTSFactoryFinder.getGeometryFactory(null)).read(((PGgeometry) results.getObject(16)).toString().split(";")[1]);

				Geometry raw = reducer.reduce(geometry);
				if(raw instanceof Polygon) {
					geometry = new MultiPolygon(new Polygon[]{(Polygon) raw}, JTSFactoryFinder.getGeometryFactory(null));
				}else {
					geometry = (MultiPolygon) reducer.reduce(geometry);
				}
				
				ArrayList<String> otherFields = new ArrayList<String>();
				for(int i = 2; i < 16; i++) {
					otherFields.add(results.getString(i));
				}
				
				int id = Integer.parseInt(otherFields.get(1));
				
				districts.add(new SpatialDistrict(id, geometry, otherFields));
			}
			
			statement.close();
			connection.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return districts;
	}
	
	@SuppressWarnings("unchecked")
	private static ArrayList<SpatialPrecinct> readPrecincts(String username, String password, String database, String table) {
		ArrayList<SpatialPrecinct> precincts = new ArrayList<SpatialPrecinct>();
		
		try {
			Class.forName("org.postgresql.Driver");
			Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + database, "postgres", password);

			((PGConnection) connection).addDataType("geometry", (Class<? extends PGobject>) Class.forName("org.postgis.PGgeometry"));

			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery("SELECT * FROM " + table);
			
			while (results.next()) {
				PrecisionModel precision = new PrecisionModel(Parser.PRECISION);
				GeometryPrecisionReducer reducer = new GeometryPrecisionReducer(precision);
				
				MultiPolygon geometry = (MultiPolygon) new WKTReader(JTSFactoryFinder.getGeometryFactory(null)).read(((PGgeometry) results.getObject(50)).toString().split(";")[1]);

				Geometry raw = reducer.reduce(geometry);
				if(raw instanceof Polygon) {
					geometry = new MultiPolygon(new Polygon[]{(Polygon) raw}, JTSFactoryFinder.getGeometryFactory(null));
				}else {
					geometry = (MultiPolygon) reducer.reduce(geometry);
				}

				int gid = results.getInt(1);
				
				ArrayList<String> otherFields = new ArrayList<String>();
				for(int i = 2; i < 50; i++) {
					otherFields.add(results.getString(i));
				}
				
				precincts.add(new SpatialPrecinct(gid, geometry, otherFields));
			}
			
			statement.close();
			connection.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return precincts;
	}
	
	@SuppressWarnings("unchecked")
	private static ArrayList<SpatialCensusBlock> readCensusBlocks(String username, String password, String database, String table) {
		ArrayList<SpatialCensusBlock> censusBlockGroups = new ArrayList<SpatialCensusBlock>();
		
		try {
			Class.forName("org.postgresql.Driver");
			Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + database, "postgres", password);

			((PGConnection) connection).addDataType("geometry", (Class<? extends PGobject>) Class.forName("org.postgis.PGgeometry"));

			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery("SELECT * FROM " + table);
			
			while (results.next()) {
				PrecisionModel precision = new PrecisionModel(Parser.PRECISION);
				GeometryPrecisionReducer reducer = new GeometryPrecisionReducer(precision);
				
				MultiPolygon geometry = (MultiPolygon) new WKTReader(JTSFactoryFinder.getGeometryFactory(null)).read(((PGgeometry) results.getObject(92)).toString().split(";")[1]);

				Geometry raw = reducer.reduce(geometry);
				if(raw instanceof Polygon) {
					geometry = new MultiPolygon(new Polygon[]{(Polygon) raw}, JTSFactoryFinder.getGeometryFactory(null));
				}else {
					geometry = (MultiPolygon) reducer.reduce(geometry);
				}
				
				long geoid = results.getLong(6);
				
				ArrayList<String> otherFields = new ArrayList<String>();
				for(int i = 2; i < 92; i++) {
					otherFields.add(results.getString(i));
				}
				
				otherFields.set(17, otherFields.get(17).split("\\(")[0]);
				
				censusBlockGroups.add(new SpatialCensusBlock(geoid, geometry, otherFields));
			}
			
			statement.close();
			connection.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return censusBlockGroups;
	}
}
