package org.processmining.models.anomaly.profile;

import java.text.ParseException;

import org.processmining.models.activity.ActivityModel;
import org.processmining.models.relation.RelationModel;

public class AnomalyProfileModel {
	//private ActivityModel actModel = null;
	//private RelationModel relModel = null;
	
	
	
	public AnomalyProfileModel(ActivityModel actModel, RelationModel relModel) throws ParseException {
		//this.actModel = actModel;
		//this.relModel = relModel;
		
		ProcessingProfileModel processingModel = new ProcessingProfileModel(actModel);
		processingModel.printProcessingProfile();
		
		TransitionProfileModel transitionModel = new TransitionProfileModel(relModel);
		transitionModel.printTransitionProfile();
		
		OverlapProfileModel overlapModel = new OverlapProfileModel(relModel);
		overlapModel.printOverlapProfile();
		
		//TrueXProfileModel trueXModel = new TrueXProfileModel(relModel);
		//trueXModel.printTrueXProfile();
		
		//TrueYProfileModel trueYModel = new TrueYProfileModel(relModel);
		//trueYModel.printTrueYProfile();
	}
	
}
