package org.processmining.models.anomaly.profile;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.processmining.data.overlap.Overlap;
import org.processmining.data.relation.Relation;
import org.processmining.models.relation.RelationModel;

public class OverlapProfileModel {
	private RelationModel relModel;
	
	private ArrayList<Overlap> overlapList;
	
	public OverlapProfileModel(RelationModel relModel) throws ParseException {
		this.relModel = relModel;
		overlapList = new ArrayList<Overlap>();
		
		getOverlapProfileModel();
	}
	
	public void getOverlapProfileModel() throws ParseException {
		int size = relModel.getRelationCardinality();
	
		// get transition
		Map<String, Overlap> overlapMap = new HashMap<String, Overlap>();
		for(int i = 0; i < size; i++) {
			String fromActivityID = relModel.getAntecedentActivity(i);
			String toActivityID = relModel.getConsequentActivity(i);
			String fromResourceID = relModel.getAntecedentResource(i);
			String toResourceID = relModel.getConsequentResource(i);
			String relationTypeID = relModel.getRelationType(i);
			
			if(relationTypeID.equals("o") 
					|| relationTypeID.equals("s") 
					|| relationTypeID.equals("d") 
					|| relationTypeID.equals("f")
					|| relationTypeID.equals("=")){
				String key = fromActivityID + "_" + toActivityID + "_" + fromResourceID + "_" + toResourceID + "_" + relationTypeID;

				Relation rel = new Relation(relModel.getRelation(i).getAntecedent(), relModel.getRelation(i).getConsequent());
				
				// check whether those combination is in the hash map
				if(overlapMap.containsKey(key)) {
					// if it is, then add the relation
					Overlap existingOverlap = overlapMap.get(key);
					//System.out.println(transitionMap.get(key).getFromActivityID());
					existingOverlap.addRelation(rel);
					overlapMap.put(key, existingOverlap);
				}else {
					// if it is not, create a new relation
					// add the relation
					Overlap newOverlap = new Overlap(fromActivityID, toActivityID, fromResourceID, toResourceID, relationTypeID);
					newOverlap.addRelation(rel);
					overlapMap.put(key, newOverlap);
				}		
			}
		}
		
		// iter
		Iterator<String> keys = overlapMap.keySet().iterator();
		while(keys.hasNext()) {
			String key = keys.next();
			// for each key, calculate freq, avg, std, min, max, median
			Overlap existingOverlap = overlapMap.get(key);
			
			existingOverlap.calculateFreq();
			existingOverlap.calculateAverage();
			existingOverlap.calculateStdev();
			existingOverlap.calculateaMax();
			existingOverlap.calculateMin();
		
			overlapMap.put(key, existingOverlap);
			
			overlapList.add(overlapMap.get(key));
			//System.out.println(key + ": " + transitionMap.get(key).getRelationListCardinality());
		}
	}
	
	public ArrayList<Overlap> getOverlapList(){
		return overlapList;
	}
	
	public int getOverlapListSize() {
		return overlapList.size();
	}
	
	public void printOverlapProfile() {
		
		System.out.println("### Overlap Profile ###");
		
		for(int i = 0; i < overlapList.size(); i++) {
			System.out.println(
					overlapList.get(i).getFromActivityID()
					+ ", " + overlapList.get(i).getToActivityID()
					+ ", " + overlapList.get(i).getFromResourceID()
					+ ", " + overlapList.get(i).getToResourceID()
					+ ", " + overlapList.get(i).getRelation()
					+ ": " + overlapList.get(i).getFreq()
					+ ", " + overlapList.get(i).getAvg()
					+ ", " + overlapList.get(i).getStdev()
					);
		}
	}
}
