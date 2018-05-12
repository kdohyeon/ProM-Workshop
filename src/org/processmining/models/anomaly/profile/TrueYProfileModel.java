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
	
	private ArrayList<TrueY> trueYList_Act_Res;
	private ArrayList<TrueY> trueYList_Act;
	private ArrayList<TrueY> trueYList_Res;
	
	public TrueYProfileModel(RelationModel relModel) throws ParseException {
		this.relModel = relModel;
		trueYList_Act_Res = new ArrayList<TrueY>();
		trueYList_Act = new ArrayList<TrueY>();
		trueYList_Res = new ArrayList<TrueY>();
		
		getTrueYProfileModel_Act_Res();
		getTrueYProfileModel_Act();
		getTrueYProfileModel_Res();
	}
	
	public void getTrueYProfileModel_Act_Res() throws ParseException {
		int size = relModel.getRelationCardinality();
		
		// get transition
		Map<String, TrueY> trueYMap = new HashMap<String, TrueY>();
		for(int i = 0; i < size; i++) {
			String fromActivityID = relModel.getAntecedentActivityID(i);
			String toActivityID = relModel.getConsequentActivityID(i);
			String fromResourceID = relModel.getAntecedentResource(i);
			String toResourceID = relModel.getConsequentResource(i);
			String relationTypeID = relModel.getRelationType(i);
			
			if(relationTypeID.equals("o") || relationTypeID.equals("d") || relationTypeID.equals("f")) {
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
			
			trueYList_Act_Res.add(trueYMap.get(key));
			//System.out.println(key + ": " + transitionMap.get(key).getRelationListCardinality());
		}
	}
	
	public void getTrueYProfileModel_Act() throws ParseException {
		int size = relModel.getRelationCardinality();
		
		// get transition
		Map<String, TrueY> trueYMap = new HashMap<String, TrueY>();
		for(int i = 0; i < size; i++) {
			String fromActivityID = relModel.getAntecedentActivityID(i);
			String toActivityID = relModel.getConsequentActivityID(i);
			String relationTypeID = relModel.getRelationType(i);
			
			if(relationTypeID.equals("o") || relationTypeID.equals("d") || relationTypeID.equals("f")) {
				String key = fromActivityID + "_" + toActivityID + "_" + relationTypeID;

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
					TrueY newTrueY = new TrueY(fromActivityID, toActivityID, "", "", relationTypeID);
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
			
			trueYList_Act.add(trueYMap.get(key));
			//System.out.println(key + ": " + transitionMap.get(key).getRelationListCardinality());
		}
	}
	
	public void getTrueYProfileModel_Res() throws ParseException {
		int size = relModel.getRelationCardinality();
		
		// get transition
		Map<String, TrueY> trueYMap = new HashMap<String, TrueY>();
		for(int i = 0; i < size; i++) {
			String fromResourceID = relModel.getAntecedentResource(i);
			String toResourceID = relModel.getConsequentResource(i);
			String relationTypeID = relModel.getRelationType(i);
			
			if(relationTypeID.equals("o") || relationTypeID.equals("d") || relationTypeID.equals("f")) {
				String key = fromResourceID + "_" + toResourceID + "_" + relationTypeID;

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
					TrueY newTrueY = new TrueY("", "", fromResourceID, toResourceID, relationTypeID);
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
			
			trueYList_Res.add(trueYMap.get(key));
			//System.out.println(key + ": " + transitionMap.get(key).getRelationListCardinality());
		}
	}
	
	public ArrayList<TrueY> getTrueYList_Act_Res(){
		return trueYList_Act_Res;
	}
	
	public int getTrueYList_Act_Res_Size(){
		return trueYList_Act_Res.size();
	}
	
	public ArrayList<TrueY> getTrueYList_Act(){
		return trueYList_Act;
	}
	
	public int getTrueYList_Act_Size(){
		return trueYList_Act.size();
	}
	
	public ArrayList<TrueY> getTrueY_Res_List(){
		return trueYList_Res;
	}
	
	public int getTrueYList_Res_Size(){
		return trueYList_Res.size();
	}
}