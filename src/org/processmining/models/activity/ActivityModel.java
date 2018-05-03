package org.processmining.models.activity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.processmining.data.activity.Activity;

public class ActivityModel {
	private ArrayList<Activity> activityArrayList;
	
	public ActivityModel() {
		activityArrayList = new ArrayList<Activity>();
	}
	
	public void addActivity(Activity act) {
		activityArrayList.add(act);
	}
	
	public String getResource(int i) {
		return activityArrayList.get(i).getResourceID();
	}
	
	public String getStartTimestamp(int i) {
		return activityArrayList.get(i).getStartTimestamp();
	}
	
	public String getCompleteTimestamp(int i) {
		return activityArrayList.get(i).getCompleteTimestamp();
	}
	
	public String getActivityID(int i) {
		return activityArrayList.get(i).getActivityID();
	}
	
	public String getCase(int i) {
		return activityArrayList.get(i).getCaseID();
	}
	
	public int getActivityCardinality() {
		return activityArrayList.size();
	}
	
	public float getProcessingTime(int i) {
		return activityArrayList.get(i).getProcessingTime();
	}
	
	public Activity getActivity(int i) {
		return activityArrayList.get(i);
	}
	
	public ArrayList<String> getUniqueCaseID(){
		ArrayList<String> result = new ArrayList<String>();
		Set<String> caseSet = new HashSet<String>();
		for(int i = 0; i < activityArrayList.size(); i++) {
			caseSet.add(activityArrayList.get(i).getCaseID());
		}
		result.addAll(caseSet);
		return result;
	}
	
	public ArrayList<String> getUniqueActivityID(){
		ArrayList<String> result = new ArrayList<String>();
		Set<String> activitySet = new HashSet<String>();
		for(int i = 0; i < activityArrayList.size(); i++) {
			activitySet.add(activityArrayList.get(i).getActivityID());
		}
		result.addAll(activitySet);
		return result;
	}
	
	public ArrayList<String> getUniqueResourceID(){
		ArrayList<String> result = new ArrayList<String>();
		Set<String> resourceSet = new HashSet<String>();
		for(int i = 0; i < activityArrayList.size(); i++) {
			resourceSet.add(activityArrayList.get(i).getResourceID());
		}
		result.addAll(resourceSet);
		return result;
	}
}