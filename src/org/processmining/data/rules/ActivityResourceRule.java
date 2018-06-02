package org.processmining.data.rules;

public class ActivityResourceRule {
	private String act;
	private int freq;
	private float minSupp;
	private float minConf;
	
	public ActivityResourceRule(String act, int freq, float minSupp, float minConf) {
		this.act = act;
		this.minSupp = minSupp;
		this.minConf = minConf;
		this.freq = freq;
	}
	
	public String getAct() {
		return act;
	}

	public void setAct(String act) {
		this.act = act;
	}

	public float getMinSupp() {
		return minSupp;
	}

	public void setMinSupp(float minSupp) {
		this.minSupp = minSupp;
	}

	public float getMinConf() {
		return minConf;
	}

	public void setMinConf(float minConf) {
		this.minConf = minConf;
	}



	public int getFreq() {
		return freq;
	}



	public void setFreq(int freq) {
		this.freq = freq;
	}
}
