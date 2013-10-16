package edu.miami.cyto3d.ui.generator;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.miami.cyto3d.graph.generator.CycleGenerator;
import edu.miami.cyto3d.graph.model.Graph;
import edu.miami.cyto3d.graph.view.GraphView;

public class CycleGenPanel extends GeneratorPanel {

   private CycleGenerator generator = new CycleGenerator();
   
   /**
    * Create the panel.
    */
   public CycleGenPanel() {
      
      GridBagLayout gridBagLayout = new GridBagLayout();
      gridBagLayout.columnWidths = new int[]{98, 0, 0};
      gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
      gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
      gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
      setLayout(gridBagLayout);
      
      JLabel lblNodes = new JLabel("Nodes");
      GridBagConstraints gbc_lblNodes = new GridBagConstraints();
      gbc_lblNodes.anchor = GridBagConstraints.EAST;
      gbc_lblNodes.insets = new Insets(0, 0, 5, 5);
      gbc_lblNodes.gridx = 0;
      gbc_lblNodes.gridy = 0;
      add(lblNodes, gbc_lblNodes);
      
      final JSpinner spnNodes = new JSpinner();
      spnNodes.setModel(new SpinnerNumberModel(new Integer(10), new Integer(0), null, new Integer(1)));
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
      
      JLabel lblMinRadius = new JLabel("Radius");
      GridBagConstraints gbc_lblMinRadius = new GridBagConstraints();
      gbc_lblMinRadius.anchor = GridBagConstraints.EAST;
      gbc_lblMinRadius.insets = new Insets(0, 0, 5, 5);
      gbc_lblMinRadius.gridx = 0;
      gbc_lblMinRadius.gridy = 1;
      add(lblMinRadius, gbc_lblMinRadius);
      
      final JSpinner spnRadius = new JSpinner();
      spnRadius.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
      spnRadius.setValue(generator.getRadius());
      spnRadius.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent arg0) {
            generator.setRadius((Float)spnRadius.getValue());
         }
      });
      GridBagConstraints gbc_spnRadius = new GridBagConstraints();
      gbc_spnRadius.fill = GridBagConstraints.BOTH;
      gbc_spnRadius.insets = new Insets(0, 0, 5, 0);
      gbc_spnRadius.gridx = 1;
      gbc_spnRadius.gridy = 1;
      add(spnRadius, gbc_spnRadius);
      
      JLabel lblMaxRadius = new JLabel("Variation");
      GridBagConstraints gbc_lblMaxRadius = new GridBagConstraints();
      gbc_lblMaxRadius.anchor = GridBagConstraints.EAST;
      gbc_lblMaxRadius.insets = new Insets(0, 0, 5, 5);
      gbc_lblMaxRadius.gridx = 0;
      gbc_lblMaxRadius.gridy = 2;
      add(lblMaxRadius, gbc_lblMaxRadius);
      
      final JSpinner spnVariation = new JSpinner();
      spnVariation.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
      spnVariation.setValue(generator.getVariation());
      spnVariation.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent e) {
            generator.setVariation((Float)spnVariation.getValue());
         }
      });
      GridBagConstraints gbc_spnVariation = new GridBagConstraints();
      gbc_spnVariation.insets = new Insets(0, 0, 5, 0);
      gbc_spnVariation.fill = GridBagConstraints.BOTH;
      gbc_spnVariation.gridx = 1;
      gbc_spnVariation.gridy = 2;
      add(spnVariation, gbc_spnVariation);
      
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
      gbc_spnMaxEdge.gridy = 4;
      add(spnMaxEdge, gbc_spnMaxEdge);
   }

   @Override
   public void generateGraph(Graph graph, GraphView view) {
      generator.generate(graph, view);
   }
}
