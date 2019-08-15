package loader;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class Loader {
	public static ArrayList<Precinct> load(String state) {
		ArrayList<Precinct> precincts = null;
		try {
			precincts = Loader.deserialize(new String(Files.readAllBytes(Paths.get(State.MD2011))));
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i < precincts.size(); i++) {
			Precinct precinct = precincts.get(i);
			
			String[] borderingPreids = precinct.getBorderingPreids();
			Precinct[] bordering = new Precinct[borderingPreids.length];
			for(int j = 0; j < borderingPreids.length; j++) {
				String preid = borderingPreids[j];
				for(Precinct current : precincts) {
					if(current.getPreid().equals(preid)) {
						bordering[j] = current;
						
						break;
					}
				}
			}
			
			precinct.setBordering(bordering);
			
			precincts.set(i, precinct);
		}
		
		return precincts;
	}
	
	private static ArrayList<Precinct> deserialize(String json) {
		Type type = new TypeToken<ArrayList<SerializablePrecinct>>(){}.getType();
		ArrayList<SerializablePrecinct> serializable = new GsonBuilder().serializeSpecialFloatingPointValues().create().fromJson(json, type);
		
		ArrayList<Precinct> precincts = new ArrayList<Precinct>();
		for(SerializablePrecinct precinct : serializable) {
			precincts.add(precinct.precinct());
		}
		
		return precincts;
	}
}
