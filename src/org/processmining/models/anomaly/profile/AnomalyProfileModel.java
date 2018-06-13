package org.processmining.models.anomaly.profile;

import java.text.ParseException;

import org.processmining.data.rules.ActivityResourceRule;
import org.processmining.framework.util.HTMLToString;
import org.processmining.models.activity.ActivityModel;
import org.processmining.models.activityResource.ActivityResourceModel;
import org.processmining.models.relation.RelationModel;

public class AnomalyProfileModel implements HTMLToString{
	private ActivityModel actModel = null;
	private RelationModel relModel = null;
	private ActivityResourceModel actResModel = null;
	
	ProcessingProfileModel processingModel;
	RelationProfileModel performanceModel;
	TransitionProfileModel transitionModel;
	OverlapProfileModel overlapModel;
	TrueXProfileModel trueXModel;
	TrueYProfileModel trueYModel;
	
	public AnomalyProfileModel(ActivityModel actModel, RelationModel relModel, ActivityResourceModel actResModel) throws ParseException {
		this.setActModel(actModel);
		this.setRelModel(relModel);
		this.setActResModel(actResModel);
		
		System.out.println("...Processing Profile...");
		ProcessingProfileModel processingModel = new ProcessingProfileModel(actModel);
		//processingModel.printProcessingProfile();
		setProcessingModel(processingModel);
		
		System.out.println("...Relation Profile...");
		RelationProfileModel performnaceModel = new RelationProfileModel(relModel);
		setPerformanceModel(performnaceModel);
		
		System.out.println("...Transition Profile...");
		TransitionProfileModel transitionModel = new TransitionProfileModel(relModel);
		//transitionModel.printTransitionProfile();
		setTransitionModel(transitionModel);
		
		System.out.println("...Overlap Profile...");
		OverlapProfileModel overlapModel = new OverlapProfileModel(relModel);
		//overlapModel.printOverlapProfile();
		setOverlapModel(overlapModel);
		
		System.out.println("...True X Profile...");
		TrueXProfileModel trueXModel = new TrueXProfileModel(relModel);
		//trueXModel.printTrueXProfile();
		setTrueXModel(trueXModel);
		
		System.out.println("...True Y Profile...");
		TrueYProfileModel trueYModel = new TrueYProfileModel(relModel);
		//trueYModel.printTrueYProfile();
		setTrueYModel(trueYModel);
	}
	
	
	public String toHTMLString(boolean includeHTMLTags) {
		StringBuffer buffer = new StringBuffer();
		if (includeHTMLTags) {
			buffer.append("<html>");
		}
		
		buffer.append("<h3> Performance Measures </h3>");
		/*
		 * Activity
		 * */
		buffer.append("<p> ### Activity ### </p>");
		buffer.append("<table>");
		buffer.append("<tr>");
		buffer.append("<th> Activity ID </th>");
		buffer.append("<th> Frequency </th>");
		buffer.append("<th> Average </th>");
		buffer.append("<th> Standard Deviation </th>");
		buffer.append("<th> Lower Bound </th>");
		buffer.append("<th> Upper Bound </th>");
		buffer.append("</tr>");
		
		int processingListSize = processingModel.getProcessingList_Act_Size();
		for(int i = 0; i < processingListSize; i++) {
			float avg = processingModel.getProcessingList_Act().get(i).getAvg();
			float std = processingModel.getProcessingList_Act().get(i).getStdev();
			
			float lower = avg - 3*std;
			if(lower < 0) {
				lower = 0;
			}
			float upper = avg + 3*std;
			
			buffer.append("<tr>");
			buffer.append("<td>" + processingModel.getProcessingList_Act().get(i).getActivityID() + "</td>");
			buffer.append("<td>" + processingModel.getProcessingList_Act().get(i).getFreq() + "</td>");
			buffer.append("<td>" + avg + "</td>");
			buffer.append("<td>" + std + "</td>");
			buffer.append("<td>" + lower + "</td>");
			buffer.append("<td>" + upper + "</td>");
			buffer.append("</tr>");
		}
		buffer.append("</table>");
		
		/*
		 * Relation - Transition 
		 * */
		buffer.append("<p> ### Relation - Transition ### </p>");
		buffer.append("<table>");
		buffer.append("<tr>");
		buffer.append("<th> From Activity ID </th>");
		buffer.append("<th> To Activity ID </th>");
		buffer.append("<th> Relation </th>");
		buffer.append("<th> Frequency </th>");
		buffer.append("<th> Average </th>");
		buffer.append("<th> Standard Deviation </th>");
		buffer.append("<th> Lower Bound </th>");
		buffer.append("<th> Upper Bound </th>");
		buffer.append("</tr>");
		
		int transitionListSize = transitionModel.getTransitionList_Act_Size();
		for(int i = 0; i < transitionListSize; i++) {
			float avg = transitionModel.getTransitionList_Act().get(i).getAvg();
			float std = transitionModel.getTransitionList_Act().get(i).getStdev();
			
			float lower = avg - 3*std;
			if(lower < 0) {
				lower = 0;
			}
			float upper = avg + 3*std;
			
			buffer.append("<tr>");
			buffer.append("<td>" + transitionModel.getTransitionList_Act().get(i).getFromActivityID() + "</td>");
			buffer.append("<td>" + transitionModel.getTransitionList_Act().get(i).getToActivityID() + "</td>");
			buffer.append("<td>" + transitionModel.getTransitionList_Act().get(i).getRelation() + "</td>");
			buffer.append("<td>" + transitionModel.getTransitionList_Act().get(i).getFreq() + "</td>");
			buffer.append("<td>" + avg + "</td>");
			buffer.append("<td>" + std + "</td>");
			buffer.append("<td>" + lower + "</td>");
			buffer.append("<td>" + upper + "</td>");
			buffer.append("</tr>");
		}
		buffer.append("</table>");
		
		/*
		 * Relation - Overlap 
		 * */
		buffer.append("<p> ### Relation - Overlap ### </p>");
		buffer.append("<table>");
		buffer.append("<tr>");
		buffer.append("<th> From Activity ID </th>");
		buffer.append("<th> To Activity ID </th>");
		buffer.append("<th> Relation </th>");
		buffer.append("<th> Frequency </th>");
		buffer.append("<th> Average </th>");
		buffer.append("<th> Standard Deviation </th>");
		buffer.append("<th> Lower Bound </th>");
		buffer.append("<th> Upper Bound </th>");
		buffer.append("</tr>");
		int overlapListSize = overlapModel.getOverlapList_Act_Size();
		for(int i = 0; i < overlapListSize; i++) {
			float avg = overlapModel.getOverlapList_Act().get(i).getAvg();
			float std = overlapModel.getOverlapList_Act().get(i).getStdev();
			
			float lower = avg - 3*std;
			if(lower < 0) {
				lower = 0;
			}
			float upper = avg + 3*std;
			
			buffer.append("<tr>");
			buffer.append("<td>" + overlapModel.getOverlapList_Act().get(i).getFromActivityID() + "</td>");
			buffer.append("<td>" + overlapModel.getOverlapList_Act().get(i).getToActivityID() + "</td>");
			buffer.append("<td>" + overlapModel.getOverlapList_Act().get(i).getRelation() + "</td>");
			buffer.append("<td>" + overlapModel.getOverlapList_Act().get(i).getFreq() + "</td>");
			buffer.append("<td>" + avg + "</td>");
			buffer.append("<td>" + std + "</td>");
			buffer.append("<td>" + lower + "</td>");
			buffer.append("<td>" + upper + "</td>");
			buffer.append("</tr>");
		}
		buffer.append("</table>");
		
		/*
		 * Relation - True X 
		 * */
		buffer.append("<p> ### Relation - Exclusive Antecedent ### </p>");
		buffer.append("<table>");
		buffer.append("<tr>");
		buffer.append("<th> From Activity ID </th>");
		buffer.append("<th> To Activity ID </th>");
		buffer.append("<th> Relation </th>");
		buffer.append("<th> Frequency </th>");
		buffer.append("<th> Average </th>");
		buffer.append("<th> Standard Deviation </th>");
		buffer.append("<th> Lower Bound </th>");
		buffer.append("<th> Upper Bound </th>");
		buffer.append("</tr>");
		int trueXListSize = trueXModel.getTrueXList_Act_Size();
		for(int i = 0; i < trueXListSize; i++) {
			float avg = trueXModel.getTrueXList_Act().get(i).getAvg();
			float std = trueXModel.getTrueXList_Act().get(i).getStdev();
			
			float lower = avg - 3*std;
			if(lower < 0) {
				lower = 0;
			}
			float upper = avg + 3*std;
			
			buffer.append("<tr>");
			buffer.append("<td>" + trueXModel.getTrueXList_Act().get(i).getFromActivityID() + "</td>");
			buffer.append("<td>" + trueXModel.getTrueXList_Act().get(i).getToActivityID() + "</td>");
			buffer.append("<td>" + trueXModel.getTrueXList_Act().get(i).getRelation() + "</td>");
			buffer.append("<td>" + trueXModel.getTrueXList_Act().get(i).getFreq() + "</td>");
			buffer.append("<td>" + avg + "</td>");
			buffer.append("<td>" + std + "</td>");
			buffer.append("<td>" + lower + "</td>");
			buffer.append("<td>" + upper + "</td>");
			buffer.append("</tr>");
		}
		buffer.append("</table>");
		
		/*
		 * Relation - True Y
		 * */
		buffer.append("<p> ### Relation - Exclusive Consequent ### </p>");
		buffer.append("<table>");
		buffer.append("<tr>");
		buffer.append("<th> From Activity ID </th>");
		buffer.append("<th> To Activity ID </th>");
		buffer.append("<th> Relation </th>");
		buffer.append("<th> Frequency </th>");
		buffer.append("<th> Average </th>");
		buffer.append("<th> Standard Deviation </th>");
		buffer.append("<th> Lower Bound </th>");
		buffer.append("<th> Upper Bound </th>");
		buffer.append("</tr>");
		int trueYListSize = trueYModel.getTrueYList_Act_Size();
		for(int i = 0; i < trueYListSize; i++) {
			float avg = trueYModel.getTrueYList_Act().get(i).getAvg();
			float std = trueYModel.getTrueYList_Act().get(i).getStdev();
			
			float lower = avg - 3*std;
			if(lower < 0) {
				lower = 0;
			}
			float upper = avg + 3*std;
			
			buffer.append("<tr>");
			buffer.append("<td>" + trueYModel.getTrueYList_Act().get(i).getFromActivityID() + "</td>");
			buffer.append("<td>" + trueYModel.getTrueYList_Act().get(i).getToActivityID() + "</td>");
			buffer.append("<td>" + trueYModel.getTrueYList_Act().get(i).getRelation() + "</td>");
			buffer.append("<td>" + trueYModel.getTrueYList_Act().get(i).getFreq() + "</td>");
			buffer.append("<td>" + avg + "</td>");
			buffer.append("<td>" + std + "</td>");
			buffer.append("<td>" + lower + "</td>");
			buffer.append("<td>" + upper + "</td>");
			buffer.append("</tr>");
		}
		buffer.append("</table>");
		
		/*
		 * Resource
		 * */
		buffer.append("<p> ### Resource ### </p>");
		buffer.append("<table>");
		buffer.append("<tr>");
		buffer.append("<th> Activity ID </th>");
		buffer.append("<th> Resource </th>");
		buffer.append("<th> Frequency </th>");
		buffer.append("</tr>");
		int resourceActivitySize = processingModel.getProcessingList_Act_Res_Size();
		for(int i = 0; i < resourceActivitySize; i++) {
			buffer.append("<tr>");
			buffer.append("<td>" + processingModel.getProcessingList_Act_Res().get(i).getActivityID() + "</td>");
			buffer.append("<td>" + processingModel.getProcessingList_Act_Res().get(i).getResourceID() + "</td>");
			buffer.append("<td>" + processingModel.getProcessingList_Act_Res().get(i).getFreq() + "</td>");
			buffer.append("</tr>");
		}
		
		buffer.append("</table>");
		
		/*
		 * Rules
		 * */
		buffer.append("<h1> Rules </h1>");
		
		/*
		 * Control-flow, Activity 
		 * */
		buffer.append("<p> ### Control-flow, Activity ### </p>");
		buffer.append("<table>");
		buffer.append("<tr>");
		buffer.append("<th> No. </th>");
		buffer.append("<th> Activity </th>");
		buffer.append("<th> Frequency </th>");
		buffer.append("<th> Support </th>");
		buffer.append("<th> Confidence </th>");
		buffer.append("</tr>");
		actModel.getActivityRule().getRuleList();
		
		int cfActRuleSize = actModel.getActivityRule().getRuleSize();
		for(int i = 0; i < cfActRuleSize; i++) {
			int idx = i+1;
			buffer.append("<tr>");
			buffer.append("<td>" + idx + "</td>");
			buffer.append("<td>" + actModel.getActivityRule().getRuleList().get(i).getAct() + "</td>");
			buffer.append("<td>" + actModel.getActivityRule().getRuleList().get(i).getFreq() + "</td>");
			buffer.append("<td>" + actModel.getActivityRule().getRuleList().get(i).getMinSupp() + "</td>");
			buffer.append("<td>" + actModel.getActivityRule().getRuleList().get(i).getMinConf() + "</td>");
			buffer.append("</tr>");
		}
		
		buffer.append("</table>");
		
		/*
		 * Control-flow, Relation
		 * */
		buffer.append("<p> ### Control-flow, Relation ### </p>");
		buffer.append("<table>");
		buffer.append("<tr>");
		buffer.append("<th> No. </th>");
		buffer.append("<th> From Activity </th>");
		buffer.append("<th> To Activity </th>");
		buffer.append("<th> Relation Type </th>");
		buffer.append("<th> Frequency </th>");
		buffer.append("<th> Support </th>");
		buffer.append("<th> Confidence </th>");
		buffer.append("</tr>");
		int cfRuleSize = relModel.getRelationActivityMatrix().getRelationMatrixListSize();
		for(int i = 0; i < cfRuleSize; i++) {
			int idx = i+1;
			buffer.append("<tr>");
			buffer.append("<td>" + idx + "</td>");
			buffer.append("<td>" + relModel.getRelationActivityMatrix().getAntecedent(i) + "</td>");
			buffer.append("<td>" + relModel.getRelationActivityMatrix().getConsequent(i) + "</td>");
			buffer.append("<td>" + relModel.getRelationActivityMatrix().getRelationType(i) + "</td>");
			buffer.append("<td>" + relModel.getRelationActivityMatrix().getFrequency(i) + "</td>");
			buffer.append("<td>" + relModel.getRelationActivityMatrix().getSupport(i) + "</td>");
			buffer.append("<td>" + relModel.getRelationActivityMatrix().getConfidence(i) + "</td>");
			buffer.append("</tr>");
		}
		buffer.append("</table>");
				
		/*
		 * Resource, Activity
		 * */
		buffer.append("<p> ### Resource ### </p>");
		buffer.append("<table>");
		buffer.append("<tr>");
		buffer.append("<th> No. </th>");
		buffer.append("<th> Activity </th>");
		buffer.append("<th> Resource </th>");
		buffer.append("<th> Frequency </th>");
		buffer.append("<th> Support </th>");
		buffer.append("<th> Confidence </th>");
		buffer.append("</tr>");
		int rRuleSize = actModel.getActResRuleModel().getRuleSize();
		for(int i = 0; i < rRuleSize; i++) {
			ActivityResourceRule rule = actModel.getActResRuleModel().getRuleList().get(i);
			String[] parseLine = rule.getAct().split("_");
			String act = parseLine[0];
			String res = parseLine[1];
			int idx = i+1;
			buffer.append("<tr>");
			buffer.append("<td>" + idx + "</td>");
			buffer.append("<td>" + act + "</td>");
			buffer.append("<td>" + res + "</td>");
			buffer.append("<td>" + rule.getFreq() + "</td>");
			buffer.append("<td>" + rule.getMinSupp() + "</td>");
			buffer.append("<td>" + rule.getMinConf() + "</td>");
			buffer.append("</tr>");
		}
		buffer.append("</table>");
		
		
		/*
		 * Resource, Relation
		 * */
		/*
		buffer.append("<p> ### Resource, Relation ### </p>");
		buffer.append("<table>");
		buffer.append("<tr>");
		buffer.append("<th> No. </th>");
		buffer.append("<th> From Resource </th>");
		buffer.append("<th> To Resource </th>");
		buffer.append("<th> Relation Type </th>");
		buffer.append("<th> Frequency </th>");
		buffer.append("<th> Support </th>");
		buffer.append("<th> Confidence </th>");
		buffer.append("</tr>");
		rRuleSize = relModel.getRelationResourceMatrix().getRelationMatrixListSize();
		for(int i = 0; i < rRuleSize; i++) {
			int idx = i+1;
			buffer.append("<tr>");
			buffer.append("<td>" + idx + "</td>");
			buffer.append("<td>" + relModel.getRelationResourceMatrix().getAntecedent(i) + "</td>");
			buffer.append("<td>" + relModel.getRelationResourceMatrix().getConsequent(i) + "</td>");
			buffer.append("<td>" + relModel.getRelationResourceMatrix().getRelationType(i) + "</td>");
			buffer.append("<td>" + relModel.getRelationResourceMatrix().getFrequency(i) + "</td>");
			buffer.append("<td>" + relModel.getRelationResourceMatrix().getSupport(i) + "</td>");
			buffer.append("<td>" + relModel.getRelationResourceMatrix().getConfidence(i) + "</td>");
			buffer.append("</tr>");
		}
		buffer.append("</table>");
		*/
		/*
		 * VISUALIZE RESOURCE PERSPECTIVE RULES 
		 * */
		/*
		buffer.append("<p> ### Resource Perspective Rules ### </p>");
		buffer.append("<table>");
		buffer.append("<tr>");
		buffer.append("<th> From Resource </th>");
		buffer.append("<th> To Resource </th>");
		buffer.append("<th> Relation Type </th>");
		buffer.append("<th> Frequency </th>");
		buffer.append("<th> Support </th>");
		buffer.append("<th> Confidence </th>");
		buffer.append("</tr>");
		int rRuleSize = relModel.getRelationResourceMatrix().getRelationMatrixListSize();
		for(int i = 0; i < rRuleSize; i++) {
			buffer.append("<tr>");
			buffer.append("<td>" + relModel.getRelationResourceMatrix().getAntecedent(i) + "</td>");
			buffer.append("<td>" + relModel.getRelationResourceMatrix().getConsequent(i) + "</td>");
			buffer.append("<td>" + relModel.getRelationResourceMatrix().getRelationType(i) + "</td>");
			buffer.append("<td>" + relModel.getRelationResourceMatrix().getFrequency(i) + "</td>");
			buffer.append("<td>" + relModel.getRelationResourceMatrix().getSupport(i) + "</td>");
			buffer.append("<td>" + relModel.getRelationResourceMatrix().getConfidence(i) + "</td>");
			buffer.append("</tr>");
		}
		buffer.append("</table>");
		*/
		if (includeHTMLTags) {
			buffer.append("</html>");
		}
		return buffer.toString();
	}
	
