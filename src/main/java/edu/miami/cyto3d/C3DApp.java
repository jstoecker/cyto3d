package edu.miami.cyto3d;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.cytoscape.application.CyApplicationManager;

import edu.miami.cyto3d.graph.model.BasicGraph;
import edu.miami.cyto3d.graph.model.Graph;
import edu.miami.cyto3d.graph.style.PINStyle;
import edu.miami.cyto3d.graph.test.MoviePanel;
import edu.miami.cyto3d.graph.view.BasicGraphView;
import edu.miami.cyto3d.graph.view.GraphView;
import edu.miami.cyto3d.ui.C3DPanel;
import edu.miami.cyto3d.ui.ConfigPanel;
import edu.miami.cyto3d.ui.layout.ForceOptionPanel;

/**
 * Main class for Cyto3D app. Stores all the state.
 * 
 * @author justin
 */
public class C3DApp {

    public static final String APP_NAME    = "Cyto3D";

    // main graph model : this may be changed by the viewer at any time through the user interface
    Graph                      graph;
    GraphView                  graphView;
    GraphViewer                viewer;

    // configuration dialog for creating and modifying the current graph and its view
    final JDialog              configDialog;
    final ConfigPanel          configPanel = new ConfigPanel();
    final C3DPanel             c3dPanel    = new C3DPanel(this);
    final ForceOptionPanel     forcePanel  = new ForceOptionPanel();
    final JDialog              graphWindow;

    final CyApplicationManager appManager;

    public C3DApp(CyApplicationManager appManager, JFrame mainFrame) {
        this.appManager = appManager;
        
        configDialog = new JDialog(mainFrame);
        configDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        configDialog.getContentPane().add(configPanel);
        configPanel.insertTab("Network", 0, c3dPanel);
        configPanel.insertTab("Layout", 1, forcePanel);
//        configPanel.insertTab("Movie", 2, new MoviePanel(this));
        configPanel.setTab(0);
        configDialog.pack();

        graph = new BasicGraph();
        graphView = new BasicGraphView(graph);
        graph.addListener(graphView);
        viewer = new GraphViewer();

        graphWindow = new JDialog(mainFrame);
        graphWindow.setSize(600, 600);
        graphWindow.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        graphWindow.add(viewer.getCanvas());
    }

    public Graph getGraph() {
        return graph;
    }

    public GraphView getGraphView() {
        return graphView;
    }

    public GraphViewer getViewer() {
        return viewer;
    }

    public CyApplicationManager getAppManager() {
        return appManager;
    }
    
    public void setNewGraph(Graph graph, GraphView view, boolean applyStyle) {
        this.graph = graph;
        this.graphView = view;
        viewer.setGraph(graph, view);
        graphWindow.setTitle(graph.getAttributes().get("name", "Graph 3D"));

        if (applyStyle)
            new PINStyle().apply(graph, view);

        graphWindow.setVisible(true);

        forcePanel.setForceLayoutThread(viewer.getForceLayout());
    }

    public void showSettings() {
        configDialog.setVisible(true);
        c3dPanel.update();
    }
}
