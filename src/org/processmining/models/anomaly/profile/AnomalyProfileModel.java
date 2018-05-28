package org.processmining.models.anomaly.profile;

import java.text.ParseException;

import org.processmining.framework.util.HTMLToString;
import org.processmining.models.activity.ActivityModel;
import org.processmining.models.relation.RelationModel;

public class AnomalyProfileModel implements HTMLToString{
	private ActivityModel actModel = null;
	private RelationModel relModel = null;
	
	ProcessingProfileModel processingModel;
	RelationProfileModel performanceModel;
	TransitionProfileModel transitionModel;
	OverlapProfileModel overlapModel;
	TrueXProfileModel trueXModel;
	TrueYProfileModel trueYModel;
	
	public AnomalyProfileModel(ActivityModel actModel, RelationModel relModel) throws ParseException {
		this.setActModel(actModel);
		this.setRelModel(relModel);
		
		ProcessingProfileModel processingModel = new ProcessingProfileModel(actModel);
		//processingModel.printProcessingProfile();
		setProcessingModel(processingModel);
		
		RelationProfileModel performnaceModel = new RelationProfileModel(relModel);
		setPerformanceModel(performnaceModel);
		
		TransitionProfileModel transitionModel = new TransitionProfileModel(relModel);
		//transitionModel.printTransitionProfile();
		setTransitionModel(transitionModel);
		
		OverlapProfileModel overlapModel = new OverlapProfileModel(relModel);
		//overlapModel.printOverlapProfile();
		setOverlapModel(overlapModel);
		
		TrueXProfileModel trueXModel = new TrueXProfileModel(relModel);
		//trueXModel.printTrueXProfile();
		setTrueXModel(trueXModel);
		
		TrueYProfileModel trueYModel = new TrueYProfileModel(relModel);
		//trueYModel.printTrueYProfile();
		setTrueYModel(trueYModel);
	}
	
	
	public String toHTMLString(boolean includeHTMLTags) {
		StringBuffer buffer = new StringBuffer();
		if (includeHTMLTags) {
			buffer.append("<html>");
		}
		
		/*
		 * VISUALIZE PROCESSING PROFILE 
		 * */
		buffer.append("<p> ### Processing Profile ### </p>");
		buffer.append("<table>");
		buffer.append("<tr>");
		buffer.append("<th> Activity ID </th>");
		buffer.append("<th> Resource ID </th>");
		buffer.append("<th> Frequency </th>");
		buffer.append("<th> Average </th>");
		buffer.append("<th> Standard Deviation </th>");
		buffer.append("</tr>");
		
		int processingListSize = processingModel.getProcessingList_Act_Res_Size();
		for(int i = 0; i < processingListSize; i++) {
			buffer.append("<tr>");
			buffer.append("<td>" + processingModel.getProcessingList_Act_Res().get(i).getActivityID() + "</td>");
			buffer.append("<td>" + processingModel.getProcessingList_Act_Res().get(i).getResourceID() + "</td>");
			buffer.append("<td>" + processingModel.getProcessingList_Act_Res().get(i).getFreq() + "</td>");
			buffer.append("<td>" + processingModel.getProcessingList_Act_Res().get(i).getAvg() + "</td>");
			buffer.append("<td>" + processingModel.getProcessingList_Act_Res().get(i).getStdev() + "</td>");
			buffer.append("</tr>");
		}
		buffer.append("</table>");
		
		/*
		 * VISUALIZE TRANSITION PROFILE 
		 * */
		buffer.append("<p> ### Transition Profile ### </p>");
		buffer.append("<table>");
		buffer.append("<tr>");
		buffer.append("<th> From Activity ID </th>");
		buffer.append("<th> To Activity ID </th>");
		buffer.append("<th> From Resource ID </th>");
		buffer.append("<th> To Resource ID </th>");
		buffer.append("<th> Relation </th>");
		buffer.append("<th> Frequency </th>");
		buffer.append("<th> Average </th>");
		buffer.append("<th> Standard Deviation </th>");
		buffer.append("</tr>");
		
		int transitionListSize = transitionModel.getTransitionList_Act_Res_Size();
		for(int i = 0; i < transitionListSize; i++) {
			buffer.append("<tr>");
			buffer.append("<td>" + transitionModel.getTransitionList_Act_Res().get(i).getFromActivityID() + "</td>");
			buffer.append("<td>" + transitionModel.getTransitionList_Act_Res().get(i).getToActivityID() + "</td>");
			buffer.append("<td>" + transitionModel.getTransitionList_Act_Res().get(i).getFromResourceID() + "</td>");
			buffer.append("<td>" + transitionModel.getTransitionList_Act_Res().get(i).getToResourceID() + "</td>");
			buffer.append("<td>" + transitionModel.getTransitionList_Act_Res().get(i).getRelation() + "</td>");
			buffer.append("<td>" + transitionModel.getTransitionList_Act_Res().get(i).getFreq() + "</td>");
			buffer.append("<td>" + transitionModel.getTransitionList_Act_Res().get(i).getAvg() + "</td>");
			buffer.append("<td>" + transitionModel.getTransitionList_Act_Res().get(i).getStdev() + "</td>");
			buffer.append("</tr>");
		}
		buffer.append("</table>");
		
		/*
		 * VISUALIZE OVERLAP PROFILE 
		 * */
		buffer.append("<p> ### Overlap Profile ### </p>");
		buffer.append("<table>");
		buffer.append("<tr>");
		buffer.append("<th> From Activity ID </th>");
		buffer.append("<th> To Activity ID </th>");
		buffer.append("<th> From Resource ID </th>");
		buffer.append("<th> To Resource ID </th>");
		buffer.append("<th> Relation </th>");
		buffer.append("<th> Frequency </th>");
		buffer.append("<th> Average </th>");
		buffer.append("<th> Standard Deviation </th>");
		buffer.append("</tr>");
		int overlapListSize = overlapModel.getOverlapList_Act_Res_Size();
		for(int i = 0; i < overlapListSize; i++) {
			buffer.append("<tr>");
			buffer.append("<td>" + overlapModel.getOverlapList_Act_Res().get(i).getFromActivityID() + "</td>");
			buffer.append("<td>" + overlapModel.getOverlapList_Act_Res().get(i).getToActivityID() + "</td>");
			buffer.append("<td>" + overlapModel.getOverlapList_Act_Res().get(i).getFromResourceID() + "</td>");
			buffer.append("<td>" + overlapModel.getOverlapList_Act_Res().get(i).getToResourceID() + "</td>");
			buffer.append("<td>" + overlapModel.getOverlapList_Act_Res().get(i).getRelation() + "</td>");
			buffer.append("<td>" + overlapModel.getOverlapList_Act_Res().get(i).getFreq() + "</td>");
			buffer.append("<td>" + overlapModel.getOverlapList_Act_Res().get(i).getAvg() + "</td>");
			buffer.append("<td>" + overlapModel.getOverlapList_Act_Res().get(i).getStdev() + "</td>");
			buffer.append("</tr>");
		}
		buffer.append("</table>");
		
		/*
		 * VISUALIZE TRUEX PROFILE 
		 * */
		buffer.append("<p> ### TrueX Profile ### </p>");
		buffer.append("<table>");
		buffer.append("<tr>");
		buffer.append("<th> From Activity ID </th>");
		buffer.append("<th> To Activity ID </th>");
		buffer.append("<th> From Resource ID </th>");
		buffer.append("<th> To Resource ID </th>");
		buffer.append("<th> Relation </th>");
		buffer.append("<th> Frequency </th>");
		buffer.append("<th> Average </th>");
		buffer.append("<th> Standard Deviation </th>");
		buffer.append("</tr>");
		int trueXListSize = trueXModel.getTrueXList_Act_Res_Size();
		for(int i = 0; i < trueXListSize; i++) {
			buffer.append("<tr>");
			buffer.append("<td>" + trueXModel.getTrueXList_Act_Res().get(i).getFromActivityID() + "</td>");
			buffer.append("<td>" + trueXModel.getTrueXList_Act_Res().get(i).getToActivityID() + "</td>");
			buffer.append("<td>" + trueXModel.getTrueXList_Act_Res().get(i).getFromResourceID() + "</td>");
			buffer.append("<td>" + trueXModel.getTrueXList_Act_Res().get(i).getToResourceID() + "</td>");
			buffer.append("<td>" + trueXModel.getTrueXList_Act_Res().get(i).getRelation() + "</td>");
			buffer.append("<td>" + trueXModel.getTrueXList_Act_Res().get(i).getFreq() + "</td>");
			buffer.append("<td>" + trueXModel.getTrueXList_Act_Res().get(i).getAvg() + "</td>");
			buffer.append("<td>" + trueXModel.getTrueXList_Act_Res().get(i).getStdev() + "</td>");
			buffer.append("</tr>");
		}
		buffer.append("</table>");
		
		/*
		 * VISUALIZE TRUEY PROFILE 
		 * */
		buffer.append("<p> ### TrueY Profile ### </p>");
		buffer.append("<table>");
		buffer.append("<tr>");
		buffer.append("<th> From Activity ID </th>");
		buffer.append("<th> To Activity ID </th>");
		buffer.append("<th> From Resource ID </th>");
		buffer.append("<th> To Resource ID </th>");
		buffer.append("<th> Relation </th>");
		buffer.append("<th> Frequency </th>");
		buffer.append("<th> Average </th>");
		buffer.append("<th> Standard Deviation </th>");
		buffer.append("</tr>");
		int trueYListSize = trueYModel.getTrueYList_Act_Res_Size();
		for(int i = 0; i < trueYListSize; i++) {
			buffer.append("<tr>");
			buffer.append("<td>" + trueYModel.getTrueYList_Act_Res().get(i).getFromActivityID() + "</td>");
			buffer.append("<td>" + trueYModel.getTrueYList_Act_Res().get(i).getToActivityID() + "</td>");
			buffer.append("<td>" + trueYModel.getTrueYList_Act_Res().get(i).getFromResourceID() + "</td>");
			buffer.append("<td>" + trueYModel.getTrueYList_Act_Res().get(i).getToResourceID() + "</td>");
			buffer.append("<td>" + trueYModel.getTrueYList_Act_Res().get(i).getRelation() + "</td>");
			buffer.append("<td>" + trueYModel.getTrueYList_Act_Res().get(i).getFreq() + "</td>");
			buffer.append("<td>" + trueYModel.getTrueYList_Act_Res().get(i).getAvg() + "</td>");
			buffer.append("<td>" + trueYModel.getTrueYList_Act_Res().get(i).getStdev() + "</td>");
			buffer.append("</tr>");
		}
		buffer.append("</table>");
		
		/*
		 * VISUALIZE CONTROL-FLOW PERSPECTIVE RULES 
		 * */
		buffer.append("<p> ### Control-flow Perspective Rules ### </p>");
		buffer.append("<table>");
		buffer.append("<tr>");
		buffer.append("<th> From Activity </th>");
		buffer.append("<th> To Activity </th>");
		buffer.append("<th> Relation Type </th>");
		buffer.append("<th> Frequency </th>");
		buffer.append("<th> Support </th>");
		buffer.append("<th> Confidence </th>");
		buffer.append("</tr>");
		int cfRuleSize = relModel.getRelationActivityMatrix().getRelationMatrixListSize();
		for(int i = 0; i < cfRuleSize; i++) {
			buffer.append("<tr>");
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
		 * VISUALIZE RESOURCE PERSPECTIVE RULES 
		 * */
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
}
