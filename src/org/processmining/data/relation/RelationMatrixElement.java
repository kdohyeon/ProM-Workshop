package org.processmining.data.relation;

public class RelationMatrixElement {
	
	private String antecedent;
	private String consequent;
	private String relationType;
	private int freq;
	private float support;
	private float confidence;
	
	public RelationMatrixElement(String antecedent, String consequent, String relationType, int freq, float support, float confidence) {
		this.antecedent = antecedent;
		this.consequent = consequent;
		this.relationType = relationType;
		this.freq = freq;
		this.setSupport(support);
		this.setConfidence(confidence);
	}

	public String getAntecedent() {
		return antecedent;
	}

	public void setAntecedent(String antecedent) {
		this.antecedent = antecedent;
	}

	public String getConsequent() {
		return consequent;
	}

	public void setConsequent(String consequent) {
		this.consequent = consequent;
	}

	public String getRelationType() {
		return relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}

	public int getFreq() {
		return freq;
	}

	public void setFreq(int freq) {
		this.freq = freq;
	}

	public float getSupport() {
		return support;
	}

	public void setSupport(float support) {
		this.support = support;
	}

	public float getConfidence() {
		return confidence;
	}

	public void setConfidence(float confidence) {
		this.confidence = confidence;
	}
}
