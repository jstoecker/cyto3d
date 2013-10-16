package edu.miami.cyto3d.ui.generator;

import javax.swing.JPanel;

import edu.miami.cyto3d.graph.model.Graph;
import edu.miami.cyto3d.graph.view.GraphView;

public abstract class GeneratorPanel extends JPanel {

   public abstract void generateGraph(Graph graph, GraphView view);
}
