package org.processmining.plugins.anomaly.resourceGenerator;

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
import org.processmining.models.anomaly.resourceGenerator.ResourceGeneratorModel;

@Plugin(
		name = "Resource Generator", 
		returnLabels = { "XLog" }, 
		returnTypes = { XLog.class }, 
		parameterLabels = {"Log", "Parameters" }, 
		userAccessible = true)
public class ResourceGeneratorPlugin {
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
			variantLabel = "Resource Generator, default", 
			requiredParameterLabels = { 0 })
	public XLog mineDefault(PluginContext context, XLog log) throws ParseException, IOException {
		return mineParameters(context, log, new ResourceGeneratorParameters());
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
			variantLabel = "Resource Generator, dialog", 
			requiredParameterLabels = { 0 })
	public XLog mineDefault(UIPluginContext context, XLog log) throws ParseException, IOException {
		ResourceGeneratorParameters parameters = new ResourceGeneratorParameters();
		ResourceGeneratorDialog dialog = new ResourceGeneratorDialog(log, parameters);
		InteractionResult result = context.showWizard("Resource Generator", true, true, dialog);
		
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
			variantLabel = "Resource Generator, parameterized", 
			requiredParameterLabels = { 0, 1 })
	public XLog mineParameters(PluginContext context, XLog log, ResourceGeneratorParameters parameters) throws ParseException, IOException {
		Collection<ResourceGeneratorConnection> connections;
		try {
			connections = context.getConnectionManager().getConnections(ResourceGeneratorConnection.class, context, log);
			for (ResourceGeneratorConnection connection : connections) {
				if (connection.getObjectWithRole(ResourceGeneratorConnection.LOG).equals(log)
						&& connection.getParameters().equals(parameters)) {
					return connection.getObjectWithRole(ResourceGeneratorConnection.MODEL);
				}
			}
		} catch (ConnectionCannotBeObtained e) {
		}
		
		XLog model = mine(context, log, parameters);
		context.addConnection(new ResourceGeneratorConnection(log, parameters));
		return model;
	}
	
	/*
	 * The actual mining of an event log for a workshop model given parameter
	 * values.
	 */
	private XLog mine(PluginContext context, XLog log, ResourceGeneratorParameters parameters) throws IOException, ParseException {
		
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
		ResourceGeneratorModel model = new ResourceGeneratorModel(log, parameters);
		
		
		/*
		 * Return the model.
		 */
		return model.getMutatedLog();
	}
}
