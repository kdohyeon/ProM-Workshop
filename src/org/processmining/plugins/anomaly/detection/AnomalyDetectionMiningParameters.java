package org.processmining.plugins.anomaly.detection;

public class AnomalyDetectionMiningParameters {
	
	private float sigma;
	
	private boolean isProcessing;
	private boolean isTransition;
	private boolean isOverlap;
	private boolean isTrueX;
	private boolean isTrueY;
	
	private float logRuleRatio;
	
	private int controlFlow;
	private int time;
	private int resource;
	
	
	public AnomalyDetectionMiningParameters() {
		sigma = 1;
		
		isProcessing = true;
		isTransition = true;
		isOverlap = true;
		isTrueX = true;
		isTrueY = true;
		
		logRuleRatio = (float) 0.5;
		
		controlFlow = 1;
		time = 1;
		resource = 1;
		
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


	public float getLogRuleRatio() {
		return logRuleRatio;
	}

	public void setLogRuleRatio(float logRuleRatio) {
		this.logRuleRatio = logRuleRatio;
	}

	public int getControlFlow() {
		return controlFlow;
	}

	public void setControlFlow(int controlFlow) {
		this.controlFlow = controlFlow;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getResource() {
		return resource;
	}

	public void setResource(int resource) {
		this.resource = resource;
	}

	
}
