package org.processmining.models.anomaly.profile;

import java.util.ArrayList;

import org.processmining.data.processing.Processing;
import org.processmining.models.activity.ActivityModel;

public class ProcessingProfileModel {
	private ActivityModel actModel;
	
	private ArrayList<Processing> processingList;
	
	public ProcessingProfileModel(ActivityModel actModel) {
		this.actModel = actModel;
		processingList = new ArrayList<Processing>();
		
		getProcessingProfileModel();
	}
	
	private void getProcessingProfileModel() {
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
					processingList.add(p);
				}
			}
		}
	}
	
	public void printProcessingProfile() {
		for(int i = 0; i < processingList.size(); i++) {
			System.out.println(
					processingList.get(i).getActivityID() 
					+ ", " + processingList.get(i).getResourceID() 
					+ ": " + processingList.get(i).getFreq() 
					+ ", " + processingList.get(i).getAvg()
					+ ", " + processingList.get(i).getStdev());
		}
	}
}
