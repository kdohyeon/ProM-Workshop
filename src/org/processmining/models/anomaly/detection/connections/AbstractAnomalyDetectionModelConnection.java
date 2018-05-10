package org.processmining.models.anomaly.detection.connections;

import org.deckfour.xes.model.XLog;
import org.processmining.framework.connections.impl.AbstractConnection;
import org.processmining.models.anomaly.detection.AnomalyDetectionModel;
import org.processmining.models.anomaly.profile.AnomalyProfileModel;

public class AbstractAnomalyDetectionModelConnection<Parameters> extends AbstractConnection {
	/**
	 * Label for the log end of the connection.
	 */
	public final static String LOG = "Log";
	/**
	 * Label for the model end of the connection.
	 */
	public final static String DETECTION_MODEL = "Detection Model";

	public final static String PROFILE_MODEL = "Profile Model";
	
	/**
	 * The parameters used to mine the model from the log.
	 */
	private Parameters parameters;

	/**
	 * Creates a connection from an event log to a workshop model, where the
	 * model is mined from the log using the given parameters.
	 * 
	 * @param log
	 *            The event log.
	 * @param model
	 *            The mined workshop model.
	 * @param parameters
	 *            The parameters used to mine the model from the log.
	 */
	public AbstractAnomalyDetectionModelConnection(
			XLog log,
			AnomalyProfileModel profileModel,
			AnomalyDetectionModel model, 
			Parameters parameters) {
		super("Anomaly Detection Model for log");
		put(LOG, log);
		put(PROFILE_MODEL, profileModel);
		put(DETECTION_MODEL, model);
		this.parameters = parameters;
	}

	/**
	 * Gets the parameters used to mine the workshop model from the event log.
	 * 
	 * @return The parameters used to derive the workshop model from the event
	 *         log.
	 */
	public Parameters getParameters() {
		return parameters;
	}
}
