package edu.miami.cyto3d.graph.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Basic implementation of a graph.
 * 
 * @author justin
 */
public class BasicGraph implements Graph {

  protected final List<GraphListener>    listeners       = new CopyOnWriteArrayList<GraphListener>();
  protected final Set<Vertex>            vertices        = new HashSet<Vertex>();
  protected final Set<Edge>              edges           = new HashSet<Edge>();
  protected final Map<Vertex, Set<Edge>> mixedEdges      = new HashMap<Vertex, Set<Edge>>();
  protected final Map<Vertex, Set<Edge>> undirectedEdges = new HashMap<Vertex, Set<Edge>>();
  protected final Map<Vertex, Set<Edge>> inEdges         = new HashMap<Vertex, Set<Edge>>();
  protected final Map<Vertex, Set<Edge>> outEdges        = new HashMap<Vertex, Set<Edge>>();
  protected final Attributes             attributes      = new Attributes();

  public BasicGraph() {
  }

  public BasicGraph(Graph graph) {
    addAll(graph);
  }

  public BasicGraph(Set<Vertex> vertices, Set<Edge> edges) {
    this.vertices.addAll(vertices);
    this.edges.addAll(edges);
  }

  @Override
  public Set<Vertex> getVertices() {
    return Collections.unmodifiableSet(vertices);
  }

  @Override
  public Set<Edge> getEdges() {
    return Collections.unmodifiableSet(edges);
  }

  @Override
  public void clear() {
    vertices.clear();
    edges.clear();
    for (GraphListener l : listeners)
      l.graphCleared(this);
  }

  @Override
  public void addListener(GraphListener listener) {
    listeners.add(listener);
  }

  @Override
  public void removeListener(GraphListener listener) {
    listeners.remove(listener);
  }

  @Override
  public void remove(Vertex vertex) {
    vertices.remove(vertex);

    for (Edge e : new HashSet<Edge>(mixedEdges.get(vertex)))
      remove(e);

    for (GraphListener l : listeners)
      l.vertexRemoved(this, vertex);
  }

  @Override
  public void remove(Edge edge) {
    if (edges.remove(edge)) {

      mixedEdges.get(edge.getSource()).remove(edge);
      mixedEdges.get(edge.getTarget()).remove(edge);
      if (edge.isDirected()) {
        outEdges.get(edge.getSource()).remove(edge);
        inEdges.get(edge.getTarget()).remove(edge);
      } else {
        undirectedEdges.get(edge.getSource()).remove(edge);
        undirectedEdges.get(edge.getTarget()).remove(edge);
      }

      for (GraphListener l : listeners)
        l.edgeRemoved(this, edge);
    }
  }

  @Override
  public int getNumVertices() {
    return vertices.size();
  }

  @Override
  public int getNumEdges() {
    return edges.size();
  }

  @Override
  public void add(Vertex vertex) {
    if (vertices.add(vertex)) {
      for (GraphListener l : listeners)
        l.vertexCreated(this, vertex);
    }
  }

  @Override
  public void add(Edge edge) {
    edges.add(edge);

    add(mixedEdges, edge.getSource(), edge);
    add(mixedEdges, edge.getTarget(), edge);
    if (edge.isDirected()) {
      add(outEdges, edge.getSource(), edge);
      add(inEdges, edge.getTarget(), edge);
    } else {
      add(undirectedEdges, edge.getSource(), edge);
      add(undirectedEdges, edge.getTarget(), edge);
    }

    for (GraphListener l : listeners)
      l.edgeCreated(this, edge);
  }

  private void add(Map<Vertex, Set<Edge>> m, Vertex vertex, Edge edge) {
    Set<Edge> edgeSet = m.get(vertex);
    if (edgeSet == null) m.put(vertex, edgeSet = new HashSet<Edge>());
    edgeSet.add(edge);
  }

  @Override
  public void addAll(Graph graph) {
    for (Vertex v : graph.getVertices())
      add(v);
    for (Edge e : graph.getEdges())
      add(e);
  }

  @Override
  public boolean contains(Vertex vertex) {
    return vertices.contains(vertex);
  }

  @Override
  public boolean contains(Edge edge) {
    return edges.contains(edge);
  }

  @Override
  public Set<Edge> getInEdges(Vertex vertex) {
    return Collections.unmodifiableSet(inEdges.get(vertex));
  }

  @Override
  public Set<Edge> getOutEdges(Vertex vertex) {
    return Collections.unmodifiableSet(outEdges.get(vertex));
  }

  @Override
  public Set<Edge> getUndirectedEdges(Vertex vertex) {
    return Collections.unmodifiableSet(undirectedEdges.get(vertex));
  }

  @Override
  public Set<Edge> getMixedEdges(Vertex vertex) {
    return Collections.unmodifiableSet(mixedEdges.get(vertex));
  }

  @Override
  public int getUndirectedDegree(Vertex vertex) {
    Set<Edge> edges = undirectedEdges.get(vertex);
    return (edges == null) ? 0 : edges.size();
  }

  @Override
  public int getInDegree(Vertex vertex) {
    Set<Edge> edges = inEdges.get(vertex);
    return (edges == null) ? 0 : edges.size();
  }

  @Override
  public int getOutDegree(Vertex vertex) {
    Set<Edge> edges = outEdges.get(vertex);
    return (edges == null) ? 0 : edges.size();
  }

  @Override
  public int getMixedDegree(Vertex vertex) {
    Set<Edge> edges = mixedEdges.get(vertex);
    return (edges == null) ? 0 : edges.size();
  }

  @Override
  public Attributes getAttributes() {
    return attributes;
  }

  @Override
  public Vertex createVertex() {
    Vertex vertex = new BasicVertex();
    add(vertex);
    return vertex;
  }

  @Override
  public Edge createEdge(Vertex src, Vertex tgt, boolean directed) {
    Edge edge = new BasicEdge(src, tgt, directed);
    add(edge);
    return edge;
  }
}
