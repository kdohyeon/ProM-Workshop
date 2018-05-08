package org.processmining.models.anomaly.profile;

import java.text.ParseException;

import org.processmining.models.activity.ActivityModel;
import org.processmining.models.relation.RelationModel;

public class AnomalyProfileModel {
	//private ActivityModel actModel = null;
	//private RelationModel relModel = null;
	
	ProcessingProfileModel processingModel;
	TransitionProfileModel transitionModel;
	OverlapProfileModel overlapModel;
	TrueXProfileModel trueXModel;
	TrueYProfileModel trueYModel;
	
	public AnomalyProfileModel(ActivityModel actModel, RelationModel relModel) throws ParseException {
		//this.actModel = actModel;
		//this.relModel = relModel;
		
		ProcessingProfileModel processingModel = new ProcessingProfileModel(actModel);
		processingModel.printProcessingProfile();
		setProcessingModel(processingModel);
		
		TransitionProfileModel transitionModel = new TransitionProfileModel(relModel);
		transitionModel.printTransitionProfile();
		setTransitionModel(transitionModel);
		
		OverlapProfileModel overlapModel = new OverlapProfileModel(relModel);
		overlapModel.printOverlapProfile();
		setOverlapModel(overlapModel);
		
		TrueXProfileModel trueXModel = new TrueXProfileModel(relModel);
		trueXModel.printTrueXProfile();
		setTrueXModel(trueXModel);
		
		TrueYProfileModel trueYModel = new TrueYProfileModel(relModel);
		trueYModel.printTrueYProfile();
		setTrueYModel(trueYModel);
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
}
