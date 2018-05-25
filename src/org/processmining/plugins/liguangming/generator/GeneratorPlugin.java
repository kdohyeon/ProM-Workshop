package org.processmining.plugins.liguangming.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.swing.JOptionPane;

import org.deckfour.xes.in.XMxmlParser;
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

import it.unipd.math.plg.models.PlgParameters;
import it.unipd.math.plg.models.PlgProcess;
import it.unipd.math.plg.models.distributions.PlgProbabilityDistribution;


public class GeneratorPlugin{
	@SuppressWarnings("null")
	@Plugin(name = "GenerateProcess", 
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
        XLog inputLog = null;
        int logNumPerModel = 1;
        int modelNumPerParameter = 1;
        
        int times =1;
		//generate an artificial log
        for(deep = 3; deep < 4; deep++)
        {
        	for(ANDBranches =3; ANDBranches <4; ANDBranches++)
        	{
 //       		for(ANDBranches =1; ANDBranches <6; ANDBranches++)
        		{
        			for(loopPercent = 0; loopPercent <=60; loopPercent=loopPercent+50)
        			{
        				for(sequencePercent = 0; sequencePercent <=60; sequencePercent=sequencePercent+100)
            			{
        					for(ANDPercent = 0; ANDPercent <=60; ANDPercent=ANDPercent+50)
                			{
        						for(XORPercent = 0; XORPercent <=60; XORPercent=XORPercent+50)
                    			{
        							for(int modelNum=1; modelNum<=modelNumPerParameter;modelNum++)
        							{
/*										try 
										{
										 // define the new process
											PlgProcess p = new PlgProcess("test process");
							//				PlgParameters parameters = new PlgParameters(ANDBranches, PlgProbabilityDistribution.normalDistributionFactory(), ANDBranches, PlgProbabilityDistribution.normalDistributionFactory(), loopPercent, singleActivityPercent, sequencePercent, ANDPercent, XORPercent, deep, PlgProbabilityDistribution.normalDistributionFactory(), PlgProbabilityDistribution.normalDistributionFactory());
											PlgParameters parameters = new PlgParameters(ANDBranches, PlgProbabilityDistribution.uniformDistributionFactory(), ANDBranches, PlgProbabilityDistribution.uniformDistributionFactory(), loopPercent, singleActivityPercent, sequencePercent, ANDPercent, XORPercent, deep, PlgProbabilityDistribution.normalDistributionFactory(), PlgProbabilityDistribution.normalDistributionFactory());
											p.randomize(parameters);
											p.saveAsNewLog("./test-log.zip", 1000);
										 } catch (IOException e) {
											e.printStackTrace();
										 }*/
//									if(times ==1)
//									{
//										loopPercent=50;
//										ANDPercent=0;
//										XORPercent = 0;
//										
//									}
									times++;
										 // define the new process
						//				PlgProcess p = new PlgProcess("D"+deep + "_L"+loopPercent+"_S"+sequencePercent+"_A"+ANDPercent+"_X"+XORPercent+"_N"+modelNum);
        								PlgProcess p = new PlgProcess("test process");
						//				PlgParameters parameters = new PlgParameters(ANDBranches, PlgProbabilityDistribution.normalDistributionFactory(), ANDBranches, PlgProbabilityDistribution.normalDistributionFactory(), loopPercent, singleActivityPercent, sequencePercent, ANDPercent, XORPercent, deep, PlgProbabilityDistribution.normalDistributionFactory(), PlgProbabilityDistribution.normalDistributionFactory());
										PlgParameters parameters = new PlgParameters(ANDBranches, PlgProbabilityDistribution.uniformDistributionFactory(), ANDBranches, PlgProbabilityDistribution.uniformDistributionFactory(), loopPercent, singleActivityPercent, sequencePercent, ANDPercent, XORPercent, deep, PlgProbabilityDistribution.normalDistributionFactory(), PlgProbabilityDistribution.normalDistributionFactory());
										p.randomize(parameters);
										
										File fileModels = new File("d:\\modelsLast");//check if the folder exist; if not,create it
										if (!fileModels.exists()) fileModels.mkdir();
//										p.saveProcessAs("d:\\modelsLast\\D"+deep + "_L"+loopPercent+"_S"+sequencePercent+"_A"+ANDPercent+"_X"+XORPercent+"_mN"+modelNum+".plg");
										

										
										for(int logNum=1;logNum<=logNumPerModel;logNum++)
										{
											p.saveAsNewLog("./test-log.zip", 1000);
	
											//decompress the artificial log
											Decompressor.decompress("./test-log.zip", "d:\\ff");  
									/*		File srcFile = new File("d:\\ff\\test process.mxml"); 
											File file2=new File("d:\\ff\\try.mxml");
											boolean flag = srcFile.renameTo(file2);*/
											
											//input the artificial log
											XMxmlParser parser = new XMxmlParser();
											 inputLog = parser.parse(new File("d:\\ff\\test process.mxml")).get(0);
											
											//filter the artificial log
											XAttributeMap logattlist = XLogFunctions.copyAttMap(inputLog.getAttributes());
											XLog filterLog = new XLogImpl(logattlist); //the log which just contains the "complete" events
											for (int i = 0; i < inputLog.size(); i++) //i represents the number of the already created deviations
											{
												XTrace oldTrace = inputLog.get(i);
												XAttributeMap newAttributeMap = oldTrace.getAttributes();
												XTrace newTrace = new XTraceImpl(XLogFunctions.copyAttMap(newAttributeMap));
												for(int j=0; j<oldTrace.size(); j++)
												{
													String lifecycle = oldTrace.get(j).getAttributes().get("lifecycle:transition").toString();
													if(lifecycle.equals("complete")) newTrace.add(oldTrace.get(j));
														
												}
												filterLog.add(newTrace);
											}
									
											//save the artificial log (complete)	
											File fileNormal = new File("d:\\logs\\normal");//check if the folder exist; if not,create it
											if (!fileNormal.exists()) fileNormal.mkdir();
											
											XesXmlSerializer serializer = new XesXmlSerializer();
											OutputStream output = new FileOutputStream("d:\\logs\\normal\\D"+deep +"_L"+loopPercent+"_S"+sequencePercent+"_A"+ANDPercent+"_X"+XORPercent+"_mN"+modelNum +"_lN"+logNum+".xes");
											serializer.serialize(filterLog, output);
									
											//generate deviations
											for(deviationPercentage = 0.1; deviationPercentage<0.4; deviationPercentage=deviationPercentage+0.1)
											{
												for(deviationType =0;deviationType<3;deviationType++)
												{
													//generate the log with deviations
													XLog deviationsLog = new XLogImpl(logattlist);//the filterLog + deviations
													GenerateDeviations generator = new GenerateDeviations();
													deviationsLog = generator.generateDeviation(filterLog, deviationPercentage, deviationType);
													
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
													File fileExceptional = new File("d:\\logs\\exceptional");//check if the folder exist; if not,create it
													if (!fileExceptional.exists()) fileExceptional.mkdir();
													
													XesXmlSerializer serializerDeviation = new XesXmlSerializer();
													OutputStream outputDeviation = new FileOutputStream("d:\\logs\\exceptional\\P"+deviationPercentage+"T"+deviationType+"D"+deep +"_L"+loopPercent+"_S"+sequencePercent+"_A"+ANDPercent+"_X"+XORPercent+"_mN"+modelNum +"_lN"+logNum+".xes");
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
        return inputLog;
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
