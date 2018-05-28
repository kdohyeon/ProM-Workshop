package org.processmining.data.rules;

public class ActivityRule {
	private String act;
	private float minSupp;
	private float minConf;
	
	public ActivityRule(String act, float minSupp, float minConf) {
		this.act = act;
		this.minSupp = minSupp;
		this.minConf = minConf;
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
}
