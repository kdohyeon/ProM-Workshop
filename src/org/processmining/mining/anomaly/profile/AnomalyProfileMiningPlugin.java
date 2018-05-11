package org.processmining.mining.anomaly.profile;

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
import org.processmining.models.activity.ActivityModel;
import org.processmining.models.anomaly.profile.AnomalyProfileModel;
import org.processmining.models.relation.RelationModel;

@Plugin(
		name = "Anomaly Profile Miner", 
		returnLabels = { "Anomaly Profile Model" }, 
		returnTypes = { AnomalyProfileModel.class }, 
		parameterLabels = {"Log", "Parameters" }, 
		userAccessible = true)
public class AnomalyProfileMiningPlugin {
	/**
	 * Mining using default parameter values.
	 * 
	 * @param context
	 *            The given plug-in context.
	 * @param log
	 *            The given event log.
	 * @return The workshop model mined from the given log using the default
	 *         parameter values.
	 * @throws ParseException 
	 */
	@UITopiaVariant(affiliation = UITopiaVariant.POSTECH, author = "Dohyeon Kim", email = "kdohyeon@postech.ac.kr")
	@PluginVariant(variantLabel = "Mine a anomaly profile model, default", requiredParameterLabels = { 0 })
	public AnomalyProfileModel mineDefault(PluginContext context, XLog log) throws ParseException {
		return mineParameters(context, log, new AnomalyProfileMiningParameters());
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
	@PluginVariant(variantLabel = "Mine a anomaly profile model, dialog", requiredParameterLabels = { 0 })
	public AnomalyProfileModel mineDefault(UIPluginContext context, XLog log) throws ParseException {
		AnomalyProfileMiningParameters parameters = new AnomalyProfileMiningParameters();
		AnomalyProfileMiningDialog dialog = new AnomalyProfileMiningDialog(log, parameters);
		InteractionResult result = context.showWizard("Anomaly Profile Miner", true, true, dialog);
		if (result != InteractionResult.FINISHED) {
			return null;
		}
		return mineParameters(context, log, parameters);
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
	@PluginVariant(variantLabel = "Mine a anomaly profile model, parameterized", requiredParameterLabels = { 0, 1 })
	public AnomalyProfileModel mineParameters(PluginContext context, XLog log, AnomalyProfileMiningParameters parameters) throws ParseException {
		Collection<AnomalyProfileMiningConnection> connections;
		try {
			connections = context.getConnectionManager().getConnections(AnomalyProfileMiningConnection.class, context, log);
			for (AnomalyProfileMiningConnection connection : connections) {
				if (connection.getObjectWithRole(AnomalyProfileMiningConnection.LOG).equals(log)
						&& connection.getParameters().equals(parameters)) {
					return connection.getObjectWithRole(AnomalyProfileMiningConnection.MODEL);
				}
			}
		} catch (ConnectionCannotBeObtained e) {
		}
		AnomalyProfileModel model = mine(context, log, parameters);
		context.addConnection(new AnomalyProfileMiningConnection(log, model, parameters));
		return model;
	}
	
	/*
	 * The actual mining of an event log for a workshop model given parameter
	 * values.
	 */
	private AnomalyProfileModel mine(PluginContext context, XLog log, AnomalyProfileMiningParameters parameters) throws ParseException {
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
		
		/*
		 * GET ACTIVITY MODEL
		 * */
		ActivityModel actModel = new ActivityModel(log);
		
		/*
		 * GET RELATION MODEL
		 * */
		
		RelationModel relModel = new RelationModel(actModel);
		
		
		/*
		 * GET ANOMALY PROFILE MODEL - ACTIVITY & RESOURCE
		 * */
		
		System.out.println("### ACTIVITY MATRIX ###");
		relModel.getRelationActivityMatrix();
		System.out.println("### RESOURCE MATRIX ###");
		relModel.getRelationResourceMatrix();
		AnomalyProfileModel anomalyProfileModel = new AnomalyProfileModel(actModel, relModel);
		
		
		
		/* 
		 * Advance the progress bar.
		 */
		// context.getProgress().inc();
		

		/*
		 * Return the model.
		 */
		return anomalyProfileModel;
	}
}
