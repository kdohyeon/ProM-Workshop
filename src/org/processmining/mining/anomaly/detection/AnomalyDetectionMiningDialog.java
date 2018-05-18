package org.processmining.mining.anomaly.detection;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.deckfour.xes.model.XLog;
import org.processmining.models.anomaly.profile.AnomalyProfileModel;

import com.fluxicon.slickerbox.factory.SlickerDecorator;
import com.fluxicon.slickerbox.factory.SlickerFactory;

public class AnomalyDetectionMiningDialog extends JPanel {
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
	public AnomalyDetectionMiningDialog(
			XLog log,
			AnomalyProfileModel profileModel,
			final org.processmining.mining.anomaly.detection.AnomalyDetectionMiningParameters parameters) {

		/*
		 * Parameter
		 * */
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		SlickerFactory factory = SlickerFactory.instance();
		SlickerDecorator decorator = SlickerDecorator.instance();
		
		JPanel parameterPanel = factory.createRoundedPanel(15, Color.gray);
		parameterPanel.setLayout(new BoxLayout(parameterPanel, BoxLayout.Y_AXIS));
		
		JLabel parameterTitle = factory.createLabel("Select mining parameters");
		parameterTitle.setBounds(10, 10, 200, 30);
		parameterTitle.setFont(new Font("Dialog", Font.BOLD, 18));
		
		FlowLayout customLayout = new FlowLayout(FlowLayout.LEFT);
		customLayout.setHgap(2);
		
		int customPanelWidth = 405;
		
		/*
		 * Sigma
		 * */
		JPanel sigmaPane = new JPanel(customLayout);
		sigmaPane.setBackground(Color.gray);
		
		JLabel sigmaTitle = factory.createLabel("Sigma: ");
		sigmaTitle.setFont(new Font("Dialog", Font.BOLD, 14));
		
		String[] sigmaOptions = {"1", "2", "3"};
		final JComboBox<String> sigmaList = new JComboBox<String>(sigmaOptions);
		
		sigmaList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				float sigma = Float.parseFloat(sigmaList.getSelectedItem().toString());
				parameters.setSigma(sigma);
			}			
		});
		
		decorator.decorate(sigmaTitle);
		decorator.decorate(sigmaList);
		
		sigmaPane.add(sigmaTitle);
		sigmaPane.add(sigmaList);
		
		/*
		 * Control-flow with Time
		 * */
		JPanel timePane = new JPanel(customLayout);
		timePane.setBackground(Color.gray);
		
		JLabel timeTitle = factory.createLabel(("Time Options: "));
		timeTitle.setFont(new Font("Dialog", Font.BOLD, 14));
				
		final JCheckBox processingCB = new JCheckBox("Processing Time");
		processingCB.setSelected(true);
		processingCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				boolean selected = processingCB.isSelected();
				parameters.setProcessing(selected);
			}
		});
		
		final JCheckBox transitionCB = new JCheckBox("Transition Time");
		transitionCB.setSelected(true);
		transitionCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				boolean selected = transitionCB.isSelected();
				parameters.setTransition(selected);
			}
		});
		final JCheckBox overlapCB = new JCheckBox("Overlap Time");
		overlapCB.setSelected(true);
		overlapCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				boolean selected = overlapCB.isSelected();
				parameters.setOverlap(selected);
			}
		});
		final JCheckBox trueXCB = new JCheckBox("TrueX Time");
		trueXCB.setSelected(true);
		trueXCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				boolean selected = trueXCB.isSelected();
				parameters.setTrueX(selected);
			}
		});
		final JCheckBox trueYCB = new JCheckBox("TrueY Time");
		trueYCB.setSelected(true);
		trueYCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				boolean selected = trueYCB.isSelected();
				parameters.setTrueY(selected);
			}
		});
		
		decorator.decorate(timeTitle);
		decorator.decorate(processingCB);
		decorator.decorate(transitionCB);
		decorator.decorate(overlapCB);
		decorator.decorate(trueXCB);
		decorator.decorate(trueYCB);
		
		timePane.add(timeTitle);
		timePane.add(processingCB);
		timePane.add(transitionCB);
		timePane.add(overlapCB);
		timePane.add(trueXCB);
		timePane.add(trueYCB);
		
		/*
		 * Log to Rule Ratio
		 * */
		JPanel logToRuleRatioPane = new JPanel(customLayout);
		logToRuleRatioPane.setBackground(Color.GRAY);
		
		JLabel logToRuleRatioTitle = factory.createLabel("Log to rule ratio: ");
		logToRuleRatioTitle.setFont(new Font("Dialog", Font.BOLD, 14));
		
		final JLabel logToRuleRatioLabel = new JLabel("0.50");
		logToRuleRatioLabel.setPreferredSize(new Dimension(60,20));
		
		final JSlider logToRuleRatioSlider = new JSlider();
		
		logToRuleRatioSlider.setPreferredSize(new Dimension(customPanelWidth - 60, 20));
		logToRuleRatioSlider.setMajorTickSpacing(10);
		logToRuleRatioSlider.setMinorTickSpacing(5);
		logToRuleRatioSlider.setPaintTicks(true);
		logToRuleRatioSlider.setPaintLabels(true);
		logToRuleRatioSlider.setValue(50);
		parameters.setLogRuleRatio((float)0.5);
		logToRuleRatioSlider.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				float ratio = (float)(logToRuleRatioSlider.getValue() * 1.0 / 100);
				parameters.setLogRuleRatio(ratio);
				String labelStr = String.valueOf(ratio);
				logToRuleRatioLabel.setText(labelStr);
			}
		});
		
		decorator.decorate(logToRuleRatioTitle);
		decorator.decorate(logToRuleRatioSlider);
		decorator.decorate(logToRuleRatioLabel);
		
		logToRuleRatioPane.add(logToRuleRatioTitle);
		logToRuleRatioPane.add(logToRuleRatioSlider);
		logToRuleRatioPane.add(logToRuleRatioLabel);
		
		/*
		 * Alpha & Beta
		 * */
		JPanel alphaRatioPane = new JPanel(customLayout);
		alphaRatioPane.setBackground(Color.GRAY);
		
		JLabel alphaRatioTitle = factory.createLabel("Alpha & Beta ratio: ");
		alphaRatioTitle.setFont(new Font("Dialog", Font.BOLD, 14));
		
		final JLabel alphaRatioLabel = new JLabel("Alpha: 0.50");
		final JLabel betaRatioLabel = new JLabel("Beta: 0.50");
		alphaRatioLabel.setPreferredSize(new Dimension(60,20));
		betaRatioLabel.setPreferredSize(new Dimension(60,20));
		
		final JSlider alphaRatioSlider = new JSlider();
		
		alphaRatioSlider.setPreferredSize(new Dimension(customPanelWidth - 60, 20));
		alphaRatioSlider.setMajorTickSpacing(10);
		alphaRatioSlider.setMinorTickSpacing(5);
		alphaRatioSlider.setPaintTicks(true);
		alphaRatioSlider.setPaintLabels(true);
		alphaRatioSlider.setValue(50);
		parameters.setAlpha((float)0.5);
		parameters.setBeta((float)0.5);
		alphaRatioSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				float alpha = (float)(alphaRatioSlider.getValue() * 1.0 / 100);
				float beta = 1 - alpha;
				
				parameters.setAlpha(alpha);
				parameters.setBeta(beta);
				
				String labelStr1 = String.valueOf("Alpha: " + alpha);
				String labelStr2 = String.valueOf("Beta: " + beta);
				
				alphaRatioLabel.setText(labelStr1);
				betaRatioLabel.setText(labelStr2);
			}
		});
		
		decorator.decorate(alphaRatioTitle);
		decorator.decorate(alphaRatioLabel);
		decorator.decorate(betaRatioLabel);
		decorator.decorate(alphaRatioSlider);
		
		alphaRatioPane.add(alphaRatioTitle);
		alphaRatioPane.add(alphaRatioSlider);
		alphaRatioPane.add(alphaRatioLabel);
		alphaRatioPane.add(betaRatioLabel);
		
		/*
		 * Add to parameter panel
		 * */
		parameterPanel.add(sigmaPane);
		parameterPanel.add(timePane);
		parameterPanel.add(logToRuleRatioPane);
		parameterPanel.add(alphaRatioPane);
		
		add(parameterTitle);
		add(parameterPanel);
		
		validate();
		repaint();
		
	}
}
