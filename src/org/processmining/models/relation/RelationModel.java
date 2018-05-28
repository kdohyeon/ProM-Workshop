package org.processmining.models.relation;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.processmining.data.activity.Activity;
import org.processmining.data.relation.Relation;
import org.processmining.data.relation.RelationMatrix;
import org.processmining.data.relation.RelationMatrixElement;
import org.processmining.models.activity.ActivityModel;

public class RelationModel {
	
	private ArrayList<Relation> relationArrayList;
	
	private RelationMatrix relActivityMatrix;
	private RelationMatrix relResourceMatrix;
	
	private ActivityModel actModel;
	
	private float minSupp;
	private float minConf;
	
	/*
	 * Constructor for creating relation model only for this caseID
	 * */
	public RelationModel(ActivityModel actModel, String caseID) {
		relationArrayList = new ArrayList<Relation>();
		relActivityMatrix = new RelationMatrix();
		relResourceMatrix = new RelationMatrix();
		
		this.actModel = actModel;
		
		setMinSupp(0);
		setMinConf(0);
		
		createRelationModelByCaseID(caseID);
	}
		
	/*
	 * Constructor for creating relation model
	 * */
	public RelationModel(ActivityModel actModel) {
		relationArrayList = new ArrayList<Relation>();
		relActivityMatrix = new RelationMatrix();
		relResourceMatrix = new RelationMatrix();
		
		this.actModel = actModel;
		
		setMinSupp(0);
		setMinConf(0);
		
		createRelationModel();
	}
	
	/*
	 * Directly Follows
	 * Need to be fixed
	 * */
	private void createRelationModel2() {
		int actSize = actModel.getActivityCardinality();
		
		for(int i = 0; i < actSize; i++) {
			if(i != actSize - 1) {
				String currCase = actModel.getCase(i);
				String nextCase = actModel.getCase(i+1);
				
				if(currCase.equals(nextCase)) {
					try {
						Relation rel = new Relation(actModel.getActivity(i), actModel.getActivity(i+1));
						addRelation(rel);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	
	/*
	 * Directly and Indirectly Follows
	 * */
	private void createRelationModel() {
		int actSize = actModel.getActivityCardinality();
		//System.out.println("Activity Cardinality: " + actSize);
		
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
	
	/*
	 * Directly and Indirectly Follows
	 * */
	private void createRelationModelByCaseID(String caseID) {
		int actSize = actModel.getActivityCardinality();
		
		for(int i = 0; i < actSize; i++) {
			String currCase = actModel.getCase(i);
			
			if(currCase.equals(caseID)) {
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
			}			
		}
	}
	
	public int getCaseSize(String caseID) {
		int cnt = 0;
		
		for(int i = 0; i < relationArrayList.size(); i++) {
			if(caseID.equals(relationArrayList.get(i).getAntecedent().getCaseID())) {
				cnt++;
			}
		}
		
		return cnt;
	}
	
	public ArrayList<String> getCaseIDList(){
		ArrayList<String> result = new ArrayList<String>();
		Set<String> temp = new HashSet<String>();
		for(int i = 0; i < relationArrayList.size(); i++) {
			temp.add(relationArrayList.get(i).getAntecedent().getCaseID());
		}
		result.addAll(temp);
		
		return result;
	}
	
	public RelationMatrix calculateRelationResourceMatrix() {
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
						
						if(support > minSupp && confidence > minConf) {
							//RelationMatrixElement elem = new RelationMatrixElement(currAntecedent, currConsequent, currRelation, cnt, support, confidence);
							RelationMatrixElement elem = new RelationMatrixElement(currAntecedent, currConsequent, currRelation, checkCaseSet.size(), support, confidence);
							relResourceMatrix.addRelationMatrixElement(elem);													
						}

					}
				}
			}
		}
		
		//relResourceMatrix.printRelationMatrix();
		
		return relResourceMatrix;
	}
	
	public RelationMatrix calculateRelationActivityMatrix() {
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
						
						if(support > minSupp && confidence > minConf) {
							RelationMatrixElement elem = new RelationMatrixElement(currAntecedent, currConsequent, currRelation, cnt, support, confidence);
							relActivityMatrix.addRelationMatrixElement(elem);		
						}
					}
				}
			}
		}
		
		//System.out.println("CF Rule: " + relActivityMatrix.getRelationMatrixListSize());
		
		return relActivityMatrix;
	}
	
	public ArrayList<String> getUniqueRelation(){
		ArrayList<String> result = new ArrayList<String>();
		Set<String> relationSet = new HashSet<String>();
		for(int i = 0; i < relationArrayList.size(); i++) {
			relationSet.add(relationArrayList.get(i).getRelation());
		}
		result.addAll(relationSet);
		return result;
	}
	
	public RelationMatrix getRelationActivityMatrix() {
		return relActivityMatrix;
	}
	
	public RelationMatrix getRelationResourceMatrix() {
		return relResourceMatrix;
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
	
	public Activity getAntecedentActivity(int i) {
		return relationArrayList.get(i).getAntecedent();
	}
	
	public Activity getConsequentActivity(int i) {
		return relationArrayList.get(i).getConsequent();
	}
	
	public String getAntecedentActivityID(int i) {
		return relationArrayList.get(i).getAntecedent().getActivityID();
	}
	
	public String getConsequentActivityID(int i) {
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
	
	public float getTransitionTime(String caseID, String relation) {
		float result = 0;
		for(int i = 0; i < relationArrayList.size(); i++) {
			if(relationArrayList.get(i).getRelation().equals(relation) && relationArrayList.get(i).getAntecedent().getCaseID().equals(caseID)) {
				result = relationArrayList.get(i).getTransitionTime(); 
			}
		}
		return result;
	}
	
	public float getOverlapTime(int i) {
		return relationArrayList.get(i).getOverlapTime();
	}
	
	public float getOverlapTime(String caseID, String relation) {
		float result = 0;
		for(int i = 0; i < relationArrayList.size(); i++) {
			if(relationArrayList.get(i).getRelation().equals(relation) && relationArrayList.get(i).getAntecedent().getCaseID().equals(caseID)) {
				result = relationArrayList.get(i).getOverlapTime(); 
			}
		}
		return result;
	}
	
	public float getTrueXTime(int i) {
		return relationArrayList.get(i).getTrueXTime();
	}
	
	public float getTrueXTime(String caseID, String relation) {
		float result = 0;
		for(int i = 0; i < relationArrayList.size(); i++) {
			if(relationArrayList.get(i).getRelation().equals(relation) && relationArrayList.get(i).getAntecedent().getCaseID().equals(caseID)) {
				result = relationArrayList.get(i).getTrueXTime(); 
			}
		}
		return result;
	}
	
	public float getTrueYTime(int i) {
		return relationArrayList.get(i).getTrueYTime();
	}
	
	public float getTrueYTime(String caseID, String relation) {
		float result = 0;
		for(int i = 0; i < relationArrayList.size(); i++) {
			if(relationArrayList.get(i).getRelation().equals(relation) && relationArrayList.get(i).getAntecedent().getCaseID().equals(caseID)) {
				result = relationArrayList.get(i).getTrueYTime(); 
			}
		}
		return result;
	}

	public float getMinSupp() {
		return minSupp;
	}

	public void setMinSupp(float minSupp) {
		this.minSupp = minSupp;
	}

	public float getMinConf() {
		return minConf;
	}

	public void setMinConf(float minConf) {
		this.minConf = minConf;
	}
}
