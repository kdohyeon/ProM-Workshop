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
}
