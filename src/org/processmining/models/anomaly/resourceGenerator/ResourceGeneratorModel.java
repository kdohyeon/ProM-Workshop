package org.processmining.models.anomaly.resourceGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.impl.XEventImpl;
import org.deckfour.xes.model.impl.XLogImpl;
import org.deckfour.xes.model.impl.XTraceImpl;
import org.processmining.plugins.anomaly.mutator.MutatorParameters;
import org.processmining.plugins.anomaly.resourceGenerator.ResourceGeneratorParameters;

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

public class ResourceGeneratorModel {
	
	private XLog log;
	private MutatorParameters parameters;
	
	private XLog mutatedLog;
	
	private String outputPath = "C:\\Users\\dhkim-pc\\Desktop\\Evaluation_PLG\\";
	
	private Map<String, String> activityResourceMap;
	
	private float threshold;
	private float trainingOrTestPercentage;
	
	private int maxAnomalyResource;
	
	public ResourceGeneratorModel(XLog log, ResourceGeneratorParameters parameters) throws IOException, ParseException {
		System.out.println("Mutator Model");
		/*
		 * Parameters
		 * */
		float dp = parameters.getDeviationPercentageCase();
		String type = parameters.getDeviationType();
		System.out.println("Percentage: " + dp);
		System.out.println("Type: " + type);
		int caseSize = log.size();
		threshold = (float) 0.8;
		maxAnomalyResource = 0;
		trainingOrTestPercentage = (float) 0.5;
		defineActivityResource();
		
		outputPath = outputPath + log.getAttributes().get("concept:name") + "_resource" + ".csv";
		File file = new File(outputPath);
		FileWriter writer = new FileWriter(file);
		
		writer.append("Case ID" + ","); // case id
		writer.append("Activity ID" + ","); // activity id
		writer.append("Resource ID" + ","); // resource id
		writer.append("Timestamp" + ","); // date and time
		writer.append("EventID" + ","); // event id
		writer.append("EventType" + ","); // transition
		writer.append("Flag" + ","); // flag
		writer.append("Data" + ","); //data (training? or test?)
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
		
		// for each case (trace) in this log
		for(int i = 0; i < caseSize; i++) {
//		for(int i = 0; i < 2; i++) {		
			// this case
			XTrace thisTrace = log.get(i);
			XTrace newTrace;
			String traceID = thisTrace.getAttributes().get("concept:name").toString();
			
			System.out.println("Case ID: " + traceID);
			
			XAttributeMap newAttributeMap = thisTrace.getAttributes();
			XLogFunctions.putLiteral(newAttributeMap, "Flag", "Normal");
			newTrace = new XTraceImpl(XLogFunctions.copyAttMap(newAttributeMap));

			// random float object
			RandomFloat randomFloat = null;
			
			// set datatype
			String datatype = "";
			randomFloat = new RandomFloat(1, 0, 1);
			if(randomFloat.getRandomNumberElement(0) >= trainingOrTestPercentage) {
				datatype = "training";
			}else {
				datatype = "test";
			}
			XLogFunctions.setData(newTrace, datatype);
			
			// get random float number for resource
			boolean canExit = false;
			
			int eventNum = thisTrace.size();
			maxAnomalyResource = (int) (eventNum - eventNum*threshold);
			
			while(!canExit) {
				randomFloat = new RandomFloat(thisTrace.size(), 0, 1);
				
				int cnt = 0;
				
				for(int j = 0; j < randomFloat.getRandomNumber().length; j++) {
					if(randomFloat.getRandomNumberElement(j) >= threshold) {
						cnt++;
					}
				}

				if(cnt > maxAnomalyResource) {
					canExit = false;
					//System.out.println("Cannot Exit " + cnt);
				}else {
					canExit = true;	
					//System.out.println("Case ID: " + traceID + ", " + cnt + ", " + eventNum + ", " + maxAnomalyResource);
				}
			}
			
			
			
			int cnt = 1;

			for(int j = 0; j < thisTrace.size(); j++) {
				XEvent oldEvent = thisTrace.get(j);
				XEvent newEvent = new XEventImpl(XLogFunctions.copyAttMap(oldEvent.getAttributes()));
				
				String thisActivity = oldEvent.getAttributes().get("concept:name").toString();
				String thisResource = "";
				String thisEventID = traceID + "_" + cnt;
				String thisEventType = oldEvent.getAttributes().get("lifecycle:transition").toString();
				
				System.out.println("Activity: " + thisActivity + ", EventType: " + thisEventType);
				
				// get resource
				if(newTrace.size() == 0) {
					thisResource = getRandomResource(thisActivity, randomFloat.getRandomNumber(), j);
				}else {
					boolean resourceExists = false;
					
					for(int k = 0; k < newTrace.size(); k++) {
						// if an activity from old event is already in the newTrace, then get the resource name
						if(newTrace.get(k).getAttributes().get("concept:name").toString().equals(thisActivity)) {
							thisResource = newTrace.get(k).getAttributes().get("org:resource").toString();
							resourceExists = true;
						}
						
						// break!
						if(resourceExists) {
							break;
						}
					}

					if(!resourceExists) {
						thisResource = getRandomResource(thisActivity, randomFloat.getRandomNumber(), j);
					}					
				}
				
				XLogFunctions.setResource(newEvent, thisResource);
				
				// get event id
				if(newTrace.size() == 0) {
					thisEventID = traceID + "_" + cnt;
				}else {
					boolean eventExists = false;
					
					for(int k = newTrace.size()-1; k >= 0; k--) {
						if(newTrace.get(k).getAttributes().get("concept:name").toString().equals(thisActivity)
								&& newTrace.get(k).getAttributes().get("lifecycle:transition").toString().equals("start")
								&& thisEventType.equals("complete")) {
							thisEventID = newTrace.get(k).getAttributes().get("EventID").toString();
							eventExists = true;
							cnt++;
						}
						
						if(newTrace.get(k).getAttributes().get("concept:name").toString().equals(thisActivity)
								&& newTrace.get(k).getAttributes().get("lifecycle:transition").toString().equals("complete")
								&& thisEventType.equals("start")) {
							
							thisEventID = traceID + "_" + cnt;
							eventExists = true;
							cnt++;
						}
						
						// break!
						if(eventExists) {
							break;
						}
					}
					
					if(!eventExists) {
						thisEventID = traceID + "_" + cnt;
						cnt++;
					}
				}
				
				XLogFunctions.setEventID(newEvent, thisEventID);
				
				// new event
				newTrace.add(newEvent);
			}
			
			
			// add the new trace to the new log
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			for(int j = 0; j < newTrace.size(); j++) {
				writer.append(newTrace.getAttributes().get("concept:name") + ","); // case id
				writer.append(newTrace.get(j).getAttributes().get("concept:name").toString() + ","); // activity id
				writer.append(newTrace.get(j).getAttributes().get("org:resource").toString() + ","); // resource id
				Date date = sdf.parse(newTrace.get(j).getAttributes().get("time:timestamp").toString().replace("T", " "));
				writer.append(sdf.format(date) + ","); // timestamp
				writer.append(newTrace.get(j).getAttributes().get("EventID").toString() + ","); // eventID
				writer.append(newTrace.get(j).getAttributes().get("lifecycle:transition").toString() + ","); // transition
				writer.append(newTrace.getAttributes().get("Flag").toString() + ","); // flag
				writer.append(newTrace.getAttributes().get("Data").toString() + ","); // data
				writer.append("\n");
			}
			
			mutatedLog.add(newTrace);
		}
		writer.close();
	}
	
