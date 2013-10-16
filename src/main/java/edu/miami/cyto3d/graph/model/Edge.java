package edu.miami.cyto3d.graph.model;

/**
 * An edge in a graph that connects two vertices. The edge may be directed or undirected.
 * 
 * @author justin
 */
public interface Edge {
  
  /** @return The edge ID. */
  int getID();

  /** @return The first vertex of the edge. */
  Vertex getSource();

  /** @return The second vertex of the edge. */
  Vertex getTarget();
  
  /** @return The source vertex if v == target, the target vertex if v == source, or null if v is neither. */
  Vertex getOpposite(Vertex v);

  /** @return True if the edge is directed. */
  boolean isDirected();

  /** @return Edge attributes. */
  Attributes getAttributes();
}