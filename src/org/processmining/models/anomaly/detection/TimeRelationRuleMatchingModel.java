package org.processmining.models.anomaly.detection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.processmining.data.overlap.Overlap;
import org.processmining.data.transition.Transition;
import org.processmining.data.trueX.TrueX;
import org.processmining.data.trueY.TrueY;
import org.processmining.models.anomaly.profile.AnomalyProfileModel;
import org.processmining.models.anomaly.profile.OverlapProfileModel;
import org.processmining.models.anomaly.profile.TransitionProfileModel;
import org.processmining.models.anomaly.profile.TrueXProfileModel;
import org.processmining.models.anomaly.profile.TrueYProfileModel;
import org.processmining.models.relation.RelationModel;
import org.processmining.plugins.anomaly.detection.AnomalyDetectionMiningParameters;

public class TimeRelationRuleMatchingModel extends AbstractRuleMatchingModel{
	
	private AnomalyProfileModel profileModel;
	private RelationModel testRelModel;
	private AnomalyDetectionMiningParameters parameters;
	private Map<String, Float> resultMap;
	
	public TimeRelationRuleMatchingModel(AnomalyProfileModel profileModel, RelationModel testRelModel, AnomalyDetectionMiningParameters parameters) {
		/*
		 * Parameters
		 * */
		this.setParameters(parameters);
		this.setProfileModel(profileModel);
		this.setTestRelModel(testRelModel);
		resultMap = new HashMap<String, Float>();
		
		// get case id list
		System.out.println("... Getting Case ID List...");
		ArrayList<String> caseIDList = new ArrayList<String>();
		caseIDList = testRelModel.getCaseIDList();
		
		// for each relation
		System.out.println("... Getting Relation List...");
		ArrayList<String> testRelSet = new ArrayList<String>();
		testRelSet.addAll(testRelModel.getUniqueRelation());
		
		// transition performance measure
		System.out.println("... Getting Transition List...");
		TransitionProfileModel transitionModel = profileModel.getTransitionModel();
		ArrayList<Transition> transition = new ArrayList<Transition>();
		transition = transitionModel.getTransitionList_Act();
		
		// overlap performance measure
		System.out.println("... Getting Overlap List...");
		OverlapProfileModel overlapModel = profileModel.getOverlapModel();
		ArrayList<Overlap> overlap = new ArrayList<Overlap>();
		overlap = overlapModel.getOverlapList_Act();
		
		// true x performance measure
		System.out.println("... Getting True X List...");
		TrueXProfileModel trueXModel = profileModel.getTrueXModel();
		ArrayList<TrueX> trueX = new ArrayList<TrueX>();
		trueX = trueXModel.getTrueXList_Act();
		
		// true y performance measure
		System.out.println("... Getting True Y List...");
		TrueYProfileModel trueYModel = profileModel.getTrueYModel();
		ArrayList<TrueY> trueY = new ArrayList<TrueY>();
		trueY = trueYModel.getTrueYList_Act();
		
		for(int i = 0; i < caseIDList.size(); i++) {
			
			if(i % 10 == 0) {
				//System.out.println(caseIDList.get(i));
			}
			
			String thisCase = caseIDList.get(i);
			//System.out.println("Case ID: " + thisCase);
			
			ArrayList<String> temp = new ArrayList<String>();
			for(int j = 0; j < testRelModel.getRelationCardinality(); j++) {
				if(thisCase.equals(testRelModel.getRelation(j).getAntecedent().getCaseID())) {
					temp.add(testRelModel.getRelation(j).getRelation());
				}
			}
			
			int cnt = 0;
			for(int j = 0; j < temp.size(); j++) {
				String thisRelation = temp.get(j);
				String[] parseLine = thisRelation.split("_");
				//String ante = parseLine[0];
				//String cons = parseLine[2];
				String relType = parseLine[1];
				
				//System.out.println(thisRelation + ", " + relType);
				
				if(relType.equals("<")) { // type 1
					for(int k = 0; k < transition.size(); k++) {
						//System.out.println(transition.get(k).getActivityPair());
						
						if(transition.get(k).getActivityPair().equals(thisRelation)) {
							float avg = transition.get(k).getAvg();
							float std = transition.get(k).getStdev();
							float lowerBound = avg - 3*std;
							float upperBound = avg + 3*std;
							
							float transitionTime = testRelModel.getTransitionTime(thisCase, thisRelation);
							
							if(lowerBound <= transitionTime && transitionTime <= upperBound) {
								cnt++;
							}
							break;
						}
					}
				}else if(relType.equals("m")) { // type 2
					cnt++;
				}else if(relType.equals("o") || relType.equals("d")) { // type 3 or type 5
					// if true x, overlap, true y time all fall in, then cnt++
					boolean isOverlap = false;
					boolean isTrueX = false;
					boolean isTrueY = false;
					
					for(int k = 0; k < overlap.size(); k++) {
						if(overlap.get(k).getActivityPair().equals(thisRelation)) {
							float avg = overlap.get(k).getAvg();
							float std = overlap.get(k).getStdev();
							float lowerBound = avg - 3*std;
							float upperBound = avg + 3*std;
							
							float overlapTime = testRelModel.getOverlapTime(thisCase, thisRelation);
							
							if(lowerBound <= overlapTime && overlapTime <= upperBound) {
								isOverlap = true;
							}
							break;
						}
					}
					
					for(int k = 0; k < trueX.size(); k++) {
						if(trueX.get(k).getActivityPair().equals(thisRelation)) {
							float avg = trueX.get(k).getAvg();
							float std = trueX.get(k).getStdev();
							float lowerBound = avg - 3*std;
							float upperBound = avg + 3*std;
							
							float trueXTime = testRelModel.getTrueXTime(thisCase, thisRelation);
							
							if(lowerBound <= trueXTime && trueXTime <= upperBound) {
								isTrueX = true;
							}
							break;
						}
					}
					
					for(int k = 0; k < trueY.size(); k++) {
						if(trueY.get(k).getActivityPair().equals(thisRelation)) {
							float avg = trueY.get(k).getAvg();
							float std = trueY.get(k).getStdev();
							float lowerBound = avg - 3*std;
							float upperBound = avg + 3*std;
							
							float trueYTime = testRelModel.getTrueYTime(thisCase, thisRelation);
							
							if(lowerBound <= trueYTime && trueYTime <= upperBound) {
								isTrueY = true;
							}
							break;
						}
					}
					
					if(isOverlap && isTrueX && isTrueY) {
						cnt++;
					}
					
				}else if(relType.equals("s")) { // type 4
					// if overlap, true y time all fall in, then cnt++
					boolean isOverlap = false;
					boolean isTrueY = false;
					
					for(int k = 0; k < overlap.size(); k++) {
						if(overlap.get(k).getActivityPair().equals(thisRelation)) {
							float avg = overlap.get(k).getAvg();
							float std = overlap.get(k).getStdev();
							float lowerBound = avg - 3*std;
							float upperBound = avg + 3*std;
							
							float overlapTime = testRelModel.getOverlapTime(thisCase, thisRelation);
							
							if(lowerBound <= overlapTime && overlapTime <= upperBound) {
								isOverlap = true;
							}
							break;
						}
					}
					
					for(int k = 0; k < trueY.size(); k++) {
						if(trueY.get(k).getActivityPair().equals(thisRelation)) {
							float avg = trueY.get(k).getAvg();
							float std = trueY.get(k).getStdev();
							float lowerBound = avg - 3*std;
							float upperBound = avg + 3*std;
							
							float trueYTime = testRelModel.getTrueYTime(thisCase, thisRelation);
							
							if(lowerBound <= trueYTime && trueYTime <= upperBound) {
								isTrueY = true;
							}
							break;
						}
					}
					
					if(isOverlap && isTrueY) {
						cnt++;
					}
				}else if(relType.equals("f")) { // type 6
					// if true x, overlap time all fall in, then cnt++
					boolean isOverlap = false;
					boolean isTrueX = false;
					
					for(int k = 0; k < overlap.size(); k++) {
						if(overlap.get(k).getActivityPair().equals(thisRelation)) {
							float avg = overlap.get(k).getAvg();
							float std = overlap.get(k).getStdev();
							float lowerBound = avg - 3*std;
							float upperBound = avg + 3*std;
							
							float overlapTime = testRelModel.getOverlapTime(thisCase, thisRelation);
							
							if(lowerBound <= overlapTime && overlapTime <= upperBound) {
								isOverlap = true;
							}
							break;
						}
					}
					
					for(int k = 0; k < trueX.size(); k++) {
						if(trueX.get(k).getActivityPair().equals(thisRelation)) {
							float avg = trueX.get(k).getAvg();
							float std = trueX.get(k).getStdev();
							float lowerBound = avg - 3*std;
							float upperBound = avg + 3*std;
							
							float trueXTime = testRelModel.getTrueXTime(thisCase, thisRelation);
							
							if(lowerBound <= trueXTime && trueXTime <= upperBound) {
								isTrueX = true;
							}
							break;
						}
					}
					
					if(isOverlap && isTrueX) {
						cnt++;
					}
				}else if(relType.equals("=")) { // type 7
					boolean isOverlap = false;
					
					for(int k = 0; k < overlap.size(); k++) {
						if(overlap.get(k).getActivityPair().equals(thisRelation)) {
							float avg = overlap.get(k).getAvg();
							float std = overlap.get(k).getStdev();
							float lowerBound = avg - 3*std;
							float upperBound = avg + 3*std;
							
							float overlapTime = testRelModel.getOverlapTime(thisCase, thisRelation);
							
							if(lowerBound <= overlapTime && overlapTime <= upperBound) {
								isOverlap = true;
							}
							break;
						}
					}
					
					if(isOverlap) {
						cnt++;
					}
				}
				
				
			}
			
			//System.out.println("Time Relation - " + thisCase);
			float unmatched = temp.size() - cnt;
			float timeActivity = (float)( (unmatched * 1.0) / temp.size());
			//System.out.println(timeActivity);
			
			// calculate
			resultMap.put(thisCase, timeActivity);
			
		}
	}

	public AnomalyProfileModel getProfileModel() {
		return profileModel;
	}

	public void setProfileModel(AnomalyProfileModel profileModel) {
		this.profileModel = profileModel;
	}

	public RelationModel getTestRelModel() {
		return testRelModel;
	}

	public void setTestRelModel(RelationModel testRelModel) {
		this.testRelModel = testRelModel;
	}

	public AnomalyDetectionMiningParameters getParameters() {
		return parameters;
	}

	public void setParameters(AnomalyDetectionMiningParameters parameters) {
		this.parameters = parameters;
	}

	public Map<String, Float> getResultMap() {
		return resultMap;
	}

	public void setResultMap(Map<String, Float> resultMap) {
		this.resultMap = resultMap;
	}
}
