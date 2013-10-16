package edu.miami.cyto3d.graph.model;

/**
 * Implementation of an edge. An instance of this class can only be created through a Graph.
 * 
 * @author justin
 */
public class BasicEdge implements Edge {

  private static int       ID_COUNTER = 0;

  private final Attributes attributes = new Attributes();
  private final Vertex     source;
  private final Vertex     target;
  private final boolean    directed;
  private final int        id;

  protected BasicEdge(Vertex source, Vertex target, boolean directed) {
    this.source = source;
    this.target = target;
    this.directed = directed;
    id = ID_COUNTER++;
  }

  @Override
  public Vertex getSource() {
    return source;
  }

  @Override
  public Vertex getTarget() {
    return target;
  }

  @Override
  public boolean isDirected() {
    return directed;
  }

  @Override
  public Attributes getAttributes() {
    return attributes;
  }

  @Override
  public Vertex getOpposite(Vertex v) {
    if (v == source) return target;
    if (v == target) return source;
    return null;
  }

  @Override
  public String toString() {
    return String.format("{%s, %s}", source.toString(), target.toString());
  }

  @Override
  public int getID() {
    return id;
  }
}
