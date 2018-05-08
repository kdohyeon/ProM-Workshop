package org.processmining.models.anomaly.profile;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.processmining.data.relation.Relation;
import org.processmining.data.trueY.TrueY;
import org.processmining.models.relation.RelationModel;

public class TrueYProfileModel {
private RelationModel relModel;
	
	private ArrayList<TrueY> trueYList;
	
	public TrueYProfileModel(RelationModel relModel) throws ParseException {
		this.relModel = relModel;
		trueYList = new ArrayList<TrueY>();
		
		getTrueYProfileModel();
	}
	
	public void getTrueYProfileModel() throws ParseException {
		int size = relModel.getRelationCardinality();
		
		// get transition
		Map<String, TrueY> trueYMap = new HashMap<String, TrueY>();
		for(int i = 0; i < size; i++) {
			String fromActivityID = relModel.getAntecedentActivity(i);
			String toActivityID = relModel.getConsequentActivity(i);
			String fromResourceID = relModel.getAntecedentResource(i);
			String toResourceID = relModel.getConsequentResource(i);
			String relationTypeID = relModel.getRelationType(i);
			
			if(relationTypeID.equals("o") || relationTypeID.equals("d") || relationTypeID.equals("s")) {
				String key = fromActivityID + "_" + toActivityID + "_" + fromResourceID + "_" + toResourceID + "_" + relationTypeID;

				Relation rel = new Relation(relModel.getRelation(i).getAntecedent(), relModel.getRelation(i).getConsequent());
				
				// check whether those combination is in the hash map
				if(trueYMap.containsKey(key)) {
					// if it is, then add the relation
					TrueY existingTrueY = trueYMap.get(key);
					//System.out.println(transitionMap.get(key).getFromActivityID());
					existingTrueY.addRelation(rel);
					trueYMap.put(key, existingTrueY);
				}else {
					// if it is not, create a new relation
					// add the relation
					TrueY newTrueY = new TrueY(fromActivityID, toActivityID, fromResourceID, toResourceID, relationTypeID);
					newTrueY.addRelation(rel);
					trueYMap.put(key, newTrueY);
				}
			}	
		}
		
		// iter
		Iterator<String> keys = trueYMap.keySet().iterator();
		while(keys.hasNext()) {
			String key = keys.next();
			// for each key, calculate freq, avg, std, min, max, median
			TrueY existingTrueY = trueYMap.get(key);
			
			existingTrueY.calculateFreq();
			existingTrueY.calculateAverage();
			existingTrueY.calculateStdev();
			existingTrueY.calculateaMax();
			existingTrueY.calculateMin();
		
			trueYMap.put(key, existingTrueY);
			
			trueYList.add(trueYMap.get(key));
			//System.out.println(key + ": " + transitionMap.get(key).getRelationListCardinality());
		}
	}
	
	public void printTrueYProfile() {
		
		System.out.println("### TrueY Profile ###");
		
		for(int i = 0; i < trueYList.size(); i++) {
			System.out.println(
					trueYList.get(i).getFromActivityID()
					+ ", " + trueYList.get(i).getToActivityID()
					+ ", " + trueYList.get(i).getFromResourceID()
					+ ", " + trueYList.get(i).getToResourceID()
					+ ", " + trueYList.get(i).getRelation()
					+ ": " + trueYList.get(i).getFreq()
					+ ", " + trueYList.get(i).getAvg()
					+ ", " + trueYList.get(i).getStdev()
					);
		}
	}
}
