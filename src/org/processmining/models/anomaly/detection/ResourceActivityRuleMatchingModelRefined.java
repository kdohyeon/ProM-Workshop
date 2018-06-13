package org.processmining.models.anomaly.detection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.models.activityResource.ActivityResourceModel;

public class ResourceActivityRuleMatchingModelRefined extends AbstractRuleMatchingModel{
	
	private Map<String, Float> rMap;
	
	public ResourceActivityRuleMatchingModelRefined(XLog testLog, ActivityResourceModel actResModel) {
		/*
		 * Parameters
		 * */
		
		ArrayList<String> activitySideRules = new ArrayList<String>();
		activitySideRules.addAll(actResModel.getActivitySideRules());
		
		ArrayList<String> resourceSideRules = new ArrayList<String>();
		resourceSideRules.addAll(actResModel.getResourceSideRules());
		
		rMap = new HashMap<String, Float>();
		
		
		int caseNum = testLog.size();
		for(int i = 0; i < caseNum; i++) {
			XTrace thisCase = testLog.get(i);
			String caseID = thisCase.getAttributes().get("concept:name").toString();
			int eventNum = thisCase.size();
			int anomalyCnt = 0;
			
			//System.out.println("Case ID: " + caseID + ", Event Num: " + eventNum);
			
			for(int j = 0; j < eventNum; j++) {
				String thisActivity = thisCase.get(j).getAttributes().get("concept:name").toString();
				String thisResource = thisCase.get(j).getAttributes().get("org:resource").toString();
				
				boolean isAnomaly1 = false;
				boolean isAnomaly2 = false;
				
				for(int k = 0; k < activitySideRules.size(); k++) {
					String compActivity = activitySideRules.get(k).split("_")[0];
					String compResource = activitySideRules.get(k).split("_")[1];
					
					if(thisActivity.equals(compActivity) && thisResource.equals(compResource)) {
						isAnomaly1 = true;
						break;
					}
				}
				
				for(int k = 0; k < resourceSideRules.size(); k++) {
					String compActivity = resourceSideRules.get(k).split("_")[0];
					String compResource = resourceSideRules.get(k).split("_")[1];
					
					if(thisActivity.equals(compActivity) && thisResource.equals(compResource)) {
						isAnomaly2 = true;
						break;
					}
				}
				
				if(!isAnomaly1 && !isAnomaly2) {
					anomalyCnt++;
					//System.out.println(thisActivity + ", " + thisResource);
				}
			}
			
			float anomalyScore = (float)( (anomalyCnt*1.0) / eventNum);
			rMap.put(caseID, anomalyScore);
		}
	}

	public Map<String, Float> getrMap() {
		return rMap;
	}

	public void setrMap(Map<String, Float> rMap) {
		this.rMap = rMap;
	}
	
	
}
