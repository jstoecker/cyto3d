package edu.miami.cyto3d.graph.model;

import java.util.Set;

/**
 * A set of vertices and edges.
 * 
 * @author justin
 */
public interface Graph {

  /** @return The number of vertices in the graph. */
  int getNumVertices();

  /** @return The number of edges in the graph. */
  int getNumEdges();

  /** @return A read-only set of all the vertices in the graph. */
  Set<Vertex> getVertices();

  /** @return A read-only set of all edges (directed or undirected) in the graph. */
  Set<Edge> getEdges();

  /** Removes all vertices and edges from the graph. */
  void clear();

  /** Adds a vertex to this graph. */
  void add(Vertex vertex);

  /** Adds an edge to the graph. */
  void add(Edge edge);

  /** Adds all the vertices and edges in another graph to this graph. */
  void addAll(Graph graph);

  /** Removes vertex from the graph and any edges attached to it. */
  void remove(Vertex vertex);

  /** Removes an existing edge from the graph. */
  void remove(Edge edge);

  /** Checks if the graph contains a given vertex. */
  boolean contains(Vertex vertex);

  /** Checks if the graph contains a given edge. */
  boolean contains(Edge edge);

  /** @return An unmodifiable set of all directed edges entering vertex. */
  Set<Edge> getInEdges(Vertex vertex);

  /** @return An unmodifiable set of all directed edges coming out of vertex. */
  Set<Edge> getOutEdges(Vertex vertex);

  /** @return An unmodifiable set of all undirected edges connected to vertex. */
  Set<Edge> getUndirectedEdges(Vertex vertex);

  /** @return An unmodifiable set of all edges (directed and undirected) connected to vertex. */
  Set<Edge> getMixedEdges(Vertex vertex);

  /** @return The number of undirected edges connected to vertex. */
  int getUndirectedDegree(Vertex vertex);

  /** @return The number of directed edges coming into vertex. */
  int getInDegree(Vertex vertex);

  /** @return The number of directed edges coming out of vertex. */
  int getOutDegree(Vertex vertex);

  /**
   * @return The number of directed and undirected edges connected to this vertex. Equivalent to
   *         inDegree + outDegree + undirectedDegree.
   */
  int getMixedDegree(Vertex vertex);

  /** Registers a listener for changes in the graph model. */
  void addListener(GraphListener listener);

  /** Unregisters a listener for changes in the graph model. */
  void removeListener(GraphListener listener);

  /** @return Attributes that apply to the entire graph. */
  Attributes getAttributes();

  /** Creates a new vertex, adds it to the graph, and returns a reference to it. */
  Vertex createVertex();

  /** Creates a new edge, adds it to the graph, and returns a reference to it. */
  Edge createEdge(Vertex src, Vertex tgt, boolean directed);
}
