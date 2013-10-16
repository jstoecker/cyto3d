package edu.miami.cyto3d.ui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ConfigPanel extends JPanel {

   private final JTabbedPane            tabbedPane;

   public void insertTab(String title, int index, Component c) {
      tabbedPane.insertTab(title, null, c, null, index);
      tabbedPane.setSelectedIndex(index);
   }

   public void addTab(String title, Component c) {
      tabbedPane.addTab(title, c);
      tabbedPane.setSelectedIndex(tabbedPane.getTabCount()-1);
   }
   
   public void setTab(int i) {
     tabbedPane.setSelectedIndex(i);
   }

   /**
    * Create the panel.
    */
   public ConfigPanel() {

      initLookAndFeel();

      setLayout(new BorderLayout(0, 0));

      tabbedPane = new JTabbedPane(JTabbedPane.TOP);
      tabbedPane.setOpaque(true);
      add(tabbedPane, BorderLayout.CENTER);
   }

   private void toggleLocked(JComponent component, boolean locked) {
      component.setEnabled(!locked);
      Component[] children = component.getComponents();
      for (int i = 0; i < children.length; i++) {
         if (children[i] instanceof JComponent) {
            JComponent child = (JComponent) children[i];
            toggleLocked(child, locked);
         }
      }
   }

   private void initLookAndFeel() {
      try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (ClassNotFoundException e) {
         e.printStackTrace();
      } catch (InstantiationException e) {
         e.printStackTrace();
      } catch (IllegalAccessException e) {
         e.printStackTrace();
      } catch (UnsupportedLookAndFeelException e) {
         e.printStackTrace();
      }
   }
}
