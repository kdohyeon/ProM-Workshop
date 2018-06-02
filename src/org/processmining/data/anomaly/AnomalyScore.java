package org.processmining.data.anomaly;

import org.processmining.plugins.anomaly.detection.AnomalyDetectionMiningParameters;

public class AnomalyScore {
	
	private String caseID;
	private float overallScore;
	private ControlFlowScore cfScore;
	private ResourceScore rScore;
	private AnomalyDetectionMiningParameters parameters;
	
	public AnomalyScore(String caseID, ControlFlowScore cfScore, ResourceScore rScore, AnomalyDetectionMiningParameters parameters) {
		this.setCaseID(caseID);
		this.cfScore = cfScore;
		this.rScore = rScore;
		this.parameters = parameters;
		
		calculateOverallScore();
		
	}

	/*
	 * Calculate the overall score
	 * */
	private void calculateOverallScore() {
		/*
		System.out.println(parameters.getAlpha() + ", " + cfScore.getControlFlowScore() + ", " + parameters.getBeta() + ", " + rScore.getResourceScore());
		overallScore = parameters.getAlpha() * cfScore.getControlFlowScore() + parameters.getBeta() * rScore.getResourceScore();
		System.out.println(overallScore);
		*/
	}
	
	/*
	 * Get the overall score
	 * */
	public float getOverallScore() {
		return overallScore;
	}

	public String getCaseID() {
		return caseID;
	}

	public void setCaseID(String caseID) {
		this.caseID = caseID;
	}

	public int compareTo(AnomalyScore value) {
		// TODO Auto-generated method stub
		int result = 0;
		if(this.getOverallScore() > value.getOverallScore()) {
			result = 1;
		}else if(this.getOverallScore() < value.getOverallScore()) {
			result = -1;
		}else {
			result = 0;
		}
		return result;
	}

	public ControlFlowScore getCfScore() {
		return cfScore;
	}

	public void setCfScore(ControlFlowScore cfScore) {
		this.cfScore = cfScore;
	}

	public ResourceScore getrScore() {
		return rScore;
	}

	public void setrScore(ResourceScore rScore) {
		this.rScore = rScore;
	}

	public AnomalyDetectionMiningParameters getParameters() {
		return parameters;
	}

	public void setParameters(AnomalyDetectionMiningParameters parameters) {
		this.parameters = parameters;
	}

	public void setOverallScore(float overallScore) {
		this.overallScore = overallScore;
	}
}
