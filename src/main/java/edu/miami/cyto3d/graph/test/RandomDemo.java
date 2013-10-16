package edu.miami.cyto3d.graph.test;

import javax.swing.JFrame;

import edu.miami.cyto3d.GraphViewer;
import edu.miami.cyto3d.graph.generator.BoxGenerator;
import edu.miami.cyto3d.graph.model.BasicGraph;
import edu.miami.cyto3d.graph.model.Graph;
import edu.miami.cyto3d.graph.view.BasicGraphView;
import edu.miami.cyto3d.graph.view.GraphView;

public class RandomDemo {

  public RandomDemo() {
    Graph graph = new BasicGraph();
    GraphView view = new BasicGraphView(graph);
    graph.addListener(view);
    
    BoxGenerator graphGen = new BoxGenerator();
    graphGen.setNumNodes(15);
    graphGen.setNumEdges(15);
    graphGen.generate(graph, view);

    GraphViewer viewer = new GraphViewer();
    viewer.setGraph(graph, view);

    JFrame frame = new JFrame("Graph Test");
    frame.setSize(800, 800);
    frame.setLocationRelativeTo(null);
    frame.add(viewer.getCanvas());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }

  public static void main(String[] args) {
    new RandomDemo();
  }
}
