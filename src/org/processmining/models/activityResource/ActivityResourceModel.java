package org.processmining.models.activityResource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.plugins.anomaly.profile.AnomalyProfileMiningParameters;

public class ActivityResourceModel {
	
	private int allEvent;
	private XLog log;
	private Map<String, Map<String, Integer>> actResMap;
	private ArrayList<String> activitySideRules;
	private ArrayList<String> resourceSideRules;
	private float minSupp;
	private float minConf;
	
	/**
	 * Constructor
	 * 
	 * @param parameters 
	 * */
	public ActivityResourceModel(XLog log, AnomalyProfileMiningParameters parameters) {
		// parameters
		this.log = log;
		allEvent = 0;
		actResMap = new HashMap<String, Map<String, Integer>>();
		activitySideRules = new ArrayList<String>();
		resourceSideRules = new ArrayList<String>();
		setMinSupp(parameters.getMinSupport());
		setMinConf(parameters.getMinConfidence());
		
		//setMinConf((float) (0.95*1.0));
		//System.out.println("Newly Set Min Conf = " + getMinConf());
		
		getActivityResourceModel();
		computeActivitySideRules();
		computeResourceSideRules();
	}
	
	/**
	 * compute activity-side rules
	 * */
	private void computeActivitySideRules() {
		Iterator<String> actItr = actResMap.keySet().iterator();
		while(actItr.hasNext()) {
			String actName = actItr.next();
			int summedActFreq = getSummedActivityFrequency(actName);
			
			Iterator<String> resItr = actResMap.get(actName).keySet().iterator();
			while(resItr.hasNext()) {
				String resName = resItr.next();
				int actResFreq = getUnitFrequency(actName, resName);
				
				float result = (float)((actResFreq * 1.0) / summedActFreq);
				
				if(result > minConf) {
					activitySideRules.add(actName + "_" + resName);
				}
			}
		}
	}
	
	/**
	 * compute resource-side rules
	 * */
	private void computeResourceSideRules() {
		Iterator<String> actItr = actResMap.keySet().iterator();
		while(actItr.hasNext()) {
			String actName = actItr.next();
			
			Iterator<String> resItr = actResMap.get(actName).keySet().iterator();
			while(resItr.hasNext()) {
				String resName = resItr.next();
				
				int summedResFreq = getSummedResourceFrequency(resName);
				
				int actResFreq = getUnitFrequency(actName, resName);
				
				float result = (float)((actResFreq * 1.0) / summedResFreq);
				
				if(result > minConf) {
					resourceSideRules.add(actName + "_" + resName);
				}
			}
		}
	}
	
	/**
	 * get activity resource model
	 * */
	private void getActivityResourceModel() {
		int caseNum = log.size();
		for(int i = 0; i < caseNum; i++) {
			XTrace thisCase = log.get(i);
			int eventNum = thisCase.size();
			
			for(int j = 0; j < eventNum; j++) {
				String thisActivity = thisCase.get(j).getAttributes().get("concept:name").toString();
				String thisResource = thisCase.get(j).getAttributes().get("org:resource").toString();
				
				// check if actResMap contains thisActivity as a key
				if(actResMap.containsKey(thisActivity)) {
					// check if actResMap contains thisResource as a key
					Map<String, Integer> resMap = actResMap.get(thisActivity);
					if(resMap.containsKey(thisResource)) {
						// increase the frequency
						int currFreq = resMap.get(thisResource);
						currFreq++;
						resMap.put(thisResource, currFreq);
					}else {
						// if resource is not in the map, then put this resource
						resMap.put(thisResource, 1);
					}
				}else {
					// if activity is not in the map, then put this activity and resource
					actResMap.put(thisActivity, new HashMap<String, Integer>());
					actResMap.get(thisActivity).put(thisResource, 1);
				}
				
				allEvent++;
			}
		}
		
	}
	
	/**
	 * GET frequency of an activity and a resource
	 * */
	public int getUnitFrequency(String actName, String resName) {
		int result = 0;
		if(actResMap.containsKey(actName)) {
			//System.out.println("The input activity: " + actName + " is available!");
			if(actResMap.get(actName).containsKey(resName)) {
				//System.out.println("The input resource: " + resName + " is available!");
				result = actResMap.get(actName).get(resName);
				//System.out.println(result);		
			}else {
				//System.out.println("The input resource: " + resName + " is not available!");
			}
		}else {
			// System.out.println("The input activity: " + actName + " is not available!");
		}
		return result;
	}
	
	/**
	 * GET summed frequencies of an activity
	 * */
	public int getSummedActivityFrequency(String actName) {
		int result = 0;
		
		if(actResMap.containsKey(actName)) {
			//System.out.println("The input activity: " + actName + " is available!");
			//System.out.println("Printing... " + actName);

			Iterator<String> itr = actResMap.get(actName).keySet().iterator();
			
			while(itr.hasNext()) {
				String resName = itr.next();
				result = result + actResMap.get(actName).get(resName);
			}
			//System.out.println("Summed: " + result);
		}else {
			//System.out.println("The input activity: " + actName + " is not available!");
		}
		
		return result;
	}
	
