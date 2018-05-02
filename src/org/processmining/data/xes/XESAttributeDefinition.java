package org.processmining.data.xes;

public class XESAttributeDefinition {
	
	/**
	 * KEY
	 * */
	private static String CASE_ID = "concept:name";
	private static String RESOURCE = "Resource";
	private static String ACTIVITY = "Activity";
	private static String EVENT_TYPE = "EventType";
	private static String TIMESTAMP = "time:timestamp";
	private static String EVENT_ID  = "EventID";
	
	/**
	 * VALUE
	 * */
	private static String EVENT_TYPE_START = "start";
	private static String EVENT_TYPE_COMPLETE = "complete";
	
	public XESAttributeDefinition() {
		
	}

	public static String getRESOURCE() {
		return RESOURCE;
	}

	public static void setRESOURCE(String rESOURCE) {
		RESOURCE = rESOURCE;
	}

	public static String getACTIVITY() {
		return ACTIVITY;
	}

	public static void setACTIVITY(String aCTIVITY) {
		ACTIVITY = aCTIVITY;
	}

	public static String getEVENT_TYPE() {
		return EVENT_TYPE;
	}

	public static void setEVENT_TYPE(String eVENT_TYPE) {
		EVENT_TYPE = eVENT_TYPE;
	}

	public static String getTIMESTAMP() {
		return TIMESTAMP;
	}

	public static void setTIMESTAMP(String tIMESTAMP) {
		TIMESTAMP = tIMESTAMP;
	}

	public static String getEVENT_TYPE_START() {
		return EVENT_TYPE_START;
	}

	public static void setEVENT_TYPE_START(String eVENT_TYPE_START) {
		EVENT_TYPE_START = eVENT_TYPE_START;
	}

	public static String getEVENT_TYPE_COMPLETE() {
		return EVENT_TYPE_COMPLETE;
	}

	public static void setEVENT_TYPE_COMPLETE(String eVENT_TYPE_COMPLETE) {
		EVENT_TYPE_COMPLETE = eVENT_TYPE_COMPLETE;
	}

	public static String getEVENT_ID() {
		return EVENT_ID;
	}

	public static void setEVENT_ID(String eVENT_ID) {
		EVENT_ID = eVENT_ID;
	}

	public static String getCASE_ID() {
		return CASE_ID;
	}

	public static void setCASE_ID(String cASE_ID) {
		CASE_ID = cASE_ID;
	}
}
