package org.processmining.models.activity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XLog;
import org.processmining.data.activity.Activity;
import org.processmining.data.rules.ActivityResourceRule;
import org.processmining.data.rules.ActivityRule;
import org.processmining.data.xes.XESAttributeDefinition;
import org.processmining.model.rules.ActivityResourceRuleModel;
import org.processmining.model.rules.ActivityRuleModel;

/**
 * Note that there should be no underbar "_" in any of resources
 * */

public class ActivityModel {
	private ArrayList<Activity> activityArrayList;
	private ActivityRuleModel actRuleModel;
	private ActivityResourceRuleModel actResRuleModel;
		
	private float minSupp;
	private float minConf;
	
	public ActivityModel(XLog log) throws ParseException {
		activityArrayList = new ArrayList<Activity>();
		
		actRuleModel = new ActivityRuleModel();
		actResRuleModel = new ActivityResourceRuleModel();
		
		minSupp = 0;
		minConf = 0;
		
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
	
	public void calculateActivityResourceRule() {
		
	}
	
	public void calculateActivityRule() {
		int caseFrequency = this.getUniqueCaseID().size();
		
		Set<String> activitySet = new HashSet<String>();
		Set<String> activityResourceSet = new HashSet<String>();
		Set<String> resourceSet = new HashSet<String>();
		for(int i = 0; i < activityArrayList.size(); i++) {
			activitySet.add(activityArrayList.get(i).getActivityID());
			activityResourceSet.add(activityArrayList.get(i).getActivityID() + "_" + activityArrayList.get(i).getResourceID());
			resourceSet.add(activityArrayList.get(i).getResourceID());
		}
		
		ArrayList<String> activityList = new ArrayList<String>();
		ArrayList<String> activityResourceList = new ArrayList<String>();
		ArrayList<String> resourceList = new ArrayList<String>();
		activityList.addAll(activitySet);
		activityResourceList.addAll(activityResourceSet);
		resourceList.addAll(resourceSet);
		
		
		for(int i = 0; i < activityList.size(); i++) {
			String thisActivity = activityList.get(i);
			
			Map<String, Integer> checkCaseSet = new HashMap<String, Integer>();
			for(int j = 0; j < activityArrayList.size(); j++) {
				if(thisActivity.equals(activityArrayList.get(j).getActivityID())) {
					String caseID = activityArrayList.get(j).getCaseID();
					checkCaseSet.put(caseID, 1);
				}
			}
			
			if(checkCaseSet.size() > 0) {
				float support = (float) (checkCaseSet.size() * 1.0 / caseFrequency);
				float confidence = (float) (checkCaseSet.size() * 1.0 / this.getCaseFrequencyOfActivity(thisActivity));
				if(support > minSupp && confidence > minConf) {
					ActivityRule rule = new ActivityRule(thisActivity, checkCaseSet.size(), support, confidence);
					actRuleModel.addActivityRule(rule);
				}
			}
		}
		
		for(int i = 0; i < activityResourceList.size(); i++) {
			String thisActivityResource = activityResourceList.get(i);
			String thisActivity = thisActivityResource.split("_")[0];
			String thisResource = thisActivityResource.split("_")[1];
			
			Map<String, Integer> checkCaseSet = new HashMap<String, Integer>();
			for(int j = 0; j < activityArrayList.size(); j++) {
				String compareActivityResource = activityArrayList.get(j).getActivityID() + "_" + activityArrayList.get(j).getResourceID();
				if(thisActivityResource.equals(compareActivityResource)) {
					String caseID = activityArrayList.get(j).getCaseID();
					checkCaseSet.put(caseID, 1);
				}
			}
			
			if(checkCaseSet.size() > 0) {
				float support = (float) (checkCaseSet.size() * 1.0 / caseFrequency);
				float confidence = (float) (checkCaseSet.size() * 1.0 / this.getCaseFrequencyOfResource(thisResource));
				if(support > minSupp && confidence > minConf) {
					ActivityResourceRule rule = new ActivityResourceRule(thisActivityResource, checkCaseSet.size(), support, confidence);
					actResRuleModel.addActivityResourceRule(rule);
				}
			}
		}
	}
	
	public ActivityRuleModel getActivityRule(){
		return actRuleModel;
	}
	
	public void createActivityModel(XLog log) throws ParseException {

		XESAttributeDefinition def = new XESAttributeDefinition();
		
		// for each case
		for(int i = 0; i < log.size(); i++) {
			String caseID = log.get(i).getAttributes().get("concept:name").toString();
			//System.out.println(caseID);
			
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
							//System.out.println(eventID);
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
	
	public String getStartTimestamp(String caseID, String activityID) {
		String result = "";
		for(int i = 0; i < activityArrayList.size(); i++) {
			if(activityArrayList.get(i).getCaseID().equals(caseID) && activityArrayList.get(i).getActivityID().equals(activityID)) {
				result = activityArrayList.get(i).getStartTimestamp();
				break;
			}
		}
		return result;
	}
	
	public String getCompleteTimestamp(int i) {
		return activityArrayList.get(i).getCompleteTimestamp();
	}
	
	public String getCompleteTimestamp(String caseID, String activityID) {
		String result = "";
		for(int i = 0; i < activityArrayList.size(); i++) {
			if(activityArrayList.get(i).getCaseID().equals(caseID) && activityArrayList.get(i).getActivityID().equals(activityID)) {
				result = activityArrayList.get(i).getCompleteTimestamp();
				break;
			}
		}
		return result;
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
	
	public float getProcessingTime(String caseID, String activityID) {
		float result = 0;
		for(int i = 0; i < activityArrayList.size(); i++) {
			if(activityArrayList.get(i).getCaseID().equals(caseID) && activityArrayList.get(i).getActivityID().equals(activityID)) {
				result = activityArrayList.get(i).getProcessingTime();
				break;
			}
		}
		return result;
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
	
	public ArrayList<String> getUniqueActivityResourceID(){
		ArrayList<String> result = new ArrayList<String>();
		Set<String> activityResourceSet = new HashSet<String>();
		for(int i = 0; i < activityArrayList.size(); i++) {
			activityResourceSet.add(activityArrayList.get(i).getActivityID() + "_" + activityArrayList.get(i).getResourceID());
		}
		result.addAll(activityResourceSet);
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

	public ArrayList<Activity> getActivityArrayList() {
		return activityArrayList;
	}

	public void setActivityArrayList(ArrayList<Activity> activityArrayList) {
		this.activityArrayList = activityArrayList;
	}

	public ActivityRuleModel getActRuleModel() {
		return actRuleModel;
	}

	public void setActRuleModel(ActivityRuleModel actRuleModel) {
		this.actRuleModel = actRuleModel;
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

	public ActivityResourceRuleModel getActResRuleModel() {
		return actResRuleModel;
	}

	public void setActResRuleModel(ActivityResourceRuleModel actResRuleModel) {
		this.actResRuleModel = actResRuleModel;
	}
}