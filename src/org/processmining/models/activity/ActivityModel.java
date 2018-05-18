package org.processmining.models.activity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XLog;
import org.processmining.data.activity.Activity;
import org.processmining.data.xes.XESAttributeDefinition;

public class ActivityModel {
	private ArrayList<Activity> activityArrayList;
	
	public ActivityModel(XLog log) throws ParseException {
		activityArrayList = new ArrayList<Activity>();
		this.createActivityModel(log);
	}
	
	public int getCaseFrequencyOfActivity(String actName) {
		int result = 0;
		
		Set<String> caseSet = new HashSet<String>();
		for(int i = 0; i < activityArrayList.size(); i++) {
			if(activityArrayList.get(i).getActivityID().equals(actName)) {
				caseSet.add(activityArrayList.get(i).getCaseID());
			}
		}
		
		result = caseSet.size();
		
		return result;
	}
	
	public void createActivityModel(XLog log) throws ParseException {

		XESAttributeDefinition def = new XESAttributeDefinition();
		
		// for each case
		for(int i = 0; i < log.size(); i++) {
			String caseID = log.get(i).getAttributes().get("concept:name").toString();
			
			// for each event
			for(int j = 0; j < log.get(i).size(); j++) {
				
				// find the start and complete pair
				
				XAttributeMap map = log.get(i).get(j).getAttributes();
				
				int currJ = 0;
				
				Activity act;
				if(map.get(def.getEVENT_TYPE()).toString().equals(def.getEVENT_TYPE_START())) {
					String eventID = map.get(def.getEVENT_ID()).toString();
					//String eventType_complete = def.getEVENT_TYPE_COMPLETE();
					
					boolean isFound = false;
					while(!isFound) {
						if(currJ == log.get(i).size()) {
							System.out.println(eventID);
							break;
						}
						
						XAttributeMap iterMap = log.get(i).get(currJ).getAttributes();
						currJ++;
						if(iterMap.get(def.getEVENT_ID()).toString().equals(eventID) 
								&& iterMap.get(def.getEVENT_TYPE()).toString().equals(def.getEVENT_TYPE_COMPLETE())) {
							
							String activityID = map.get("concept:name").toString();
							String start_timestamp = map.get("time:timestamp").toString();
							String resource = map.get("org:resource").toString();
							String complete_timestamp = iterMap.get(def.getTIMESTAMP()).toString();
							
							act = new Activity(
									caseID, activityID, resource,
									start_timestamp, complete_timestamp,
									eventID
									);

							addActivity(act);
							isFound = true;
						}else {
							
						}
					}
				}				
			}
		}
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
	
	public int getCaseFrequencyOfResource(String resName) {
		int result = 0;
		
		Set<String> caseSet = new HashSet<String>();
		for(int i = 0; i < activityArrayList.size(); i++) {
			if(activityArrayList.get(i).getResourceID().equals(resName)) {
				caseSet.add(activityArrayList.get(i).getCaseID());
			}
		}
		
		result = caseSet.size();
		
		return result;
	}
}