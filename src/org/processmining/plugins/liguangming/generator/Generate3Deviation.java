package org.processmining.plugins.liguangming.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.swing.JOptionPane;

import org.deckfour.xes.in.XesXmlParser;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.impl.XAttributeMapImpl;
import org.deckfour.xes.model.impl.XEventImpl;
import org.deckfour.xes.model.impl.XLogImpl;
import org.deckfour.xes.model.impl.XTraceImpl;
import org.deckfour.xes.out.XesXmlSerializer;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.lib.mxml.LogException;
import org.processmining.plugins.liguangming.createdeviation.plugins.XLogFunctions;


public class Generate3Deviation{
	@SuppressWarnings("null")
	@Plugin(name = "Generate3Deviation", 
			parameterLabels = { }, 
			returnLabels = { "Petri net" }, 
			returnTypes = { XLog.class }, 
			userAccessible = true, 
			help = "Generate a petri net")
	@UITopiaVariant(affiliation = "TU/e", 
			        author = "G.Li", 
			        email = "g.li.3@tue.nl")
	public XLog  createDeviation(UIPluginContext context) throws LogException,Exception
//	public XLog createDeviation(XLog oldLog,double deviationProbability,int deviationCountInTrace) 
	{
		//parameters for generating logs
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
        XLog filterLog = null;
        int logNumPerModel = 10;
        int modelNumPerParameter = 3;
        int times =0;
        
		//generate an artificial log
        for(deep = 1; deep < 4; deep++)
        {
//        	for(ANDBranches =3; ANDBranches <4; ANDBranches++)
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
											
											times ++;
        									if(times==1)
        									{
        									
        										
        										XORPercent =100;
        										ANDPercent =0;
        										sequencePercent=100;
        										loopPercent=100;       										
        										deep =3; 
        										modelNum=3;
        										logNum=4;
        									}
        									
											String logName = "e:\\Experiment\\logs\\normal\\D"+deep +"_L"+loopPercent+"_S"+sequencePercent+"_A"+ANDPercent+"_X"+XORPercent+"_mN"+modelNum +"_lN"+logNum+".xes";
	
											
											//input the artificial log
		      								XesXmlParser parser = new XesXmlParser();
		      								filterLog = parser.parse(new File(logName)).get(0);
											
											//filter the artificial log
											XAttributeMap logattlist = XLogFunctions.copyAttMap(filterLog.getAttributes());
										
											
									
											
									
											//generate deviations
											for(deviationPercentage = 0.1; deviationPercentage<0.4; deviationPercentage=deviationPercentage+0.1)
											{
												for(deviationType =0;deviationType<3;deviationType++)
												{
													XLog normalLog = (XLog)filterLog.clone();
													//generate the log with deviations
													XLog deviationsLog = new XLogImpl(logattlist);//the filterLog + deviations
													GenerateDeviations generator = new GenerateDeviations();
													deviationsLog = generator.generateDeviation(normalLog, deviationPercentage, deviationType);
													
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
													File fileExceptional = new File("e:\\Experiment\\logs\\exceptional");//check if the folder exist; if not,create it
													if (!fileExceptional.exists()) fileExceptional.mkdir();
													
													XesXmlSerializer serializerDeviation = new XesXmlSerializer();
													OutputStream outputDeviation = new FileOutputStream("e:\\Experiment\\logs\\exceptional\\P"+deviationPercentage+"_T"+deviationType+"_D"+deep +"_L"+loopPercent+"_S"+sequencePercent+"_A"+ANDPercent+"_X"+XORPercent+"_mN"+modelNum +"_lN"+logNum+".xes");
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
        
        
		//detect deviations
/*		DeviationDetectingPlugin detectorCyclicSampling = new DeviationDetectingPlugin();
		detectorCyclicSampling.detectDeviation(deviationsLog, profile, deviationPercentage, loopTimes); // InputLog; profile 0:D, 1:I, 2:both; deviationPercentage; loopTimes
		
		double accuracy;
		double precison;
		double recall;
		accuracy = detectorCyclicSampling.readAccuracy();
		precison = detectorCyclicSampling.readPrecision();
		recall = detectorCyclicSampling.readRecall();
		
		
		try {  
		       
            File writename = new File("E:\\result.txt"); // 
            writename.createNewFile(); //   
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));  
            out.write("parameter setting:");
            out.write("\r\n");
            out.write("ANDBranches:"+ ANDBranches +"_XORBranches:"+ XORBranches +"_loopPercent:"+ loopPercent
            		+"_singleActivityPercent:"+ singleActivityPercent +"_sequencePercent:"+ sequencePercent
            		+"_ANDPercent:"+ ANDPercent +"_XORPercent:"+ XORPercent+"_deep:"+ deep);
            out.write("\r\n");
            out.write("performance:");
			out.write("Accuracy:"+ accuracy +"_precision:"+ precison +"_recall:"+ recall);
    		out.write("\r\n");
    		

            out.flush(); //  
            out.close(); // 

        } 
		catch (Exception e) 
		{  
        e.printStackTrace();  
         }		*/
		
		
        	
        }
        return filterLog;
	}
        
	private XEvent makeEvent(String name, Date time, String lifecycle) {
		XAttributeMap attMap = new XAttributeMapImpl();
		XLogFunctions.putLiteral(attMap, "concept:name", name);
//    		XLogFunctions.putLiteral(attMap, "lifecycle:transition", "COMPLETE");
		XLogFunctions.putLiteral(attMap, "lifecycle:transition", lifecycle);
		XLogFunctions.putLiteral(attMap, "org:resource", "artificial");
		XLogFunctions.putTimestamp(attMap, "time:timestamp", time);
		XEvent newEvent = new XEventImpl(attMap);
		return newEvent;
	}
}
