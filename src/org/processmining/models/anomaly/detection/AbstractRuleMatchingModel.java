package org.processmining.models.anomaly.detection;

public class AbstractRuleMatchingModel {
	public AbstractRuleMatchingModel() {
		
	}
	
	public float divide(int numer, int denom) {
		float result = 0;
		result = (float)((numer * 1.0) / denom);
		return result;
	}
}
