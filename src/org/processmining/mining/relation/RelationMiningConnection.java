package org.processmining.mining.relation;

import org.deckfour.xes.model.XLog;
import org.processmining.models.relation.RelationModel;
import org.processmining.models.relation.connections.AbstractRelationModelConnection;

public class RelationMiningConnection extends AbstractRelationModelConnection<RelationMiningParameter> {
	/**
	 * Creates the connection between the log, model, and parameters.
	 * @param log The given event log.
	 * @param model The given workshop model.
	 * @param parameters The given conversion parameters.
	 */
	public RelationMiningConnection(XLog log, RelationModel model, RelationMiningParameter parameters) {
		super(log, model, parameters);
	}
}
