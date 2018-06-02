package org.processmining.plugins.anomaly.mutator;

import org.deckfour.xes.model.XLog;
import org.processmining.models.anomaly.mutator.connections.MutatorModelConnection;

public class MutatorConnection extends MutatorModelConnection<MutatorParameters> {
	/**
	 * Creates the connection between the log, model, and parameters.
	 * @param log The given event log.
	 * @param model The given workshop model.
	 * @param parameters The given conversion parameters.
	 */
	public MutatorConnection(XLog log, MutatorParameters parameters) {
		super(log, parameters);
	}
}