	/**
	 * GET summed frequencies of a resource
	 * */
	public int getSummedResourceFrequency(String resName) {
		//System.out.println("Printing... " + resName);
		Iterator<String> itr = actResMap.keySet().iterator();
		int result = 0;
		while(itr.hasNext()) {
			String actName = itr.next();
			int actFreq = 0;
			if(actResMap.get(actName).containsKey(resName)) {
				actFreq = actResMap.get(actName).get(resName);
			}
			result = result + actFreq;
		}
		//System.out.println("Summed: " + result);
		return result;
	}
	
	/**
	 * get all the frequency
	 * */
	public int getAllEvent() {
		return allEvent;
	}
	
	/**
	 * PRINT frequency of an activity and a resource
	 * */
	public void printUnitFrequency(String actName, String resName) {
		int result = 0;
		if(actResMap.containsKey(actName)) {
			System.out.println("The input activity: " + actName + " is available!");
			if(actResMap.get(actName).containsKey(resName)) {
				System.out.println("The input resource: " + resName + " is available!");
				result = actResMap.get(actName).get(resName);
				System.out.println(result);		
			}else {
				System.out.println("The input resource: " + resName + " is not available!");
			}
		}else {
			System.out.println("The input activity: " + actName + " is not available!");
		}
	}
	
	/**
	 * PRINT resource frequencies of an activity
	 * */
	public void printActivityFrequency(String actName) {
		if(actResMap.containsKey(actName)) {
			System.out.println("The input activity: " + actName + " is available!");
			System.out.println("Printing... " + actName);

			Iterator<String> itr = actResMap.get(actName).keySet().iterator();
			while(itr.hasNext()) {
				String resName = itr.next();
				System.out.println(resName + " : " + actResMap.get(actName).get(resName));
			}
		}else {
			System.out.println("The input activity: " + actName + " is not available!");
		}
	}
	
	/**
	 * PRINT activity frequencies of a resource
	 * */
	public void printResourceFrequency(String resName) {
		System.out.println("Printing... " + resName);
		Iterator<String> itr = actResMap.keySet().iterator();
		while(itr.hasNext()) {
			String actName = itr.next();
			int actFreq = 0;
			if(actResMap.get(actName).containsKey(resName)) {
				actFreq = actResMap.get(actName).get(resName);
			}
			System.out.println(actName + " : " + actFreq);
		}
	}	
	
	/**
	 * PRINT summed frequencies of an activity
	 * */
	public void printSummedActivityFrequency(String actName) {
		if(actResMap.containsKey(actName)) {
			System.out.println("The input activity: " + actName + " is available!");
			System.out.println("Printing... " + actName);

			Iterator<String> itr = actResMap.get(actName).keySet().iterator();
			int result = 0;
			while(itr.hasNext()) {
				String resName = itr.next();
				result = result + actResMap.get(actName).get(resName);
			}
			System.out.println("Summed: " + result);
		}else {
			System.out.println("The input activity: " + actName + " is not available!");
		}
	}
	
	/**
	 * PRINT summed frequencies of a resource
	 * */
	public void printSummedResourceFrequency(String resName) {
		System.out.println("Printing... " + resName);
		Iterator<String> itr = actResMap.keySet().iterator();
		int result = 0;
		while(itr.hasNext()) {
			String actName = itr.next();
			int actFreq = 0;
			if(actResMap.get(actName).containsKey(resName)) {
				actFreq = actResMap.get(actName).get(resName);
			}
			result = result + actFreq;
		}
		System.out.println("Summed: " + result);
	}
	
	/**
	 * PRINT activity-side rules
	 * */
	public void printActivitySideRules() {
		int size = activitySideRules.size();
		for(int i = 0; i < size; i++) {
			int cnt = i+1;
			System.out.println(cnt + ": " + activitySideRules.get(i));
		}
	}
	
	/**
	 * PRINT resource-side rules
	 * */
	public void printResourceSideRules() {
		int size = resourceSideRules.size();
		for(int i = 0; i < size; i++) {
			int cnt = i+1;
			System.out.println(cnt + ": " + resourceSideRules.get(i));
		}
	}
	
	/**
	 * GETTERS AND SETTERS
	 * */
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

	public Map<String, Map<String, Integer>> getActResMap() {
		return actResMap;
	}

	public void setActResMap(Map<String, Map<String, Integer>> actResMap) {
		this.actResMap = actResMap;
	}

	public ArrayList<String> getActivitySideRules() {
		return activitySideRules;
	}

	public void setActivitySideRules(ArrayList<String> activitySideRules) {
		this.activitySideRules = activitySideRules;
	}

	public ArrayList<String> getResourceSideRules() {
		return resourceSideRules;
	}

	public void setResourceSideRules(ArrayList<String> resourceSideRules) {
		this.resourceSideRules = resourceSideRules;
	}

	public void setAllEvent(int allEvent) {
		this.allEvent = allEvent;
	}
	
	
}
