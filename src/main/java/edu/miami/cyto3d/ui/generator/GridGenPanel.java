package edu.miami.cyto3d.ui.generator;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.miami.cyto3d.graph.generator.GridGenerator;
import edu.miami.cyto3d.graph.model.Graph;
import edu.miami.cyto3d.graph.view.GraphView;

public class GridGenPanel extends GeneratorPanel {

   private GridGenerator generator = new GridGenerator();

   /**
    * Create the panel.
    */
   public GridGenPanel() {

      GridBagLayout gridBagLayout = new GridBagLayout();
      gridBagLayout.columnWidths = new int[] { 98, 0, 0 };
      gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
      gridBagLayout.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
      gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
      setLayout(gridBagLayout);

      JLabel lblNodes = new JLabel("Nodes");

      GridBagConstraints gbc_lblNodes = new GridBagConstraints();
      gbc_lblNodes.anchor = GridBagConstraints.EAST;
      gbc_lblNodes.insets = new Insets(0, 0, 5, 5);
      gbc_lblNodes.gridx = 0;
      gbc_lblNodes.gridy = 0;
      add(lblNodes, gbc_lblNodes);

      final JSpinner spnNodes = new JSpinner();
      spnNodes.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent e) {
            generator.setNumNodes((Integer)spnNodes.getValue());
         }
      });
      spnNodes.setModel(new SpinnerNumberModel(new Integer(50), new Integer(0), null, new Integer(1)));
      spnNodes.setValue(generator.getNumNodes());
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
      spnEdges.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent e) {
            generator.setNumEdges((Integer)spnEdges.getValue());
         }
      });
      spnEdges.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
      spnEdges.setValue(generator.getNumEdges());
      GridBagConstraints gbc_spnEdges = new GridBagConstraints();
      gbc_spnEdges.fill = GridBagConstraints.BOTH;
      gbc_spnEdges.insets = new Insets(0, 0, 5, 0);
      gbc_spnEdges.gridx = 1;
      gbc_spnEdges.gridy = 1;
      add(spnEdges, gbc_spnEdges);

      JLabel lblMaxRadius = new JLabel("Width");
      GridBagConstraints gbc_lblMaxRadius = new GridBagConstraints();
      gbc_lblMaxRadius.anchor = GridBagConstraints.EAST;
      gbc_lblMaxRadius.insets = new Insets(0, 0, 5, 5);
      gbc_lblMaxRadius.gridx = 0;
      gbc_lblMaxRadius.gridy = 2;
      add(lblMaxRadius, gbc_lblMaxRadius);

      final JSpinner spnWidth = new JSpinner();
      spnWidth.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent e) {
            generator.setWidth((Integer)spnWidth.getValue());
         }
      });
      spnWidth.setModel(new SpinnerNumberModel(new Integer(10), new Integer(1), null, new Integer(1)));
      spnWidth.setValue(generator.getWidth());
      GridBagConstraints gbc_spnWidth = new GridBagConstraints();
      gbc_spnWidth.insets = new Insets(0, 0, 5, 0);
      gbc_spnWidth.fill = GridBagConstraints.BOTH;
      gbc_spnWidth.gridx = 1;
      gbc_spnWidth.gridy = 2;
      add(spnWidth, gbc_spnWidth);
      
      JLabel lblLength = new JLabel("Length");
      GridBagConstraints gbc_lblLength = new GridBagConstraints();
      gbc_lblLength.anchor = GridBagConstraints.EAST;
      gbc_lblLength.insets = new Insets(0, 0, 5, 5);
      gbc_lblLength.gridx = 0;
      gbc_lblLength.gridy = 3;
      add(lblLength, gbc_lblLength);
      
      final JSpinner spnLength = new JSpinner();
      spnLength.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent e) {
            generator.setLength((Integer)spnLength.getValue());
         }
      });
      spnLength.setModel(new SpinnerNumberModel(new Integer(10), new Integer(1), null, new Integer(1)));
      spnLength.setValue(generator.getLength());
      GridBagConstraints gbc_spnLength = new GridBagConstraints();
      gbc_spnLength.insets = new Insets(0, 0, 5, 0);
      gbc_spnLength.fill = GridBagConstraints.HORIZONTAL;
      gbc_spnLength.gridx = 1;
      gbc_spnLength.gridy = 3;
      add(spnLength, gbc_spnLength);
      
      JLabel lblSpacing = new JLabel("Spacing");
      GridBagConstraints gbc_lblSpacing = new GridBagConstraints();
      gbc_lblSpacing.anchor = GridBagConstraints.EAST;
      gbc_lblSpacing.insets = new Insets(0, 0, 5, 5);
      gbc_lblSpacing.gridx = 0;
      gbc_lblSpacing.gridy = 4;
      add(lblSpacing, gbc_lblSpacing);
      
      final JSpinner spnSpacing = new JSpinner();
      spnSpacing.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
      spnSpacing.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent e) {
            generator.setSpacing((Integer)spnSpacing.getValue());
         }
      });
      spnSpacing.setValue(generator.getSpacing());
      GridBagConstraints gbc_spnSpacing = new GridBagConstraints();
      gbc_spnSpacing.insets = new Insets(0, 0, 5, 0);
      gbc_spnSpacing.fill = GridBagConstraints.HORIZONTAL;
      gbc_spnSpacing.gridx = 1;
      gbc_spnSpacing.gridy = 4;
      add(spnSpacing, gbc_spnSpacing);
      
      JLabel lblMinEdge = new JLabel("Min. Edge");
      GridBagConstraints gbc_lblMinEdge = new GridBagConstraints();
      gbc_lblMinEdge.anchor = GridBagConstraints.EAST;
      gbc_lblMinEdge.insets = new Insets(0, 0, 5, 5);
      gbc_lblMinEdge.gridx = 0;
      gbc_lblMinEdge.gridy = 5;
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
      gbc_spnMinEdge.gridy = 5;
      add(spnMinEdge, gbc_spnMinEdge);
      
      JLabel lblMaxEdge = new JLabel("Max. Edge");
      GridBagConstraints gbc_lblMaxEdge = new GridBagConstraints();
      gbc_lblMaxEdge.anchor = GridBagConstraints.EAST;
      gbc_lblMaxEdge.insets = new Insets(0, 0, 0, 5);
      gbc_lblMaxEdge.gridx = 0;
      gbc_lblMaxEdge.gridy = 6;
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
      gbc_spnMaxEdge.gridy = 6;
      add(spnMaxEdge, gbc_spnMaxEdge);
   }

   @Override
   public void generateGraph(Graph graph, GraphView view) {
      generator.generate(graph, view);
   }
}
