package org.processmining.models.activity;

import java.util.ArrayList;

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
	
	public String getActivity(int i) {
		return activityArrayList.get(i).getActivityID();
	}
	
	public String getCase(int i) {
		return activityArrayList.get(i).getCaseID();
	}
}