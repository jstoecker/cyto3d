package edu.miami.cyto3d.ui.graphics;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.miami.jgloo.util.Bloom;

public class BloomPanel extends JPanel {

  private static final float MAX_INTENSITY  = 20;
  private static final float MAX_BLURRINESS = 8;
  private static final float MAX_THRESHOLD  = 1;

  private JSlider            spnThreshold;
  private JSlider            spnBlurriness;
  private JSlider            spnIntensity;

  Bloom                      bloom;

  public void setBloom(Bloom bloom) {
    this.bloom = bloom;

    spnThreshold.setValue((int) (bloom.getThreshold() / MAX_THRESHOLD * spnThreshold.getMaximum()));
    spnBlurriness.setValue((int) (bloom.getBlurriness() / MAX_BLURRINESS * spnBlurriness
        .getMaximum()));
    spnIntensity.setValue((int) (bloom.getIntensity() / MAX_INTENSITY * spnIntensity.getMaximum()));
  }

  /**
   * Create the panel.
   */
  public BloomPanel() {
    setBorder(new MatteBorder(1, 0, 0, 0, (Color) Color.LIGHT_GRAY));
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[] { 95, 0, 0 };
    gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
    gridBagLayout.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
    gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
    setLayout(gridBagLayout);

    JLabel lblIntensity = new JLabel("Intensity");
    GridBagConstraints gbc_lblIntensity = new GridBagConstraints();
    gbc_lblIntensity.anchor = GridBagConstraints.EAST;
    gbc_lblIntensity.insets = new Insets(0, 0, 5, 5);
    gbc_lblIntensity.gridx = 0;
    gbc_lblIntensity.gridy = 0;
    add(lblIntensity, gbc_lblIntensity);

    spnIntensity = new JSlider();
    spnIntensity.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        if (bloom != null) {
          float intensity = (float) spnIntensity.getValue() / spnIntensity.getMaximum()
              * MAX_INTENSITY;
          bloom.setIntensity(intensity);
        }
      }
    });
    GridBagConstraints gbc_spnIntensity = new GridBagConstraints();
    gbc_spnIntensity.fill = GridBagConstraints.HORIZONTAL;
    gbc_spnIntensity.insets = new Insets(0, 0, 5, 0);
    gbc_spnIntensity.gridx = 1;
    gbc_spnIntensity.gridy = 0;
    add(spnIntensity, gbc_spnIntensity);

    JLabel lblThreshold = new JLabel("Threshold");
    GridBagConstraints gbc_lblThreshold = new GridBagConstraints();
    gbc_lblThreshold.anchor = GridBagConstraints.EAST;
    gbc_lblThreshold.insets = new Insets(0, 0, 5, 5);
    gbc_lblThreshold.gridx = 0;
    gbc_lblThreshold.gridy = 1;
    add(lblThreshold, gbc_lblThreshold);

    spnThreshold = new JSlider();
    spnThreshold.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent arg0) {
        if (bloom != null) {
          float threshold = (float) spnThreshold.getValue() / spnThreshold.getMaximum()
              * MAX_THRESHOLD;
          bloom.setThreshold(threshold);
        }
      }
    });
    GridBagConstraints gbc_spnTreshold = new GridBagConstraints();
    gbc_spnTreshold.fill = GridBagConstraints.HORIZONTAL;
    gbc_spnTreshold.insets = new Insets(0, 0, 5, 0);
    gbc_spnTreshold.gridx = 1;
    gbc_spnTreshold.gridy = 1;
    add(spnThreshold, gbc_spnTreshold);

    JLabel lblBluriness = new JLabel("Blurriness");
    GridBagConstraints gbc_lblBluriness = new GridBagConstraints();
    gbc_lblBluriness.anchor = GridBagConstraints.EAST;
    gbc_lblBluriness.insets = new Insets(0, 0, 0, 5);
    gbc_lblBluriness.gridx = 0;
    gbc_lblBluriness.gridy = 2;
    add(lblBluriness, gbc_lblBluriness);

    spnBlurriness = new JSlider();
    spnBlurriness.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        if (bloom != null) {
          float blurriness = (float) spnBlurriness.getValue() / spnBlurriness.getMaximum()
              * MAX_BLURRINESS;
          bloom.setBlurriness(blurriness);
        }
      }
    });
    spnBlurriness.setValue(4);
    spnBlurriness.setMaximum(80);
    GridBagConstraints gbc_spnBluriness = new GridBagConstraints();
    gbc_spnBluriness.fill = GridBagConstraints.HORIZONTAL;
    gbc_spnBluriness.gridx = 1;
    gbc_spnBluriness.gridy = 2;
    add(spnBlurriness, gbc_spnBluriness);

  }
}
