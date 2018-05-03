package org.processmining.mining.anomaly.profile;

import java.text.ParseException;
import java.util.Collection;

import org.deckfour.uitopia.api.event.TaskListener.InteractionResult;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.data.activity.Activity;
import org.processmining.data.relation.Relation;
import org.processmining.data.xes.XESAttributeDefinition;
import org.processmining.framework.connections.ConnectionCannotBeObtained;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.models.activity.ActivityModel;
import org.processmining.models.anomaly.profile.AnomalyProfileModel;
import org.processmining.models.relation.RelationModel;

@Plugin(name = "Anomaly Profile Miner", returnLabels = { "Anomaly Profile Model" }, returnTypes = { AnomalyProfileModel.class }, parameterLabels = {
		"Log", "Parameters" }, userAccessible = true)
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
		
		ActivityModel actModel = new ActivityModel();
		//ArrayList<Activity> actList = new ArrayList<Activity>();
		
		/*
		 * Get activity list
		 * */
		// for each case
		for(int i = 0; i < log.size(); i++) {
			// for each event
			String caseID = log.get(i).getAttributes().get("concept:name").toString();
			for(int j = 0; j < log.get(i).size(); j++) {
				
				// find the start and complete pair
				XESAttributeDefinition def = new XESAttributeDefinition();
				XAttributeMap map = log.get(i).get(j).getAttributes();
				
				int currJ = j;
				
				Activity act;
				if(map.get(def.getEVENT_TYPE()).toString().equals(def.getEVENT_TYPE_START())) {
					String activityID = map.get("concept:name").toString();
					String start_timestamp = map.get("time:timestamp").toString();
					String resource = map.get("org:resource").toString();
					
					String eventID = map.get(def.getEVENT_ID()).toString();
					//String eventType_complete = def.getEVENT_TYPE_COMPLETE();
					
					boolean isFound = false;
					while(!isFound) {
						XAttributeMap iterMap = log.get(i).get(currJ++).getAttributes();
						
						if(iterMap.get("EventID").toString().equals(eventID) && iterMap.get("EventType").toString().equals("complete")) {
							String complete_timestamp = iterMap.get(def.getTIMESTAMP()).toString();
							act = new Activity(
									caseID, activityID, resource,
									start_timestamp, complete_timestamp,
									eventID
									);

							actModel.addActivity(act);
							isFound = true;
						}
					}
				}				
			}
		}
		
		RelationModel relModel = new RelationModel();
		
		int actSize = actModel.getActivityCardinality();
		System.out.println("Activity Cardinality: " + actSize);
		
		for(int i = 0; i < actSize; i++) {
			System.out.println(
					actModel.getCase(i) 
					+ ", " + actModel.getActivityID(i) 
					+ ": " + actModel.getProcessingTime(i));
			
			String currCase = actModel.getCase(i);
			
			for(int j = i+1; j < actSize; j++) {
				String thisCase = actModel.getCase(j);
				if(currCase.equals(thisCase)) {
					try {
						Relation rel = new Relation(actModel.getActivity(i), actModel.getActivity(j));
						relModel.addRelation(rel);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			//System.out.println(actList.get(i).getCaseID() + ", " + actList.get(i).getActivityID() + ", " + actList.get(i).getStartTimestamp() + ", " + actList.get(i).getCompleteTimestamp());
		}
		
		int relListSize = relModel.getRelationCardinality();
		System.out.println("Case ID --- Ante, Cons: RelType, Trans, Overlap, TrueX, TrueY");
		for(int i = 0; i < relListSize; i++) {
			System.out.println(
					relModel.getCaseID(i)
					+ " --- " + relModel.getAntecedentActivity(i) 
					+ ", " + relModel.getConsequentActivity(i)
					+ ": " + relModel.getRelationType(i)
					+ ", " + relModel.getTransitionTime(i)
					+ ", " + relModel.getOverlapTime(i)
					+ ", " + relModel.getTrueXTime(i)
					+ ", " + relModel.getTrueYTime(i));
		}
		
		/*
		 * Anomaly Profile Model
		 * */
		
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
