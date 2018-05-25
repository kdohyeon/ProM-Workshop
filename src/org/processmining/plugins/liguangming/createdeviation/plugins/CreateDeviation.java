package org.processmining.plugins.liguangming.createdeviation.plugins;

/*
 * input: log
 * output: log
 * 
 * Add a deviation event for a trace of the input log based on a configurable probability
 */
//import java.awt.List;
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.deckfour.uitopia.api.event.TaskListener.InteractionResult;
import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.impl.XAttributeMapImpl;
import org.deckfour.xes.model.impl.XEventImpl;
import org.deckfour.xes.model.impl.XLogImpl;
import org.deckfour.xes.model.impl.XTraceImpl;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;
//import org.processmining.plugins.simplelogoperations.XLogFunctions;
import org.processmining.plugins.CheckBoxDemo;
import org.processmining.plugins.liguangming.createdeviation.dialogs.CreateDeviationFrame;

public class CreateDeviation {
	@Plugin(name = " CreateDeviation", 
			parameterLabels = { "Log" }, 
			returnLabels = { "Altered log" }, 
			returnTypes = { XLog.class }, 
			userAccessible = true, 
			help = "The plugin will change the given percentage of the cases into deviation. The deviation will be attached a case attribution"
					+ " named Flag whose value is Exceptional while the normal case will attached with Normal")
	@UITopiaVariant(affiliation = "TU/e", 
			        author = "G.Li", 
			        email = "g.li.3@tue.nl")
	public XLog createDeviation(UIPluginContext context, XLog Log) 
//	public XLog createDeviation(XLog oldLog,double deviationProbability,int deviationCountInTrace) 
	{
		XLog  oldLog = (XLog) Log.clone();//keep the input log unchanged
		XLog  changeableLog = (XLog) Log.clone();//keep the input log unchanged
		XLog  traceLog = (XLog) Log.clone();//keep the input log unchanged
		
		//Definition of myself
		double deviationProbability = 0;
		int deviationCountInTrace = 3;
		

		//create the new log and copy the attributes of the old log to the new one
		XAttributeMap logattlist = XLogFunctions.copyAttMap(oldLog.getAttributes());
		XLog newLog = new XLogImpl(logattlist);
		
		CreateDeviationFrame parameters = new CreateDeviationFrame();
		InteractionResult interActionResult = context.showConfiguration("DeviationDetecting", parameters);
		
		if (interActionResult.equals(InteractionResult.CANCEL)) {
			context.getFutureResult(0).cancel(true);
			JOptionPane.showMessageDialog(null, "cancle", "world", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		if (interActionResult.equals(InteractionResult.CONTINUE)) {
//			context.getFutureResult(0).;
			
		
			 double readDeviationPercentage=parameters.getDeviationPercentage();
		       JOptionPane.showMessageDialog(null,"DeviationPercentage:"+readDeviationPercentage);
		       deviationProbability=readDeviationPercentage;
		}
		
		
		  
		XLogInfo info = XLogInfoFactory.createLogInfo(oldLog);
		String []tempEventName = new String[info.getNameClasses().size()];
		
/*		XConceptExtension nceptExtension = XConceptExtension.instance();
		 JOptionPane.showMessageDialog(null,"getEventClasses:"+info.getEventClasses().size());
		 JOptionPane.showMessageDialog(null,"getNameClasses:"+info.getNameClasses().size());
		 
		 nceptExtension.extractName(Log.get(0).get(1));
		 JOptionPane.showMessageDialog(null,"getName:"+nceptExtension.extractName(Log.get(0).get(1)));*/
		 

		
		int n=0;
		
		try {  
		       
            File writename = new File("D:\\eventclass.txt"); // 
            writename.createNewFile(); //   
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));  



		for (XEventClass eventClass : info.getNameClasses().getClasses())
		{
			tempEventName[n]=eventClass.toString();
//			changeMap.put(eventClass.toString(), changeEventName[n]);
			n++;
			out.write("add:"+eventClass.toString());
			out.write("\r\n");
		}
		
        out.flush(); //  
        out.close(); // 
		}
		catch (Exception e) 
		{  
        e.printStackTrace();  
         }	

		
		//randomly select deviationProbability*traceNum traces, and keep their IDs
		List randomIDList = new ArrayList();
		for (int i = 0; i < deviationProbability*Log.size(); i++) 
		{
			int logSize=changeableLog.size();
			int randomPosition=(int)(Math.random()*logSize);//雅㎫뵟[0,logSize-1]�쉪�빐�빊�쉹�쑛�빊
			XConceptExtension conceptExtension = XConceptExtension.instance();
			String traceID = conceptExtension.extractName(changeableLog.get(randomPosition));
			randomIDList.add(traceID);
			changeableLog.remove(randomPosition);
		
		}
		
		int nAdd=0,nReduce=0,nReplace=0;
		for (int i = 0; i < Log.size(); i++) //i represents the number of the already created deviations
		{
//			double traceNum=oldLog.size();
			
//			XTrace oldTrace = oldLog.get(i);
			
			XConceptExtension conceptExtension = XConceptExtension.instance();
			String traceID = conceptExtension.extractName(traceLog.get(i));
			
			XTrace oldTrace = traceLog.get(i);
			XTrace newTrace;
			
			//create the new trace and copy the attributes of the old trace to the new one
			if(!randomIDList.contains(traceID))
			{
				XAttributeMap newAttributeMap = oldTrace.getAttributes();
				XLogFunctions.putLiteral(newAttributeMap,"Flag", "Normal");
				newTrace = new XTraceImpl(XLogFunctions.copyAttMap(newAttributeMap));
				for (int j = 0; j < oldTrace.size(); j++)
				{
					XEvent oldEvent = oldTrace.get(j);
					XEvent newEvent = new XEventImpl(XLogFunctions.copyAttMap(oldEvent.getAttributes()));
					newTrace.add(newEvent);	
				}
			}
			else
			{
				XAttributeMap newAttributeMap = oldTrace.getAttributes();
				XLogFunctions.putLiteral(newAttributeMap,"Flag", "Exceptional");
				newTrace = new XTraceImpl(XLogFunctions.copyAttMap(newAttributeMap));
	
				//generate the probability of inserting a deviation event,random()鴉싪눎�뒯雅㎫뵟訝�訝�(positive sign, greater than or equal to 0.0 and less than 1.0)�쉪�룎暎얍벧�쉹�쑛�빊
				double randomNumber=Math.random();
				deviationCountInTrace =(int)(Math.random()*6)+1;
				for(int deviationNum=0;deviationNum<deviationCountInTrace;deviationNum++)
				{
				//generate the place of the insertion
					int randomPosition=(int)(Math.random()*oldTrace.size());//雅㎫뵟[0,oldTrace.size()-1]�쉪�빐�빊�쉹�쑛�빊
						
					newTrace.clear();
					//copy the part before the insertion
					for (int j = 0; j < randomPosition; j++)
					{
						XEvent oldEvent = oldTrace.get(j);
						XEvent newEvent = new XEventImpl(XLogFunctions.copyAttMap(oldEvent.getAttributes()));
						newTrace.add(newEvent);
						
					}
	
	
				//add a deviation event into the trace
	
//				String s=oldTrace.getAttributes().get("concept:name" ).toString();	
//				if( Integer.parseInt(s)<=deviationProbability*oldLog.size())

					//randomly select the deviation type
					int deviationType=(int)(Math.random()*3);
					
					//insert an event
					if(deviationType==0)
					{
						//insert a event
						Date time;
						String lifecycle;
						int randomEventName=(int)(Math.random()*info.getNameClasses().size());//雅㎫뵟[0,info.getEventClasses().size()-1]�쉪�빐�빊�쉹�쑛�빊
						time = XLogFunctions.getTime(oldTrace.get(randomPosition));
						lifecycle = oldTrace.get(randomPosition).getAttributes().get("lifecycle:transition").toString();
						newTrace.add(makeEvent(tempEventName[randomEventName], time, lifecycle));
	
						//copy the part behind the insertion
						for (int j = randomPosition; j < oldTrace.size(); j++)
						{
							XEvent oldEvent = oldTrace.get(j);
							XEvent newEvent = new XEventImpl(XLogFunctions.copyAttMap(oldEvent.getAttributes()));
							newTrace.add(newEvent);
						}
						nAdd++;
						
					}
					//reduce an event
					else if(deviationType==1)
					{
						//copy the part behind the insertion
						if(oldTrace.size()==1) randomPosition=-1;
						for (int j = randomPosition+1; j < oldTrace.size(); j++)
						{
							XEvent oldEvent = oldTrace.get(j);
							XEvent newEvent = new XEventImpl(XLogFunctions.copyAttMap(oldEvent.getAttributes()));
							newTrace.add(newEvent);
						}
						nReduce++;
					}
					
					//replace an event
					else if(deviationType==2)
					{
	
						Date time;
						boolean equalFlag=false;
						String eventName;
						String lifecycle;
						int randomEventName;
						do
						{							
							randomEventName=(int)(Math.random()*info.getNameClasses().size());//return a number in [0,info.getEventClasses().size()-1]
							eventName = conceptExtension.extractName(oldTrace.get(randomPosition));
							
							if(tempEventName[randomEventName].equals(eventName))
							{
								equalFlag=true;
							}
							else
							{
								equalFlag=false;
								time = XLogFunctions.getTime(oldTrace.get(randomPosition));
								lifecycle = oldTrace.get(randomPosition).getAttributes().get("lifecycle:transition").toString();
								
								newTrace.add(makeEvent(tempEventName[randomEventName], time,lifecycle));
							}
						}while(equalFlag);				
						
						 
						//copy the part behind the insertion
						for (int j = randomPosition+1; j < oldTrace.size(); j++)
						{
							XEvent oldEvent = oldTrace.get(j);
							XEvent newEvent = new XEventImpl(XLogFunctions.copyAttMap(oldEvent.getAttributes()));
							newTrace.add(newEvent);
						}	
						nReplace++;
					}
					
					//change an numerical attribute
					else if(deviationType==4)
					{
						newTrace.clear();
			
						//copy the part behind the insertion
						for (int j = 0; j < oldTrace.size(); j++)
						{
							XEvent oldEvent = oldTrace.get(j);
							
							XEvent newEvent =  changeNumericalAttribute(oldEvent);
							newTrace.add(newEvent);
						}
//							nAdd++;
					}
					
					//change model
/*					else if(deviationType==3)
					{
						newTrace.clear();
						
						
						//copy the part behind the insertion
						for (int j = 0; j < oldTrace.size(); j++)
						{
							XEvent oldEvent = oldTrace.get(j);
							
							String oldName= oldEvent.getAttributes().get("concept:name").toString();
							
							Date time;
							String newName=changeMap.get(oldName);
							time = XLogFunctions.getTime(oldTrace.get(0));
							newTrace.add(makeEvent(newName, time));
						}
//							nAdd++;
					}*/
				
					//prepare for the next deviation
					oldTrace.clear();
					for (int j = 0; j < newTrace.size(); j++)
					{
						XEvent oldEvent = newTrace.get(j);
						XEvent newEvent = new XEventImpl(XLogFunctions.copyAttMap(oldEvent.getAttributes()));
						oldTrace.add(newEvent);
					}
				
				}
			}
			/////////////////////////////ranking start	
			try {  
			       
	            File writename = new File("D:\\deviationnumber.txt"); // 
	            writename.createNewFile(); //   
	            BufferedWriter out = new BufferedWriter(new FileWriter(writename));  

				out.write("add:"+nAdd+"_reduce:"+nReduce+"_replace:"+nReplace);
	    		out.write("\r\n");
	    		

	            out.flush(); //  
	            out.close(); // 

	        } 
			catch (Exception e) 
			{  
	        e.printStackTrace();  
	         }		
	/////////////////////////////ranking end
			
			//add the new trace to the new log
			newLog.add(newTrace);
//			context.getProgress().inc();
		}
		return newLog;
	}

