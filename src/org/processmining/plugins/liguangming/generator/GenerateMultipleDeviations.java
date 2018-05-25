package org.processmining.plugins.liguangming.generator;

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
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.in.XesXmlParser;
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
import org.deckfour.xes.out.XesXmlSerializer;
//import org.processmining.plugins.simplelogoperations.XLogFunctions;
import org.processmining.plugins.CheckBoxDemo;
import org.processmining.plugins.liguangming.createdeviation.plugins.XLogFunctions;

public class GenerateMultipleDeviations {

	public XLog generateMultipleDeviation(XLog Log,double deviationProbability) 
	{
		XLog  oldLog = (XLog) Log.clone();//keep the input log unchanged
		XLog  changeableLog = (XLog) Log.clone();//keep the input log unchanged
		XLog  traceLog = (XLog) Log.clone();//keep the input log unchanged
		
		//Definition of myself
//		double deviationProbability = 0;
//		int deviationCountInTrace = 3;
		
		int deviationCountInTrace = 1;// indicate the number of the deviation units
//		deviationCountInTrace =(int)(Math.random()*6)+1;
//		int deviationType=(int)(Math.random()*3);
//		int deviationType = 1;// indicate the type of the deviation units,0:insert ,1:reduce, 2:replace

		
		//create the new log and copy the attributes of the old log to the new one
		XAttributeMap logattlist = XLogFunctions.copyAttMap(oldLog.getAttributes());
		XLog newLog = new XLogImpl(logattlist);
		
				
		  
		XLogInfo info = XLogInfoFactory.createLogInfo(oldLog);
		String []tempEventName = new String[info.getNameClasses().size()];
		
		 

		
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
				
				for(int deviationType=0;deviationType<3;deviationType++)
				{
				//generate the place of the insertion
					int randomPosition=(int)(Math.random()*oldTrace.size());//雅㎫뵟[0,oldTrace.size()-1]�쉪�빐�빊�쉹�쑛�빊
				//	int randomPosition=(int)(Math.random()*(oldTrace.size()-2));//雅㎫뵟[0,oldTrace.size()-3]�쉪�빐�빊�쉹�쑛�빊		
				//	randomPosition = randomPosition + 1;//雅㎫뵟[1,oldTrace.size()-2]�쉪�빐�빊�쉹�쑛�빊
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

	private static XEvent makeEvent(String name, Date time, String lifecycle) {
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
	 
	   public static void main(String[] args) throws Exception 
	   {
		      
			int ANDBranches =3; //indicate the maximum number of AND branches (must be > 1)
	        int XORBranches = 3; //indicate the maximum number of XOR branches (must be > 1)
	        int loopPercent = 50; //indicate the loop probability (must be >= 0 and <= 100)
	        int singleActivityPercent = 50; //indicate the probability of single activity (must be >= 0 and <= 100)
	        int sequencePercent = 50; //indicate the probability of sequence activity (must be >= 0 and <= 100)
	        int ANDPercent = 50; //indicate the probability of AND split-join (must be >= 0 and <= 100)
	        int XORPercent = 50; //indicate the probability of XOR split-join (must be >= 0 and <= 100)
	        int deep = 3; //indicate the maximum network deep
	        
	        //parameters for generating deviations
	        double deviationPercentage = 0.3;
	        int deviationType = 0;
	        
	        //parameters for detecting deviations
	        int profile = 2;
	        int loopTimes = 1;
	        int logNumPerModel = 1;
	        int modelNumPerParameter = 1;
	        
	        
	        
			//generate an artificial log
	        for(deep = 1; deep < 3; deep++)
	        {
	        	for(ANDBranches =5; ANDBranches <6; ANDBranches++)
	        	{
	 //       		for(ANDBranches =1; ANDBranches <6; ANDBranches++)
	        		{
	        			for(loopPercent = 0; loopPercent <=100; loopPercent=loopPercent+100)
	        			{
	        				for(sequencePercent = 0; sequencePercent <=100; sequencePercent=sequencePercent+100)
	            			{
	        					for(ANDPercent = 0; ANDPercent <=100; ANDPercent=ANDPercent+100)
	                			{
	        						for(XORPercent = 0; XORPercent <=100; XORPercent=XORPercent+100)
	                    			{
	        							for(int modelNum=1; modelNum<=modelNumPerParameter;modelNum++)
	        							{
											for(int logNum=1;logNum<=logNumPerModel;logNum++)
											{
		
												//input the artificial log
												XesXmlParser parser = new XesXmlParser();
												java.io.File f = new java.io.File("d:\\logs\\normal\\D"+deep +"_L"+loopPercent+"_S"+sequencePercent+"_A"+ANDPercent+"_X"+XORPercent+"_mN"+modelNum +"_lN"+logNum+".xes");
		            							if(!f.exists()) continue;
		            							
												XLog readLog = parser.parse(new File("d:\\logs\\normal\\D"+deep +"_L"+loopPercent+"_S"+sequencePercent+"_A"+ANDPercent+"_X"+XORPercent+"_mN"+modelNum +"_lN"+logNum+".xes")).get(0);																										
										
												//generate deviations
												for(deviationPercentage = 0.1; deviationPercentage<0.4; deviationPercentage=deviationPercentage+0.1)
												{
//													for(deviationType =0;deviationType<3;deviationType++)
													{
														XLog inputLog = (XLog) readLog.clone();
														XAttributeMap logattlist = XLogFunctions.copyAttMap(inputLog.getAttributes());
														//generate the log with deviations
														XLog deviationsLog = new XLogImpl(logattlist);//the filterLog + deviations
														GenerateMultipleDeviations  generator = new  GenerateMultipleDeviations();
														deviationsLog = generator.generateMultipleDeviation(inputLog,deviationPercentage);
														
														//generate the log with deviations plus start and end
														XLog deviationsPlusLog = new XLogImpl(logattlist);//the filterLog + deviations+start+end
														
														for (int i = 0; i < deviationsLog.size(); i++) 
														{
															XTrace oldTrace = deviationsLog.get(i);
														
															XAttributeMap newAttributeMap = oldTrace.getAttributes();
															XTrace newTrace = new XTraceImpl(XLogFunctions.copyAttMap(newAttributeMap));
															
															if(oldTrace.size() == 0)
															{
																JOptionPane.showMessageDialog(null, "Trace size equals to 0 in GeneratorPlugin", "Trace Size Exception", JOptionPane.ERROR_MESSAGE);
															}
															Date time = XLogFunctions.getTime(oldTrace.get(0));
															String lifecycle = oldTrace.get(0).getAttributes().get("lifecycle:transition").toString();
															newTrace.add(makeEvent("start", time, lifecycle));
															for (int j = 0; j < oldTrace.size(); j++)
															{
																XEvent oldEvent = oldTrace.get(j);
																XEvent newEvent = new XEventImpl(XLogFunctions.copyAttMap(oldEvent.getAttributes()));
																newTrace.add(newEvent);
															}
															
															time = XLogFunctions.getTime(oldTrace.get(oldTrace.size()-1));
															lifecycle = oldTrace.get(oldTrace.size()-1).getAttributes().get("lifecycle:transition").toString();
															newTrace.add(makeEvent("end", time, lifecycle));
															
															deviationsPlusLog.add(newTrace);
														}
														
														//save the log with deviations
														File fileExceptional = new File("d:\\logs\\exceptional-3units");//check if the folder exist; if not,create it
														if (!fileExceptional.exists()) fileExceptional.mkdir();
														
														XesXmlSerializer serializerDeviation = new XesXmlSerializer();
														OutputStream outputDeviation = new FileOutputStream("d:\\logs\\exceptional-3units\\P"+deviationPercentage+"_T3units"+"_D"+deep +"_L"+loopPercent+"_S"+sequencePercent+"_A"+ANDPercent+"_X"+XORPercent+"_mN"+modelNum +"_lN"+logNum+".xes");
														serializerDeviation.serialize(deviationsPlusLog, outputDeviation);
													}
												}
											}
	        							}
	                    			}
	                			}
	            			}
	        			}
	        		}
	        	}

	    }
	  }
}
