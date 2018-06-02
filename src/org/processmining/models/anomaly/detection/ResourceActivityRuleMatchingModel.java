package org.processmining.models.anomaly.detection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.processmining.data.rules.ActivityResourceRule;
import org.processmining.models.activity.ActivityModel;
import org.processmining.plugins.anomaly.detection.AnomalyDetectionMiningParameters;

public class ResourceActivityRuleMatchingModel extends AbstractRuleMatchingModel{
	private ActivityModel trainingActModel;
	private ActivityModel testActModel;
	private AnomalyDetectionMiningParameters parameters;
	private Map<String, Float> resultMap;
	
	public ResourceActivityRuleMatchingModel(ActivityModel trainingActModel, ActivityModel testActModel, AnomalyDetectionMiningParameters parameters) {
		/*
		 * Parameters
		 * */
		this.setParameters(parameters);
		this.setTestActModel(testActModel);
		this.setTrainingActModel(trainingActModel);
		resultMap = new HashMap<String, Float>();
		
		/*
		 * Resource
		 * */
		// get the size of r act rule size
		int rResActRuleSize = trainingActModel.getActResRuleModel().getRuleSize();
		ArrayList<ActivityResourceRule> resourceActivityRuleList = new ArrayList<ActivityResourceRule>();
		resourceActivityRuleList = trainingActModel.getActResRuleModel().getRuleList();
		ArrayList<String> resourceActivityRules = new ArrayList<String>();
		for(int i = 0; i < rResActRuleSize; i++) {
			resourceActivityRules.add(resourceActivityRuleList.get(i).getAct());
		}
				
		// get the case id list
		ArrayList<String> caseIDList = new ArrayList<String>();
		caseIDList = testActModel.getUniqueCaseID();
		
		// for each case
		for(int i = 0; i < caseIDList.size(); i++) {
			String thisCase = caseIDList.get(i);
			
			ArrayList<String> temp = new ArrayList<String>();
			for(int j = 0; j < testActModel.getActivityCardinality(); j++) {
				if(thisCase.equals(testActModel.getActivity(j).getCaseID())) {
					temp.add(
							testActModel.getActivity(j).getActivityID()
							+ "_" + testActModel.getActivity(j).getResourceID());
				}
			}
			
			// this.computeRuleToLog
			float a = this.computeLogToRule(resourceActivityRules, temp);
			
			// this.computeLogToRule
			float b = this.computeRuleToLog(resourceActivityRules, temp);		
			
			// print
			//System.out.println("Case: " + thisCase + " -- " + a + ", " + b);
			
			// calculate
			float weight1, weight2;
			weight1 = (float) 0.5; // equal weight
			weight2 = (float) 0.5; // equal weight
			
			float score = weight1*a + weight2*b;
			resultMap.put(thisCase, score);
		}
	}

	public ActivityModel getTrainingActModel() {
		return trainingActModel;
	}

	public void setTrainingActModel(ActivityModel trainingActModel) {
		this.trainingActModel = trainingActModel;
	}

	public ActivityModel getTestActModel() {
		return testActModel;
	}

	public void setTestActModel(ActivityModel testActModel) {
		this.testActModel = testActModel;
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
