package org.processmining.plugins.anomaly.profile;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.deckfour.xes.model.XLog;

import com.fluxicon.slickerbox.factory.SlickerDecorator;
import com.fluxicon.slickerbox.factory.SlickerFactory;

public class AnomalyProfileMiningDialog extends JPanel {
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
	public AnomalyProfileMiningDialog(XLog log, final org.processmining.plugins.anomaly.profile.AnomalyProfileMiningParameters parameters) {

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		SlickerFactory factory = SlickerFactory.instance();
		SlickerDecorator decorator = SlickerDecorator.instance();
		
		int customPanelWidth = 405;
		
		/*
		 * Parameter
		 * */
		JPanel parameterPanel = factory.createRoundedPanel(15, Color.gray);
		parameterPanel.setLayout(new BoxLayout(parameterPanel, BoxLayout.Y_AXIS));
		
		JLabel parameterTitle = factory.createLabel("Select mining parameters");
		parameterTitle.setBounds(10, 10, 200, 30);
		parameterTitle.setFont(new Font("Dialog", Font.BOLD, 18));
		
		/*
		 * Minimum Support
		 * */
		JLabel supportTitle = factory.createLabel("Minimum support: ");
		supportTitle.setFont(new Font("Dialog", Font.BOLD, 14));
		
		FlowLayout customLayout = new FlowLayout(FlowLayout.LEFT);
		customLayout.setHgap(2);
		
		JPanel supportSliderPane = new JPanel(customLayout);
		supportSliderPane.setBackground(Color.GRAY);
		
		final JLabel supportSliderLabel = new JLabel("0.50");
		supportSliderLabel.setPreferredSize(new Dimension(60,20));
		
		final JSlider supportSlider = new JSlider();
		
		supportSlider.setPreferredSize(new Dimension(customPanelWidth - 60, 20));
		supportSlider.setMajorTickSpacing(10);
		supportSlider.setMinorTickSpacing(5);
		supportSlider.setPaintTicks(true);
		supportSlider.setPaintLabels(true);
		supportSlider.setValue(50);
		parameters.setMinSupport((float)0.5);
		supportSlider.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				float minSupp = (float)(supportSlider.getValue() * 1.0 / 100);
				parameters.setMinSupport(minSupp);
				String labelStr = String.valueOf(minSupp);
				supportSliderLabel.setText(labelStr);
			}
			
		});
		
		decorator.decorate(supportTitle);
		decorator.decorate(supportSlider);
		decorator.decorate(supportSliderLabel);
		
		supportSliderPane.add(supportTitle);
		supportSliderPane.add(supportSlider);
		supportSliderPane.add(supportSliderLabel);
		
		/*
		 * Minimum Confidence
		 * */
		JLabel confidenceTitle = factory.createLabel("Minimum confidence: ");
		confidenceTitle.setFont(new Font("Dialog", Font.BOLD, 14));
		
		JPanel confidenceSliderPane = new JPanel(customLayout);
		confidenceSliderPane.setBackground(Color.GRAY);
		
		final JLabel confidenceSliderLabel = new JLabel("0.50");
		confidenceSliderLabel.setPreferredSize(new Dimension(60,20));
		
		final JSlider confidenceSlider = new JSlider();
		
		confidenceSlider.setPreferredSize(new Dimension(customPanelWidth - 60, 20));
		confidenceSlider.setMajorTickSpacing(10);
		confidenceSlider.setMinorTickSpacing(5);
		confidenceSlider.setPaintTicks(true);
		confidenceSlider.setPaintLabels(true);
		confidenceSlider.setValue(50);
		parameters.setMinConfidence((float)0.5);
		confidenceSlider.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				float minConf = (float)(confidenceSlider.getValue() * 1.0 / 100);
				parameters.setMinConfidence(minConf);
				String labelStr = String.valueOf(minConf);
				confidenceSliderLabel.setText(labelStr);
			}
			
		});
		
		decorator.decorate(confidenceTitle);
		decorator.decorate(confidenceSlider);
		decorator.decorate(confidenceSliderLabel);
		
		confidenceSliderPane.add(confidenceTitle);
		confidenceSliderPane.add(confidenceSlider);
		confidenceSliderPane.add(confidenceSliderLabel);
		
		/* 
		 * Add to parameter panel
		 * */
		parameterPanel.add(supportSliderPane);
		parameterPanel.add(confidenceSliderPane);
		
		add(parameterTitle);
		add(parameterPanel);
		
		
		validate();
		repaint();
		
		
		
	}
	
}
