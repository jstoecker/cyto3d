package edu.miami.cyto3d.graph.test;

import javax.swing.JFrame;

import edu.miami.cyto3d.GraphViewer;
import edu.miami.cyto3d.graph.model.BasicGraph;
import edu.miami.cyto3d.graph.model.Edge;
import edu.miami.cyto3d.graph.model.Graph;
import edu.miami.cyto3d.graph.model.Vertex;
import edu.miami.cyto3d.graph.util.GraphMaxFlow;
import edu.miami.cyto3d.graph.view.BasicGraphView;
import edu.miami.cyto3d.graph.view.GraphView;
import edu.miami.math.util.GMath;

/**
 * Adjusts graph properties according to max flow based clustering.
 * 
 * @author justin
 */
public class MaxFlowClustering {

  static final String MF_ATTRIBUTE     = "interactivity";
  static final String EL_ATTRIBUTE     = "length";

  static final float  minEL            = 3;
  static final float  maxEL            = 10;

  GraphMaxFlow        gmf;
  Graph               originalGraph;
  float               maxFlowThreshold = 3.5f;
  Vertex              clusterVert;

  Graph               clusterGraph;
  GraphView           clusterGraphView;

  GraphViewer         viewer;

  public MaxFlowClustering() {
    createViewer();

    originalGraph = new BasicGraph();

    Vertex a = originalGraph.createVertex();
    Vertex b = originalGraph.createVertex();
    Vertex c = originalGraph.createVertex();
    Vertex d = originalGraph.createVertex();

    Edge ab = originalGraph.createEdge(a, b, false);
    ab.getAttributes().set(MF_ATTRIBUTE, 0.5f);
    Edge bc = originalGraph.createEdge(b, c, false);
    bc.getAttributes().set(MF_ATTRIBUTE, 0.5f);
    Edge cd = originalGraph.createEdge(c, d, false);
    cd.getAttributes().set(MF_ATTRIBUTE, 0.5f);
    Edge da = originalGraph.createEdge(d, a, false);
    da.getAttributes().set(MF_ATTRIBUTE, 1);

    // assign initial edge lengths
    for (Edge edge : originalGraph.getEdges()) {
      float edgeLength = minEL + (maxEL - minEL) * (1 - edge.getAttributes().get(MF_ATTRIBUTE, 0));
      edge.getAttributes().set(EL_ATTRIBUTE, edgeLength);
    }

    // compute all-pairs max flow
    gmf = new GraphMaxFlow(originalGraph, MF_ATTRIBUTE);

    clusterVert = a;
    cluster();
  }

  void createViewer() {
    viewer = new GraphViewer();

    JFrame frame = new JFrame("Graph Viewer");
    frame.setSize(800, 800);
    frame.add(viewer.getCanvas());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }

  public void setMaxFlowThreshold(float maxFlowThreshold) {
    this.maxFlowThreshold = maxFlowThreshold;
  }

  public void cluster() {
    clusterGraph = new BasicGraph(originalGraph);
    clusterGraphView = new BasicGraphView(clusterGraph);
    clusterGraph.addListener(clusterGraphView);

    for (Vertex v : originalGraph.getVertices()) {
      clusterGraphView.get(v).setPosition(GMath.rndVec3f(-10, 10));
      
      if (v != clusterVert) {
        if (gmf.getMaxFlow(clusterVert, v) >= maxFlowThreshold) {
          // find edge if it exists
          Edge edge = null;
          for (Edge e : originalGraph.getMixedEdges(clusterVert)) {
            if (e.getOpposite(clusterVert) == v) {
              edge = e;
              break;
            }
          }

          // edge doesnt exist, so create it
          if (edge == null) {
            edge = clusterGraph.createEdge(clusterVert, v, false);
          }

          // set length of edge to 0
          edge.getAttributes().set(EL_ATTRIBUTE, 0f);
        }
      }
    }

    viewer.setGraph(clusterGraph, clusterGraphView);
  }

  public static void main(String[] args) {
    new MaxFlowClustering();
  }
}
