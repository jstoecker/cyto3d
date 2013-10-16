package edu.miami.cyto3d.graph.test;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import edu.miami.cyto3d.GraphViewer;
import edu.miami.cyto3d.graph.layout.force.ForceLayout;
import edu.miami.cyto3d.graph.model.BasicGraph;
import edu.miami.cyto3d.graph.model.Edge;
import edu.miami.cyto3d.graph.model.Graph;
import edu.miami.cyto3d.graph.model.Vertex;
import edu.miami.cyto3d.graph.morph.GraphFrame;
import edu.miami.cyto3d.graph.morph.GraphInterframe;
import edu.miami.cyto3d.graph.view.BasicGraphView;
import edu.miami.cyto3d.graph.view.GraphView;
import edu.miami.math.util.GMath;
import edu.miami.math.vector.Vec4;

/**
 * Example of a morph between two graphs. Hold the 'M' key to transition between graphs.
 * 
 * @author justin
 */
public class MorphDemo {

  float           t = 0;
  GraphInterframe morph;
  GraphViewer     viewer;

  public MorphDemo() {
    
    // create first graph
    Graph graph1 = new BasicGraph();
    GraphView view1 = new BasicGraphView(graph1);
    graph1.addListener(view1);
    Vertex a = graph1.createVertex();
    Vertex b = graph1.createVertex();
    Vertex c = graph1.createVertex();
    Vertex d = graph1.createVertex();
    Vertex e = graph1.createVertex();
    Vertex f = graph1.createVertex();
    Vertex g = graph1.createVertex();
    graph1.createEdge(a, b, false);
    graph1.createEdge(b, c, false);
    graph1.createEdge(c, a, false);
    graph1.createEdge(d, c, false);
    graph1.createEdge(c, e, false);
    graph1.createEdge(e, d, false);
    graph1.createEdge(f, a, false);
    graph1.createEdge(g, d, false);
    for (Vertex v : graph1.getVertices()) {
      view1.get(v).setPosition(GMath.rndVec3f(-5, 5));
      view1.get(v).setPrimaryColor(new Vec4(1, 0.5f, 0.5f, 1));
    }
    for (Edge edge : graph1.getEdges()) {
      view1.get(edge).setPrimaryColor(new Vec4(0.5f, 0.25f, 0.25f, 1));
    }
    GraphFrame f1 = new GraphFrame(graph1, view1);

    // create second graph
    Graph graph2 = new BasicGraph();
    GraphView view2 = new BasicGraphView(graph2);
    graph2.addListener(view2);
    Vertex h = graph2.createVertex();
    Vertex i = graph2.createVertex();
    Vertex j = graph2.createVertex();
    Vertex k = graph2.createVertex();
    graph2.add(a);
    graph2.add(b);
    graph2.add(d);
    graph2.add(e);
    graph2.add(f);
    graph2.add(g);
    for (Edge edge : graph1.getEdges()) {
      if (graph2.contains(edge.getSource()) && graph2.contains(edge.getTarget())) graph2.add(edge);
    }
    graph2.createEdge(b, h, false);
    graph2.createEdge(h, i, false);
    graph2.createEdge(h, j, false);
    graph2.createEdge(j, g, false);
    graph2.createEdge(j, e, false);
    graph2.createEdge(e, k, false);
    graph2.createEdge(k, f, false);
    graph2.createEdge(k, b, false);
    for (Vertex v : graph2.getVertices()) {
      view2.get(v).setPosition(GMath.rndVec3f(-4, 4));
      view2.get(v).setPrimaryColor(new Vec4(0.5f, 0.5f, 1.0f, 1));
    }
    GraphFrame f2 = new GraphFrame(graph2, view2);
    morph = new GraphInterframe(f1, f2);

    // do a static layout of the two graphs
    ForceLayout fl = new ForceLayout(graph2, view2);
    fl.setMaxIterations(1000);
    fl.layoutGraph();
    fl = new ForceLayout(graph1, view1);
    fl.setMaxIterations(1000);
    fl.layoutGraph();

    viewer = new GraphViewer();
    viewer.setGraph(graph1, view1);
    viewer.getForceLayout().setEnabled(false);

    viewer.getCanvas().addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_M) {
          float lerp = (float) Math.cos(t += 0.025f) * 0.5f + 0.5f;
          morph.lerp(lerp);
          viewer.setGraph(morph.getGraph(), morph.getGraphView());
        }
      }
    });

    JFrame frame = new JFrame("Graph Test");
    frame.setSize(800, 800);
    frame.setLocationRelativeTo(null);
    frame.add(viewer.getCanvas());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }

  public static void main(String[] args) {
    new MorphDemo();
  }
}
