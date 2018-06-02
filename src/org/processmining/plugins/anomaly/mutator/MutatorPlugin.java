package org.processmining.plugins.anomaly.mutator;

import java.io.IOException;
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
import org.processmining.models.anomaly.mutator.MutatorModel;

@Plugin(
		name = "Mutator", 
		returnLabels = { "XLog" }, 
		returnTypes = { XLog.class }, 
		parameterLabels = {"Log", "Parameters" }, 
		userAccessible = true)
public class MutatorPlugin {
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
	 * @throws IOException 
	 */
	@UITopiaVariant(
			affiliation = UITopiaVariant.POSTECH, 
			author = "Dohyeon Kim", 
			email = "kdohyeon@postech.ac.kr")
	@PluginVariant(
			variantLabel = "Mutator, default", 
			requiredParameterLabels = { 0 })
	public XLog mineDefault(PluginContext context, XLog log) throws ParseException, IOException {
		return mineParameters(context, log, new MutatorParameters());
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
	 * @throws IOException 
	 */
	@UITopiaVariant(
			affiliation = UITopiaVariant.POSTECH, 
			author = "Dohyeon Kim", 
			email = "kdohyeon@postech.ac.kr")
	@PluginVariant(
			variantLabel = "Mutator, dialog", 
			requiredParameterLabels = { 0 })
	public XLog mineDefault(UIPluginContext context, XLog log) throws ParseException, IOException {
		MutatorParameters parameters = new MutatorParameters();
		MutatorDialog dialog = new MutatorDialog(log, parameters);
		InteractionResult result = context.showWizard("Mutator", true, true, dialog);
		
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
	 * @throws IOException 
	 */
	@UITopiaVariant(
			affiliation = UITopiaVariant.POSTECH, 
			author = "Dohyeon Kim", 
			email = "kdohyeon@postech.ac.kr")
	@PluginVariant(
			variantLabel = "Mutator, parameterized", 
			requiredParameterLabels = { 0, 1 })
	public XLog mineParameters(PluginContext context, XLog log, MutatorParameters parameters) throws ParseException, IOException {
		Collection<MutatorConnection> connections;
		try {
			connections = context.getConnectionManager().getConnections(MutatorConnection.class, context, log);
			for (MutatorConnection connection : connections) {
				if (connection.getObjectWithRole(MutatorConnection.LOG).equals(log)
						&& connection.getParameters().equals(parameters)) {
					return connection.getObjectWithRole(MutatorConnection.MODEL);
				}
			}
		} catch (ConnectionCannotBeObtained e) {
		}
		
		XLog model = mine(context, log, parameters);
		context.addConnection(new MutatorConnection(log, parameters));
		return model;
	}
	
	/*
	 * The actual mining of an event log for a workshop model given parameter
	 * values.
	 */
	private XLog mine(PluginContext context, XLog log, MutatorParameters parameters) throws IOException, ParseException {
		
		/*
		 * Inform the progress bar when we're done.
		 */
		context.getProgress().setMaximum(log.size());

		/*
		 * Set the parameters here
		 * */
		
		/*
		 * Random Log Generator Model
		 * */
		MutatorModel model = new MutatorModel(log, parameters);
		
		
		/*
		 * Return the model.
		 */
		return model.getMutatedLog();
	}
}
