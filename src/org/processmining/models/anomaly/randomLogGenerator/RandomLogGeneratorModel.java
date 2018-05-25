package org.processmining.models.anomaly.randomLogGenerator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.data.xes.XESAttributeDefinition;
import org.processmining.plugins.anomaly.randomGenerator.RandomLogGeneratorParameters;

import analysis.basics.random.RandomFloat;

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

public class RandomLogGeneratorModel {
	
	private XLog log;
	private RandomLogGeneratorParameters parameters;
	
	private XLog mutatedLog;
	
	public RandomLogGeneratorModel(XLog log, RandomLogGeneratorParameters parameters) {
		
		/*
		 * Parameters
		 * */
		XESAttributeDefinition def = new XESAttributeDefinition();
		int caseSize = log.size();
		int numberOfDays = 30;
		
		/*
		 * Random Log Generation
		 * */
		if(parameters.isTimeMutatorOn()) {
			// for each case (trace) in this log
			for(int i = 0; i < caseSize; i++) {
				// this case
				XTrace thisTrace = log.get(i);
				int traceSize = thisTrace.size();
				System.out.println(thisTrace.getAttributes().get(def.getCASE_ID()));
				
				// get a random number
				RandomFloat random = new RandomFloat(traceSize);
				
				// for each event in this trace
				for(int j = 0; j < traceSize; j++) {
					System.out.println(thisTrace.get(j).getAttributes().get(def.getCASE_ID()));
					
					// if the random number is larger than the mutation percentage in a case, then mutate!					
					if(random.getRandomNumberElement(j) >= parameters.getMutationPercentageInCase()) {
						// if this event is 'start'
						if(thisTrace.get(j).getAttributes().get("EventType").toString().equals("start")) {
							// then minus the time
							try {
								DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
								Date tempDate = format.parse(thisTrace.get(j).getAttributes().get(def.getTIMESTAMP()).toString());
								Date origDate = tempDate;
								
								Calendar cal = Calendar.getInstance();
								cal.setTime(tempDate);
								cal.add(Calendar.DATE, 5);
								
								tempDate = cal.getTime();
								
								XFactory factory = XFactoryRegistry.instance().currentDefault();
								XAttributeMap tempMap = thisTrace.get(j).getAttributes();
								tempMap.put(def.getTIMESTAMP(), factory.createAttributeTimestamp(def.getTIMESTAMP(), tempDate, null));
								
								System.out.println("Original: " + origDate);
								System.out.println("In XES: " + thisTrace.get(j).getAttributes().get(def.getTIMESTAMP()).toString());
								System.out.println("In Memory: " + tempDate);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}// else if this event is 'complete'
						else if(thisTrace.get(j).getAttributes().get("EventType").toString().equals("complete")) {
							// then plus the time
							
						}
					}
				}
			}
		}
		
		
		if(parameters.isNewResourceMutatorOn()) {
			
		}
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

	public RandomLogGeneratorParameters getParameters() {
		return parameters;
	}

	public void setParameters(RandomLogGeneratorParameters parameters) {
		this.parameters = parameters;
	}

	public XLog getMutatedLog() {
		return mutatedLog;
	}

	public void setMutatedLog(XLog mutatedLog) {
		this.mutatedLog = mutatedLog;
	}
}
