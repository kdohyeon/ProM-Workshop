package org.processmining.models.anomaly.mutator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.impl.XAttributeMapImpl;
import org.deckfour.xes.model.impl.XEventImpl;
import org.deckfour.xes.model.impl.XLogImpl;
import org.deckfour.xes.model.impl.XTraceImpl;
import org.processmining.plugins.anomaly.mutator.MutatorParameters;

import analysis.basics.random.RandomFloat;
import analysis.basics.random.RandomInteger;

/**
 * @author kdohyeon
 * @since 2018. 05. 21
 * 
 * @description: 
 * This model mutates all of the input XES Log. 
 * Therefore, it does not consider what percentage of the log should be mutated.
 * 
 * @parameters:
 * Time mutation:
 * - Start OR complete time mutation
 * 
 *  Resource mutation:
 *  - New resource
 * */

public class MutatorModel {
	
	private XLog log;
	private MutatorParameters parameters;
	
	private XLog mutatedLog;
	
	private String outputPath = "C:\\Users\\dhkim-pc\\Desktop\\Evaluation_PLG\\";
	
	public MutatorModel(XLog log, MutatorParameters parameters) throws IOException, ParseException {
		System.out.println("Mutator Model");
		/*
		 * Parameters
		 * */
		float dp = parameters.getDeviationPercentageCase();
		String type = parameters.getDeviationType();
		System.out.println("Percentage: " + dp);
		System.out.println("Type: " + type);
		int caseSize = log.size();
		
		
		outputPath = outputPath + type + "_" + dp + "_" + log.getAttributes().get("concept:name") + ".csv";
		File file = new File(outputPath);
		FileWriter writer = new FileWriter(file);
		
		writer.append("Case ID" + ","); // case id
		writer.append("Activity ID" + ","); // activity id
		writer.append("Resource ID" + ","); // resource id
		writer.append("Timestamp" + ","); // date and time
		writer.append("EventType" + ","); // transition
		writer.append("Flag" + ","); // flag
		writer.append("EventID" + ","); // event id
		writer.append("\n");
		
		/*
		 * Instantiate
		 * */

		//create the new log and copy the attributes of the old log to the new one
		XAttributeMap logattlist = XLogFunctions.copyAttMap(log.getAttributes());
		mutatedLog = new XLogImpl(logattlist);
		
		/*
		 * Random Log Generation
		 * */
		
		
		// get a random number
		RandomFloat randomFloat = new RandomFloat(caseSize, 0, 1);
		List<String> randomIDList = new ArrayList<String>();
		
		// for each case (trace) in this log
		for(int i = 0; i < caseSize; i++) {
			// this case
			XTrace thisTrace = log.get(i);
			XTrace newTrace;
			int traceSize = thisTrace.size();
			String traceID = thisTrace.getAttributes().get("concept:name").toString();
			
			System.out.println("Case ID: " + traceID);
			
			// if the random number is less than or equal to the parameter, then mutate !
			// and create the new trace and copy the attributes of the old trace to the new one
			//System.out.println(randomFloat.getRandomNumberElement(i) + ", " + dp);
			
			if(randomFloat.getRandomNumberElement(i) <= dp) {
				// copy the old trace and mark this trace as "anomaly"
				System.out.println("Let's change this trace!");
				XAttributeMap newAttributeMap = thisTrace.getAttributes();
				XLogFunctions.putLiteral(newAttributeMap, "Flag", "Anomaly");
				newTrace = new XTraceImpl(XLogFunctions.copyAttMap(newAttributeMap));
				
				randomIDList.add(traceID);
				
				RandomInteger randomTrace = new RandomInteger(2, 0, traceSize-1);
				int randomPosition = randomTrace.getRandomNumber()[0];
				//System.out.println("Trace Size: " + traceSize);
				//System.out.println("Random Position: " + randomPosition);
				
				if(parameters.getDeviationType().equals("Add")) { // if deviation type == "Add"
					String resource = "";
					
					// copy the old trace to the new event until the position
					for(int j = 0; j < randomPosition; j++) {
						XEvent oldEvent = thisTrace.get(j);
						XEvent newEvent = new XEventImpl(XLogFunctions.copyAttMap(oldEvent.getAttributes()));
						newTrace.add(newEvent);
					}
					
					// add a new element (new activity id, new resource, new start date, new complete date) in a random position
					//System.out.println("Add!");
					
					Date start_date;
					Date complete_date;
					
					// start_date = add some random time to start_date (original)
					start_date = XLogFunctions.getTime(thisTrace.get(randomPosition));
					
					// get random second from 900 ~ 2700
					RandomInteger randomInteger = new RandomInteger(1,900,2700);
					int randomSecond = randomInteger.getRandomNumber()[0];
					// add the random second to the start_date (original)
					Calendar calendar1 = Calendar.getInstance();
					calendar1.setTime(start_date);
					calendar1.add(Calendar.SECOND, randomSecond);
					
					complete_date = calendar1.getTime();
					// complete_date = add some random time to the start_date (updated)
					randomInteger = new RandomInteger(1,900,2700);
					randomSecond = randomInteger.getRandomNumber()[0];
					Calendar calendar2 = Calendar.getInstance();
					calendar2.setTime(complete_date);
					calendar2.add(Calendar.SECOND, randomSecond);
					
					String activity = this.getRandomActivity();
					resource = this.getRandomResource();
					String eventID = traceID + "_" + randomPosition;
					newTrace.add(addNewEvent(activity, resource, calendar1.getTime(), "start", eventID));
					newTrace.add(addNewEvent(activity, resource, calendar2.getTime(), "complete", eventID));
					
					// add rest of the event
					for(int j = randomPosition; j < thisTrace.size(); j++) {
						XEvent oldEvent = thisTrace.get(j);
						XEvent newEvent = new XEventImpl(XLogFunctions.copyAttMap(oldEvent.getAttributes()));
						
						newTrace.add(newEvent);
					}
					
				}else if(parameters.getDeviationType().equals("Remove")) { // else if deviation type == "Remove"
					// remove the event that is at randomPosition;
					
					String resource = "";
					// copy the old trace to the new event until the position
					String eventID = thisTrace.get(randomPosition).getAttributes().get("EventID").toString();
					
					for(int j = 0; j < traceSize; j++) {
						if(j == randomPosition || eventID.equals(thisTrace.get(j).getAttributes().get("EventID").toString())) {
							
						}else {
							XEvent oldEvent = thisTrace.get(j);
							XEvent newEvent = new XEventImpl(XLogFunctions.copyAttMap(oldEvent.getAttributes()));

							newTrace.add(newEvent);
	
						}
					}					
				}else if(parameters.getDeviationType().equals("Replace")) { // else if deviation type == "Replace"
					// get two random index in the trace
					// swap randomPosition event and secondRandomPosition event (swap the activity name and resource id, keep the timestamp)
					int secondRandomPosition = randomTrace.getRandomNumber()[1];
					
					int position1_1 = 0; // activity a, start
					int position1_2 = 0; // activity a, complete
					int position2_1 = 0; // activity b, start
					int position2_2 = 0; // activity b, complete
					
					String eventID1 = thisTrace.get(randomPosition).getAttributes().get("EventID").toString();
					String eventID2 = thisTrace.get(secondRandomPosition).getAttributes().get("EventID").toString();
					
					String eventType1 = thisTrace.get(randomPosition).getAttributes().get("EventType").toString();
					String eventType2 = thisTrace.get(secondRandomPosition).getAttributes().get("EventType").toString();
					
					if(eventType1.equals("start")) {
						position1_1 = randomPosition;
						for(int j = 0; j < traceSize; j++) {
							if(thisTrace.get(j).getAttributes().get("EventID").toString().equals(eventID1) 
									&& thisTrace.get(j).getAttributes().get("EventType").toString().equals("complete")) {
								position1_2 = j;
								break;
							}
						}
					}else if(eventType1.equals("complete")){
						position1_2 = randomPosition;
						for(int j = 0; j < traceSize; j++) {
							if(thisTrace.get(j).getAttributes().get("EventID").toString().equals(eventID1) 
									&& thisTrace.get(j).getAttributes().get("EventType").toString().equals("start")) {
								position1_1 = j;
								break;
							}
						}
					}else {
						System.out.println("others!");
					}
					
					if(eventType2.equals("start")) {
						position2_1 = secondRandomPosition;
						for(int j = 0; j < traceSize; j++) {
							if(thisTrace.get(j).getAttributes().get("EventID").toString().equals(eventID2) 
									&& thisTrace.get(j).getAttributes().get("EventType").toString().equals("complete")) {
								position2_2 = j;
								break;
							}
						}
					}else if(eventType2.equals("complete")) {
						position2_2 = secondRandomPosition;
						for(int j = 0; j < traceSize; j++) {
							if(thisTrace.get(j).getAttributes().get("EventID").toString().equals(eventID2) 
									&& thisTrace.get(j).getAttributes().get("EventType").toString().equals("start")) {
								position2_1 = j;
								break;
							}
						}
					}else {
						System.out.println("others!");
					}
					
					/*
					if(randomPosition < secondRandomPosition) {
						position1 = randomPosition;
						position2 = secondRandomPosition;
					}else {
						position1 = secondRandomPosition;
						position2 = randomPosition;
					}
					
					// add the first part until the position1
					String resource = "";
					for(int j = 0; j < position1; j++) {
						XEvent oldEvent = thisTrace.get(j);
						XEvent newEvent = new XEventImpl(XLogFunctions.copyAttMap(oldEvent.getAttributes()));
						
						// get resource
						String thisActivity = oldEvent.getAttributes().get("concept:name").toString();
						if(newTrace.size() == 0) {
							resource = this.getRandomResource();
						}
						
						for(int k = 0; k < newTrace.size(); k++) {
							// if an activity from old event is already in the newTrace, then get the resource name
							if(newTrace.get(k).getAttributes().get("concept:name").toString().equals(thisActivity)) {
								resource = newTrace.get(k).getAttributes().get("org:resource").toString();
								break;
							}else {
								resource = this.getRandomResource();		
							}
						}
						
						XLogFunctions.setResource(newEvent, resource);
						newTrace.add(newEvent);
					}
					
					// position2 event == secondRandomPosition
					XEvent event2 = thisTrace.get(position2);
					String activityID2;
					String eventID2, resource2 = null, transitionStart2, transitionComplete2;
					Date startDate2 = null, completeDate2 = null;
					eventID2 = event2.getAttributes().get("EventID").toString();
					
					// get resource
					String thisActivity = event2.getAttributes().get("concept:name").toString();
					if(newTrace.size() == 0) {
						resource2 = this.getRandomResource();
					}
					for(int k = 0; k < newTrace.size(); k++) {
						// if an activity from old event is already in the newTrace, then get the resource name
						if(newTrace.get(k).getAttributes().get("concept:name").toString().equals(thisActivity)) {
							resource2 = newTrace.get(k).getAttributes().get("org:resource").toString();
							break;
						}else {
							resource2 = this.getRandomResource();		
						}
					}
					
					XLogFunctions.setResource(event2, resource2);
					
					transitionStart2 = "start";
					transitionComplete2 = "complete";
					
					for(int k = 0; k < thisTrace.size(); k++) {
						System.out.println(thisTrace.get(k).getAttributes().get("EventID").toString() + ", " + eventID2);
						System.out.println(thisTrace.get(k).getAttributes().get("EventType").toString() + ", " + transitionStart2);
						
						if(thisTrace.get(k).getAttributes().get("EventID").toString().equals(eventID2)
								&& thisTrace.get(k).getAttributes().get("EventType").toString().equals(transitionStart2)) {
							startDate2 = XLogFunctions.getTime(thisTrace.get(k));
						}
						
						if(thisTrace.get(k).getAttributes().get("EventID").toString().equals(eventID2)
								&& thisTrace.get(k).getAttributes().get("EventType").toString().equals(transitionComplete2)) {
							completeDate2 = XLogFunctions.getTime(thisTrace.get(k));
						}
					}
					
					newTrace.add(addNewEvent(thisActivity,resource2,startDate2,transitionStart2));
					newTrace.add(addNewEvent(thisActivity,resource2,completeDate2,transitionComplete2));
					
					
					// add the second part until the position2
					for(int j = position1+1; j < position2; j++) {
						XEvent oldEvent = thisTrace.get(j);
						XEvent newEvent = new XEventImpl(XLogFunctions.copyAttMap(oldEvent.getAttributes()));
						
						// get resource
						thisActivity = oldEvent.getAttributes().get("concept:name").toString();
						if(newTrace.size() == 0) {
							resource = this.getRandomResource();
						}
						
						for(int k = 0; k < newTrace.size(); k++) {
							// if an activity from old event is already in the newTrace, then get the resource name
							if(newTrace.get(k).getAttributes().get("concept:name").toString().equals(thisActivity)) {
								resource = newTrace.get(k).getAttributes().get("org:resource").toString();
								break;
							}else {
								resource = this.getRandomResource();		
							}
						}
						
						XLogFunctions.setResource(newEvent, resource);
						newTrace.add(newEvent);
					}
					
					// add the position1 event
					XEvent event1 = thisTrace.get(position1);
					String eventID1, resource1 = null, transitionStart1, transitionComplete1;
					Date startDate1 = null, completeDate1 = null;
					eventID1 = event1.getAttributes().get("EventID").toString();
					// get resource
					thisActivity = event1.getAttributes().get("concept:name").toString();
					if(newTrace.size() == 0) {
						resource1 = this.getRandomResource();
					}
					for(int k = 0; k < newTrace.size(); k++) {
						// if an activity from old event is already in the newTrace, then get the resource name
						if(newTrace.get(k).getAttributes().get("concept:name").toString().equals(thisActivity)) {
							resource1 = newTrace.get(k).getAttributes().get("org:resource").toString();
							break;
						}else {
							resource1 = this.getRandomResource();		
						}
					}
					
					XLogFunctions.setResource(event1, resource1);
					
					transitionStart1 = "start";
					transitionComplete1 = "complete";
					
					for(int k = 0; k < thisTrace.size(); k++) {
						System.out.println(thisTrace.get(k).getAttributes().get("EventID").toString() + ", " + eventID1);
						System.out.println(thisTrace.get(k).getAttributes().get("EventType").toString() + ", " + transitionStart1);
						
						if(thisTrace.get(k).getAttributes().get("EventID").toString().equals(eventID1)
								&& thisTrace.get(k).getAttributes().get("EventType").toString().equals(transitionStart1)) {
							startDate1 = XLogFunctions.getTime(thisTrace.get(k));
						}
						
						if(thisTrace.get(k).getAttributes().get("EventID").toString().equals(eventID1)
								&& thisTrace.get(k).getAttributes().get("EventType").toString().equals(transitionComplete1)) {
							completeDate1 = XLogFunctions.getTime(thisTrace.get(k));
						}
					}
					
					newTrace.add(addNewEvent(thisActivity,resource1,startDate1,transitionStart1));
					newTrace.add(addNewEvent(thisActivity,resource1,completeDate1,transitionComplete1));
					
					// add the rest part until the end
					for(int j = position2+1; j < traceSize; j++) {
						XEvent oldEvent = thisTrace.get(j);
						XEvent newEvent = new XEventImpl(XLogFunctions.copyAttMap(oldEvent.getAttributes()));
						
						// get resource
						thisActivity = oldEvent.getAttributes().get("concept:name").toString();
						if(newTrace.size() == 0) {
							resource = this.getRandomResource();
						}
						
						for(int k = 0; k < newTrace.size(); k++) {
							// if an activity from old event is already in the newTrace, then get the resource name
							if(newTrace.get(k).getAttributes().get("concept:name").toString().equals(thisActivity)) {
								resource = newTrace.get(k).getAttributes().get("org:resource").toString();
								break;
							}else {
								resource = this.getRandomResource();		
							}
						}
						
						XLogFunctions.setResource(newEvent, resource);
						newTrace.add(newEvent);
					}
					*/
				}else {
					
				}
			}else { // else just copy the old trace to the new one
				XAttributeMap newAttributeMap = thisTrace.getAttributes();
				XLogFunctions.putLiteral(newAttributeMap, "Flag", "Normal");
				newTrace = new XTraceImpl(XLogFunctions.copyAttMap(newAttributeMap));
				for(int j = 0; j < thisTrace.size(); j++) {
					XEvent oldEvent = thisTrace.get(j);
					XEvent newEvent = new XEventImpl(XLogFunctions.copyAttMap(oldEvent.getAttributes()));
									
					newTrace.add(newEvent);
				}
			}
			
			// add the new trace to the new log
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			for(int j = 0; j < newTrace.size(); j++) {
				writer.append(newTrace.getAttributes().get("concept:name") + ","); // case id
				writer.append(newTrace.get(j).getAttributes().get("concept:name").toString() + ","); // activity id
				writer.append(newTrace.get(j).getAttributes().get("org:resource").toString() + ","); // resource id
				Date date = sdf.parse(newTrace.get(j).getAttributes().get("time:timestamp").toString().replace("T", " "));
				writer.append(sdf.format(date) + ","); // timestamp
				writer.append(newTrace.get(j).getAttributes().get("EventType").toString() + ","); // transition
				writer.append(newTrace.getAttributes().get("Flag").toString() + ","); // flag
				writer.append(newTrace.get(j).getAttributes().get("EventID").toString() + ","); // eventID
				writer.append("\n");
			}
			
			mutatedLog.add(newTrace);
		}
		writer.close();
	}
	
