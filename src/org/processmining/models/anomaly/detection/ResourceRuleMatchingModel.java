package org.processmining.models.anomaly.detection;

public class ResourceRuleMatchingModel {
	
	private float resourceScore;
	
	public ResourceRuleMatchingModel() {
		/*
		 * Parameters
		 * */
		resourceScore = 100;
	}
	
	public float getResourceScore() {
		return resourceScore;
	}
}
