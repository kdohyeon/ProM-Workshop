package org.processmining.plugins.anomaly.randomGenerator;

import org.deckfour.xes.model.XLog;
import org.processmining.models.anomaly.randomLogGenerator.connections.RandomLogGeneratorModelConnection;

public class RandomLogGeneratorConnection extends RandomLogGeneratorModelConnection<RandomLogGeneratorParameters> {
	/**
	 * Creates the connection between the log, model, and parameters.
	 * @param log The given event log.
	 * @param model The given workshop model.
	 * @param parameters The given conversion parameters.
	 */
	public RandomLogGeneratorConnection(XLog log, RandomLogGeneratorParameters parameters) {
		super(log, parameters);
	}
}