	private XEvent addNewEvent(String activityID, String resource, Date date, String transition, String eventID) {
		XAttributeMap attMap = new XAttributeMapImpl();
		
		XLogFunctions.putLiteral(attMap, "concept:name", activityID);
		XLogFunctions.putLiteral(attMap, "EventType", transition);
		XLogFunctions.putLiteral(attMap, "org:resource", resource);
		XLogFunctions.putLiteral(attMap, "EventID", eventID);
		XLogFunctions.putTimestamp(attMap, "time:timestamp", date);
		
		
		XEvent newEvent = new XEventImpl(attMap);
		/*
		System.out.println(
				"Add New Event" + 
				newEvent.getAttributes().get("lifecycle:transition") 
				+ ", " + newEvent.getAttributes().get("concept:name")
				+ ", " + newEvent.getAttributes().get("org:resource")
				+ ", " + newEvent.getAttributes().get("time:timestamp"));
		*/
		return newEvent;
		
	}
	
	private String getRandomResource() {
		String[] newResourceName = {"Resource R1","Resource R2","Resource R3","Resource R4","Resource R5"};
		RandomInteger randomRes = new RandomInteger(newResourceName.length, 0, newResourceName.length-1);
		
		String resourceID = newResourceName[randomRes.getRandomNumber()[0]];
		
		return resourceID;
	}
	
	private String getRandomActivity() {
		String[] newActivityName = {
				"Activity AA",
				"Activity BB",
				"Activity CC",
				"Activity DD",
				"Activity EE",
				"Activity FF",
				"Activity GG",
				"Activity HH",
				"Activity II",
				"Activity JJ",
				"Activity KK"};
		
		RandomInteger randomAct = new RandomInteger(newActivityName.length, 0, newActivityName.length-1);
		String activityID = newActivityName[randomAct.getRandomNumber()[0]];
		
		return activityID;
	}
	
	/*
	 * GETTERS AND SETTERS
	 * */
	
	public XLog getLog() {
		return log;
	}

	public void setLog(XLog log) {
		this.log = log;
	}

	public MutatorParameters getParameters() {
		return parameters;
	}

	public void setParameters(MutatorParameters parameters) {
		this.parameters = parameters;
	}

	public XLog getMutatedLog() {
		return mutatedLog;
	}

	public void setMutatedLog(XLog mutatedLog) {
		this.mutatedLog = mutatedLog;
	}
}
