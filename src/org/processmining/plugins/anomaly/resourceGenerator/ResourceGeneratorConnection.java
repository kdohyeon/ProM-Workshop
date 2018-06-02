package org.processmining.plugins.anomaly.resourceGenerator;

import org.deckfour.xes.model.XLog;
import org.processmining.models.anomaly.resourceGenerator.connections.AbstractResourceGeneratorModelConnection;

public class ResourceGeneratorConnection extends AbstractResourceGeneratorModelConnection<ResourceGeneratorParameters> {
	/**
	 * Creates the connection between the log, model, and parameters.
	 * @param log The given event log.
	 * @param model The given workshop model.
	 * @param parameters The given conversion parameters.
	 */
	public ResourceGeneratorConnection(XLog log, ResourceGeneratorParameters parameters) {
		super(log, parameters);
	}
}
