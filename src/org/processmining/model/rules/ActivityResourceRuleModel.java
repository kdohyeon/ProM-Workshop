package org.processmining.model.rules;

import java.util.ArrayList;

import org.processmining.data.rules.ActivityResourceRule;

public class ActivityResourceRuleModel {
	ArrayList<ActivityResourceRule> ruleList;
	
	
	public ActivityResourceRuleModel() {
		ruleList = new ArrayList<ActivityResourceRule>();
	}
	
	// add a rule
	public void addActivityResourceRule(ActivityResourceRule rule) {
		ruleList.add(rule);
	}
	
	// get the list
	public ArrayList<ActivityResourceRule> getRuleList(){
		return ruleList;
	}
	
	// get the size
	public int getRuleSize() {
		return ruleList.size();
	}
}
