package org.processmining.models.anomaly.detection;

import java.text.ParseException;

import org.deckfour.xes.model.XLog;
import org.processmining.framework.util.HTMLToString;
import org.processmining.mining.anomaly.detection.AnomalyDetectionMiningParameters;
import org.processmining.models.activity.ActivityModel;
import org.processmining.models.anomaly.profile.AnomalyProfileModel;
import org.processmining.models.relation.RelationModel;

public class AnomalyDetectionModel implements HTMLToString{
	
	private XLog log;
	private AnomalyProfileModel profileModel;
	
	private ActivityModel testActModel;
	private RelationModel testRelModel;
	
	private float anomalyScore;
	
	public AnomalyDetectionModel(XLog log, AnomalyProfileModel profileModel, AnomalyDetectionMiningParameters parameters) throws ParseException {
		this.log = log;
		this.profileModel = profileModel;
		
		/*
		 * Test data
		 * */
		ActivityModel testActModel = new ActivityModel(log);
		this.testActModel = testActModel;
		RelationModel testRelModel = new RelationModel(testActModel);
		this.testRelModel = testRelModel;
		
		/*
		 * Training data - profile model
		 * */
		ActivityModel trainingActModel = profileModel.getActModel();
		RelationModel trainingRelModel = profileModel.getRelModel();
		
		/*
		 * Control-flow Perspective Rule Matching
		 * */
		ControlFlowRuleMatchingModel cfRuleMatching = new ControlFlowRuleMatchingModel(
				trainingActModel, testActModel, trainingRelModel, testRelModel, profileModel, parameters);
		
		/*
		 * Resource Perspective Rule Matching
		 * */
		ResourceRuleMatchingModel rRuleMatching = new ResourceRuleMatchingModel(
				trainingActModel, testActModel, trainingRelModel, testRelModel, profileModel, parameters);
		
		/*
		 * Anomaly Score
		 * */
		float alpha = parameters.getAlpha();
		float beta = parameters.getBeta();
		
		anomalyScore = alpha*cfRuleMatching.getControlFlowScore() + beta*rRuleMatching.getResourceScore();
	}
	
	public float getAnomalyScore() {
		return anomalyScore;
	}
	
	public String toHTMLString(boolean includeHTMLTags) {
		StringBuffer buffer = new StringBuffer();
		if (includeHTMLTags) {
			buffer.append("<html>");
		}
		
		buffer.append("<p> Anomaly Detection Model </p>");
		
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
			buffer.append("<td>" + testRelModel.getRelation(i).getAntecedent().getCaseID());
			buffer.append("<td>" + testRelModel.getRelation(i).getAntecedent().getActivityID());
			buffer.append("<td>" + testRelModel.getRelation(i).getConsequent().getActivityID());
			buffer.append("<td>" + testRelModel.getRelation(i).getAntecedent().getResourceID());
			buffer.append("<td>" + testRelModel.getRelation(i).getConsequent().getResourceID());
			buffer.append("<td>" + testRelModel.getRelation(i).getRelationType());
			buffer.append("<td>" + testRelModel.getRelation(i).getTransitionTime());
			buffer.append("<td>" + testRelModel.getRelation(i).getOverlapTime());
			buffer.append("<td>" + testRelModel.getRelation(i).getTrueXTime());
			buffer.append("<td>" + testRelModel.getRelation(i).getTrueYTime());
		}
		buffer.append("</table>");
		
		if (includeHTMLTags) {
			buffer.append("</html>");
		}
		return buffer.toString();
	}
}
