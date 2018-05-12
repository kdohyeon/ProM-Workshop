package org.processmining.mining.anomaly.profile;

public class AnomalyProfileMiningParameters {
	private float minSupport;
	private float minConfidence;
	
	
	public AnomalyProfileMiningParameters() {
		minSupport = 0;
		minConfidence = 0;
	}


	public float getMinSupport() {
		return minSupport;
	}


	public void setMinSupport(float minSupport) {
		this.minSupport = minSupport;
	}


	public float getMinConfidence() {
		return minConfidence;
	}


	public void setMinConfidence(float minConfidence) {
		this.minConfidence = minConfidence;
	}	
}
