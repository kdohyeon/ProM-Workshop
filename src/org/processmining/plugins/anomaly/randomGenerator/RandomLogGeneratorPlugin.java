package org.processmining.plugins.anomaly.randomGenerator;

import java.text.ParseException;
import java.util.Collection;

import org.deckfour.uitopia.api.event.TaskListener.InteractionResult;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.connections.ConnectionCannotBeObtained;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.models.anomaly.randomLogGenerator.RandomLogGeneratorModel;

@Plugin(
		name = "Random Log Generator", 
		returnLabels = { "XLog" }, 
		returnTypes = { XLog.class }, 
		parameterLabels = {"Log", "Parameters" }, 
		userAccessible = true)
public class RandomLogGeneratorPlugin {
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
	@UITopiaVariant(
			affiliation = UITopiaVariant.POSTECH, 
			author = "Dohyeon Kim", 
			email = "kdohyeon@postech.ac.kr")
	@PluginVariant(
			variantLabel = "Random Log Generator, default", 
			requiredParameterLabels = { 0 })
	public XLog mineDefault(PluginContext context, XLog log) throws ParseException {
		return mineParameters(context, log, new RandomLogGeneratorParameters());
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
	@UITopiaVariant(
			affiliation = UITopiaVariant.POSTECH, 
			author = "Dohyeon Kim", 
			email = "kdohyeon@postech.ac.kr")
	@PluginVariant(
			variantLabel = "Random Log Generator, dialog", 
			requiredParameterLabels = { 0 })
	public XLog mineDefault(UIPluginContext context, XLog log) throws ParseException {
		RandomLogGeneratorParameters parameters = new RandomLogGeneratorParameters();
		RandomLogGeneratorDialog dialog = new RandomLogGeneratorDialog(log, parameters);
		InteractionResult result = context.showWizard("Random Log Generator", true, true, dialog);
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
	@UITopiaVariant(
			affiliation = UITopiaVariant.POSTECH, 
			author = "Dohyeon Kim", 
			email = "kdohyeon@postech.ac.kr")
	@PluginVariant(
			variantLabel = "Random Log Generator, parameterized", 
			requiredParameterLabels = { 0, 1 })
	public XLog mineParameters(PluginContext context, XLog log, RandomLogGeneratorParameters parameters) throws ParseException {
		Collection<RandomLogGeneratorConnection> connections;
		try {
			connections = context.getConnectionManager().getConnections(RandomLogGeneratorConnection.class, context, log);
			for (RandomLogGeneratorConnection connection : connections) {
				if (connection.getObjectWithRole(RandomLogGeneratorConnection.LOG).equals(log)
						&& connection.getParameters().equals(parameters)) {
					return connection.getObjectWithRole(RandomLogGeneratorConnection.MODEL);
				}
			}
		} catch (ConnectionCannotBeObtained e) {
		}
		
		XLog model = mine(context, log, parameters);
		context.addConnection(new RandomLogGeneratorConnection(log, parameters));
		return model;
	}
	
	/*
	 * The actual mining of an event log for a workshop model given parameter
	 * values.
	 */
	private XLog mine(PluginContext context, XLog log, RandomLogGeneratorParameters parameters) {
		
		/*
		 * Inform the progress bar when we're done.
		 */
		context.getProgress().setMaximum(log.size());

		/*
		 * Set the parameters here
		 * */
		parameters.setisTimeMutatorOn(true);
		parameters.setMutationPercentageInCase((float)0.5);
		
		/*
		 * Random Log Generator Model
		 * */
		RandomLogGeneratorModel model = new RandomLogGeneratorModel(log, parameters);
		
		XLog mutatedLog = model.getMutatedLog();
		
		/*
		 * Return the model.
		 */
		return mutatedLog;
	}
}
