package org.processmining.plugins.anomaly.mutator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.deckfour.xes.model.XLog;

import com.fluxicon.slickerbox.factory.SlickerDecorator;
import com.fluxicon.slickerbox.factory.SlickerFactory;

public class MutatorDialog extends JPanel {
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
	public MutatorDialog(XLog log, final MutatorParameters parameters) {
		/*
		 * Parameter
		 * */
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		int customPanelWidth = 405;
		
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
		 * Deviation Percentage
		 * */
		JLabel percentageTitle = factory.createLabel("Deviation Percentage: ");
		percentageTitle.setFont(new Font("Dialog", Font.BOLD, 14));
		
		FlowLayout customLayout = new FlowLayout(FlowLayout.LEFT);
		customLayout.setHgap(2);
				
		JPanel percentageSliderPane = new JPanel(customLayout);
		percentageSliderPane.setBackground(Color.GRAY);
		
		final JLabel percentageSliderLabel = new JLabel("0.10");
		percentageSliderLabel.setPreferredSize(new Dimension(60,20));
		
		final JSlider percentageSlider = new JSlider();
		
		percentageSlider.setPreferredSize(new Dimension(customPanelWidth - 60, 20));
		percentageSlider.setMajorTickSpacing(10);
		percentageSlider.setMinorTickSpacing(5);
		percentageSlider.setPaintTicks(true);
		percentageSlider.setPaintLabels(true);
		percentageSlider.setValue(10);
		float dp = (float)0.1;
		parameters.setDeviationPercentageCase(dp);
		
		percentageSlider.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				float dp = (float)(percentageSlider.getValue() * 1.0 / 100);
				parameters.setDeviationPercentageCase(dp);
				String labelStr = String.valueOf(dp);
				percentageSliderLabel.setText(labelStr);
				
				System.out.println(dp + ", " + parameters.getDeviationPercentageCase());
			}
			
		});
		System.out.println(dp + ", " + parameters.getDeviationPercentageCase());
		
		decorator.decorate(percentageTitle);
		decorator.decorate(percentageSlider);
		decorator.decorate(percentageSliderLabel);
		
		percentageSliderPane.add(percentageTitle);
		percentageSliderPane.add(percentageSlider);
		percentageSliderPane.add(percentageSliderLabel);
		
		/*
		 * Deviation Type
		 * Add, Remove, or Replace
		 * */
		JLabel deviationTypeTitle = factory.createLabel("Deviation Type: ");
		deviationTypeTitle.setFont(new Font("Dialog", Font.BOLD, 14));
				
		JPanel deviationTypePane = new JPanel(customLayout);
		deviationTypePane.setBackground(Color.GRAY);
		
		String[] types = {"All", "Control-flow", "Time", "Resource"};
		final JComboBox<String> deviationTypeComboBox = new JComboBox<String>(types);
		deviationTypeComboBox.setSelectedIndex(0);
		parameters.setDeviationType(deviationTypeComboBox.getSelectedItem().toString());
		
		deviationTypeComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				parameters.setDeviationType(deviationTypeComboBox.getSelectedItem().toString());
			}
		});
		
		decorator.decorate(deviationTypeTitle);
		decorator.decorate(deviationTypeComboBox);
		
		deviationTypePane.add(deviationTypeTitle);
		deviationTypePane.add(deviationTypeComboBox);
		
		/* 
		 * Add to parameter panel
		 * */
		parameterPanel.add(percentageSliderPane);
		parameterPanel.add(deviationTypePane);
		
		add(parameterTitle);
		add(parameterPanel);
		
		validate();
		repaint();
		
	}
	
}
