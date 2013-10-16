package edu.miami.cyto3d.ui.graphics;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.SoftBevelBorder;

import edu.miami.cyto3d.graph.style.PINStyle;
import edu.miami.math.vector.RVec3;
import edu.miami.math.vector.RVec4;
import edu.miami.math.vector.Vec3;
import edu.miami.math.vector.Vec4;

public class ProteinStylePanel extends JPanel {

   private JPanel   pNodeMax;
   private JPanel   pNodeMin;
   private JButton  btnEdgeShort;
   private JButton  btnEdgeLong;
   private JPanel   pEdgeShort;
   private JPanel   pEdgeLong;

   private PINStyle style;

   public void setStyle(PINStyle style) {
      this.style = style;
      pNodeMax.setBackground(toColor(style.getLargeNodeColor()));
      pNodeMin.setBackground(toColor(style.getSmallNodeColor()));
      pEdgeLong.setBackground(toColor(style.getLongEdgeColor()));
      pEdgeShort.setBackground(toColor(style.getShortEdgeColor()));
   }

   private static Vec3 toVec3(Color color) {
      float r = color.getRed() / 255.0f;
      float g = color.getGreen() / 255.0f;
      float b = color.getBlue() / 255.0f;
      return new Vec3(r, g, b);
   }

   private static Color toColor(RVec3 color) {
      return new Color(color.x(), color.y(), color.z());
   }

   private static Color toColor(RVec4 color) {
      return new Color(color.x(), color.y(), color.z(), color.w());
   }

   private static Vec4 toVec4(Color color) {
      float r = color.getRed() / 255.0f;
      float g = color.getGreen() / 255.0f;
      float b = color.getBlue() / 255.0f;
      return new Vec4(r, g, b, 1.0f);
   }

   /**
    * Create the panel.
    */
   public ProteinStylePanel() {
      setBorder(new MatteBorder(1, 0, 0, 0, (Color) Color.LIGHT_GRAY));
      GridBagLayout gridBagLayout = new GridBagLayout();
      gridBagLayout.columnWidths = new int[] { 148, 0, 0 };
      gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
      gridBagLayout.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
      gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
      setLayout(gridBagLayout);

      JButton btnNodeDim = new JButton("Node Small");
      btnNodeDim.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            Color color = JColorChooser.showDialog(null, "Small Node Color",
                  pNodeMin.getBackground());
            if (color != null) {
               pNodeMin.setBackground(color);
               style.setSmallNodeColor(toVec4(color));
            }
         }
      });
      GridBagConstraints gbc_btnNodeDim = new GridBagConstraints();
      gbc_btnNodeDim.fill = GridBagConstraints.HORIZONTAL;
      gbc_btnNodeDim.insets = new Insets(0, 0, 5, 5);
      gbc_btnNodeDim.gridx = 0;
      gbc_btnNodeDim.gridy = 0;
      add(btnNodeDim, gbc_btnNodeDim);

      pNodeMin = new JPanel();
      pNodeMin.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
      pNodeMin.setBackground(Color.GREEN);
      GridBagConstraints gbc_pNodeMin = new GridBagConstraints();
      gbc_pNodeMin.insets = new Insets(0, 0, 5, 0);
      gbc_pNodeMin.fill = GridBagConstraints.BOTH;
      gbc_pNodeMin.gridx = 1;
      gbc_pNodeMin.gridy = 0;
      add(pNodeMin, gbc_pNodeMin);

      JButton btnNodeMax = new JButton("Node Large");
      btnNodeMax.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            Color color = JColorChooser.showDialog(null, "Large Node Color",
                  pNodeMax.getBackground());
            if (color != null) {
               pNodeMax.setBackground(color);
               style.setLargeNodeColor(toVec4(color));
            }
         }
      });
      GridBagConstraints gbc_btnNodeMax = new GridBagConstraints();
      gbc_btnNodeMax.fill = GridBagConstraints.HORIZONTAL;
      gbc_btnNodeMax.insets = new Insets(0, 0, 5, 5);
      gbc_btnNodeMax.gridx = 0;
      gbc_btnNodeMax.gridy = 1;
      add(btnNodeMax, gbc_btnNodeMax);

      pNodeMax = new JPanel();
      pNodeMax.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
      pNodeMax.setBackground(Color.BLUE);
      GridBagConstraints gbc_pNodeMax = new GridBagConstraints();
      gbc_pNodeMax.insets = new Insets(0, 0, 5, 0);
      gbc_pNodeMax.fill = GridBagConstraints.BOTH;
      gbc_pNodeMax.gridx = 1;
      gbc_pNodeMax.gridy = 1;
      add(pNodeMax, gbc_pNodeMax);

      btnEdgeShort = new JButton("Edge Short");
      btnEdgeShort.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            Color color = JColorChooser.showDialog(null, "Short Edge Color",
                  pEdgeShort.getBackground());
            if (color != null) {
               pEdgeShort.setBackground(color);
               style.setShortEdgeColor(toVec4(color));
            }
         }
      });
      GridBagConstraints gbc_btnEdgeShort = new GridBagConstraints();
      gbc_btnEdgeShort.fill = GridBagConstraints.HORIZONTAL;
      gbc_btnEdgeShort.insets = new Insets(0, 0, 5, 5);
      gbc_btnEdgeShort.gridx = 0;
      gbc_btnEdgeShort.gridy = 2;
      add(btnEdgeShort, gbc_btnEdgeShort);

      pEdgeShort = new JPanel();
      GridBagConstraints gbc_pEdgeShort = new GridBagConstraints();
      gbc_pEdgeShort.insets = new Insets(0, 0, 5, 0);
      gbc_pEdgeShort.fill = GridBagConstraints.BOTH;
      gbc_pEdgeShort.gridx = 1;
      gbc_pEdgeShort.gridy = 2;
      add(pEdgeShort, gbc_pEdgeShort);

      btnEdgeLong = new JButton("Edge Long");
      btnEdgeLong.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            Color color = JColorChooser.showDialog(null, "Long Edge Color",
                  pEdgeLong.getBackground());
            if (color != null) {
               pEdgeLong.setBackground(color);
               style.setLongEdgeColor(toVec4(color));
            }
         }
      });
      GridBagConstraints gbc_btnEdgeLong = new GridBagConstraints();
      gbc_btnEdgeLong.fill = GridBagConstraints.HORIZONTAL;
      gbc_btnEdgeLong.insets = new Insets(0, 0, 5, 5);
      gbc_btnEdgeLong.gridx = 0;
      gbc_btnEdgeLong.gridy = 3;
      add(btnEdgeLong, gbc_btnEdgeLong);

      pEdgeLong = new JPanel();
      GridBagConstraints gbc_pEdgeLong = new GridBagConstraints();
      gbc_pEdgeLong.insets = new Insets(0, 0, 5, 0);
      gbc_pEdgeLong.fill = GridBagConstraints.BOTH;
      gbc_pEdgeLong.gridx = 1;
      gbc_pEdgeLong.gridy = 3;
      add(pEdgeLong, gbc_pEdgeLong);

   }
}
