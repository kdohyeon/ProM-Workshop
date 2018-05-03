package org.processmining.models.anomaly.profile;

import org.processmining.models.activity.ActivityModel;
import org.processmining.models.relation.RelationModel;

public class AnomalyProfileModel {
	private ActivityModel actModel = null;
	private RelationModel relModel = null;
	
	public AnomalyProfileModel(ActivityModel actModel, RelationModel relModel) {
		this.actModel = actModel;
		this.relModel = relModel;
	}
}
