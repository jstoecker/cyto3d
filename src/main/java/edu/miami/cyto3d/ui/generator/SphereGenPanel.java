package edu.miami.cyto3d.ui.generator;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.miami.cyto3d.graph.generator.SphereGenerator;
import edu.miami.cyto3d.graph.model.Graph;
import edu.miami.cyto3d.graph.view.GraphView;

public class SphereGenPanel extends GeneratorPanel {

   private SphereGenerator generator = new SphereGenerator();

   /**
    * Create the panel.
    */
   public SphereGenPanel() {

      GridBagLayout gridBagLayout = new GridBagLayout();
      gridBagLayout.columnWidths = new int[] { 98, 0, 0 };
      gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
      gridBagLayout.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
      gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
      setLayout(gridBagLayout);

      JLabel lblNodes = new JLabel("Nodes");
      GridBagConstraints gbc_lblNodes = new GridBagConstraints();
      gbc_lblNodes.anchor = GridBagConstraints.EAST;
      gbc_lblNodes.insets = new Insets(0, 0, 5, 5);
      gbc_lblNodes.gridx = 0;
      gbc_lblNodes.gridy = 0;
      add(lblNodes, gbc_lblNodes);

      final JSpinner spnNodes = new JSpinner();
      spnNodes.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
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

      JLabel lblMinRadius = new JLabel("Edges");
      GridBagConstraints gbc_lblMinRadius = new GridBagConstraints();
      gbc_lblMinRadius.anchor = GridBagConstraints.EAST;
      gbc_lblMinRadius.insets = new Insets(0, 0, 5, 5);
      gbc_lblMinRadius.gridx = 0;
      gbc_lblMinRadius.gridy = 1;
      add(lblMinRadius, gbc_lblMinRadius);

      final JSpinner spnEdges = new JSpinner();
      spnEdges.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
      spnEdges.setValue(generator.getNumEdges());
      spnEdges.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent e) {
            generator.setNumEdges((Integer)spnEdges.getValue());
         }
      });
      GridBagConstraints gbc_spnEdges = new GridBagConstraints();
      gbc_spnEdges.fill = GridBagConstraints.BOTH;
      gbc_spnEdges.insets = new Insets(0, 0, 5, 0);
      gbc_spnEdges.gridx = 1;
      gbc_spnEdges.gridy = 1;
      add(spnEdges, gbc_spnEdges);

      JLabel lblMaxRadius = new JLabel("Min. Distance");
      GridBagConstraints gbc_lblMaxRadius = new GridBagConstraints();
      gbc_lblMaxRadius.anchor = GridBagConstraints.EAST;
      gbc_lblMaxRadius.insets = new Insets(0, 0, 5, 5);
      gbc_lblMaxRadius.gridx = 0;
      gbc_lblMaxRadius.gridy = 2;
      add(lblMaxRadius, gbc_lblMaxRadius);

      final JSpinner spnMinD = new JSpinner();
      spnMinD.setModel(new SpinnerNumberModel(new Float(0), new Float(0), null, new Float(1)));
      spnMinD.setValue(generator.getMinDistance());
      spnMinD.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent e) {
            generator.setMinDistance((Float)spnMinD.getValue());
         }
      });
      GridBagConstraints gbc_spnMinD = new GridBagConstraints();
      gbc_spnMinD.insets = new Insets(0, 0, 5, 0);
      gbc_spnMinD.fill = GridBagConstraints.BOTH;
      gbc_spnMinD.gridx = 1;
      gbc_spnMinD.gridy = 2;
      add(spnMinD, gbc_spnMinD);
      
      JLabel lblMaxDistance = new JLabel("Max. Distance");
      GridBagConstraints gbc_lblMaxDistance = new GridBagConstraints();
      gbc_lblMaxDistance.anchor = GridBagConstraints.EAST;
      gbc_lblMaxDistance.insets = new Insets(0, 0, 5, 5);
      gbc_lblMaxDistance.gridx = 0;
      gbc_lblMaxDistance.gridy = 3;
      add(lblMaxDistance, gbc_lblMaxDistance);
      
      final JSpinner spnMaxD = new JSpinner();
      spnMaxD.setModel(new SpinnerNumberModel(new Float(0), new Float(0), null, new Float(1)));
      spnMaxD.setValue(generator.getMaxDistance());
      spnMaxD.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent e) {
            generator.setMaxDistance((Float)spnMaxD.getValue());
         }
      });
      GridBagConstraints gbc_spnMaxD = new GridBagConstraints();
      gbc_spnMaxD.insets = new Insets(0, 0, 5, 0);
      gbc_spnMaxD.fill = GridBagConstraints.HORIZONTAL;
      gbc_spnMaxD.gridx = 1;
      gbc_spnMaxD.gridy = 3;
      add(spnMaxD, gbc_spnMaxD);
      
      JLabel lblMinEdge = new JLabel("Min. Edge");
      GridBagConstraints gbc_lblMinEdge = new GridBagConstraints();
      gbc_lblMinEdge.anchor = GridBagConstraints.EAST;
      gbc_lblMinEdge.insets = new Insets(0, 0, 5, 5);
      gbc_lblMinEdge.gridx = 0;
      gbc_lblMinEdge.gridy = 4;
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
      gbc_spnMinEdge.gridy = 4;
      add(spnMinEdge, gbc_spnMinEdge);
      
      JLabel lblMaxEdge = new JLabel("Max. Edge");
      GridBagConstraints gbc_lblMaxEdge = new GridBagConstraints();
      gbc_lblMaxEdge.anchor = GridBagConstraints.EAST;
      gbc_lblMaxEdge.insets = new Insets(0, 0, 0, 5);
      gbc_lblMaxEdge.gridx = 0;
      gbc_lblMaxEdge.gridy = 5;
      add(lblMaxEdge, gbc_lblMaxEdge);
      
      final JSpinner spnMaxEdge = new JSpinner();
      spnMaxEdge.setModel(new SpinnerNumberModel(new Float(0), new Float(0), null, new Float(1)));
      spnMaxEdge.setValue(generator.getMaxEdgeLength());
      spnMaxEdge.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent e) {
            generator.setMaxEdgeLength((Float)spnMaxEdge.getValue());
         }
      });
      GridBagConstraints gbc_spnMaxEdge = new GridBagConstraints();
      gbc_spnMaxEdge.fill = GridBagConstraints.HORIZONTAL;
      gbc_spnMaxEdge.gridx = 1;
      gbc_spnMaxEdge.gridy = 5;
      add(spnMaxEdge, gbc_spnMaxEdge);
   }

   @Override
   public void generateGraph(Graph graph, GraphView view) {
      generator.generate(graph, view);
   }
}
