package org.processmining.plugins.anomaly.mutator;

public class MutatorParameters {
	
	private float deviationPercentageCase;
	private float deviationPercentageTrace;
	private String deviationType;
	
	public MutatorParameters() {
		setDeviationPercentageCase((float)0.1);
		setDeviationPercentageTrace((float)0.1);
		setDeviationType("");
	}

	public float getDeviationPercentageCase() {
		return deviationPercentageCase;
	}

	public void setDeviationPercentageCase(float deviationPercentage) {
		this.deviationPercentageCase = deviationPercentage;
	}
	
	public float getDeviationPercentageTrace() {
		return deviationPercentageTrace;
	}

	public void setDeviationPercentageTrace(float deviationPercentage) {
		this.deviationPercentageTrace = deviationPercentage;
	}

	public String getDeviationType() {
		return deviationType;
	}

	/**
	 * @param deviationType : "Add", "Remove", "Replace"
	 * */
	public void setDeviationType(String deviationType) {
		this.deviationType = deviationType;
	}
}
