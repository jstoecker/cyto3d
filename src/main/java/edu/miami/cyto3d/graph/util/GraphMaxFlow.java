package edu.miami.cyto3d.graph.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import edu.miami.cyto3d.graph.model.Edge;
import edu.miami.cyto3d.graph.model.Graph;
import edu.miami.cyto3d.graph.model.Vertex;

/**
 * Computes max flow between all pairs of vertices in a graph.
 * 
 * @author justin
 */
public class GraphMaxFlow {

  Map<Vertex, Integer> vmap = new HashMap<Vertex, Integer>();
  double[][]           maxflows;

  public GraphMaxFlow(Graph graph, String attribute) {

    // map vertex objects to indices in the maxflow class
    int i = 0;
    for (Vertex v : graph.getVertices()) {
      vmap.put(v, i++);
    }

    // init max flow class by adding vertices and all edges in the graph
    MaxFlow mf = new MaxFlow(graph.getNumVertices());
    for (Edge e : graph.getEdges()) {
      Vertex src = e.getSource();
      Vertex tgt = e.getTarget();
      float capacity = e.getAttributes().get(attribute, 0.0f);
      mf.addEdge(vmap.get(src), vmap.get(tgt), capacity);
    }

    // compute all pairs max flow (only populate upper triangle of matrix)
    maxflows = new double[graph.getNumVertices()][graph.getNumVertices()];
    for (i = 0; i < maxflows.length; i++) {
      for (int j = i + 1; j < maxflows.length; j++) {
        maxflows[i][j] = mf.getMaxFlow(i, j);
      }
    }
  }

  public double getMaxFlow(Vertex a, Vertex b) {
    // only upper triangle used (flow is same from i to j as j to i)
    int i = vmap.get(a);
    int j = vmap.get(b);
    return (j > i) ? maxflows[i][j] : maxflows[j][i];
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < maxflows.length; i++)
      sb.append(Arrays.toString(maxflows[i]) + "\n");
    return sb.toString();
  }
}