	private XEvent makeEvent(String name, Date time, String lifecycle) {
		XAttributeMap attMap = new XAttributeMapImpl();
		XLogFunctions.putLiteral(attMap, "concept:name", name);
//		XLogFunctions.putLiteral(attMap, "lifecycle:transition", "COMPLETE");
		XLogFunctions.putLiteral(attMap, "lifecycle:transition", lifecycle);
		XLogFunctions.putLiteral(attMap, "org:resource", "artificial");
		XLogFunctions.putTimestamp(attMap, "time:timestamp", time);
		XEvent newEvent = new XEventImpl(attMap);
		return newEvent;
	}
	//change the numerical attribute
	private XEvent changeNumericalAttribute(XEvent oldEvent) {
		XAttributeMap attMap = new XAttributeMapImpl();
		XLogFunctions.putLiteral(attMap, "concept:name", oldEvent.getAttributes().get("concept:name").toString());
//		XLogFunctions.putLiteral(attMap, "lifecycle:transition", "complete");
		XLogFunctions.putLiteral(attMap, "org:resource", oldEvent.getAttributes().get("org:resource").toString());
		XLogFunctions.putTimestamp(attMap, "time:timestamp", XLogFunctions.getTime(oldEvent));
		Long numericalAttribute=Long.parseLong(oldEvent.getAttributes().get("Amount").toString());
		numericalAttribute = 1000 - numericalAttribute;
		XLogFunctions.putLiteral(attMap, "Amount",numericalAttribute.toString());

		XEvent newEvent = new XEventImpl(attMap);
		return newEvent;
	}
	
	 public static void createAndShowGUI()
	    {
	       JFrame frame = new JFrame("Hello world");
	       frame.setLayout(null);
	       JComponent panel = new CheckBoxDemo(6);
	       panel.setBackground(Color.red);
	       panel.setBounds(0, 0, 600, 300);
	       frame.add(panel);
	       frame.pack();
	       frame.setSize(600, 400);
	       frame.setVisible(true);
	      
	    }
	 
}
