package edu.miami.cyto3d.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;

import edu.miami.cyto3d.C3DApp;
import edu.miami.cyto3d.GraphMapper;
import edu.miami.cyto3d.graph.model.Graph;
import edu.miami.cyto3d.graph.view.GraphView;

public class C3DPanel extends JPanel {

    private JComboBox    cbNetwork;
    private JComboBox    cbELAttribute;
    private JCheckBox    chckbxFixedEdgeLengths;
    private JPanel       panel;
    private JLabel       lblMinEdgeLength;
    private JLabel       lblMaxEdgeLength;
    private JSpinner     spnMinEdge;
    private JSpinner     spnMaxEdge;
    private JPanel       panel_1;

    private Graph        graph;
    private GraphView    graphView;

    private final C3DApp app;

    public void setGraph(Graph graph, GraphView view) {
        this.graph = graph;
        this.graphView = view;
    }

    public C3DPanel(final C3DApp plugin) {
        this.app = plugin;
        setLayout(new BorderLayout(0, 0));

        panel = new JPanel();
        add(panel, BorderLayout.CENTER);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[] { 149, 137, 0 };
        gbl_panel.rowHeights = new int[] { 27, 29, 0, 0, 0, 0, 0 };
        gbl_panel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
        gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        panel.setLayout(gbl_panel);

        JLabel lblNetwork = new JLabel("Network");
        GridBagConstraints gbc_lblNetwork = new GridBagConstraints();
        gbc_lblNetwork.insets = new Insets(0, 0, 5, 5);
        gbc_lblNetwork.anchor = GridBagConstraints.EAST;
        gbc_lblNetwork.gridx = 0;
        gbc_lblNetwork.gridy = 0;
        panel.add(lblNetwork, gbc_lblNetwork);

        cbNetwork = new JComboBox();
        GridBagConstraints gbc_cbNetwork = new GridBagConstraints();
        gbc_cbNetwork.fill = GridBagConstraints.HORIZONTAL;
        gbc_cbNetwork.anchor = GridBagConstraints.NORTH;
        gbc_cbNetwork.insets = new Insets(0, 0, 5, 0);
        gbc_cbNetwork.gridx = 1;
        gbc_cbNetwork.gridy = 0;
        panel.add(cbNetwork, gbc_cbNetwork);

        JLabel lblEdgeLengthAttribute = new JLabel("Edge Length");
        GridBagConstraints gbc_lblEdgeLengthAttribute = new GridBagConstraints();
        gbc_lblEdgeLengthAttribute.anchor = GridBagConstraints.EAST;
        gbc_lblEdgeLengthAttribute.insets = new Insets(0, 0, 5, 5);
        gbc_lblEdgeLengthAttribute.gridx = 0;
        gbc_lblEdgeLengthAttribute.gridy = 1;
        panel.add(lblEdgeLengthAttribute, gbc_lblEdgeLengthAttribute);

        cbELAttribute = new JComboBox();
        GridBagConstraints gbc_cbELAttribute = new GridBagConstraints();
        gbc_cbELAttribute.fill = GridBagConstraints.HORIZONTAL;
        gbc_cbELAttribute.insets = new Insets(0, 0, 5, 0);
        gbc_cbELAttribute.anchor = GridBagConstraints.NORTH;
        gbc_cbELAttribute.gridx = 1;
        gbc_cbELAttribute.gridy = 1;
        panel.add(cbELAttribute, gbc_cbELAttribute);

        lblMinEdgeLength = new JLabel("Min. Edge Length");
        GridBagConstraints gbc_lblMinEdgeLength = new GridBagConstraints();
        gbc_lblMinEdgeLength.anchor = GridBagConstraints.EAST;
        gbc_lblMinEdgeLength.insets = new Insets(0, 0, 5, 5);
        gbc_lblMinEdgeLength.gridx = 0;
        gbc_lblMinEdgeLength.gridy = 2;
        panel.add(lblMinEdgeLength, gbc_lblMinEdgeLength);

        spnMinEdge = new JSpinner();
        spnMinEdge.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null,
                new Integer(1)));
        GridBagConstraints gbc_spnMinEdge = new GridBagConstraints();
        gbc_spnMinEdge.fill = GridBagConstraints.HORIZONTAL;
        gbc_spnMinEdge.insets = new Insets(0, 0, 5, 0);
        gbc_spnMinEdge.gridx = 1;
        gbc_spnMinEdge.gridy = 2;
        panel.add(spnMinEdge, gbc_spnMinEdge);

        lblMaxEdgeLength = new JLabel("Max. Edge Length");
        GridBagConstraints gbc_lblMaxEdgeLength = new GridBagConstraints();
        gbc_lblMaxEdgeLength.anchor = GridBagConstraints.EAST;
        gbc_lblMaxEdgeLength.insets = new Insets(0, 0, 5, 5);
        gbc_lblMaxEdgeLength.gridx = 0;
        gbc_lblMaxEdgeLength.gridy = 3;
        panel.add(lblMaxEdgeLength, gbc_lblMaxEdgeLength);

        spnMaxEdge = new JSpinner();
        spnMaxEdge.setModel(new SpinnerNumberModel(new Integer(5), new Integer(1), null,
                new Integer(1)));
        GridBagConstraints gbc_spnMaxEdge = new GridBagConstraints();
        gbc_spnMaxEdge.fill = GridBagConstraints.HORIZONTAL;
        gbc_spnMaxEdge.insets = new Insets(0, 0, 5, 0);
        gbc_spnMaxEdge.gridx = 1;
        gbc_spnMaxEdge.gridy = 3;
        panel.add(spnMaxEdge, gbc_spnMaxEdge);

        chckbxFixedEdgeLengths = new JCheckBox("Equal Edge Lengths");
        GridBagConstraints gbc_chckbxFixedEdgeLengths = new GridBagConstraints();
        gbc_chckbxFixedEdgeLengths.anchor = GridBagConstraints.EAST;
        gbc_chckbxFixedEdgeLengths.insets = new Insets(0, 0, 5, 0);
        gbc_chckbxFixedEdgeLengths.gridx = 1;
        gbc_chckbxFixedEdgeLengths.gridy = 4;
        panel.add(chckbxFixedEdgeLengths, gbc_chckbxFixedEdgeLengths);
        chckbxFixedEdgeLengths.setHorizontalTextPosition(SwingConstants.LEFT);

        panel_1 = new JPanel();
        add(panel_1, BorderLayout.SOUTH);

        JButton btnView = new JButton("View");
        panel_1.add(btnView);
        btnView.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                String attribute = (String) cbELAttribute.getSelectedItem();

                // CyNetwork network = Cytoscape.getNetwork((String) cbNetwork.getSelectedItem());

                CyNetwork network = plugin.getAppManager().getCurrentNetwork();

                float minEL = (Integer) spnMinEdge.getValue();
                float maxEL = (Integer) spnMaxEdge.getValue();
                new GraphMapper(network, attribute, plugin).map(minEL, maxEL);
            }
        });
    }

    public void update() {
        // TODO: change to listener

        CyNetwork network = app.getAppManager().getCurrentNetwork();
        
        cbNetwork.removeAllItems();
        cbNetwork.addItem(network);
        
        cbELAttribute.removeAllItems();
        cbELAttribute.addItem("Ivalue");


        // cbELAttribute.removeAllItems();
        // String[] edgeAttributes = Cytoscape.getEdgeAttributes().getAttributeNames();
        // for (String attribute : edgeAttributes) {
        // cbELAttribute.addItem(attribute);
        // System.out.println(attribute);
        // }
    }
}
