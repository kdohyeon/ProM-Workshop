package org.processmining.models.relation;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.deckfour.xes.classification.XEventClasses;
import org.processmining.data.relation.Relation;
import org.processmining.data.relation.RelationMatrix;
import org.processmining.data.relation.RelationMatrixElement;
import org.processmining.models.activity.ActivityModel;

public class RelationModel {
	
	private ArrayList<Relation> relationArrayList;
	
	private RelationMatrix relActivityMatrix;
	private RelationMatrix relResourceMatrix;
	
	private ActivityModel actModel;
	
	public RelationModel(ActivityModel actModel) {
		relationArrayList = new ArrayList<Relation>();
		relActivityMatrix = new RelationMatrix();
		relResourceMatrix = new RelationMatrix();
		
		this.actModel = actModel;
		
		createRelationModel();
	}
	
	private void createRelationModel() {
		int actSize = actModel.getActivityCardinality();
		System.out.println("Activity Cardinality: " + actSize);
		
		for(int i = 0; i < actSize; i++) {
			/*
			System.out.println(
					actModel.getCase(i) 
					+ ", " + actModel.getActivityID(i) 
					+ ": " + actModel.getProcessingTime(i));
			*/
			
			String currCase = actModel.getCase(i);
			
			for(int j = i+1; j < actSize; j++) {
				String thisCase = actModel.getCase(j);
				if(currCase.equals(thisCase)) {
					try {
						Relation rel = new Relation(actModel.getActivity(i), actModel.getActivity(j));
						addRelation(rel);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			//System.out.println(actList.get(i).getCaseID() + ", " + actList.get(i).getActivityID() + ", " + actList.get(i).getStartTimestamp() + ", " + actList.get(i).getCompleteTimestamp());
		}
		/*
		int relListSize = relModel.getRelationCardinality();
		System.out.println("Case ID --- Ante, Cons: RelType, Trans, Overlap, TrueX, TrueY");
		for(int i = 0; i < relListSize; i++) {
			
			System.out.println(
					relModel.getCaseID(i)
					+ " --- " + relModel.getAntecedentActivity(i) 
					+ ", " + relModel.getConsequentActivity(i)
					+ ": " + relModel.getRelationType(i)
					+ ", " + relModel.getTransitionTime(i)
					+ ", " + relModel.getOverlapTime(i)
					+ ", " + relModel.getTrueXTime(i)
					+ ", " + relModel.getTrueYTime(i));
		}
		*/
	}
	
	public RelationModel(XEventClasses eventClasses) {
		
	}
	
	public RelationMatrix getRelationResourceMatrix() {
		Set<String> antecedentSet = new HashSet<String>();
		Set<String> consequentSet = new HashSet<String>();
		Set<String> relationSet = new HashSet<String>();
		for(int i = 0; i < relationArrayList.size(); i++) {
			antecedentSet.add(relationArrayList.get(i).getAntecedent().getResourceID());
			consequentSet.add(relationArrayList.get(i).getConsequent().getResourceID());
			relationSet.add(relationArrayList.get(i).getRelationType());
		}
		
		ArrayList<String> antecedentList = new ArrayList<String>();
		ArrayList<String> consequentList = new ArrayList<String>();
		ArrayList<String> relationList = new ArrayList<String>();
		antecedentList.addAll(antecedentSet);
		consequentList.addAll(consequentSet);
		relationList.addAll(relationSet);
		
		int caseFrequency = actModel.getUniqueCaseID().size();
		
		for(int i = 0; i < antecedentList.size(); i++) {
			String currAntecedent = antecedentList.get(i);
			
			for(int j = 0; j < consequentList.size(); j++) {
				String currConsequent = consequentList.get(j);
				
				for(int k = 0; k < relationList.size(); k++) {
					String currRelation = relationList.get(k);
					
					//int cnt = 0;
					Map<String, Integer> checkCaseSet = new HashMap<String, Integer>();
					for(int l = 0; l < relationArrayList.size(); l++) {
						if(relationArrayList.get(l).getAntecedent().getResourceID().equals(currAntecedent) 
								&& relationArrayList.get(l).getConsequent().getResourceID().equals(currConsequent)
								&& relationArrayList.get(l).getRelationType().equals(currRelation)) {
							String caseID = relationArrayList.get(l).getAntecedent().getCaseID();
							checkCaseSet.put(caseID, 1);
							//cnt++;
						}	
					}
					
					if(checkCaseSet.size() > 0) {
						//float support = (float) (cnt * 1.0 / caseFrequency);
						float support = (float) (checkCaseSet.size() * 1.0 / caseFrequency);
						//float confidence = (float) (cnt * 1.0 / actModel.getCaseFrequencyOfResource(currAntecedent));
						float confidence = (float) (checkCaseSet.size() * 1.0 / actModel.getCaseFrequencyOfResource(currAntecedent));
						
						//RelationMatrixElement elem = new RelationMatrixElement(currAntecedent, currConsequent, currRelation, cnt, support, confidence);
						RelationMatrixElement elem = new RelationMatrixElement(currAntecedent, currConsequent, currRelation, checkCaseSet.size(), support, confidence);
						relResourceMatrix.addRelationMatrixElement(elem);						
					}
				}
			}
		}
		
		//relResourceMatrix.printRelationMatrix();
		
		return relResourceMatrix;
	}
	
	public RelationMatrix getRelationActivityMatrix() {
		Set<String> antecedentSet = new HashSet<String>();
		Set<String> consequentSet = new HashSet<String>();
		Set<String> relationSet = new HashSet<String>();
		for(int i = 0; i < relationArrayList.size(); i++) {
			antecedentSet.add(relationArrayList.get(i).getAntecedent().getActivityID());
			consequentSet.add(relationArrayList.get(i).getConsequent().getActivityID());
			relationSet.add(relationArrayList.get(i).getRelationType());
		}
		
		ArrayList<String> antecedentList = new ArrayList<String>();
		ArrayList<String> consequentList = new ArrayList<String>();
		ArrayList<String> relationList = new ArrayList<String>();
		antecedentList.addAll(antecedentSet);
		consequentList.addAll(consequentSet);
		relationList.addAll(relationSet);
		
		int caseFrequency = actModel.getUniqueCaseID().size();
		
		for(int i = 0; i < antecedentList.size(); i++) {
			String currAntecedent = antecedentList.get(i);
			
			for(int j = 0; j < consequentList.size(); j++) {
				String currConsequent = consequentList.get(j);
				
				for(int k = 0; k < relationList.size(); k++) {
					String currRelation = relationList.get(k);
					
					int cnt = 0;
					for(int l = 0; l < relationArrayList.size(); l++) {
						if(relationArrayList.get(l).getAntecedent().getActivityID().equals(currAntecedent) 
								&& relationArrayList.get(l).getConsequent().getActivityID().equals(currConsequent)
								&& relationArrayList.get(l).getRelationType().equals(currRelation)) {
							cnt++;
						}	
					}
					
					if(cnt > 0) {
						float support = (float) (cnt * 1.0 / caseFrequency);
						float confidence = (float) (cnt * 1.0 / actModel.getCaseFrequencyOfActivity(currAntecedent));
						
						RelationMatrixElement elem = new RelationMatrixElement(currAntecedent, currConsequent, currRelation, cnt, support, confidence);
						relActivityMatrix.addRelationMatrixElement(elem);	
					}
				}
			}
		}
		
		//relActivityMatrix.printRelationMatrix();
		
		return relActivityMatrix;
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
	
	public Relation getRelation(int i) {
		return relationArrayList.get(i);
	}
	
	public String getAntecedentResource(int i) {
		return relationArrayList.get(i).getAntecedent().getResourceID();
	}
	
	public String getConsequentResource(int i) {
		return relationArrayList.get(i).getConsequent().getResourceID();
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
	
	public float getTransitionTime(int i) {
		return relationArrayList.get(i).getTransitionTime();
	}
	
	public float getOverlapTime(int i) {
		return relationArrayList.get(i).getOverlapTime();
	}
	
	public float getTrueXTime(int i) {
		return relationArrayList.get(i).getTrueXTime();
	}
	
	public float getTrueYTime(int i) {
		return relationArrayList.get(i).getTrueYTime();
	}
}