	public ProcessingProfileModel getProcessingModel() {
		return processingModel;
	}

	public void setProcessingModel(ProcessingProfileModel processingModel) {
		this.processingModel = processingModel;
	}

	public TransitionProfileModel getTransitionModel() {
		return transitionModel;
	}

	public void setTransitionModel(TransitionProfileModel transitionModel) {
		this.transitionModel = transitionModel;
	}

	public OverlapProfileModel getOverlapModel() {
		return overlapModel;
	}

	public void setOverlapModel(OverlapProfileModel overlapModel) {
		this.overlapModel = overlapModel;
	}

	public TrueXProfileModel getTrueXModel() {
		return trueXModel;
	}

	public void setTrueXModel(TrueXProfileModel trueXModel) {
		this.trueXModel = trueXModel;
	}

	public TrueYProfileModel getTrueYModel() {
		return trueYModel;
	}

	public void setTrueYModel(TrueYProfileModel trueYModel) {
		this.trueYModel = trueYModel;
	}

	public ActivityModel getActModel() {
		return actModel;
	}

	public void setActModel(ActivityModel actModel) {
		this.actModel = actModel;
	}

	public RelationModel getRelModel() {
		return relModel;
	}

	public void setRelModel(RelationModel relModel) {
		this.relModel = relModel;
	}


	public RelationProfileModel getPerformanceModel() {
		return performanceModel;
	}


	public void setPerformanceModel(RelationProfileModel performanceModel) {
		this.performanceModel = performanceModel;
	}


	public ActivityResourceModel getActResModel() {
		return actResModel;
	}


	public void setActResModel(ActivityResourceModel actResModel) {
		this.actResModel = actResModel;
	}
}
