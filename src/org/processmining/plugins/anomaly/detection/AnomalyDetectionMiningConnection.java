package org.processmining.plugins.anomaly.detection;

import org.deckfour.xes.model.XLog;
import org.processmining.models.anomaly.detection.AnomalyDetectionModel;
import org.processmining.models.anomaly.detection.connections.AbstractAnomalyDetectionModelConnection;
import org.processmining.models.anomaly.profile.AnomalyProfileModel;

public class AnomalyDetectionMiningConnection extends AbstractAnomalyDetectionModelConnection<AnomalyDetectionMiningParameters> {
	/**
	 * Creates the connection between the log, model, and parameters.
	 * @param log The given event log.
	 * @param model The given workshop model.
	 * @param parameters The given conversion parameters.
	 */
	public AnomalyDetectionMiningConnection(
			XLog log, 
			AnomalyProfileModel profileModel,
			AnomalyDetectionModel model, 
			AnomalyDetectionMiningParameters parameters) {
		super(log, profileModel, model, parameters);
	}
}
