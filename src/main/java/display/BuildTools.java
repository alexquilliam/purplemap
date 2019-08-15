package display;

import java.awt.Color;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import org.geotools.data.DataUtilities;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;

import algorithm.District;
import algorithm.DistrictGraph;
import algorithm.PrecinctGraph;
import loader.Precinct;

public class BuildTools {
	public static MapContent draw(MapContent map, PrecinctGraph precincts) {
		SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
		builder.setName("Precinct");
		builder.add("district", Integer.class);
		builder.setCRS(DefaultGeographicCRS.WGS84);
		builder.add("geometry", MultiPolygon.class);

		SimpleFeatureType type = builder.buildFeatureType();

		TreeMap<Integer, Style> styles = BuildTools.generateStyles(precincts);
		for (Precinct precinct : precincts) {
			SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(type);
			featureBuilder.add(precinct.getDistrict());
			featureBuilder.add(precinct.getGeometry());

			SimpleFeature feature = featureBuilder.buildFeature("Precinct");

			Layer layer = new FeatureLayer(DataUtilities.collection(Arrays.asList(feature)), styles.get(precinct.getDistrict()));
			map.addLayer(layer);
		}

		return map;
	}

	public static MapContent draw(MapContent map, DistrictGraph districts) {
		SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
		builder.setName("District");
		
		builder.add("District", Integer.class);
		builder.add("Trump Votes", Integer.class);
		builder.add("Clinton Votes", Integer.class);
		builder.add("Stein Votes", Integer.class);
		builder.add("Johnson Votes", Integer.class);
		
		builder.add("Population", Integer.class);
		builder.add("White Population", Integer.class);
		builder.add("Hispanic Population", Integer.class);
		builder.add("Black Population", Integer.class);
		builder.add("Asian Population", Integer.class);
		
		builder.setCRS(DefaultGeographicCRS.WGS84);
		builder.add("Geometry", MultiPolygon.class);

		SimpleFeatureType type = builder.buildFeatureType();

		TreeMap<Integer, Style> styles = BuildTools.generateStyles(districts);
		for (District district : districts) {
			SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(type);
			featureBuilder.add(district.getId());
			
			featureBuilder.add(district.getTrumpVotes());
			featureBuilder.add(district.getClintonVotes());
			featureBuilder.add(district.getSteinVotes());
			featureBuilder.add(district.getJohnsonVotes());
			
			featureBuilder.add(district.getPopulation());
			featureBuilder.add(district.getWhitePopulation());
			featureBuilder.add(district.getHispanicPopulation());
			featureBuilder.add(district.getBlackPopulation());
			featureBuilder.add(district.getAsianPopulation());
			
			featureBuilder.add(district.getGeometry());

			SimpleFeature feature = featureBuilder.buildFeature("District");

			Layer layer = new FeatureLayer(DataUtilities.collection(Arrays.asList(feature)), styles.get(district.getId()));
			map.addLayer(layer);
		}

		return map;
	}
	
	private static TreeMap<Integer, Style> generateStyles(PrecinctGraph precincts) {
		TreeMap<Integer, Style> styles = new TreeMap<Integer, Style>();
		for (Precinct precinct : precincts) {
			if(!styles.containsKey(precinct.getDistrict())) {
				Color color = new Color(ThreadLocalRandom.current().nextInt(0, 256), ThreadLocalRandom.current().nextInt(0, 256), ThreadLocalRandom.current().nextInt(0, 256));
				styles.put(precinct.getDistrict(), BuildTools.createStyle(color, 0, color, 1));
			}
		}

		return styles;
	}

	private static TreeMap<Integer, Style> generateStyles(DistrictGraph districts) {
		TreeMap<Integer, Style> styles = new TreeMap<Integer, Style>();
		for (District district : districts) {
			if(!styles.containsKey(district.getId())) {
				Color color = new Color(ThreadLocalRandom.current().nextInt(0, 256), ThreadLocalRandom.current().nextInt(0, 256), ThreadLocalRandom.current().nextInt(0, 256));
				styles.put(district.getId(), BuildTools.createStyle(null, 0, color, 1));
			}
		}

		return styles;
	}
	
	private static Style createStyle(Color line, double width, Color fill, double opacity) {
		StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
		FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory();

		Stroke stroke = null;
		if(line != null && width != 0) {
			stroke = styleFactory.createStroke(filterFactory.literal(line), filterFactory.literal(width), filterFactory.literal(1));
		}

		Fill infill = styleFactory.createFill(filterFactory.literal(fill), filterFactory.literal(opacity));

		PolygonSymbolizer symbolizer = styleFactory.createPolygonSymbolizer(stroke, infill, null);

		Rule rule = styleFactory.createRule();
		rule.symbolizers().add(symbolizer);

		FeatureTypeStyle type = styleFactory.createFeatureTypeStyle(new Rule[] { rule });
		Style style = styleFactory.createStyle();
		style.featureTypeStyles().add(type);

		return style;
	}
}
