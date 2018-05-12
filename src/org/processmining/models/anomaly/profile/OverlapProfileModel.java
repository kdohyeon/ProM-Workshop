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
	
	private ArrayList<Overlap> overlapList_Act_Res;
	private ArrayList<Overlap> overlapList_Act;
	private ArrayList<Overlap> overlapList_Res;
	
	public OverlapProfileModel(RelationModel relModel) throws ParseException {
		this.relModel = relModel;
		overlapList_Act_Res = new ArrayList<Overlap>();
		overlapList_Act = new ArrayList<Overlap>();
		overlapList_Res = new ArrayList<Overlap>();
				
		getOverlapProfileModel_Act_Res();
		getOverlapProfileModel_Act();
		getOverlapProfileModel_Res();
	}
	
	public void getOverlapProfileModel_Act_Res() throws ParseException {
		int size = relModel.getRelationCardinality();
	
		// get transition
		Map<String, Overlap> overlapMap = new HashMap<String, Overlap>();
		for(int i = 0; i < size; i++) {
			String fromActivityID = relModel.getAntecedentActivityID(i);
			String toActivityID = relModel.getConsequentActivityID(i);
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
			
			overlapList_Act_Res.add(overlapMap.get(key));
			//System.out.println(key + ": " + transitionMap.get(key).getRelationListCardinality());
		}
	}
	
	public void getOverlapProfileModel_Act() throws ParseException {
		int size = relModel.getRelationCardinality();
	
		// get transition
		Map<String, Overlap> overlapMap = new HashMap<String, Overlap>();
		for(int i = 0; i < size; i++) {
			String fromActivityID = relModel.getAntecedentActivityID(i);
			String toActivityID = relModel.getConsequentActivityID(i);
			String relationTypeID = relModel.getRelationType(i);
			
			if(relationTypeID.equals("o") 
					|| relationTypeID.equals("s") 
					|| relationTypeID.equals("d") 
					|| relationTypeID.equals("f")
					|| relationTypeID.equals("=")){
				String key = fromActivityID + "_" + toActivityID + "_" + relationTypeID;

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
					Overlap newOverlap = new Overlap(fromActivityID, toActivityID, "", "", relationTypeID);
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
			
			overlapList_Act.add(overlapMap.get(key));
			//System.out.println(key + ": " + transitionMap.get(key).getRelationListCardinality());
		}
	}
	
	public void getOverlapProfileModel_Res() throws ParseException {
		int size = relModel.getRelationCardinality();
	
		// get transition
		Map<String, Overlap> overlapMap = new HashMap<String, Overlap>();
		for(int i = 0; i < size; i++) {
			String fromResourceID = relModel.getAntecedentResource(i);
			String toResourceID = relModel.getConsequentResource(i);
			String relationTypeID = relModel.getRelationType(i);
			
			if(relationTypeID.equals("o") 
					|| relationTypeID.equals("s") 
					|| relationTypeID.equals("d") 
					|| relationTypeID.equals("f")
					|| relationTypeID.equals("=")){
				String key = fromResourceID + "_" + toResourceID + "_" + relationTypeID;

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
					Overlap newOverlap = new Overlap("", "", fromResourceID, toResourceID, relationTypeID);
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
			
			overlapList_Res.add(overlapMap.get(key));
			//System.out.println(key + ": " + transitionMap.get(key).getRelationListCardinality());
		}
	}
	
	public ArrayList<Overlap> getOverlapList_Act_Res(){
		return overlapList_Act_Res;
	}
	
	public int getOverlapList_Act_Res_Size() {
		return overlapList_Act_Res.size();
	}
	
	public ArrayList<Overlap> getOverlapList_Act(){
		return overlapList_Act;
	}
	
	public int getOverlapList_Act_Size() {
		return overlapList_Act.size();
	}
	
	public ArrayList<Overlap> getOverlapList_Res(){
		return overlapList_Res;
	}
	
	public int getOverlapList_Res_Size() {
		return overlapList_Res.size();
	}	
}
