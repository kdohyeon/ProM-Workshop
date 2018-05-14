package org.processmining.mining.anomaly.score;

import java.util.ArrayList;

import org.processmining.data.anomaly.AnomalyScore;

public class AnomalyScoreModel {
	
	private ArrayList<AnomalyScore> anomalyScoreList;
	
	public AnomalyScoreModel() {
		setAnomalyScoreList(new ArrayList<AnomalyScore>());
	}

	/* Functions */
	
	public void addAnomalyScore(AnomalyScore as) {
		anomalyScoreList.add(as);
	}
	
	
	/*
	 * Getters and Setters
	 * */
	
	public ArrayList<AnomalyScore> getAnomalyScoreList() {
		return anomalyScoreList;
	}

	public void setAnomalyScoreList(ArrayList<AnomalyScore> anomalyScoreList) {
		this.anomalyScoreList = anomalyScoreList;
	}
}
