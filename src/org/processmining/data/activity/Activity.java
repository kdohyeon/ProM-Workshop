package org.processmining.data.activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Activity {
	
	private String caseID;
	private String activityID;
	private String resourceID;
	private String start_timestamp;
	private String complete_timestamp;
	private String eventID;
	private String eventType;

	private float processingTime;
	
	public Activity(String caseID, String activityID, String resourceID, String start_timestamp, String complete_timestamp, String eventID) throws ParseException {
		this.caseID = caseID;
		this.activityID = activityID;
		this.resourceID = resourceID;
		this.start_timestamp = start_timestamp;
		this.complete_timestamp = complete_timestamp;
		this.eventID = eventID;
	
		calculateProcessingTime();
	}
	
	private void calculateProcessingTime() throws ParseException {
		String date_format = "yyyy-MM-dd HH:mm:ss";
		start_timestamp = start_timestamp.replace("T", " ");
		//start_timestamp = start_timestamp.replace("12", "13"); // because it detects 12 and 24 as the same hour, change the hour 12 to 13 temporarily
		complete_timestamp = complete_timestamp.replace("T", " ");
		//complete_timestamp = complete_timestamp.replace("12", "13"); // because it detects 12 and 24 as the same hour, change the hour 12 to 13 temporarily
		DateFormat format = new SimpleDateFormat(date_format);
		
		Date start_date = format.parse(start_timestamp);
		Date complete_date = format.parse(complete_timestamp);
		
		float conversionValue = 60 * 60 * 1000; // hour
		float diff = ((complete_date.getTime() - start_date.getTime()) / (conversionValue));
		//NumberFormat formatter = new DecimalFormat("#0.00");
		
		//System.out.println(start_timestamp + ", " + complete_timestamp);
		//System.out.println(start_date.getTime() + ", " + complete_date.getTime() + ", " + diff);
		
		processingTime = diff;
	}

	public float getProcessingTime() {
		return processingTime;
	}
	
	public String getCaseID() {
		return caseID;
	}

	public void setCaseID(String caseID) {
		this.caseID = caseID;
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

	public String getStartTimestamp() {
		return start_timestamp;
	}

	public void setStartTimestamp(String start_timestamp) {
		this.start_timestamp = start_timestamp;
	}
	
	public String getCompleteTimestamp() {
		return complete_timestamp;
	}

	public void setCompleteTimestamp(String complete_timestamp) {
		this.complete_timestamp = complete_timestamp;
	}

	public String getEventID() {
		return eventID;
	}

	public void setEventID(String eventID) {
		this.eventID = eventID;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
}
