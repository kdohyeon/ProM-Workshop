package org.processmining.mining.anomaly.detection;

public class AnomalyDetectionMiningParameters {
	
	private float sigma;
	
	private boolean isProcessing;
	private boolean isTransition;
	private boolean isOverlap;
	private boolean isTrueX;
	private boolean isTrueY;
	
	private float logRuleRatio;
	
	private float alpha;
	private float beta;
	
	
	public AnomalyDetectionMiningParameters() {
		sigma = 1;
		
		isProcessing = true;
		isTransition = true;
		isOverlap = true;
		isTrueX = true;
		isTrueY = true;
		
		logRuleRatio = (float) 0.5;
		
		alpha = (float) 0.5;
		beta = (float) (1.0 - alpha);
		
	}

	public float getSigma() {
		return sigma;
	}


	public void setSigma(float sigma) {
		this.sigma = sigma;
	}


	public boolean isProcessing() {
		return isProcessing;
	}


	public void setProcessing(boolean isProcessing) {
		this.isProcessing = isProcessing;
	}


	public boolean isTransition() {
		return isTransition;
	}


	public void setTransition(boolean isTransition) {
		this.isTransition = isTransition;
	}


	public boolean isOverlap() {
		return isOverlap;
	}


	public void setOverlap(boolean isOverlap) {
		this.isOverlap = isOverlap;
	}


	public boolean isTrueX() {
		return isTrueX;
	}


	public void setTrueX(boolean isTrueX) {
		this.isTrueX = isTrueX;
	}


	public boolean isTrueY() {
		return isTrueY;
	}


	public void setTrueY(boolean isTrueY) {
		this.isTrueY = isTrueY;
	}


	public float getAlpha() {
		return alpha;
	}


	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}


	public float getBeta() {
		return beta;
	}


	public void setBeta(float beta) {
		this.beta = beta;
	}

	public float getLogRuleRatio() {
		return logRuleRatio;
	}

	public void setLogRuleRatio(float logRuleRatio) {
		this.logRuleRatio = logRuleRatio;
	}

	
}
