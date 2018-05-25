package org.processmining.plugins.liguangming.createdeviation.dialogs;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.fluxicon.slickerbox.factory.SlickerFactory;

/**
 * An UI-component that consists of a slider and a textfield, of which the value
 * will be kept equal. If possible will the sub-components be obtained through a
 * SlickerFactory. When this is not possible, the style of objects created will
 * try to mimic the style of objects created through a SlickerFactory, but no
 * guarantee can be given.
 * 
 * Components of this class will be able to select a double value.
 * 
 * @author Niels Lambrigts (niels.lambrigts@gmail.com)
 * @author Jochen De Weerdt (Jochen.DeWeerdt@econ.kuleuven.be)
 * @date 2011-07-29
 */
public class FancyDoubleSlider extends JPanel {

	private static final long serialVersionUID = -1446236250530860531L;

	/**
	 * The visible JTextField.
	 */
	private JTextField textfield;

	/**
	 * The visible JSlider.
	 */
	private JSlider slider;

	/**
	 * The current value of this slider.
	 */
	double current;

	/**
	 * The minimal value of this slider.
	 */
	double min;

	/**
	 * The maximal value of this slider.
	 */
	double max;

	/**
	 * Make sure that the value of the label is kept consistent with the value
	 * of the slider. This listener will monitor the slider and change the label
	 * accordingly.
	 */
	private ChangeListener keepLabelConsistent = new ChangeListener() {

		@Override
		public void stateChanged(ChangeEvent event) {
			textfield.getDocument()
					.removeDocumentListener(keepSliderConsistent);
			current = getValueFromSlider();
			textfield.setText(new Double(current).toString());
			textfield.getDocument().addDocumentListener(keepSliderConsistent);
		}
	};

	/**
	 * Make sure that the text in the textfield is a correctly formed double.
	 */
	private KeyListener keepLabelTextConsistent = new KeyListener() {

		@Override
		public void keyPressed(KeyEvent arg0) {
			// do nothing
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// do nothing
		}

		@Override
		public void keyTyped(KeyEvent event) {
			if (event.getKeyChar() < '0' || event.getKeyChar() > '9') {
				if (event.getKeyChar() == '.') {
					if (textfield.getText().contains("."))
						event.consume();
				} else {
					event.consume();
				}
			}
		}
	};

	/**
	 * Make sure that the value of the slider is kept consistent with the value
	 * of the label. This listener will monitor the label and change the slider
	 * accordingly.
	 */
	private DocumentListener keepSliderConsistent = new DocumentListener() {

		@Override
		public void changedUpdate(DocumentEvent event) {
			updateLabel();
		}

		@Override
		public void insertUpdate(DocumentEvent arg0) {
			updateLabel();
		}

		@Override
		public void removeUpdate(DocumentEvent arg0) {
			updateLabel();
		}

		public void updateLabel() {
			slider.removeChangeListener(keepLabelConsistent);
			try {
				current = Double.parseDouble(textfield.getText());
			} catch (Exception e) {
				current = min;
			}

			if (current >= max) {
				slider.setValue(slider.getMaximum());
				slider.addChangeListener(keepLabelConsistent);
				return;
			} else if (current <= min) {
				slider.setValue(slider.getMinimum());
				slider.addChangeListener(keepLabelConsistent);
				return;
			}

			slider.setValue(getSliderFromValue());
			slider.addChangeListener(keepLabelConsistent);
		}
	};

	/**
	 * Create a new FancyDoubleSlider.
	 * 
	 * @param min
	 *            The minimal value which this slider can have.
	 * @param current
	 *            The current value of this slider.
	 * @param max
	 *            The maximal value which this slider can have.
	 */
	public FancyDoubleSlider(double min, double current, double max) {
		SlickerFactory slicker = SlickerFactory.instance();
		this.setOpaque(false);
		textfield = new JTextField();
		slider = slicker.createSlider(SwingConstants.HORIZONTAL);

		slider.setMinimum(0);
		slider.setMaximum(Integer.MAX_VALUE);

		this.current = current;
		this.min = min;
		this.max = max;

		slider.setPreferredSize(new Dimension(150,
				slider.getPreferredSize().height));
		textfield.setPreferredSize(new Dimension(40, textfield
				.getPreferredSize().height));
		slider.setMinimumSize(textfield.getPreferredSize());
		textfield.setMinimumSize(textfield.getPreferredSize());

		textfield.setOpaque(false);
		textfield.setBorder(null);
		textfield.setFont(slicker.createLabel("").getFont());
		textfield.setDisabledTextColor(textfield.getForeground());

		this.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.BASELINE_LEADING;
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(slider, c);

		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.BELOW_BASELINE_LEADING;
		c.gridx = 1;
		c.gridy = 0;
		this.add(textfield, c);

		slider.addChangeListener(keepLabelConsistent);
		textfield.addKeyListener(keepLabelTextConsistent);
		textfield.getDocument().addDocumentListener(keepSliderConsistent);

		setValue(current);
	}

	/**
	 * Calculate and return the position that the slider should have to
	 * represent the current value given in the textfield.
	 */
	private int getSliderFromValue() {
		double position = current;
		position -= min;
		position /= max;
		position *= slider.getMaximum();
		return (int) position;
	}

	/**
	 * Return the current value of this slider.
	 */
	public double getValue() {
		return current;
	}

	/**
	 * Calculate and return the value that the current position of the slider
	 * represents.
	 */
	private double getValueFromSlider() {
		double position = (double) slider.getValue()
				/ (double) slider.getMaximum();
		position *= max - min;
		position += min;
		return ((long) (position * 100.0d + 0.5d)) / 100.0d;
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		slider.setEnabled(enabled);
		textfield.setEnabled(enabled);
	}

	/**
	 * Change the current value of the slider.
	 * 
	 * @param current
	 *            The new value for this slider.
	 */
	public void setValue(double current) {
		this.current = current;
		textfield.setText(new Double(current).toString());
	}
}
