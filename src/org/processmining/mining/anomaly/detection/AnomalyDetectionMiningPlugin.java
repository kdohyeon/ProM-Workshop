package org.processmining.mining.anomaly.detection;

import java.text.ParseException;
import java.util.Collection;

import org.deckfour.uitopia.api.event.TaskListener.InteractionResult;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.connections.ConnectionCannotBeObtained;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.models.anomaly.detection.AnomalyDetectionModel;
import org.processmining.models.anomaly.profile.AnomalyProfileModel;

@Plugin(
		name = "Anomaly Detection Miner", 
		returnLabels = { "Anomaly Detection Model" }, 
		returnTypes = { AnomalyDetectionModel.class }, 
		parameterLabels = {"Log", "Parameters" }, 
		userAccessible = true)
public class AnomalyDetectionMiningPlugin {
	
	@UITopiaVariant(affiliation = UITopiaVariant.POSTECH, author = "Dohyeon Kim", email = "kdohyeon@postech.ac.kr")
	@PluginVariant(variantLabel = "Mine a anomaly detection model, default", requiredParameterLabels = { 0, 1 })
	public AnomalyDetectionModel mineDefault(PluginContext context, XLog log, AnomalyProfileModel profileModel) throws ParseException {
		return mineParameters(context, log, profileModel, new AnomalyDetectionMiningParameters());
	}
	
	/**
	 * Mining using user-provided parameter values.
	 * 
	 * @param context
	 *            The given GUI-aware plug-in context.
	 * @param log
	 *            The given event log.
	 * @return The workshop model mined from the given log using the
	 *         user-provided parameter values.
	 * @throws ParseException 
	 */
	@UITopiaVariant(affiliation = UITopiaVariant.POSTECH, author = "Dohyeon Kim", email = "kdohyeon@postech.ac.kr")
	@PluginVariant(variantLabel = "Mine a anomaly detection model, dialog", requiredParameterLabels = { 0, 1 })
	public AnomalyDetectionModel mineDefault(UIPluginContext context, XLog log, AnomalyProfileModel profileModel) throws ParseException {
		AnomalyDetectionMiningParameters parameters = new AnomalyDetectionMiningParameters();
		AnomalyDetectionMiningDialog dialog = new AnomalyDetectionMiningDialog(log, profileModel, parameters);
		InteractionResult result = context.showWizard("Anomaly Detection Miner", true, true, dialog);
		if (result != InteractionResult.FINISHED) {
			return null;
		}
		return mineParameters(context, log, profileModel, parameters);
	}
	
	/**
	 * Mining using given parameter values.
	 * 
	 * @param context
	 *            The given plug-in context.
	 * @param log
	 *            The given event log.
	 * @param parameters
	 *            The given parameter values.
	 * @return The workshop model mined from the given log using the given
	 *         parameter values.
	 * @throws ParseException 
	 */
	@UITopiaVariant(affiliation = UITopiaVariant.POSTECH, author = "Dohyeon Kim", email = "kdohyeon@postech.ac.kr")
	@PluginVariant(variantLabel = "Mine a anomaly detection model, parameterized", requiredParameterLabels = { 0, 1 })
	public AnomalyDetectionModel mineParameters(PluginContext context, XLog log, AnomalyProfileModel profileModel, AnomalyDetectionMiningParameters parameters) throws ParseException {
		Collection<AnomalyDetectionMiningConnection> connections;
		try {
			connections = context.getConnectionManager().getConnections(AnomalyDetectionMiningConnection.class, context, log, profileModel);
			for (AnomalyDetectionMiningConnection connection : connections) {
				if (connection.getObjectWithRole(AnomalyDetectionMiningConnection.LOG).equals(log)
						&& connection.getParameters().equals(parameters)) {
					return connection.getObjectWithRole(AnomalyDetectionMiningConnection.DETECTION_MODEL);
				}
			}
		} catch (ConnectionCannotBeObtained e) {
		}
		AnomalyDetectionModel model = mine(context, log, profileModel, parameters);
		context.addConnection(new AnomalyDetectionMiningConnection(log, profileModel, model, parameters));
		return model;
	}
	
	/*
	 * The actual mining of an event log for a workshop model given parameter
	 * values.
	 */
	private AnomalyDetectionModel mine(
			PluginContext context, 
			XLog log,
			AnomalyProfileModel profileModel,
			AnomalyDetectionMiningParameters parameters) throws ParseException {
		/*
		 * Create event classes based on the given classifier.
		 */
		XLogInfo info = XLogInfoFactory.createLogInfo(log, parameters.getClassifier());
		/*
		 * Create an empty model.
		 */
		
		/*
		 * Inform the progress bar when we're done.
		 */
		context.getProgress().setMaximum(log.size());
		
		AnomalyDetectionModel anomalyDetectionModel = new AnomalyDetectionModel(log, profileModel);
		
		/* 
		 * Advance the progress bar.
		 */
		// context.getProgress().inc();
		

		/*
		 * Return the model.
		 */
		return anomalyDetectionModel;
	}
}
