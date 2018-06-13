package org.processmining.plugins.anomaly.profile;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;

import org.deckfour.uitopia.api.event.TaskListener.InteractionResult;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.data.overlap.Overlap;
import org.processmining.data.relation.RelationMatrix;
import org.processmining.data.transition.Transition;
import org.processmining.data.trueX.TrueX;
import org.processmining.data.trueY.TrueY;
import org.processmining.framework.connections.ConnectionCannotBeObtained;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.models.activity.ActivityModel;
import org.processmining.models.activityResource.ActivityResourceModel;
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
		 * Create an empty model.
		 */
		
		/*
		 * Inform the progress bar when we're done.
		 */
		context.getProgress().setMaximum(log.size());
		
		/*
		 * GET PARAMETERS
		 * */
		
		
		/*
		 * Get Control-flow rules
		 * */
		// Activity rules
		System.out.println("...Control-flow, Activity Rules...");
		ActivityModel actModel = new ActivityModel(log);
		actModel.setMinSupp(parameters.getMinSupport());
		actModel.setMinConf(parameters.getMinConfidence());
		actModel.calculateActivityRule();
		
		/*
		for(int i = 0; i < actModel.getActivityRule().getRuleSize(); i++) {
			System.out.println(
					actModel.getActivityRule().getRuleList().get(i).getAct()
					+ ", " + actModel.getActivityRule().getRuleList().get(i).getMinSupp()
					+ ", " + actModel.getActivityRule().getRuleList().get(i).getMinConf());
		}
		*/
		
		// Relation rules
		System.out.println("...Control-flow, Relation Rules...");
		System.out.println("...Modelling...");
		RelationModel relModel = new RelationModel(actModel);
		relModel.setMinSupp(parameters.getMinSupport());
		relModel.setMinConf(parameters.getMinConfidence());
		System.out.println("...Calculate Activity Relation Matrix...");
		relModel.calculateRelationActivityMatrix();
		System.out.println("...Calculate Resource Relation Matrix...");
		relModel.calculateRelationResourceMatrix();
		RelationMatrix activityRelMatrix = new RelationMatrix();
		System.out.println("...Activity Relation Matrix...");
		activityRelMatrix = relModel.getRelationActivityMatrix();
		
		
		//activityRelMatrix.printRelationMatrix();
		
		// Activity-Resource frequency
		System.out.println("...Activity-Resource...");
		ActivityResourceModel actResModel = new ActivityResourceModel(log, parameters);
		
		/*
		 * Get time rules
		 * */
		// get anomaly profile model
		AnomalyProfileModel anomalyProfileModel = new AnomalyProfileModel(actModel, relModel, actResModel);
				
		// Activity time range
		System.out.println("Time, Activity Time Ranges");
		anomalyProfileModel.getProcessingModel().getProcessingList_Act();
		/*
		for(int i = 0; i < anomalyProfileModel.getProcessingModel().getProcessingList_Act().size(); i++) {
			System.out.println(
					anomalyProfileModel.getProcessingModel().getProcessingList_Act().get(i).getActivityID()
					+ ": " + anomalyProfileModel.getProcessingModel().getProcessingList_Act().get(i).getAvg()
					+ " +/- " + anomalyProfileModel.getProcessingModel().getProcessingList_Act().get(i).getStdev());
		}
		*/
		
		// Relation time range
		System.out.println("Time, Relation Time Ranges - Transition");
		ArrayList<Transition> transitionTime = new ArrayList<Transition>();
		transitionTime = anomalyProfileModel.getTransitionModel().getTransitionList_Act();
		/*
		for(int i = 0; i < transitionTime.size(); i++) {
			System.out.println(
					transitionTime.get(i).getFromActivityID() 
					+ " " + transitionTime.get(i).getRelation() 
					+ " " + transitionTime.get(i).getToActivityID()
					+ ": " + transitionTime.get(i).getAvg() + " +/- " + transitionTime.get(i).getStdev());
		}
		*/
		
		System.out.println("Time, Relation Time Ranges - Overlap");
		ArrayList<Overlap> overlapTime = new ArrayList<Overlap>();
		overlapTime = anomalyProfileModel.getOverlapModel().getOverlapList_Act();
		/*
		for(int i = 0; i < overlapTime.size(); i++) {
			System.out.println(
					overlapTime.get(i).getFromActivityID() 
					+ " " + overlapTime.get(i).getRelation() 
					+ " " + overlapTime.get(i).getToActivityID()
					+ ": " + overlapTime.get(i).getAvg() + " +/- " + overlapTime.get(i).getStdev());
		}
		*/
		
		System.out.println("Time, Relation Time Ranges - TrueX");
		ArrayList<TrueX> trueXTime = new ArrayList<TrueX>();
		trueXTime = anomalyProfileModel.getTrueXModel().getTrueXList_Act();
		/*
		for(int i = 0; i < trueXTime.size(); i++) {
			System.out.println(
					trueXTime.get(i).getFromActivityID() 
					+ " " + trueXTime.get(i).getRelation() 
					+ " " + trueXTime.get(i).getToActivityID()
					+ ": " + trueXTime.get(i).getAvg() + " +/- " + trueXTime.get(i).getStdev());
		}
		*/
		
		System.out.println("Time, Relation Time Ranges - TrueY");
		ArrayList<TrueY> trueYTime = new ArrayList<TrueY>();
		trueYTime = anomalyProfileModel.getTrueYModel().getTrueYList_Act();
		/*
		for(int i = 0; i < trueYTime.size(); i++) {
			System.out.println(
					trueYTime.get(i).getFromActivityID() 
					+ " " + trueYTime.get(i).getRelation() 
					+ " " + trueYTime.get(i).getToActivityID()
					+ ": " + trueYTime.get(i).getAvg() + " +/- " + trueYTime.get(i).getStdev());
		}
		*/
		
		/*
		 * Get resource rules
		 * */
		System.out.println("Resource, Activity-Resource Rules");
		System.out.println("...Activity-Side Rules...");
		actResModel.printActivitySideRules();
		System.out.println("...Resource-Side Rules...");
		actResModel.printResourceSideRules();
		
		/* 
		 * Advance the progress bar.
		 */
		context.getProgress().inc();
		

		/*
		 * Return the model.
		 */
		return anomalyProfileModel;
	}
}
