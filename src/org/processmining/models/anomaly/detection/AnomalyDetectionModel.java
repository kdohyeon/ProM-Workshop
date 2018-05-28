package org.processmining.models.anomaly.detection;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.deckfour.xes.model.XLog;
import org.processmining.data.xes.PathDefinition;
import org.processmining.framework.util.HTMLToString;
import org.processmining.models.activity.ActivityModel;
import org.processmining.models.anomaly.profile.AnomalyProfileModel;
import org.processmining.models.relation.RelationModel;
import org.processmining.plugins.anomaly.detection.AnomalyDetectionMiningParameters;

public class AnomalyDetectionModel implements HTMLToString{
	
	//private XLog log;
	//private AnomalyProfileModel profileModel;
	
	//private ActivityModel testActModel;
	private RelationModel testRelModel;
	
	Map<String, Float> cfMap = new HashMap<String, Float>();
	Map<String, Float> tMap = new HashMap<String, Float>();
	Map<String, Float> rActResMap = new HashMap<String, Float>();
	
	//private Map<String, AnomalyScore> anomalyScoreMap;
	//private Map<String, AnomalyScore> sortedAnomalyScoreMap;
	private Map<String, Float> sortedAnomalyScoreMap;
	
	PathDefinition path = new PathDefinition();
	File file = new File(path.getCurrPathTestRelation());
	FileWriter writer = new FileWriter(file);
	
