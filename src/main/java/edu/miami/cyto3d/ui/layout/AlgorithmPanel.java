package edu.miami.cyto3d.ui.layout;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

public class AlgorithmPanel extends JPanel {

    private static final String FORCE_ALGORITHM    = "Force";

    private final JPanel        algOptionPanel;
    private final ButtonGroup   algTypeButtonGroup = new ButtonGroup();

    ForceOptionPanel            forceOptionPanel;

    public AlgorithmPanel() {

        setLayout(new BorderLayout(0, 0));

        AlgButtonActionListener abActionListener = new AlgButtonActionListener();

        JPanel panel = new JPanel();
        panel.setBorder(new MatteBorder(1, 0, 0, 0, (Color) new Color(192, 192, 192)));
        add(panel);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[] { 140, 1, 0 };
        gbl_panel.rowHeights = new int[] { 51, 0 };
        gbl_panel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
        gbl_panel.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
        panel.setLayout(gbl_panel);

        JPanel algTypePanel = new JPanel();
        algTypePanel.setBorder(new MatteBorder(0, 0, 0, 1, (Color) Color.LIGHT_GRAY));
        GridBagConstraints gbc_algTypePanel = new GridBagConstraints();
        gbc_algTypePanel.insets = new Insets(0, 0, 0, 5);
        gbc_algTypePanel.fill = GridBagConstraints.BOTH;
        gbc_algTypePanel.gridx = 0;
        gbc_algTypePanel.gridy = 0;
        panel.add(algTypePanel, gbc_algTypePanel);
        GridBagLayout gbl_algTypePanel = new GridBagLayout();
        gbl_algTypePanel.columnWidths = new int[] { 0, 0 };
        gbl_algTypePanel.rowHeights = new int[] { 0, 0, 0 };
        gbl_algTypePanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gbl_algTypePanel.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
        algTypePanel.setLayout(gbl_algTypePanel);

        JRadioButton rdbtnNewRadioButton = new JRadioButton(FORCE_ALGORITHM);
        rdbtnNewRadioButton.addActionListener(abActionListener);
        algTypeButtonGroup.add(rdbtnNewRadioButton);
        rdbtnNewRadioButton.setHorizontalTextPosition(SwingConstants.LEFT);
        GridBagConstraints gbc_rdbtnNewRadioButton = new GridBagConstraints();
        gbc_rdbtnNewRadioButton.anchor = GridBagConstraints.EAST;
        gbc_rdbtnNewRadioButton.gridx = 0;
        gbc_rdbtnNewRadioButton.gridy = 1;
        algTypePanel.add(rdbtnNewRadioButton, gbc_rdbtnNewRadioButton);

        algOptionPanel = new JPanel(new CardLayout());
        GridBagConstraints gbc_algOptionPanel = new GridBagConstraints();
        gbc_algOptionPanel.fill = GridBagConstraints.BOTH;
        gbc_algOptionPanel.gridx = 1;
        gbc_algOptionPanel.gridy = 0;
        panel.add(algOptionPanel, gbc_algOptionPanel);
        algOptionPanel.setLayout(new CardLayout(0, 0));
    }

    private class AlgButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            CardLayout cl = (CardLayout) algOptionPanel.getLayout();
            cl.show(algOptionPanel, ((JRadioButton) e.getSource()).getText());
        }
    }
}
