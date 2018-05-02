package org.processmining.models.relation;

import java.util.ArrayList;

import org.deckfour.xes.classification.XEventClasses;
import org.processmining.data.relation.Relation;

public class RelationModel {
	
	private ArrayList<Relation> relationArrayList;
	
	public RelationModel() {
		relationArrayList = new ArrayList<Relation>();
	}
	
	public RelationModel(XEventClasses eventClasses) {
		
	}
	
	public void addRelation(Relation rel) {
		relationArrayList.add(rel);
	}
	
	public int getRelationCardinality() {
		return relationArrayList.size();
	}
	
	public String getCaseID(int i) {
		return relationArrayList.get(i).getAntecedent().getCaseID(); // it does not matter whether it is ante or cons
	}
	
	public String getAntecedentActivity(int i) {
		return relationArrayList.get(i).getAntecedent().getActivityID();
	}
	
	public String getConsequentActivity(int i) {
		return relationArrayList.get(i).getConsequent().getActivityID();
	}
	
	public String getAntecedentStartTimestamp(int i) {
		return relationArrayList.get(i).getAntecedent().getStartTimestamp();
	}
	
	public String getAntecedentCompleteTimestamp(int i) {
		return relationArrayList.get(i).getAntecedent().getCompleteTimestamp();
	}
	
	public String getConsequentStartTimestamp(int i) {
		return relationArrayList.get(i).getConsequent().getStartTimestamp();
	}
	
	public String getConsequentCompleteTimestamp(int i) {
		return relationArrayList.get(i).getConsequent().getCompleteTimestamp();
	}
	
	public String getRelationType(int i) {
		return relationArrayList.get(i).getRelationType();
	}
	
	
}
