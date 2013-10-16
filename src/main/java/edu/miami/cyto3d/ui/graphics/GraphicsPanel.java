package edu.miami.cyto3d.ui.graphics;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class GraphicsPanel extends JPanel {

   private final BloomPanel        bloomPanel;
   private final ProteinStylePanel colorPanel;
   
   public BloomPanel getBloomPanel() {
      return bloomPanel;
   }

   public ProteinStylePanel getColorPanel() {
      return colorPanel;
   }

   /**
    * Create the panel.
    */
   public GraphicsPanel() {
      setLayout(new BorderLayout(0, 0));
      JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
      add(tabbedPane, BorderLayout.CENTER);

      bloomPanel = new BloomPanel();
      tabbedPane.addTab("Bloom", null, bloomPanel, null);

      colorPanel = new ProteinStylePanel();
      tabbedPane.addTab("Color", null, colorPanel, null);
   }
}
