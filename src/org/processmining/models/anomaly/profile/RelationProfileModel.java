package org.processmining.models.anomaly.profile;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.processmining.data.performance.RelationPerformance;
import org.processmining.data.relation.Relation;
import org.processmining.models.relation.RelationModel;

public class RelationProfileModel {
	private RelationModel relModel;
	private ArrayList<RelationPerformance> relationActList;
	
	public RelationProfileModel(RelationModel relModel) throws ParseException {
		this.relModel = relModel;
		relationActList = new ArrayList<RelationPerformance>();
		getRelationProfileModel_Act();
	}

	public void getRelationProfileModel_Act() throws ParseException {
		int size = relModel.getRelationCardinality();
		
		// get transition
		Map<String, RelationPerformance> relationMap = new HashMap<String, RelationPerformance>();
		for(int i = 0; i < size; i++) {
			String fromActivityID = relModel.getAntecedentActivityID(i);
			String toActivityID = relModel.getConsequentActivityID(i);
			String relationTypeID = relModel.getRelationType(i);
			
			String key = fromActivityID + "_" + toActivityID + "_" + relationTypeID;

			Relation rel = new Relation(relModel.getRelation(i).getAntecedent(), relModel.getRelation(i).getConsequent());
			
			// check whether those combination is in the hash map
			if(relationMap.containsKey(key)) {
				// if it is, then add the relation
				RelationPerformance existingTransition = relationMap.get(key);
				//System.out.println(transitionMap.get(key).getFromActivityID());
				existingTransition.addRelation(rel);
				relationMap.put(key, existingTransition);
			}else {
				// if it is not, create a new relation
				// add the relation
				RelationPerformance newTransition = new RelationPerformance(fromActivityID, toActivityID, "", "", relationTypeID);
				newTransition.addRelation(rel);
				relationMap.put(key, newTransition);
			}
		}		
		
		// iter
		Iterator<String> keys = relationMap.keySet().iterator();
		while(keys.hasNext()) {
			String key = keys.next();
			// for each key, calculate freq, avg, std, min, max, median
			RelationPerformance existingTransition = relationMap.get(key);
			
			existingTransition.calculateFreq();
			existingTransition.calculateAverage();
			existingTransition.calculateStdev();
			existingTransition.calculateaMax();
			existingTransition.calculateMin();
		
			relationMap.put(key, existingTransition);
			
			relationActList.add(relationMap.get(key));
			//System.out.println(key + ": " + existingTransition.getAvg() + "+/-" + existingTransition.getStdev());
		}
	}
	
	public int getRelationActListSize() {
		return relationActList.size();
	}
	
	public ArrayList<RelationPerformance> getRelationActList(){
		return relationActList;
	}
	
	
}
