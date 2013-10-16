package edu.miami.cyto3d.ui.layout;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.miami.cyto3d.graph.layout.force.ForceLayoutThread;

public class ForceOptionPanel extends JPanel {

   private JSpinner    spnInterval;
   private JSpinner    spnDamping;
   private JCheckBox   chckbxEnabled;
   private JSpinner    spnSpringiness;
   private JCheckBox chckbxUseVelocity;
   
   private ForceLayoutThread flt;
   
   public void setForceLayoutThread(ForceLayoutThread flt) {
      this.flt = flt;
      spnInterval.setValue(flt.getInterval());
      spnDamping.setValue(flt.getLayout().getDamping());
      spnSpringiness.setValue(flt.getLayout().getSpringiness());
      chckbxEnabled.setSelected(flt.isEnabled());
      chckbxUseVelocity.setSelected(flt.getLayout().isUseVelocity());
   }

   public ForceOptionPanel() {

      setLayout(new BorderLayout(0, 0));

      JPanel panel = new JPanel();
      add(panel, BorderLayout.CENTER);
      GridBagLayout gbl_panel = new GridBagLayout();
      gbl_panel.columnWidths = new int[] { 138, 0, 0 };
      gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
      gbl_panel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
      gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
      panel.setLayout(gbl_panel);

      JLabel lblDamping = new JLabel("Damping");
      GridBagConstraints gbc_lblDamping = new GridBagConstraints();
      gbc_lblDamping.anchor = GridBagConstraints.EAST;
      gbc_lblDamping.insets = new Insets(0, 0, 5, 5);
      gbc_lblDamping.gridx = 0;
      gbc_lblDamping.gridy = 0;
      panel.add(lblDamping, gbc_lblDamping);

      spnDamping = new JSpinner();
      spnDamping.setModel(new SpinnerNumberModel(new Float(0), new Float(0), new Float(1),
            new Float(0)));
      spnDamping.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent e) {
            flt.getLayout().setDamping((Float)spnDamping.getValue());
         }
      });
      GridBagConstraints gbc_spnDamping = new GridBagConstraints();
      gbc_spnDamping.fill = GridBagConstraints.HORIZONTAL;
      gbc_spnDamping.insets = new Insets(0, 0, 5, 0);
      gbc_spnDamping.gridx = 1;
      gbc_spnDamping.gridy = 0;
      panel.add(spnDamping, gbc_spnDamping);

      JLabel lblSpringiness = new JLabel("Springiness");
      GridBagConstraints gbc_lblSpringiness = new GridBagConstraints();
      gbc_lblSpringiness.anchor = GridBagConstraints.EAST;
      gbc_lblSpringiness.insets = new Insets(0, 0, 5, 5);
      gbc_lblSpringiness.gridx = 0;
      gbc_lblSpringiness.gridy = 1;
      panel.add(lblSpringiness, gbc_lblSpringiness);

      spnSpringiness = new JSpinner();
      spnSpringiness.setModel(new SpinnerNumberModel(new Float(0), new Float(0), null, new Float(1)));
      spnSpringiness.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent e) {
            flt.getLayout().setSpringiness((Float)spnSpringiness.getValue());
         }
      });
      GridBagConstraints gbc_spnSpringiness = new GridBagConstraints();
      gbc_spnSpringiness.fill = GridBagConstraints.HORIZONTAL;
      gbc_spnSpringiness.insets = new Insets(0, 0, 5, 0);
      gbc_spnSpringiness.gridx = 1;
      gbc_spnSpringiness.gridy = 1;
      panel.add(spnSpringiness, gbc_spnSpringiness);

      JLabel lblIterationInterval = new JLabel("Iteration Interval (ms)");
      GridBagConstraints gbc_lblIterationInterval = new GridBagConstraints();
      gbc_lblIterationInterval.anchor = GridBagConstraints.EAST;
      gbc_lblIterationInterval.insets = new Insets(0, 0, 5, 5);
      gbc_lblIterationInterval.gridx = 0;
      gbc_lblIterationInterval.gridy = 2;
      panel.add(lblIterationInterval, gbc_lblIterationInterval);

      spnInterval = new JSpinner();
      spnInterval.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent arg0) {
            flt.setInterval((Long)spnInterval.getValue());
         }
      });
      spnInterval.setModel(new SpinnerNumberModel(new Long(0), new Long(0), null, new Long(1)));
      GridBagConstraints gbc_spnInterval = new GridBagConstraints();
      gbc_spnInterval.fill = GridBagConstraints.HORIZONTAL;
      gbc_spnInterval.insets = new Insets(0, 0, 5, 0);
      gbc_spnInterval.gridx = 1;
      gbc_spnInterval.gridy = 2;
      panel.add(spnInterval, gbc_spnInterval);

      chckbxEnabled = new JCheckBox("Enabled");
      chckbxEnabled.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            flt.setEnabled(chckbxEnabled.isSelected());
         }
      });
      chckbxEnabled.setHorizontalTextPosition(SwingConstants.LEFT);
      GridBagConstraints gbc_chckbxEnabled = new GridBagConstraints();
      gbc_chckbxEnabled.anchor = GridBagConstraints.EAST;
      gbc_chckbxEnabled.insets = new Insets(0, 0, 5, 0);
      gbc_chckbxEnabled.gridx = 1;
      gbc_chckbxEnabled.gridy = 3;
      panel.add(chckbxEnabled, gbc_chckbxEnabled);
      
      chckbxUseVelocity = new JCheckBox("Use Velocity");
      chckbxUseVelocity.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent arg0) {
            flt.getLayout().setUseVelocity(chckbxUseVelocity.isSelected());
         }
      });
      chckbxUseVelocity.setHorizontalTextPosition(SwingConstants.LEFT);
      GridBagConstraints gbc_chckbxUseVelocity = new GridBagConstraints();
      gbc_chckbxUseVelocity.anchor = GridBagConstraints.EAST;
      gbc_chckbxUseVelocity.gridx = 1;
      gbc_chckbxUseVelocity.gridy = 4;
      panel.add(chckbxUseVelocity, gbc_chckbxUseVelocity);
   }
}
