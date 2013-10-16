package edu.miami.cyto3d.ui.generator;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

import edu.miami.cyto3d.graph.model.Graph;
import edu.miami.cyto3d.graph.view.GraphView;

public class GeneratorControlPanel extends JPanel {

   private static final String                   BOX_GRAPH        = "Box";
   private static final String                   SPHERE_GRAPH     = "Sphere";
   private static final String                   GRID_GRAPH       = "Grid";
   private static final String                   CYCLE_GRAPH      = "Cycle";
   private static final String                   DAISY_GRAPH      = "Daisy";

   private final JPanel                          optionPanel;
   private final HashMap<String, GeneratorPanel> panelMap         = new HashMap<String, GeneratorPanel>();
   private final ButtonGroup                     graphButtonGroup = new ButtonGroup();
   private GeneratorPanel                        currentGeneratorPanel;

   Graph                                         graph;
   GraphView                                     view;

   public void setGraph(Graph graph, GraphView view) {
      this.graph = graph;
      this.view = view;
   }

   /**
    * Create the panel.
    */
   public GeneratorControlPanel() {

      final GraphButtonActionListener gbActionListener = new GraphButtonActionListener();
      setLayout(new BorderLayout(0, 0));

      JPanel masterPanel = new JPanel();
      masterPanel.setBorder(new MatteBorder(1, 0, 0, 0, (Color) Color.LIGHT_GRAY));
      add(masterPanel);
      GridBagLayout gbl_masterPanel = new GridBagLayout();
      gbl_masterPanel.columnWidths = new int[] { 102, 84, 0 };
      gbl_masterPanel.rowHeights = new int[] { 135, 0 };
      gbl_masterPanel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
      gbl_masterPanel.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
      masterPanel.setLayout(gbl_masterPanel);

      JPanel buttonPanel = new JPanel();
      GridBagConstraints gbc_buttonPanel = new GridBagConstraints();
      gbc_buttonPanel.fill = GridBagConstraints.VERTICAL;
      gbc_buttonPanel.anchor = GridBagConstraints.EAST;
      gbc_buttonPanel.insets = new Insets(0, 0, 0, 5);
      gbc_buttonPanel.gridx = 0;
      gbc_buttonPanel.gridy = 0;
      masterPanel.add(buttonPanel, gbc_buttonPanel);
      buttonPanel.setBorder(new MatteBorder(0, 0, 0, 1, (Color) new Color(192, 192, 192)));
      GridBagLayout gbl_buttonPanel = new GridBagLayout();
      gbl_buttonPanel.columnWidths = new int[] { 0, 0 };
      gbl_buttonPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
      gbl_buttonPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
      gbl_buttonPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
      buttonPanel.setLayout(gbl_buttonPanel);

      JRadioButton rdbtnBox = new JRadioButton(BOX_GRAPH);
      rdbtnBox.setHorizontalTextPosition(SwingConstants.LEFT);
      rdbtnBox.addActionListener(gbActionListener);
      graphButtonGroup.add(rdbtnBox);
      GridBagConstraints gbc_rdbtnProteins = new GridBagConstraints();
      gbc_rdbtnProteins.anchor = GridBagConstraints.EAST;
      gbc_rdbtnProteins.insets = new Insets(10, 0, 5, 0);
      gbc_rdbtnProteins.gridx = 0;
      gbc_rdbtnProteins.gridy = 0;
      buttonPanel.add(rdbtnBox, gbc_rdbtnProteins);

      JRadioButton rdbtnRandom = new JRadioButton(SPHERE_GRAPH);
      rdbtnRandom.setHorizontalTextPosition(SwingConstants.LEFT);
      rdbtnRandom.addActionListener(gbActionListener);
      graphButtonGroup.add(rdbtnRandom);
      GridBagConstraints gbc_rdbtnRandom = new GridBagConstraints();
      gbc_rdbtnRandom.anchor = GridBagConstraints.EAST;
      gbc_rdbtnRandom.insets = new Insets(0, 0, 5, 0);
      gbc_rdbtnRandom.gridx = 0;
      gbc_rdbtnRandom.gridy = 1;
      buttonPanel.add(rdbtnRandom, gbc_rdbtnRandom);

      JRadioButton rdbtnGrid = new JRadioButton(GRID_GRAPH);
      rdbtnGrid.setHorizontalTextPosition(SwingConstants.LEFT);
      rdbtnGrid.addActionListener(gbActionListener);
      graphButtonGroup.add(rdbtnGrid);
      GridBagConstraints gbc_rdbtnGrid = new GridBagConstraints();
      gbc_rdbtnGrid.anchor = GridBagConstraints.EAST;
      gbc_rdbtnGrid.insets = new Insets(0, 0, 5, 0);
      gbc_rdbtnGrid.gridx = 0;
      gbc_rdbtnGrid.gridy = 2;
      buttonPanel.add(rdbtnGrid, gbc_rdbtnGrid);

      JRadioButton rdbtnCycle = new JRadioButton(CYCLE_GRAPH);
      rdbtnCycle.setHorizontalTextPosition(SwingConstants.LEFT);
      rdbtnCycle.addActionListener(gbActionListener);
      graphButtonGroup.add(rdbtnCycle);
      GridBagConstraints gbc_rdbtnCycle = new GridBagConstraints();
      gbc_rdbtnCycle.anchor = GridBagConstraints.EAST;
      gbc_rdbtnCycle.insets = new Insets(0, 0, 5, 0);
      gbc_rdbtnCycle.gridx = 0;
      gbc_rdbtnCycle.gridy = 3;
      buttonPanel.add(rdbtnCycle, gbc_rdbtnCycle);

      JRadioButton rdbtnDaisy = new JRadioButton(DAISY_GRAPH);
      rdbtnDaisy.setHorizontalTextPosition(SwingConstants.LEFT);
      rdbtnDaisy.addActionListener(gbActionListener);
      graphButtonGroup.add(rdbtnDaisy);
      GridBagConstraints gbc_rdbtnDaisy = new GridBagConstraints();
      gbc_rdbtnDaisy.insets = new Insets(0, 0, 5, 0);
      gbc_rdbtnDaisy.anchor = GridBagConstraints.EAST;
      gbc_rdbtnDaisy.gridx = 0;
      gbc_rdbtnDaisy.gridy = 4;
      buttonPanel.add(rdbtnDaisy, gbc_rdbtnDaisy);

      CardLayout cl_optionPanel = new CardLayout();
      cl_optionPanel.setVgap(10);
      cl_optionPanel.setHgap(10);
      optionPanel = new JPanel(cl_optionPanel);
      optionPanel.setBorder(null);
      optionPanel.setOpaque(false);
      GridBagConstraints gbc_optionPanel = new GridBagConstraints();
      gbc_optionPanel.gridx = 1;
      gbc_optionPanel.gridy = 0;
      gbc_optionPanel.fill = GridBagConstraints.BOTH;
      masterPanel.add(optionPanel, gbc_optionPanel);

      JPanel southPanel = new JPanel();
      southPanel.setBorder(new MatteBorder(1, 0, 0, 0, (Color) Color.LIGHT_GRAY));
      add(southPanel, BorderLayout.SOUTH);
      GridBagLayout gbl_southPanel = new GridBagLayout();
      gbl_southPanel.columnWidths = new int[] { 217, 108, 84, 0 };
      gbl_southPanel.rowHeights = new int[] { 29, 0 };
      gbl_southPanel.columnWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
      gbl_southPanel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
      southPanel.setLayout(gbl_southPanel);

      JButton btnCreate = new JButton("Create");
      GridBagConstraints gbc_btnCreate = new GridBagConstraints();
      gbc_btnCreate.insets = new Insets(3, 0, 3, 0);
      gbc_btnCreate.fill = GridBagConstraints.HORIZONTAL;
      gbc_btnCreate.anchor = GridBagConstraints.NORTH;
      gbc_btnCreate.gridx = 2;
      gbc_btnCreate.gridy = 0;
      southPanel.add(btnCreate, gbc_btnCreate);
      btnCreate.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            currentGeneratorPanel.generateGraph(graph, view);
         }
      });

      addGraphOptionPanel(new BoxGenPanel(), BOX_GRAPH);
      addGraphOptionPanel(new CycleGenPanel(), CYCLE_GRAPH);
      addGraphOptionPanel(new DaisyGenPanel(), DAISY_GRAPH);
      addGraphOptionPanel(new SphereGenPanel(), SPHERE_GRAPH);
      addGraphOptionPanel(new GridGenPanel(), GRID_GRAPH);

      rdbtnBox.doClick();
   }

   private void addGraphOptionPanel(GeneratorPanel gop, String name) {
      optionPanel.add(gop, name);
      panelMap.put(name, gop);
   }

   private class GraphButtonActionListener implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
         CardLayout cl = (CardLayout) optionPanel.getLayout();
         String graphName = ((JRadioButton) e.getSource()).getText();
         cl.show(optionPanel, graphName);
         currentGeneratorPanel = panelMap.get(graphName);
      }
   }
}
