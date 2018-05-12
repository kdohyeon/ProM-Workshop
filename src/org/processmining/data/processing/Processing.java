package org.processmining.data.processing;

import java.util.ArrayList;

import org.processmining.data.activity.Activity;

public class Processing {
	
	private String activityID;
	private String resourceID;
	
	private ArrayList<Activity> activityList;
	
	private float avg;
	private float stdev;
	private float min;
	private float max;
	private float median;
	private int freq;
	
	public Processing(String activityID, String resourceID) {
		this.activityID = activityID;
		this.resourceID = resourceID;
		
		this.activityList = new ArrayList<Activity>();
	}
	
	public Processing(String activityID) {
		this.activityID = activityID;
		this.resourceID = activityID;
		
		this.activityList = new ArrayList<Activity>();
	}
	
	public int calculateFreq() {
		freq = activityList.size();
		return freq;
	}
	
	public float calculateSum() {
		float result = 0;
		for(int i = 0; i < activityList.size(); i++) {
			result = result + activityList.get(i).getProcessingTime();
		}
		
		return result;
	}
	
	public float calculateAverage() {
		float result = 0;
		result = calculateSum();
		
		avg = result / activityList.size();
		return avg;
	}
	
	public float calculateStdev() {
		float result = 0;
		float avg = calculateAverage();
		
		for(int i = 0; i < activityList.size(); i++){
			result = (float) (result + Math.pow((activityList.get(i).getProcessingTime() - avg), 2));
		}
		
		result = (float)Math.sqrt(result / activityList.size());
		
		stdev = result;
		
		return result;
	}
	
	public float calculateMin() {
		float result = 0;
		
		result = activityList.get(0).getProcessingTime();
		for(int i = 1; i < activityList.size(); i++)
		{
			if(result > activityList.get(i).getProcessingTime())
			{
				result = activityList.get(i).getProcessingTime();
			}
		}
		
		min = result;
		return min;
	}
	
	public float calculateaMax() {
		float result = 0;
		
		result = activityList.get(0).getProcessingTime();
		for(int i = 1; i < activityList.size(); i++)
		{
			if(result < activityList.get(i).getProcessingTime())
			{
				result = activityList.get(i).getProcessingTime();
			}
		}
		
		max = result;
		return max;
	}

	public void addActivity(Activity act) {
		activityList.add(act);
	}
	
	public String getActivityID() {
		return activityID;
	}

	public void setActivityID(String activityID) {
		this.activityID = activityID;
	}

	public String getResourceID() {
		return resourceID;
	}

	public void setResourceID(String resourceID) {
		this.resourceID = resourceID;
	}

	public float getAvg() {
		return avg;
	}

	public void setAvg(float avg) {
		this.avg = avg;
	}

	public float getStdev() {
		return stdev;
	}

	public void setStdev(float stdev) {
		this.stdev = stdev;
	}

	public float getMin() {
		return min;
	}

	public void setMin(float min) {
		this.min = min;
	}

	public float getMax() {
		return max;
	}

	public void setMax(float max) {
		this.max = max;
	}

	public float getMedian() {
		return median;
	}

	public void setMedian(float median) {
		this.median = median;
	}

	public int getFreq() {
		return freq;
	}

	public void setFreq(int freq) {
		this.freq = freq;
	}
	
}
