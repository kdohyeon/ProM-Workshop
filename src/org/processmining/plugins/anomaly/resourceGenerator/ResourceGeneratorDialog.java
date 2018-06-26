package org.processmining.plugins.anomaly.resourceGenerator;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.deckfour.xes.model.XLog;

import com.fluxicon.slickerbox.factory.SlickerDecorator;
import com.fluxicon.slickerbox.factory.SlickerFactory;

public class ResourceGeneratorDialog extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7639879370139576539L;

	/**
	 * Parameter dialog for mining the given event log for a workflow model.
	 * 
	 * @param log
	 *            The given event log.
	 * @param parameters
	 *            The parameters which will be used for the mining.
	 */
	public ResourceGeneratorDialog(XLog log, final ResourceGeneratorParameters parameters) {
		/*
		 * Parameter
		 * */
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		int customPanelWidth = 405;
		FlowLayout customLayout = new FlowLayout(FlowLayout.LEFT);
		customLayout.setHgap(2);
		
		SlickerFactory factory = SlickerFactory.instance();
		SlickerDecorator decorator = SlickerDecorator.instance();
		
		JPanel parameterPanel = factory.createRoundedPanel(15, Color.gray);
		parameterPanel.setLayout(new BoxLayout(parameterPanel, BoxLayout.Y_AXIS));
		
		JLabel parameterTitle = factory.createLabel("Select mining parameters");
		parameterTitle.setBounds(10, 10, 200, 30);
		parameterTitle.setFont(new Font("Dialog", Font.BOLD, 18));
		
		FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
		flowLayout.setHgap(2);
		
		/*
		 * Number of Activities for each Resource: 
		 * */
		JLabel activityNumTitle = factory.createLabel("Number of Activities for each Resource: ");
		activityNumTitle.setFont(new Font("Dialog", Font.BOLD, 14));
				
		JPanel activityNumPane = new JPanel(customLayout);
		activityNumPane.setBackground(Color.GRAY);
		
		String[] types = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
		final JComboBox<String> activityNumComboBox = new JComboBox<String>(types);
		activityNumComboBox.setSelectedIndex(0);
		parameters.setActivityNum(Integer.valueOf(activityNumComboBox.getSelectedItem().toString()));
		
		activityNumComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				parameters.setActivityNum(Integer.valueOf(activityNumComboBox.getSelectedItem().toString()));
				System.out.println(parameters.getActivityNum());
			}
		});
		
		decorator.decorate(activityNumTitle);
		decorator.decorate(activityNumComboBox);
		
		activityNumPane.add(activityNumTitle);
		activityNumPane.add(activityNumComboBox);
		
		/* 
		 * Add to parameter panel
		 * */
		parameterPanel.add(activityNumPane);
		
		add(parameterTitle);
		add(parameterPanel);
		
		validate();
		repaint();
		
	}
	
}
