package org.processmining.data.anomaly;

import org.processmining.mining.anomaly.detection.AnomalyDetectionMiningParameters;

public class ControlFlowScore {
	private String caseID;
	private float cfScore;
	private float ruleToLog;
	private float logToRule;
	private AnomalyDetectionMiningParameters parameters;
	
	public ControlFlowScore(String caseID, float ruleToLog, float logToRule, AnomalyDetectionMiningParameters parameters) {
		this.setCaseID(caseID);
		this.ruleToLog = ruleToLog;
		this.logToRule = logToRule;	
		this.setParameters(parameters);
		
		calculateControlFlowScore();
	}
	
	private void calculateControlFlowScore() {
		cfScore = (1-parameters.getLogRuleRatio()) * ruleToLog + (parameters.getLogRuleRatio()) * logToRule;
	}
	
	public float getControlFlowScore() {
		return cfScore;
	}
	
	public float getRuleToLog() {
		return ruleToLog;
	}

	public void setRuleToLog(float ruleToLog) {
		this.ruleToLog = ruleToLog;
	}

	public float getLogToRule() {
		return logToRule;
	}

	public void setLogToRule(float logToRule) {
		this.logToRule = logToRule;
	}

	public String getCaseID() {
		return caseID;
	}

	public void setCaseID(String caseID) {
		this.caseID = caseID;
	}

	public AnomalyDetectionMiningParameters getParameters() {
		return parameters;
	}

	public void setParameters(AnomalyDetectionMiningParameters parameters) {
		this.parameters = parameters;
	}
	
	
}
