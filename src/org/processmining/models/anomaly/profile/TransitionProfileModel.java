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
	
	private ArrayList<Transition> transitionList;
	
	public TransitionProfileModel(RelationModel relModel) throws ParseException {
		this.relModel = relModel;
		transitionList = new ArrayList<Transition>();
		
		getTransitionProfileModel();
	}
	
	public void getTransitionProfileModel() throws ParseException {
		int size = relModel.getRelationCardinality();
		
		// get transition
		Map<String, Transition> transitionMap = new HashMap<String, Transition>();
		for(int i = 0; i < size; i++) {
			String fromActivityID = relModel.getAntecedentActivity(i);
			String toActivityID = relModel.getConsequentActivity(i);
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
			
			transitionList.add(transitionMap.get(key));
			//System.out.println(key + ": " + transitionMap.get(key).getRelationListCardinality());
		}
	}
	
	public int getTransitionListSize() {
		return transitionList.size();
	}
	
	public ArrayList<Transition> getTransitionList(){
		return transitionList;
	}
	
	public void printTransitionProfile() {
		
		System.out.println("### Transition Profile ###");
		
		for(int i = 0; i < transitionList.size(); i++) {
			System.out.println(
					transitionList.get(i).getFromActivityID()
					+ ", " + transitionList.get(i).getToActivityID()
					+ ", " + transitionList.get(i).getFromResourceID()
					+ ", " + transitionList.get(i).getToResourceID()
					+ ", " + transitionList.get(i).getRelation()
					+ ": " + transitionList.get(i).getFreq()
					+ ", " + transitionList.get(i).getAvg()
					+ ", " + transitionList.get(i).getStdev()
					);
		}
	}
}
