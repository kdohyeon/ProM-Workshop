package org.processmining.models.anomaly.profile;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.processmining.data.relation.Relation;
import org.processmining.data.transition.Transition;
import org.processmining.models.relation.RelationModel;

public class TransitionProfileModel {
	
	private RelationModel relModel;
	
	private ArrayList<Transition> transitionList_Act_Res;
	private ArrayList<Transition> transitionList_Act;
	private ArrayList<Transition> transitionList_Res;
	
	public TransitionProfileModel(RelationModel relModel) throws ParseException {
		this.relModel = relModel;
		transitionList_Act_Res = new ArrayList<Transition>();
		transitionList_Act = new ArrayList<Transition>();
		transitionList_Res = new ArrayList<Transition>();
		
		getTransitionProfileModel_Act_Res();
		getTransitionProfileModel_Act();
		getTransitionProfileModel_Res();
	}
	
	public void getTransitionProfileModel_Act_Res() throws ParseException {
		int size = relModel.getRelationCardinality();
		
		// get transition
		Map<String, Transition> transitionMap = new HashMap<String, Transition>();
		for(int i = 0; i < size; i++) {
			String fromActivityID = relModel.getAntecedentActivityID(i);
			String toActivityID = relModel.getConsequentActivityID(i);
			String fromResourceID = relModel.getAntecedentResource(i);
			String toResourceID = relModel.getConsequentResource(i);
			String relationTypeID = relModel.getRelationType(i);
			
			if(relationTypeID.equals("<")) {
				String key = fromActivityID + "_" + toActivityID + "_" + fromResourceID + "_" + toResourceID + "_" + relationTypeID;

				Relation rel = new Relation(relModel.getRelation(i).getAntecedent(), relModel.getRelation(i).getConsequent());
				
				// check whether those combination is in the hash map
				if(transitionMap.containsKey(key)) {
					// if it is, then add the relation
					Transition existingTransition = transitionMap.get(key);
					//System.out.println(transitionMap.get(key).getFromActivityID());
					existingTransition.addRelation(rel);
					transitionMap.put(key, existingTransition);
				}else {
					// if it is not, create a new relation
					// add the relation
					Transition newTransition = new Transition(fromActivityID, toActivityID, fromResourceID, toResourceID, relationTypeID);
					newTransition.addRelation(rel);
					transitionMap.put(key, newTransition);
				}
			}
		}
		
		// iter
		Iterator<String> keys = transitionMap.keySet().iterator();
		while(keys.hasNext()) {
			String key = keys.next();
			// for each key, calculate freq, avg, std, min, max, median
			Transition existingTransition = transitionMap.get(key);
			
			existingTransition.calculateFreq();
			existingTransition.calculateAverage();
			existingTransition.calculateStdev();
			existingTransition.calculateaMax();
			existingTransition.calculateMin();
		
			transitionMap.put(key, existingTransition);
			
			transitionList_Act_Res.add(transitionMap.get(key));
			//System.out.println(key + ": " + transitionMap.get(key).getRelationListCardinality());
		}
	}
	
	public void getTransitionProfileModel_Act() throws ParseException {
		int size = relModel.getRelationCardinality();
		
		// get transition
		Map<String, Transition> transitionMap = new HashMap<String, Transition>();
		for(int i = 0; i < size; i++) {
			String fromActivityID = relModel.getAntecedentActivityID(i);
			String toActivityID = relModel.getConsequentActivityID(i);
			String relationTypeID = relModel.getRelationType(i);
			
			if(relationTypeID.equals("<")) {
				String key = fromActivityID + "_" + toActivityID + "_" + relationTypeID;

				Relation rel = new Relation(relModel.getRelation(i).getAntecedent(), relModel.getRelation(i).getConsequent());
				
				// check whether those combination is in the hash map
				if(transitionMap.containsKey(key)) {
					// if it is, then add the relation
					Transition existingTransition = transitionMap.get(key);
					//System.out.println(transitionMap.get(key).getFromActivityID());
					existingTransition.addRelation(rel);
					transitionMap.put(key, existingTransition);
				}else {
					// if it is not, create a new relation
					// add the relation
					Transition newTransition = new Transition(fromActivityID, toActivityID, "", "", relationTypeID);
					newTransition.addRelation(rel);
					transitionMap.put(key, newTransition);
				}
			}
		}
		
		// iter
		Iterator<String> keys = transitionMap.keySet().iterator();
		while(keys.hasNext()) {
			String key = keys.next();
			// for each key, calculate freq, avg, std, min, max, median
			Transition existingTransition = transitionMap.get(key);
			
			existingTransition.calculateFreq();
			existingTransition.calculateAverage();
			existingTransition.calculateStdev();
			existingTransition.calculateaMax();
			existingTransition.calculateMin();
		
			transitionMap.put(key, existingTransition);
			
			transitionList_Act.add(transitionMap.get(key));
			//System.out.println(key + ": " + existingTransition.getAvg() + "+/-" + existingTransition.getStdev());
		}
	}
	
	public void getTransitionProfileModel_Res() throws ParseException {
		int size = relModel.getRelationCardinality();
		
		// get transition
		Map<String, Transition> transitionMap = new HashMap<String, Transition>();
		for(int i = 0; i < size; i++) {
			String fromResourceID = relModel.getAntecedentResource(i);
			String toResourceID = relModel.getConsequentResource(i);
			String relationTypeID = relModel.getRelationType(i);
			
			if(relationTypeID.equals("<")) {
				String key = fromResourceID + "_" + toResourceID + "_" + relationTypeID;

				Relation rel = new Relation(relModel.getRelation(i).getAntecedent(), relModel.getRelation(i).getConsequent());
				
				// check whether those combination is in the hash map
				if(transitionMap.containsKey(key)) {
					// if it is, then add the relation
					Transition existingTransition = transitionMap.get(key);
					//System.out.println(transitionMap.get(key).getFromActivityID());
					existingTransition.addRelation(rel);
					transitionMap.put(key, existingTransition);
				}else {
					// if it is not, create a new relation
					// add the relation
					Transition newTransition = new Transition("", "", fromResourceID, toResourceID, relationTypeID);
					newTransition.addRelation(rel);
					transitionMap.put(key, newTransition);
				}
			}
		}
		
		// iter
		Iterator<String> keys = transitionMap.keySet().iterator();
		while(keys.hasNext()) {
			String key = keys.next();
			// for each key, calculate freq, avg, std, min, max, median
			Transition existingTransition = transitionMap.get(key);
			
			existingTransition.calculateFreq();
			existingTransition.calculateAverage();
			existingTransition.calculateStdev();
			existingTransition.calculateaMax();
			existingTransition.calculateMin();
		
			transitionMap.put(key, existingTransition);
			
			transitionList_Res.add(transitionMap.get(key));
			//System.out.println(key + ": " + existingTransition.getAvg() + "+/-" + existingTransition.getStdev());
		}
	}
	
	public int getTransitionList_Act_Res_Size() {
		return transitionList_Act_Res.size();
	}
	
	public ArrayList<Transition> getTransitionList_Act_Res(){
		return transitionList_Act_Res;
	}
	
	public int getTransitionList_Act_Size() {
		return transitionList_Act.size();
	}
	
	public ArrayList<Transition> getTransitionList_Act(){
		return transitionList_Act;
	}
	
	public int getTransitionList_Res_Size() {
		return transitionList_Res.size();
	}
	
	public ArrayList<Transition> getTransitionList_Res(){
		return transitionList_Res;
	}
	
	
}
