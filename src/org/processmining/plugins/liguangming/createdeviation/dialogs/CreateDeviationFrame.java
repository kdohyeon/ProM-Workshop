package org.processmining.plugins.liguangming.createdeviation.dialogs;
 
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.fluxicon.slickerbox.factory.SlickerFactory;
 
public class CreateDeviationFrame extends JPanel {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FancyDoubleSlider sliderDeviationPercentage;//indicate what percentage of the log will be changed as deviation
   
	public CreateDeviationFrame() {
		this.add(deviationPercentagePane());

	}
       
	/**
	 * Create the "Loop Times"-section.
	 */
	private JPanel deviationPercentagePane() {
		SlickerFactory slicker = SlickerFactory.instance();
		JPanel pane = slicker.createRoundedPanel(15, Color.GRAY);
		pane.setLayout(new GridBagLayout());

		JLabel percentageLabel = slicker.createLabel("Deviation Percentage");
		percentageLabel.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 18));
		percentageLabel.setForeground(new Color(40, 40, 40));

		sliderDeviationPercentage = new FancyDoubleSlider(0, 0.3, 1);


		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.weightx = 1.0;
		c.anchor = GridBagConstraints.LINE_START;
		pane.add(percentageLabel, c);
		
		//row 1, line 3
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 0;
		c.gridwidth = 3;
		c.weightx = 1.0;
		c.anchor = GridBagConstraints.LINE_START;
		pane.add(sliderDeviationPercentage, c);
		
		pane.setMaximumSize(new Dimension(Integer.MAX_VALUE, pane
				.getPreferredSize().height));
		return pane;
	}
    
	public double getDeviationPercentage() {
		return sliderDeviationPercentage.getValue();
	}
    
    
 

   
    public static void main(String[] args) {
      
    	 JFrame frame = new JFrame("Hello world");
         
    	 CreateDeviationFrame panel = new CreateDeviationFrame();
        
         frame.add(panel);
      //   frame.pack();
         
        frame.setSize(600, 400);
         frame.setVisible(true);
    }

 

}