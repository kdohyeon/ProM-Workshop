package org.processmining.mining.anomaly.score;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.processmining.data.anomaly.ControlFlowScore;
import org.processmining.mining.anomaly.detection.AnomalyDetectionMiningParameters;

public class ControlFlowScoreModel extends AbstractRuleLogScoreModel{
	
	private Map<String, Float> ruleToLogMap;
	private Map<String, Float> logToRuleMap;
	private Map<String, ControlFlowScore> controlFlowScoreMap;
	
	public ControlFlowScoreModel() {
		ruleToLogMap = new HashMap<String, Float>();
		logToRuleMap = new HashMap<String, Float>();
		controlFlowScoreMap = new HashMap<String, ControlFlowScore>();
	}
	
	public void calculateControlFlowScore(AnomalyDetectionMiningParameters parameters) {
		ArrayList<String> caseList = new ArrayList<String>();
		caseList.addAll(ruleToLogMap.keySet());
		
		for(int i = 0; i < caseList.size(); i++) {
			String currCaseID = caseList.get(i);
			
			ControlFlowScore score = new ControlFlowScore(currCaseID, ruleToLogMap.get(currCaseID), logToRuleMap.get(currCaseID), parameters);
			controlFlowScoreMap.put(currCaseID, score);
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
	
	public Map<String, ControlFlowScore> getControlFlowScoreMap() {
		return controlFlowScoreMap;
	}

	public void setControlFlowScoreMap(Map<String, ControlFlowScore> controlFlowScoreMap) {
		this.controlFlowScoreMap = controlFlowScoreMap;
	}
	
	
}
