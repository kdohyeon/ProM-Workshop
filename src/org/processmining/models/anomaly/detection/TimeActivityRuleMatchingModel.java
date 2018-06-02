package org.processmining.models.anomaly.detection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.processmining.data.processing.Processing;
import org.processmining.models.activity.ActivityModel;
import org.processmining.models.anomaly.profile.AnomalyProfileModel;
import org.processmining.models.anomaly.profile.ProcessingProfileModel;
import org.processmining.plugins.anomaly.detection.AnomalyDetectionMiningParameters;

public class TimeActivityRuleMatchingModel extends AbstractRuleMatchingModel{
	private AnomalyProfileModel profileModel;
	private ActivityModel testActModel;
	private AnomalyDetectionMiningParameters parameters;
	private Map<String, Float> resultMap;
	
	public TimeActivityRuleMatchingModel(AnomalyProfileModel profileModel, ActivityModel testActModel, AnomalyDetectionMiningParameters parameters) {
		/*
		 * Parameters
		 * */
		this.setParameters(parameters);
		this.setProfileModel(profileModel);
		this.setTestActModel(testActModel);
		resultMap = new HashMap<String, Float>();
		
		// get case id list
		ArrayList<String> caseIDList = new ArrayList<String>();
		caseIDList = testActModel.getUniqueCaseID();
		
		// for each activity
		ArrayList<String> testActSet = new ArrayList<String>();
		testActSet.addAll(testActModel.getUniqueActivityID());
		
		// processing performance measure
		ProcessingProfileModel processingModel = profileModel.getProcessingModel();
		ArrayList<Processing> processing = new ArrayList<Processing>();
		processing = processingModel.getProcessingList_Act();
		
		for(int i = 0; i < caseIDList.size(); i++) {
			String thisCase = caseIDList.get(i);
			
			ArrayList<String> temp = new ArrayList<String>();
			for(int j = 0; j < testActModel.getActivityCardinality(); j++) {
				if(thisCase.equals(testActModel.getActivity(j).getCaseID())) {
					temp.add(testActModel.getActivity(j).getActivityID());
				}
			}
			
			int cnt = 0;
			for(int j = 0; j < temp.size(); j++) {
				String thisActivity = temp.get(j);
				for(int k = 0; k < processing.size(); k++) {
					if(processing.get(k).getActivityID().equals(thisActivity)) {
						float avg = processing.get(k).getAvg();
						float std = processing.get(k).getStdev();
						float lowerBound = avg - 3*std;
						float upperBound = avg + 3*std;
						
						float processingTime = testActModel.getProcessingTime(thisCase, thisActivity);
						
						if(lowerBound <= processingTime && processingTime <= upperBound) {
							cnt++;
						}
						break;
					}
				}
			}
			
			//System.out.println("Time Activity - " + thisCase);
			float unmatched = temp.size() - cnt;
			float timeActivity = (float)( (unmatched * 1.0) / temp.size());
			//System.out.println(timeActivity);
			
			// calculate
			resultMap.put(thisCase, timeActivity);
		}
		
		// check whether it fits in the profile model
		
		
	}

	public AnomalyProfileModel getProfileModel() {
		return profileModel;
	}

	public void setProfileModel(AnomalyProfileModel profileModel) {
		this.profileModel = profileModel;
	}

	public ActivityModel getTestActModel() {
		return testActModel;
	}

	public void setTestActModel(ActivityModel testActModel) {
		this.testActModel = testActModel;
	}

	public AnomalyDetectionMiningParameters getParameters() {
		return parameters;
	}

	public void setParameters(AnomalyDetectionMiningParameters parameters) {
		this.parameters = parameters;
	}

	public Map<String, Float> getResultMap() {
		return resultMap;
	}

	public void setResultMap(Map<String, Float> resultMap) {
		this.resultMap = resultMap;
	}
}
