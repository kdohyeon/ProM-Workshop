package org.processmining.models.anomaly.profile;

import java.util.ArrayList;

import org.processmining.data.processing.Processing;
import org.processmining.models.activity.ActivityModel;

public class ProcessingProfileModel {
	private ActivityModel actModel;
	
	private ArrayList<Processing> processingList_Act_Res;
	private ArrayList<Processing> processingList_Act;
	private ArrayList<Processing> processingList_Res;
	
	public ProcessingProfileModel(ActivityModel actModel) {
		this.actModel = actModel;
		processingList_Act_Res = new ArrayList<Processing>();
		processingList_Act = new ArrayList<Processing>();
		processingList_Res = new ArrayList<Processing>();
		
		getProcessingProfileModel_Act_Res();
		getProcessingProfileModel_Act();
		getProcessingProfileModel_Res();
	}
	
	private void getProcessingProfileModel_Act_Res() {
		int size = actModel.getActivityCardinality();
		
		ArrayList<String> activityList = new ArrayList<String>();
		activityList = actModel.getUniqueActivityID();
		
		ArrayList<String> resourceList = new ArrayList<String>();
		resourceList = actModel.getUniqueResourceID();

		for(int i  = 0; i < activityList.size(); i++) { // for each activity
			String activityID = activityList.get(i);
			
			for(int j = 0; j < resourceList.size(); j++) { // for each resource
				String resourceID = resourceList.get(j);
				
				Processing p = new Processing(activityID, resourceID);
				
				boolean isActivity = false;
				boolean isResource = false;
				
				for(int k = 0; k < size; k++) {
					if(actModel.getActivityID(k).equals(activityID) && actModel.getResource(k).equals(resourceID)) {
						isActivity = true;
						isResource = true;
						p.addActivity(actModel.getActivity(k));
					}
				}
				 
				if(isActivity && isResource) {
					p.calculateFreq();
					p.calculateAverage();
					p.calculateaMax();
					p.calculateMin();
					p.calculateStdev();
					
					processingList_Act_Res.add(p);
				}
			}
		}
	}
	
	private void getProcessingProfileModel_Act() {
		//System.out.println("### GET PROCESSING PROFILE MODEL ACTIVITY ONLY");
		int size = actModel.getActivityCardinality();
		
		ArrayList<String> activityList = new ArrayList<String>();
		activityList = actModel.getUniqueActivityID();
		
		for(int i  = 0; i < activityList.size(); i++) { // for each activity
			String activityID = activityList.get(i);
			
			Processing p = new Processing(activityID);
			
			boolean isActivity = false;
			
			for(int k = 0; k < size; k++) {
				if(actModel.getActivityID(k).equals(activityID)) {
					isActivity = true;
					p.addActivity(actModel.getActivity(k));
				}
			}
			
			if(isActivity) {
				p.calculateFreq();
				p.calculateAverage();
				p.calculateaMax();
				p.calculateMin();
				p.calculateStdev();
				
				processingList_Act.add(p);
				
				//System.out.println(activityID + ": " + p.getAvg() + " +/- " + p.getStdev());
			}
		}
	}
	
	private void getProcessingProfileModel_Res() {
		int size = actModel.getActivityCardinality();
		
		ArrayList<String> resourceList = new ArrayList<String>();
		resourceList = actModel.getUniqueResourceID();
		
		for(int i  = 0; i < resourceList.size(); i++) { // for each activity
			String resourceID = resourceList.get(i);
			
			Processing p = new Processing(resourceID);
			
			boolean isResource = false;
			
			for(int k = 0; k < size; k++) {
				if(actModel.getResource(k).equals(resourceID)) {
					isResource = true;
					p.addActivity(actModel.getActivity(k));
				}
			}
			
			if(isResource) {
				p.calculateFreq();
				p.calculateAverage();
				p.calculateaMax();
				p.calculateMin();
				p.calculateStdev();
				
				processingList_Res.add(p);
			}
		}
	}
	
	public ArrayList<Processing> getProcessingList_Act_Res(){
		return processingList_Act_Res;
	}
	
	public int getProcessingList_Act_Res_Size() {
		return processingList_Act_Res.size();
	}
	
	public ArrayList<Processing> getProcessingList_Act(){
		return processingList_Act;
	}
	
	public int getProcessingList_Act_Size() {
		return processingList_Act.size();
	}
	
	public ArrayList<Processing> getProcessingList_Res(){
		return processingList_Res;
	}
	
	public int getProcessingList_Res_Size() {
		return processingList_Res.size();
	}
}
