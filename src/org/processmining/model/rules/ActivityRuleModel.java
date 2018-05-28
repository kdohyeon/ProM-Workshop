package org.processmining.model.rules;

import java.util.ArrayList;

import org.processmining.data.rules.ActivityRule;

public class ActivityRuleModel {
	ArrayList<ActivityRule> ruleList;
	
	
	public ActivityRuleModel() {
		ruleList = new ArrayList<ActivityRule>();
	}
	
	// add a rule
	public void addActivityRule(ActivityRule rule) {
		ruleList.add(rule);
	}
	
	// get the list
	public ArrayList<ActivityRule> getRuleList(){
		return ruleList;
	}
	
	// get the size
	public int getRuleSize() {
		return ruleList.size();
	}
}
