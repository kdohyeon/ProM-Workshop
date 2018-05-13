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
	
	private ArrayList<TrueX> trueXList_Act_Res;
	private ArrayList<TrueX> trueXList_Act;
	private ArrayList<TrueX> trueXList_Res;
	
	public TrueXProfileModel(RelationModel relModel) throws ParseException {
		this.relModel = relModel;
		trueXList_Act_Res = new ArrayList<TrueX>();
		trueXList_Act = new ArrayList<TrueX>();
		trueXList_Res = new ArrayList<TrueX>();
		
		getTrueXProfileModel_Act_Res();
		getTrueXProfileModel_Act();
		getTrueXProfileModel_Res();
	}
	
	public void getTrueXProfileModel_Act_Res() throws ParseException {
		int size = relModel.getRelationCardinality();
		
		// get transition
		Map<String, TrueX> trueXMap = new HashMap<String, TrueX>();
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
			
			trueXList_Act_Res.add(trueXMap.get(key));
			//System.out.println(key + ": " + transitionMap.get(key).getRelationListCardinality());
		}
	}
	
	public void getTrueXProfileModel_Act() throws ParseException {
		int size = relModel.getRelationCardinality();
		
		// get transition
		Map<String, TrueX> trueXMap = new HashMap<String, TrueX>();
		for(int i = 0; i < size; i++) {
			String fromActivityID = relModel.getAntecedentActivityID(i);
			String toActivityID = relModel.getConsequentActivityID(i);
			String relationTypeID = relModel.getRelationType(i);
			
			if(relationTypeID.equals("o") || relationTypeID.equals("d") || relationTypeID.equals("f")) {
				String key = fromActivityID + "_" + toActivityID + "_" + relationTypeID;

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
					TrueX newTrueX = new TrueX(fromActivityID, toActivityID, "", "", relationTypeID);
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
			
			trueXList_Act.add(trueXMap.get(key));
			//System.out.println(key + ": " + transitionMap.get(key).getRelationListCardinality());
		}
	}
	
	public void getTrueXProfileModel_Res() throws ParseException {
		int size = relModel.getRelationCardinality();
		
		// get transition
		Map<String, TrueX> trueXMap = new HashMap<String, TrueX>();
		for(int i = 0; i < size; i++) {
			String fromResourceID = relModel.getAntecedentResource(i);
			String toResourceID = relModel.getConsequentResource(i);
			String relationTypeID = relModel.getRelationType(i);
			
			if(relationTypeID.equals("o") || relationTypeID.equals("d") || relationTypeID.equals("f")) {
				String key = fromResourceID + "_" + toResourceID + "_" + relationTypeID;

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
					TrueX newTrueX = new TrueX("", "", fromResourceID, toResourceID, relationTypeID);
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
			
			trueXList_Res.add(trueXMap.get(key));
			//System.out.println(key + ": " + transitionMap.get(key).getRelationListCardinality());
		}
	}
	
	public ArrayList<TrueX> getTrueXList_Act_Res(){
		return trueXList_Act_Res;
	}
	
	public int getTrueXList_Act_Res_Size(){
		return trueXList_Act_Res.size();
	}
	
	public ArrayList<TrueX> getTrueXList_Act(){
		return trueXList_Act;
	}
	
	public int getTrueXList_Act_Size(){
		return trueXList_Act.size();
	}
	
	public ArrayList<TrueX> getTrueXList_Res(){
		return trueXList_Res;
	}
	
	public int getTrueXList_Res_Size(){
		return trueXList_Res.size();
	}
}
