package edu.miami.cyto3d.graph.view;

/**
 * Listener interface for changes in a graph view.
 * 
 * @author justin
 */
public interface GraphViewListener {

  void viewCreated(GraphView graphView, VertexView vertexView);

  void viewCreated(GraphView graphView, EdgeView edgeView);

  void viewRemoved(GraphView graphView, VertexView vertexView);

  void viewRemoved(GraphView graphView, EdgeView edgeView);

  void viewCleared(GraphView graphView);
}