package org.processmining.models.anomaly.profile.visualizer;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.connections.ConnectionCannotBeObtained;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.models.anomaly.profile.AnomalyProfileModel;

public class AnomalyProfileModelVisualizer {
	@Plugin(
			name = "Show Anomaly Profile Model", 
			returnLabels = { "Visualization of Anomaly Profile Model" }, 
			returnTypes = { JComponent.class }, 
			parameterLabels = { "Anomaly Profile Model" }, 
			userAccessible = false)
	
	@Visualizer
	public JComponent visualize(PluginContext context, AnomalyProfileModel model) throws ConnectionCannotBeObtained {
		/*
		 * Have the framework convert the workshop model to a workshop graph.
		 */
		/*
		WorkshopGraph graph = context.tryToFindOrConstructFirstObject(WorkshopGraph.class,
				WorkshopConversionConnection.class, WorkshopConversionConnection.MODEL, model);
		*/
		/*
		 * Visualize the resulting workshop graph.
		 */
		//return ProMJGraphVisualizer.instance().visualizeGraph(context, graph);
		MyJComponent com = new MyJComponent();
		
		return com;
	}
}

class MyJComponent extends JComponent{
	public void paint(Graphics g) {
		g.setColor(Color.red);
		g.fillOval(50, 10, 150, 150);
	}
}
