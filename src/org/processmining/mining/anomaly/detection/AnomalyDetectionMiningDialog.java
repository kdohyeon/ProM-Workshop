package org.processmining.mining.anomaly.detection;

import javax.swing.JPanel;

import org.deckfour.xes.model.XLog;
import org.processmining.models.anomaly.profile.AnomalyProfileModel;

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
		double size[][] = { { TableLayoutConstants.FILL }, { 30, TableLayoutConstants.FILL } };
		setLayout(new TableLayout(size));
		
		add(SlickerFactory.instance().createLabel("<html><h2>Select mining parameters</h2>"), "0, 0");

		Object classifiers[] = log.getClassifiers().toArray();

		final JList classifierList = new javax.swing.JList(classifiers);
		classifierList.setName("Select classifier");
		classifierList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		classifierList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				parameters.setClassifier((XEventClassifier) classifierList.getSelectedValue());
			}
		});
		
		JScrollPane classifierScrollPane = new javax.swing.JScrollPane();
		SlickerDecorator.instance().decorate(classifierScrollPane, SlickerColors.COLOR_BG_3, SlickerColors.COLOR_FG,
				SlickerColors.COLOR_BG_1);
		classifierScrollPane.setPreferredSize(new Dimension(250, 300));
		classifierScrollPane.setViewportView(classifierList);
		add(classifierScrollPane, "0, 1");
		*/
	}
}
