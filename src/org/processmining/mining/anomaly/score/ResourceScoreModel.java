package org.processmining.mining.anomaly.score;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.processmining.data.anomaly.ResourceScore;
import org.processmining.mining.anomaly.detection.AnomalyDetectionMiningParameters;

public class ResourceScoreModel extends AbstractRuleLogScoreModel{
	private Map<String, Float> ruleToLogMap;
	private Map<String, Float> logToRuleMap;
	private Map<String, ResourceScore> resourceScoreMap;
	
	public ResourceScoreModel() {
		ruleToLogMap = new HashMap<String, Float>();
		logToRuleMap = new HashMap<String, Float>();
		resourceScoreMap = new HashMap<String, ResourceScore>();
	}
	
	public void calculateResourceScore(AnomalyDetectionMiningParameters parameters) {
		ArrayList<String> caseList = new ArrayList<String>();
		caseList.addAll(ruleToLogMap.keySet());
		
		for(int i = 0; i < caseList.size(); i++) {
			String currCaseID = caseList.get(i);
			
			ResourceScore score = new ResourceScore(currCaseID, ruleToLogMap.get(currCaseID), logToRuleMap.get(currCaseID), parameters);
			resourceScoreMap.put(currCaseID, score);
		}
	}
	
	public void addRuleToLogElem(String caseID, float value) {
		ruleToLogMap.put(caseID, value);
	}
	
	public void addLogToRuleElem(String caseID, float value) {
		logToRuleMap.put(caseID, value);
	}

	
	/*
	 * Getters and Setters
	 * */
	public Map<String, Float> getRuleToLogMap() {
		return ruleToLogMap;
	}

	public void setRuleToLogMap(Map<String, Float> ruleToLogMap) {
		this.ruleToLogMap = ruleToLogMap;
	}

	public Map<String, Float> getLogToRuleMap() {
		return logToRuleMap;
	}

	public void setLogToRuleMap(Map<String, Float> logToRuleMap) {
		this.logToRuleMap = logToRuleMap;
	}
	
	public Map<String, ResourceScore> getResourceScoreMap() {
		return resourceScoreMap;
	}

	public void setResourceScoreMap(Map<String, ResourceScore> resourceScoreMap) {
		this.resourceScoreMap = resourceScoreMap;
	}
}
