package org.processmining.plugins.anomaly.randomGenerator;

public class RandomLogGeneratorParameters {
	
	private boolean isTimeMutatorOn;
	private boolean isNewResourceMutatorOn;
	
	private float mutationPercentageInCase;
	
	public RandomLogGeneratorParameters() {
		isTimeMutatorOn = false;
		isNewResourceMutatorOn = false;
		
		setMutationPercentageInCase((float)0.5);
	}

	public boolean isTimeMutatorOn() {
		return isTimeMutatorOn;
	}

	public void setisTimeMutatorOn(boolean isTimeMutatorOn) {
		this.isTimeMutatorOn = isTimeMutatorOn;
	}

	public boolean isNewResourceMutatorOn() {
		return isNewResourceMutatorOn;
	}

	public void setNewResourceMutatorOn(boolean isNewResourceMutatorOn) {
		this.isNewResourceMutatorOn = isNewResourceMutatorOn;
	}

	public float getMutationPercentageInCase() {
		return mutationPercentageInCase;
	}

	public void setMutationPercentageInCase(float mutationPercentageInCase) {
		this.mutationPercentageInCase = mutationPercentageInCase;
	}
	
}
