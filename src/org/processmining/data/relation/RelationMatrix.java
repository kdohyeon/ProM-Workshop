package org.processmining.data.relation;

import java.util.ArrayList;

public class RelationMatrix {
	
	private ArrayList<RelationMatrixElement> relationMatrixList;
	
	public RelationMatrix() {
		relationMatrixList = new ArrayList<RelationMatrixElement>();
	}
	
	public void addRelationMatrixElement(RelationMatrixElement elem) {
		relationMatrixList.add(elem);
	}
	
	public void printRelationMatrix() {
		
		System.out.println("### Relation Matrix ###");
		for(int i = 0; i < relationMatrixList.size(); i++) {
			if(relationMatrixList.get(i).getFreq() > 0) {
				System.out.println(relationMatrixList.get(i).getAntecedent() 
						+ ", " + relationMatrixList.get(i).getConsequent()
						+ ", " + relationMatrixList.get(i).getRelationType()
						+ " -- freq: " + relationMatrixList.get(i).getFreq()
						+ ", supp.: " + relationMatrixList.get(i).getSupport()
						+ ", conf.: " + relationMatrixList.get(i).getConfidence());
			}
		}
	}
	
	public int getRelationMatrixListSize() {
		return relationMatrixList.size();
	}
	
	public String getAntecedent(int i) {
		return relationMatrixList.get(i).getAntecedent();
	}
	
	public String getConsequent(int i) {
		return relationMatrixList.get(i).getConsequent();
	}
	
	public String getRelationType(int i) {
		return relationMatrixList.get(i).getRelationType();
	}
	
	public int getFrequency(int i) {
		return relationMatrixList.get(i).getFreq();
	}
	
	public float getSupport(int i) {
		return relationMatrixList.get(i).getSupport();
	}
	
	public float getConfidence(int i) {
		return relationMatrixList.get(i).getConfidence();
	}
}
