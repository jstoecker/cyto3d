package edu.miami.cyto3d.graph.model;

/**
 * Listener interface for changes in a graph model.
 * 
 * @author justin
 * @param <V> - The type of vertex.
 * @param <E> - The type of edge.
 */
public interface GraphListener {

  /** A single vertex has been created. */
  void vertexCreated(Graph graph, Vertex vertex);

  /** A single vertex has been removed. */
  void vertexRemoved(Graph graph, Vertex vertex);

  /** A single edge has been created. */
  void edgeCreated(Graph graph, Edge edge);

  /** A single edge has been removed. */
  void edgeRemoved(Graph graph, Edge edge);

  /** All vertices and edges have been removed. */
  void graphCleared(Graph graph);
}