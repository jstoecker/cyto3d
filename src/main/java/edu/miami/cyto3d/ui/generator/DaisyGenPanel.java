package edu.miami.cyto3d.ui.generator;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.miami.cyto3d.graph.generator.DaisyGenerator;
import edu.miami.cyto3d.graph.model.Graph;
import edu.miami.cyto3d.graph.view.GraphView;

public class DaisyGenPanel extends GeneratorPanel {

   private DaisyGenerator generator = new DaisyGenerator();

   /**
    * Create the panel.
    */
   public DaisyGenPanel() {

      GridBagLayout gridBagLayout = new GridBagLayout();
      gridBagLayout.columnWidths = new int[] { 98, 0, 0 };
      gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
      gridBagLayout.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
      gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
      setLayout(gridBagLayout);

      JLabel lblNodes = new JLabel("Nodes");
      GridBagConstraints gbc_lblNodes = new GridBagConstraints();
      gbc_lblNodes.anchor = GridBagConstraints.EAST;
      gbc_lblNodes.insets = new Insets(0, 0, 5, 5);
      gbc_lblNodes.gridx = 0;
      gbc_lblNodes.gridy = 0;
      add(lblNodes, gbc_lblNodes);

      final JSpinner spnNodes = new JSpinner();
      spnNodes
            .setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
      spnNodes.setValue(generator.getNumNodes());
      spnNodes.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent e) {
            generator.setNumNodes((Integer)spnNodes.getValue());
         }
      });
      GridBagConstraints gbc_spnNodes = new GridBagConstraints();
      gbc_spnNodes.fill = GridBagConstraints.HORIZONTAL;
      gbc_spnNodes.insets = new Insets(0, 0, 5, 0);
      gbc_spnNodes.gridx = 1;
      gbc_spnNodes.gridy = 0;
      add(spnNodes, gbc_spnNodes);

      JLabel lblMinRadius = new JLabel("Min. Radius");
      GridBagConstraints gbc_lblMinRadius = new GridBagConstraints();
      gbc_lblMinRadius.anchor = GridBagConstraints.EAST;
      gbc_lblMinRadius.insets = new Insets(0, 0, 5, 5);
      gbc_lblMinRadius.gridx = 0;
      gbc_lblMinRadius.gridy = 1;
      add(lblMinRadius, gbc_lblMinRadius);

      final JSpinner spnMinR = new JSpinner();
      spnMinR.setModel(new SpinnerNumberModel(new Float(0), new Float(0), null, new Float(1)));
      spnMinR.setValue(generator.getMinRadius());
      spnMinR.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent e) {
            generator.setMinRadius((Float) spnMinR.getValue());
         }
      });
      GridBagConstraints gbc_spnMinR = new GridBagConstraints();
      gbc_spnMinR.fill = GridBagConstraints.BOTH;
      gbc_spnMinR.insets = new Insets(0, 0, 5, 0);
      gbc_spnMinR.gridx = 1;
      gbc_spnMinR.gridy = 1;
      add(spnMinR, gbc_spnMinR);

      JLabel lblMaxRadius = new JLabel("Max. Radius");
      GridBagConstraints gbc_lblMaxRadius = new GridBagConstraints();
      gbc_lblMaxRadius.anchor = GridBagConstraints.EAST;
      gbc_lblMaxRadius.insets = new Insets(0, 0, 5, 5);
      gbc_lblMaxRadius.gridx = 0;
      gbc_lblMaxRadius.gridy = 2;
      add(lblMaxRadius, gbc_lblMaxRadius);

      final JSpinner spnMaxR = new JSpinner();
      spnMaxR.setModel(new SpinnerNumberModel(new Float(0), new Float(0), null, new Float(1)));
      spnMaxR.setValue(generator.getMaxRadius());
      spnMaxR.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent e) {
            generator.setMaxRadius((Float) spnMaxR.getValue());
         }
      });
      GridBagConstraints gbc_spnMaxR = new GridBagConstraints();
      gbc_spnMaxR.insets = new Insets(0, 0, 5, 0);
      gbc_spnMaxR.fill = GridBagConstraints.BOTH;
      gbc_spnMaxR.gridx = 1;
      gbc_spnMaxR.gridy = 2;
      add(spnMaxR, gbc_spnMaxR);
      
      JLabel lblMinEdge = new JLabel("Min. Edge");
      GridBagConstraints gbc_lblMinEdge = new GridBagConstraints();
      gbc_lblMinEdge.anchor = GridBagConstraints.EAST;
      gbc_lblMinEdge.insets = new Insets(0, 0, 5, 5);
      gbc_lblMinEdge.gridx = 0;
      gbc_lblMinEdge.gridy = 3;
      add(lblMinEdge, gbc_lblMinEdge);
      
      final JSpinner spnMinEdge = new JSpinner();
      spnMinEdge.setModel(new SpinnerNumberModel(new Float(0), new Float(0), null, new Float(1)));
      spnMinEdge.setValue(generator.getMinEdgeLength());
      spnMinEdge.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent e) {
            generator.setMinEdgeLength((Float)spnMinEdge.getValue());
         }
      });
      GridBagConstraints gbc_spnMinEdge = new GridBagConstraints();
      gbc_spnMinEdge.fill = GridBagConstraints.HORIZONTAL;
      gbc_spnMinEdge.insets = new Insets(0, 0, 5, 0);
      gbc_spnMinEdge.gridx = 1;
      gbc_spnMinEdge.gridy = 3;
      add(spnMinEdge, gbc_spnMinEdge);
      
      JLabel lblMaxEdge = new JLabel("Max. Edge");
      GridBagConstraints gbc_lblMaxEdge = new GridBagConstraints();
      gbc_lblMaxEdge.anchor = GridBagConstraints.EAST;
      gbc_lblMaxEdge.insets = new Insets(0, 0, 0, 5);
      gbc_lblMaxEdge.gridx = 0;
      gbc_lblMaxEdge.gridy = 4;
      add(lblMaxEdge, gbc_lblMaxEdge);
      
      final JSpinner spnMaxEdge = new JSpinner();
      spnMaxEdge.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent e) {
            generator.setMaxEdgeLength((Float)spnMaxEdge.getValue());
         }
      });
      spnMaxEdge.setModel(new SpinnerNumberModel(new Float(0), new Float(0), null, new Float(1)));
      spnMaxEdge.setValue(generator.getMaxEdgeLength());
      GridBagConstraints gbc_spnMaxEdge = new GridBagConstraints();
      gbc_spnMaxEdge.fill = GridBagConstraints.HORIZONTAL;
      gbc_spnMaxEdge.gridx = 1;
      gbc_spnMaxEdge.gridy = 4;
      add(spnMaxEdge, gbc_spnMaxEdge);

   }

   @Override
   public void generateGraph(Graph graph, GraphView view) {
      generator.generate(graph, view);
   }
}
