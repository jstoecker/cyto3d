package edu.miami.cyto3d.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.cytoscape.model.CyNetwork;

import edu.miami.cyto3d.C3DApp;
import edu.miami.cyto3d.GraphMapper;
import edu.miami.cyto3d.graph.layout.force.ForceLayoutThread;
import edu.miami.cyto3d.graph.model.Graph;
import edu.miami.cyto3d.graph.view.GraphView;
import edu.miami.cyto3d.util.ColorPanel;
import edu.miami.cyto3d.util.PropertyLayout;
import edu.miami.math.vector.Vec4;

public class C3DPanel extends JPanel {

    private JComboBox         cbNetwork;
    private JTextField        tfEdgeAttribute;
    private JSpinner          spnMinEdgeLength;
    private JSpinner          spnMaxEdgeLength;
    private JSpinner          spnDamping;
    private JSpinner          spnSpringiness;
    private JSpinner          spnIteration;
    private JCheckBox         chkReverseEdgeMapping;
    private JCheckBox         chkLayoutActive;
    private ColorPanel        colMinEdge;
    private ColorPanel        colMaxEdge;

    private ForceLayoutThread flt;
    private final C3DApp      app;

    public void setGraph(Graph graph, GraphView view) {
    }

    public C3DPanel(final C3DApp plugin) {
        this.app = plugin;
        
        setLayout(new BorderLayout());

        JPanel settingsPanel = new JPanel();
        PropertyLayout layout = new PropertyLayout();
        layout.add("Network", cbNetwork = new JComboBox());
        layout.add("Edge Attribute", tfEdgeAttribute = new JTextField("Ivalue"));
        layout.add(chkReverseEdgeMapping = new JCheckBox("Reverse Edge Length", true));
        layout.add("Min Edge Length", spnMinEdgeLength = new JSpinner(new SpinnerNumberModel(1.0f, 0.1f, 1000.0f, 1.0f)));
        layout.add("Min Edge Color", colMinEdge = new ColorPanel("Min Edge Length Color", plugin.getPinStyle().getShortEdgeColor()));
        layout.add("Max Edge Length", spnMaxEdgeLength = new JSpinner(new SpinnerNumberModel(5.0f, 0.1f, 1000.0f, 1.0f)));
        layout.add("Max Edge Color", colMaxEdge = new ColorPanel("Max Edge Length Color", plugin.getPinStyle().getLongEdgeColor()));
        layout.add("Damping", spnDamping = new JSpinner(new SpinnerNumberModel(0.9f, 0.0f, 1.0f, 0.1f)));
        layout.add("Springiness", spnSpringiness = new JSpinner(new SpinnerNumberModel(0.1f, 0.1f, 5.0f, 0.1f)));
        layout.add("Iteration MS", spnIteration = new JSpinner(new SpinnerNumberModel(20, 0, 1000, 10)));
        layout.add(chkLayoutActive = new JCheckBox("Layout Active", true));
        
        layout.apply(settingsPanel);
        add(settingsPanel, BorderLayout.NORTH);

        JButton viewButton = new JButton("View");
        add(viewButton, BorderLayout.SOUTH);

        // Action Listeners --------------
        
        
        
        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                 CyNetwork network = plugin.getAppManager().getCurrentNetwork();
                 String attribute = tfEdgeAttribute.getText();
                 float minEL = ((SpinnerNumberModel)spnMinEdgeLength.getModel()).getNumber().floatValue();
                 float maxEL = ((SpinnerNumberModel)spnMaxEdgeLength.getModel()).getNumber().floatValue();
                 new GraphMapper(network, attribute, plugin).map(minEL, maxEL, chkReverseEdgeMapping.isSelected());
            }
        });
        
        spnDamping.getModel().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                float damping = ((SpinnerNumberModel)spnDamping.getModel()).getNumber().floatValue();
                flt.getLayout().setDamping(damping);
            }
        });
        
        spnSpringiness.getModel().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                float springiness = ((SpinnerNumberModel)spnSpringiness.getModel()).getNumber().floatValue();
                flt.getLayout().setSpringiness(springiness);
            }
        });
        
        spnIteration.getModel().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int interval = ((SpinnerNumberModel)spnIteration.getModel()).getNumber().intValue();
                flt.setInterval(interval);
            }
        });
        
        chkLayoutActive.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                flt.setEnabled(chkLayoutActive.isSelected());
            }
        });
        
        colMinEdge.addListener(new ColorPanel.Listener() {
            public void colorChanged(ColorPanel panel, Vec4 newColor) {
                plugin.getPinStyle().setShortEdgeColor(newColor);
                plugin.getPinStyle().apply(plugin.getGraph(), plugin.getGraphView());
            }
        });
        
        colMaxEdge.addListener(new ColorPanel.Listener() {
            public void colorChanged(ColorPanel panel, Vec4 newColor) {
                plugin.getPinStyle().setLongEdgeColor(newColor);
                plugin.getPinStyle().apply(plugin.getGraph(), plugin.getGraphView());
            }
        });
    }
    
    public void setForceLayoutThread(ForceLayoutThread flt) {
       this.flt = flt;
       spnIteration.setValue(flt.getInterval());
       spnDamping.setValue(flt.getLayout().getDamping());
       spnSpringiness.setValue(flt.getLayout().getSpringiness());
       chkLayoutActive.setSelected(flt.isEnabled());
    }

    public void update() {
        // TODO: change to listener

        cbNetwork.removeAllItems();
        cbNetwork.addItem(app.getAppManager().getCurrentNetwork());
    }
}
