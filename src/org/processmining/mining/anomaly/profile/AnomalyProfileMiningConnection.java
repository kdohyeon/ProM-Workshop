package org.processmining.mining.anomaly.profile;

import org.deckfour.xes.model.XLog;
import org.processmining.models.anomaly.profile.AnomalyProfileModel;
import org.processmining.models.anomaly.profile.connections.AbstractAnomalyProfileModelConnection;

public class AnomalyProfileMiningConnection extends AbstractAnomalyProfileModelConnection<AnomalyProfileMiningParameters> {
	/**
	 * Creates the connection between the log, model, and parameters.
	 * @param log The given event log.
	 * @param model The given workshop model.
	 * @param parameters The given conversion parameters.
	 */
	public AnomalyProfileMiningConnection(XLog log, AnomalyProfileModel model, AnomalyProfileMiningParameters parameters) {
		super(log, model, parameters);
	}
}