	public AnomalyDetectionModel(XLog log, AnomalyProfileModel profileModel, AnomalyDetectionMiningParameters parameters) throws ParseException, IOException {
		//this.log = log;
		//this.profileModel = profileModel;
		//anomalyScoreMap = new HashMap<String, AnomalyScore>();
		//sortedAnomalyScoreMap = new LinkedHashMap<String, AnomalyScore>();
		sortedAnomalyScoreMap = new LinkedHashMap<String, Float>();
		
		/*
		 * Parameters
		 * */
		// Test data
		ActivityModel testActModel = new ActivityModel(log);
		//this.testActModel = testActModel;
		RelationModel testRelModel = new RelationModel(testActModel);
		this.testRelModel = testRelModel;
		
		// Case id list
		ArrayList<String> caseIDList = new ArrayList<String>();
		caseIDList = testRelModel.getCaseIDList();

		// Training data
		ActivityModel trainingActModel = profileModel.getActModel();
		RelationModel trainingRelModel = profileModel.getRelModel();
		
		/*
		 * Control-Flow, Activity
		 * */
		System.out.println(" ### Anomaly Score : Control-Flow, Activity ### ");
		ControlFlowActivityRuleMatchingModel cfActRuleMatching = new ControlFlowActivityRuleMatchingModel(trainingActModel, testActModel, parameters);
		Map<String, Float> cfActMap = new HashMap<String, Float>();
		cfActMap = cfActRuleMatching.getResultMap();
		
		/*
		 * Control-Flow, Relation
		 * */
		System.out.println(" ### Anomaly Score : Control-Flow, Relation ### ");
		ControlFlowRelationRuleMatchingModel cfRelRuleMatching = new ControlFlowRelationRuleMatchingModel(trainingRelModel, testRelModel, parameters);
		Map<String, Float> cfRelMap = new HashMap<String, Float>();
		cfRelMap = cfRelRuleMatching.getResultMap();
		
		/*
		 * Control-Flow, Overall
		 * */
		
		float weight1, weight2;
		weight1 = (float) 0.5; // equal weight
		weight2 = (float) 0.5; // equal weight
		for(int i = 0; i < caseIDList.size(); i++) {
			String thisCase = caseIDList.get(i);
			float act = cfActMap.get(thisCase);
			float rel = cfRelMap.get(thisCase);
			float result = act*weight1 + rel*weight2;
			cfMap.put(thisCase, result);
		}
		
		/*
		 * Time, Activity
		 * */
		System.out.println(" ### Anomaly Score : Time, Activity ### ");
		TimeActivityRuleMatchingModel timeActRuleMatching = new TimeActivityRuleMatchingModel(profileModel, testActModel, parameters);
		Map<String, Float> tActMap = new HashMap<String, Float>();
		tActMap = timeActRuleMatching.getResultMap();
		
		/*
		 * Time, Relation
		 * */
		System.out.println(" ### Anomaly Score : Time, Relation ### ");
		TimeRelationRuleMatchingModel timeRelRuleMatching = new TimeRelationRuleMatchingModel(profileModel, testRelModel, parameters);
		Map<String, Float> tRelMap = new HashMap<String, Float>();
		tRelMap = timeRelRuleMatching.getResultMap();
		
		/*
		 * Time, Overall
		 * */
		
		float weight3, weight4;
		weight3 = (float) 0.5; // equal weight
		weight4 = (float) 0.5; // equal weight
		for(int i = 0; i < caseIDList.size(); i++) {
			String thisCase = caseIDList.get(i);
			float act = tActMap.get(thisCase);
			float rel = tRelMap.get(thisCase);
			float result = act*weight3 + rel*weight4;
			tMap.put(thisCase, result);
		}
		
		/*
		 * Resource, Activity-Resource
		 * */
		System.out.println(" ### Anomaly Score : Resource, Activity-Resource ### ");
		ResourceActivityRuleMatchingModel rActResRuleMatching = new ResourceActivityRuleMatchingModel(trainingActModel, testActModel, parameters);
		
		rActResMap = rActResRuleMatching.getResultMap();
		
		/*
		 * Overall Anomaly Score
		 * */
		Map<String, Float> anomalyScoreMap = new HashMap<String, Float>();
		float weight5, weight6, weight7;
		weight5 = (float)(1*1.0/3);
		weight6 = (float)(1*1.0/3);
		weight7 = (float)(1*1.0/3);
		
		for(int i = 0; i < caseIDList.size(); i++) {
			String thisCase = caseIDList.get(i);
			float cf = cfMap.get(thisCase);
			float t = tMap.get(thisCase);
			float r = rActResMap.get(thisCase);
			float result = cf*weight5 + t*weight6 + r*weight7;
			anomalyScoreMap.put(thisCase, result);
		}
		
		/*
		 * Normalize
		 * */
		Map<String, Float> normalizedMap = new HashMap<String, Float>();
		Iterator<String> iter = anomalyScoreMap.keySet().iterator();
		float min = 1;
		float max = 0;
		while(iter.hasNext()) {
			String key = iter.next();
			float value = anomalyScoreMap.get(key);
			
			if(value < min) {
				min = value;
			}
			
			if(value > max) {
				max = value;
			}
		}
		
		iter = anomalyScoreMap.keySet().iterator();
		while(iter.hasNext()) {
			String key = iter.next();
			float value = anomalyScoreMap.get(key);
			float noramlized = (value - min)/(max - min);
			normalizedMap.put(key, noramlized);
		}
		
		sortedAnomalyScoreMap = sortByValue(normalizedMap);
		Iterator<String> anomalyIter = sortedAnomalyScoreMap.keySet().iterator();
		while(anomalyIter.hasNext()) {
			String key = anomalyIter.next();
			System.out.println("key, value: " + key + ", " + sortedAnomalyScoreMap.get(key));
		}
		
		/*
		 * Control-flow Perspective Rule Matching
		 * */
		/*
		ControlFlowRuleMatchingModel cfRuleMatching = new ControlFlowRuleMatchingModel(
				trainingActModel, testActModel, trainingRelModel, testRelModel, profileModel, parameters);
		
		System.out.println(" ### Control-Flow Anomaly Score ###");
		Map<String, ControlFlowScore> controlFlowScoreMap = new HashMap<String, ControlFlowScore>();
		controlFlowScoreMap = cfRuleMatching.getControlFlowScore();
		Iterator<String> cfIter = controlFlowScoreMap.keySet().iterator();
		while(cfIter.hasNext()) {
			String key = cfIter.next();
			System.out.println("key, value: " + key + ", " + controlFlowScoreMap.get(key).getControlFlowScore());
		}
		*/
		/*
		 * Resource Perspective Rule Matching
		 * */
		/*
		ResourceRuleMatchingModel rRuleMatching = new ResourceRuleMatchingModel(
				trainingActModel, testActModel, trainingRelModel, testRelModel, profileModel, parameters);
		
		System.out.println(" ### Resource Anomaly Score ###");
		Map<String, ResourceScore> resourceScoreMap = new HashMap<String, ResourceScore>();
		resourceScoreMap = rRuleMatching.getResourceScore();
		Iterator<String> rIter = resourceScoreMap.keySet().iterator();
		while(rIter.hasNext()) {
			String key = rIter.next();
			System.out.println("key, value: " + key + ", " + resourceScoreMap.get(key).getResourceScore());
		}
		*/
		
		/*
		 * Anomaly Score
		 * */
		//System.out.println(" ### Overall Anomaly Score ###");
		
		// Change the parameter (ratio of Log-To-Rule and Rule-To-Log by the frequency of each perspective's rules
		/*
		int a = 0;
		int b = 0;
		
		if(trainingRelModel.getRelationActivityMatrix().getRelationMatrixListSize() > 0) {
			a = trainingRelModel.getRelationActivityMatrix().getRelationMatrixListSize();
		}
		
		if(trainingRelModel.getRelationResourceMatrix().getRelationMatrixListSize() > 0) {
			b = trainingRelModel.getRelationResourceMatrix().getRelationMatrixListSize();
		}
		*/
		/*
		int sum = a + b;
		float alpha = (float) ((a * 1.0) / (sum));
		float beta = (float) ((b * 1.0) / (sum));
		parameters.setAlpha(alpha);
		parameters.setBeta(beta);
		
		System.out.println("a: " + a + ", b: " + b + ", alpha: " + alpha + ", beta: " + beta);
		*/
		/*
		this.calculateAnomalyScore(caseIDList, controlFlowScoreMap, resourceScoreMap, parameters);
		
		sortedAnomalyScoreMap = sortByValue(anomalyScoreMap);
		Iterator<String> anomalyIter = sortedAnomalyScoreMap.keySet().iterator();
		while(anomalyIter.hasNext()) {
			String key = anomalyIter.next();
			//System.out.println("key, value: " + key + ", " + anomalyScoreMap.get(key).getOverallScore());
		}
		*/
	}
	public Map<String, Float> sortByValue(Map<String, Float> unsortMap){
		// 1. Convert Map to List of Map
		List<Map.Entry<String, Float>> list = new LinkedList<Map.Entry<String, Float>>(unsortMap.entrySet());
		
		// 2. Sort list with Collections.sort(), provide a custom Comparator
		// Try to switch the o1, o2 position for a different order
		Collections.sort(list, new Comparator<Map.Entry<String, Float>>(){
			public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});
		
		// 3. Loop the unsorted list and put it into a new insertion order Map LinkedHashMap
		Map<String, Float> sortedMap = new LinkedHashMap<String, Float>();
		for(Map.Entry<String, Float> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		
		return sortedMap;
	}
	/*
	public Map<String, AnomalyScore> sortByValue(Map<String, AnomalyScore> unsortMap){
		// 1. Convert Map to List of Map
		List<Map.Entry<String, AnomalyScore>> list = new LinkedList<Map.Entry<String, AnomalyScore>>(unsortMap.entrySet());
		
		// 2. Sort list with Collections.sort(), provide a custom Comparator
		// Try to switch the o1, o2 position for a different order
		Collections.sort(list, new Comparator<Map.Entry<String, AnomalyScore>>(){
			public int compare(Map.Entry<String, AnomalyScore> o1, Map.Entry<String, AnomalyScore> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});
		
		// 3. Loop the unsorted list and put it into a new insertion order Map LinkedHashMap
		Map<String, AnomalyScore> sortedMap = new LinkedHashMap<String, AnomalyScore>();
		for(Map.Entry<String, AnomalyScore> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		
		return sortedMap;
	}*/
	
	/*
	public void calculateAnomalyScore(
			ArrayList<String> caseIDList, 
			Map<String, ControlFlowScore> controlFlow, 
			Map<String, ResourceScore> resource, 
			AnomalyDetectionMiningParameters parameters) {
		
		int size = caseIDList.size();
		
		for(int i = 0; i < size; i++) {
			String currCaseID = caseIDList.get(i);
			
			ControlFlowScore cfScore;
			ResourceScore rScore;
			
			if(controlFlow.containsKey(currCaseID)) {
				cfScore = controlFlow.get(currCaseID);
			}else {
				cfScore = new ControlFlowScore(currCaseID, 0, 0, parameters);
			}
			
			if(resource.containsKey(currCaseID)) {
				rScore = resource.get(currCaseID);
			}else {
				rScore = new ResourceScore(currCaseID, 0, 0, parameters);
			}
			
			AnomalyScore aScore = new AnomalyScore(currCaseID, cfScore, rScore, parameters);
			
			anomalyScoreMap.put(currCaseID, aScore);
		}		
	}
	*/
	
	public Map<String, Float> getAnomalyScore() {
		return sortedAnomalyScoreMap;
	}
	
	/*
	public Map<String, AnomalyScore> getAnomalyScore() {
		return anomalyScoreMap;
	}
	*/
	
	public String toHTMLString(boolean includeHTMLTags) {
		StringBuffer buffer = new StringBuffer();
		if (includeHTMLTags) {
			buffer.append("<html>");
		}
		
		buffer.append("<p> Anomaly Detection Model </p>");
		/*
		buffer.append("<p> ### Test Event Log Profile - Activity ### </p>");
		buffer.append("<table>");
		buffer.append("<tr>");
		buffer.append("<th> Case ID </th>");
		buffer.append("<th> Activity ID </th>");
		buffer.append("<th> Resource ID </th>");
		buffer.append("<th> Start Time</th>");
		buffer.append("<th> Complete Time</th>");
		buffer.append("<th> Processing Time</th>");
		buffer.append("</tr>");
		int activitySize = testActModel.getActivityCardinality();
		for(int i = 0; i < activitySize; i++) {
			buffer.append("<tr>");
			buffer.append("<td>" + testActModel.getActivity(i).getCaseID() + "</td>");
			buffer.append("<td>" + testActModel.getActivity(i).getActivityID() + "</td>");
			buffer.append("<td>" + testActModel.getActivity(i).getResourceID() + "</td>");
			buffer.append("<td>" + testActModel.getActivity(i).getStartTimestamp() + "</td>");
			buffer.append("<td>" + testActModel.getActivity(i).getCompleteTimestamp() + "</td>");
			buffer.append("<td>" + testActModel.getActivity(i).getProcessingTime() + "</td>");
			buffer.append("</tr>");
		}
		buffer.append("</table>");
		
		buffer.append("<p> ### Test Event Log Profile - Relation ### </p>");
		buffer.append("<table>");
		buffer.append("<tr>");
		buffer.append("<th> Case ID </th>");
		buffer.append("<th> From Activity ID </th>");
		buffer.append("<th> To Activity ID </th>");
		buffer.append("<th> From Resource ID </th>");
		buffer.append("<th> To Resource ID </th>");
		buffer.append("<th> Relation Type </th>");
		buffer.append("<th> Transition Time </th>");
		buffer.append("<th> Overlap Time </th>");
		buffer.append("<th> TrueX Time </th>");
		buffer.append("<th> TrueY Time </th>");
		buffer.append("</tr>");
		int relationSize = testRelModel.getRelationCardinality();
		for(int i = 0; i < relationSize; i++) {
			buffer.append("<tr>");
			buffer.append("<td>" + testRelModel.getRelation(i).getAntecedent().getCaseID() + "</td>");
			buffer.append("<td>" + testRelModel.getRelation(i).getAntecedent().getActivityID() + "</td>");
			buffer.append("<td>" + testRelModel.getRelation(i).getConsequent().getActivityID() + "</td>");
			buffer.append("<td>" + testRelModel.getRelation(i).getAntecedent().getResourceID() + "</td>");
			buffer.append("<td>" + testRelModel.getRelation(i).getConsequent().getResourceID() + "</td>");
			buffer.append("<td>" + testRelModel.getRelation(i).getRelationType() + "</td>");
			buffer.append("<td>" + testRelModel.getRelation(i).getTransitionTime() + "</td>");
			buffer.append("<td>" + testRelModel.getRelation(i).getOverlapTime() + "</td>");
			buffer.append("<td>" + testRelModel.getRelation(i).getTrueXTime() + "</td>");
			buffer.append("<td>" + testRelModel.getRelation(i).getTrueYTime() + "</td>");
			buffer.append("</tr>");
		}
		buffer.append("</table>");
		*/
		try {
			writer.append("CaseID,From Activity ID,To Activity ID,From Resource ID,To Resource ID,Relation Type,Transition Time,Overlap Time,TrueX Time,TrueY Time\n");
			int relationSize = testRelModel.getRelationCardinality();
			for(int i = 0; i < relationSize; i++) {
				writer.append(testRelModel.getRelation(i).getAntecedent().getCaseID()+",");
				writer.append(testRelModel.getRelation(i).getAntecedent().getActivityID()+",");
				writer.append(testRelModel.getRelation(i).getConsequent().getActivityID()+",");
				writer.append(testRelModel.getRelation(i).getAntecedent().getResourceID()+",");
				writer.append(testRelModel.getRelation(i).getConsequent().getResourceID()+",");
				writer.append(testRelModel.getRelation(i).getRelationType()+",");
				writer.append(String.valueOf(testRelModel.getRelation(i).getTransitionTime())+",");
				writer.append(String.valueOf(testRelModel.getRelation(i).getOverlapTime())+",");
				writer.append(String.valueOf(testRelModel.getRelation(i).getTrueXTime())+",");
				writer.append(String.valueOf(testRelModel.getRelation(i).getTrueYTime())+",");
				writer.append("\n");
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		buffer.append("<p> ### Anomaly Score ### </p>");
		buffer.append("<table>");
		buffer.append("<tr>");
		buffer.append("<th> No. </th>");
		buffer.append("<th> Case ID </th>");
		buffer.append("<th> Anomaly Score </th>");
		buffer.append("<th> Control-Flow Score </th>");
		buffer.append("<th> Time Score </th>");
		buffer.append("<th> Resource Score </th>");
		buffer.append("</tr>");
		
		Iterator<String> iter = sortedAnomalyScoreMap.keySet().iterator();
		int cnt = 0;
		while(iter.hasNext()) {
			/*
			if(cnt == 100) {
				break;
			}
			*/
			String key = iter.next();
			cnt++;
			
			buffer.append("<tr>");
			
			buffer.append("<td>" + cnt + "</td>");
			buffer.append("<td>" + key + "</td>");
			
			buffer.append("<td>" + sortedAnomalyScoreMap.get(key) + "</td>");
			buffer.append("<td>" + cfMap.get(key) + "</td>");
			buffer.append("<td>" + tMap.get(key) + "</td>");
			buffer.append("<td>" + rActResMap.get(key) + "</td>");
			//buffer.append("<td>" + sortedAnomalyScoreMap.get(key).getOverallScore() + "</td>");
			//buffer.append("<td>" + sortedAnomalyScoreMap.get(key).getCfScore().getControlFlowScore() + "</td>");
			//buffer.append("<td>" + sortedAnomalyScoreMap.get(key).getrScore().getResourceScore() + "</td>");			
			
			buffer.append("</tr>");
		}		
		
		if (includeHTMLTags) {
			buffer.append("</html>");
		}
		return buffer.toString();
	}
}
