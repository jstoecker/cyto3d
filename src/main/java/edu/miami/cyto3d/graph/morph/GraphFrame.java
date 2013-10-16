package edu.miami.cyto3d.graph.morph;

import edu.miami.cyto3d.graph.model.Graph;
import edu.miami.cyto3d.graph.view.GraphView;

/**
 * GraphFrame objects are the "key frames" of a graph morph animation. Each frame contains a graph,
 * a view of that graph, and the properties attributes to the graph. When a graph is morphed, the
 * user sees an GraphInterframe object which is the interpolation between two GraphFrame objects.
 * 
 * @author justin
 */
public class GraphFrame {

   protected final Graph     graph;
   protected final GraphView view;

   public GraphFrame(Graph graph, GraphView view) {
      this.graph = graph;
      this.view = view;
   }
}