	private String getRandomResource(String activityName, float[] random, int index) {
		
		String resourceID = "";
		
		float probability = random[index];
		//System.out.println(probability);
		// if prob. is greater than 0.5
		if(probability <= threshold) {
			resourceID = activityResourceMap.get(activityName);	
		}else { // else randomly allocate resource other than the main
			Set<String> resourceSet = new HashSet<String>();
			Iterator<String> iter = activityResourceMap.keySet().iterator();
			while(iter.hasNext()) {
				String key = iter.next();
				resourceSet.add(activityResourceMap.get(key));
			}
			resourceSet.remove(activityResourceMap.get(activityName));
			
			// randomly get
			ArrayList<String> temp = new ArrayList<String>();
			temp.addAll(resourceSet);
			
			RandomInteger randomInt = new RandomInteger(1,0,temp.size()-1);
			//resourceID = temp.get(randomInt.getRandomNumber()[0]) + "_" + probability + "_" + randomInt.getRandomNumber()[0];
			resourceID = temp.get(randomInt.getRandomNumber()[0]);
		}
		
		//resourceID = resourceID + "_" + probability;
		
		
		return resourceID;
	}
	
	private void defineActivityResource() {
		activityResourceMap = new HashMap<String, String>();
		activityResourceMap.put("Activity A", "Resource 1"); // 1
		activityResourceMap.put("Activity B", "Resource 1"); // 2
		activityResourceMap.put("Activity C", "Resource 2"); // 3
		activityResourceMap.put("Activity D", "Resource 2"); // 4
		activityResourceMap.put("Activity E", "Resource 3"); // 5
		activityResourceMap.put("Activity F", "Resource 3"); // 6 
		activityResourceMap.put("Activity G", "Resource 4"); // 7
		activityResourceMap.put("Activity H", "Resource 4"); // 8
		activityResourceMap.put("Activity I", "Resource 5"); // 9
		activityResourceMap.put("Activity J", "Resource 5"); // 10
		activityResourceMap.put("Activity K", "Resource 6"); // 11
		activityResourceMap.put("Activity L", "Resource 6"); // 12
		activityResourceMap.put("Activity M", "Resource 7"); // 13
		activityResourceMap.put("Activity N", "Resource 7"); // 14
		activityResourceMap.put("Activity O", "Resource 8"); // 15
		activityResourceMap.put("Activity P", "Resource 8"); // 16
		activityResourceMap.put("Activity Q", "Resource 9"); // 17
		activityResourceMap.put("Activity R", "Resource 9"); // 18
		activityResourceMap.put("Activity S", "Resource 10"); // 19
		activityResourceMap.put("Activity T", "Resource 10"); // 20
		activityResourceMap.put("Activity U", "Resource 11"); // 21
		activityResourceMap.put("Activity V", "Resource 11"); // 22
		activityResourceMap.put("Activity W", "Resource 12"); // 23
		activityResourceMap.put("Activity X", "Resource 12"); // 24
		activityResourceMap.put("Activity Y", "Resource 13"); // 25
		activityResourceMap.put("Activity Z", "Resource 13"); // 26
		
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
