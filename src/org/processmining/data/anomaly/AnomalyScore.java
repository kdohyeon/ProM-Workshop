package org.processmining.data.anomaly;

public class AnomalyScore {
	
	private String caseID;
	private float overallScore;
	private float cfScore;
	private float rScore;
	private float cfLogToRuleScore;
	private float cfRuleToLogScore;
	private float rLogToRuleScore;
	private float rRuleToLogScore;
	
	public AnomalyScore(
			String caseID,
			float overallScore,
			float cfScore,
			float rScore,
			float cfLogToRuleScore,
			float cfRuleToLogScore,
			float rLogToRuleScore,
			float rRuleToLogScore
			) {
		this.caseID = caseID;
		this.overallScore = overallScore;
		this.cfScore = cfScore;
		this.rScore = rScore;
		this.cfLogToRuleScore = cfLogToRuleScore;
		this.cfRuleToLogScore = cfRuleToLogScore;
		this.rLogToRuleScore = rLogToRuleScore;
		this.rRuleToLogScore = rRuleToLogScore;
	}

	public String getCaseID() {
		return caseID;
	}

	public void setCaseID(String caseID) {
		this.caseID = caseID;
	}

	public float getOverallScore() {
		return overallScore;
	}

	public void setOverallScore(float overallScore) {
		this.overallScore = overallScore;
	}

	public float getCfScore() {
		return cfScore;
	}

	public void setCfScore(float cfScore) {
		this.cfScore = cfScore;
	}

	public float getrScore() {
		return rScore;
	}

	public void setrScore(float rScore) {
		this.rScore = rScore;
	}

	public float getCfLogToRuleScore() {
		return cfLogToRuleScore;
	}

	public void setCfLogToRuleScore(float cfLogToRuleScore) {
		this.cfLogToRuleScore = cfLogToRuleScore;
	}

	public float getCfRuleToLogScore() {
		return cfRuleToLogScore;
	}

	public void setCfRuleToLogScore(float cfRuleToLogScore) {
		this.cfRuleToLogScore = cfRuleToLogScore;
	}

	public float getrLogToRuleScore() {
		return rLogToRuleScore;
	}

	public void setrLogToRuleScore(float rLogToRuleScore) {
		this.rLogToRuleScore = rLogToRuleScore;
	}

	public float getrRuleToLogScore() {
		return rRuleToLogScore;
	}

	public void setrRuleToLogScore(float rRuleToLogScore) {
		this.rRuleToLogScore = rRuleToLogScore;
	}
}
