package org.processmining.plugins.anomaly.detection;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

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
			final org.processmining.plugins.anomaly.detection.AnomalyDetectionMiningParameters parameters) {

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
		
		FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
		flowLayout.setHgap(2);
		
		
		/*
		 * Control-flow, Time, & Resource
		 * */
		JPanel weightPane = new JPanel(flowLayout);
		weightPane.setBackground(Color.GRAY);
		
		JLabel weightTitle = factory.createLabel("Weight of Control-flow, Time, and Resource: ");
		weightTitle.setFont(new Font("Dialog", Font.BOLD, 14));
		
		final JLabel cfLabel = new JLabel("Control-flow");
		final JLabel tLabel = new JLabel("Time");
		final JLabel rLabel = new JLabel("Resource");
		
		cfLabel.setPreferredSize(new Dimension(60,20));
		tLabel.setPreferredSize(new Dimension(60,20));
		rLabel.setPreferredSize(new Dimension(60,20));
		
		final JTextField cfTextField = new JTextField(5);
		final JTextField tTextField = new JTextField(5);
		final JTextField rTextField = new JTextField(5);
		cfTextField.setText("1");
		tTextField.setText("1");
		rTextField.setText("1");
		
		
		cfTextField.getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(DocumentEvent e) {
				System.out.println(getValue());
			}
			
			public void insertUpdate(DocumentEvent e) {
				System.out.println(getValue());
			}
			
			public void changedUpdate(DocumentEvent e) {
				System.out.println(getValue());
			}
			
			public int getValue() {
				int result = 0;
				if(cfTextField.getText().equals("")) {
					
				}else {
					result = Integer.parseInt(cfTextField.getText());	
				}
				
				parameters.setControlFlow(result);
				return result;
			}
		});
		
		tTextField.getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(DocumentEvent e) {
				System.out.println(getValue());
			}
			
			public void insertUpdate(DocumentEvent e) {
				System.out.println(getValue());
			}
			
			public void changedUpdate(DocumentEvent e) {
				System.out.println(getValue());
			}
			
			public int getValue() {
				int result = 0;
				if(tTextField.getText().equals("")) {
					
				}else {
					result = Integer.parseInt(tTextField.getText());	
				}
				
				parameters.setTime(result);
				return result;
			}
		});
		
		rTextField.getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(DocumentEvent e) {
				System.out.println(getValue());
			}
			
			public void insertUpdate(DocumentEvent e) {
				System.out.println(getValue());
			}
			
			public void changedUpdate(DocumentEvent e) {
				System.out.println(getValue());
			}
			
			public int getValue() {
				int result = 0;
				if(rTextField.getText().equals("")) {
					
				}else {
					result = Integer.parseInt(rTextField.getText());	
				}
				
				parameters.setResource(result);
				return result;
			}
		});
		
		
		decorator.decorate(weightTitle);
		decorator.decorate(cfLabel);
		decorator.decorate(tLabel);
		decorator.decorate(rLabel);
		
		weightPane.add(weightTitle);
		weightPane.add(cfLabel);
		weightPane.add(cfTextField);
		weightPane.add(tLabel);
		weightPane.add(tTextField);
		weightPane.add(rLabel);
		weightPane.add(rTextField);
		
		/*
		 * Add to parameter panel
		 * */
		parameterPanel.add(weightPane);
		
		add(parameterTitle);
		add(parameterPanel);
		
		validate();
		repaint();
		
	}
	
	
}
