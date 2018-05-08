package org.processmining.models.anomaly.profile;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.processmining.data.relation.Relation;
import org.processmining.data.trueX.TrueX;
import org.processmining.models.relation.RelationModel;

public class TrueXProfileModel {
private RelationModel relModel;
	
	private ArrayList<TrueX> trueXList;
	
	public TrueXProfileModel(RelationModel relModel) throws ParseException {
		this.relModel = relModel;
		trueXList = new ArrayList<TrueX>();
		
		getTrueXProfileModel();
	}
	
	public void getTrueXProfileModel() throws ParseException {
		int size = relModel.getRelationCardinality();
		
		// get transition
		Map<String, TrueX> trueXMap = new HashMap<String, TrueX>();
		for(int i = 0; i < size; i++) {
			String fromActivityID = relModel.getAntecedentActivity(i);
			String toActivityID = relModel.getConsequentActivity(i);
			String fromResourceID = relModel.getAntecedentResource(i);
			String toResourceID = relModel.getConsequentResource(i);
			String relationTypeID = relModel.getRelationType(i);
			
			if(relationTypeID.equals("o") || relationTypeID.equals("d") || relationTypeID.equals("f")) {
				String key = fromActivityID + "_" + toActivityID + "_" + fromResourceID + "_" + toResourceID + "_" + relationTypeID;

				Relation rel = new Relation(relModel.getRelation(i).getAntecedent(), relModel.getRelation(i).getConsequent());
				
				// check whether those combination is in the hash map
				if(trueXMap.containsKey(key)) {
					// if it is, then add the relation
					TrueX existingTrueX = trueXMap.get(key);
					//System.out.println(transitionMap.get(key).getFromActivityID());
					existingTrueX.addRelation(rel);
					trueXMap.put(key, existingTrueX);
				}else {
					// if it is not, create a new relation
					// add the relation
					TrueX newTrueX = new TrueX(fromActivityID, toActivityID, fromResourceID, toResourceID, relationTypeID);
					newTrueX.addRelation(rel);
					trueXMap.put(key, newTrueX);
				}
			}	
		}
		
		// iter
		Iterator<String> keys = trueXMap.keySet().iterator();
		while(keys.hasNext()) {
			String key = keys.next();
			// for each key, calculate freq, avg, std, min, max, median
			TrueX existingTrueX = trueXMap.get(key);
			
			existingTrueX.calculateFreq();
			existingTrueX.calculateAverage();
			existingTrueX.calculateStdev();
			existingTrueX.calculateaMax();
			existingTrueX.calculateMin();
		
			trueXMap.put(key, existingTrueX);
			
			trueXList.add(trueXMap.get(key));
			//System.out.println(key + ": " + transitionMap.get(key).getRelationListCardinality());
		}
	}
	
	public void printTrueXProfile() {
		
		System.out.println("### TrueX Profile ###");
		
		for(int i = 0; i < trueXList.size(); i++) {
			System.out.println(
					trueXList.get(i).getFromActivityID()
					+ ", " + trueXList.get(i).getToActivityID()
					+ ", " + trueXList.get(i).getFromResourceID()
					+ ", " + trueXList.get(i).getToResourceID()
					+ ", " + trueXList.get(i).getRelation()
					+ ": " + trueXList.get(i).getFreq()
					+ ", " + trueXList.get(i).getAvg()
					+ ", " + trueXList.get(i).getStdev()
					);
		}
	}
}
