package org.processmining.models.anomaly.detection;

import java.util.ArrayList;

public class AbstractRuleMatchingModel {
	public AbstractRuleMatchingModel() {
		
	}
	
	public float divide(int numer, int denom) {
		float result = 0;
		result = (float)((numer * 1.0) / denom);
		return result;
	}
	
	
	
	public float computeRuleToLog(ArrayList<String> rules, ArrayList<String> log) {
		//System.out.println(" ### Rule to Log ### ");
		float result = 0;
		int cnt = 0;
		
		for(int i = 0; i < rules.size(); i++) {
			for(int j = 0; j < log.size(); j++) {
				if(rules.get(i).equals(log.get(j))) {
					cnt++;
					break;
				}	
			}
		}
		
		int unmatched = rules.size() - cnt;
		//System.out.println("Matched: " + cnt + ", Unmatched: " + unmatched + ", Out of: " + rules.size());
		
		result = (float) (unmatched * 1.0 / rules.size());
		
		return result;
	}
	
	public float computeLogToRule(ArrayList<String> rules, ArrayList<String> log) {
		//System.out.println(" ### Log To Rule ### ");
		
		float result = 0;
		int cnt = 0;
		
		for(int i = 0; i < log.size(); i++) {
			for(int j = 0; j < rules.size(); j++) {
				if(rules.get(j).equals(log.get(i))) {
					cnt++;
					break;
				}	
			}
		}
		int unmatched = log.size() - cnt;
		//System.out.println("Matched: " + cnt + ", Unmatched: " + unmatched + ", Out of: " + log.size());
		
		result = (float)(unmatched * 1.0 / log.size());
		
		return result;
	}
}
