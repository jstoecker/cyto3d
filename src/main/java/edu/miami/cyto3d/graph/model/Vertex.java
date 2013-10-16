package edu.miami.cyto3d.graph.model;

/**
 * A vertex in a graph.
 * 
 * @author justin
 */
public interface Vertex {

  /** @return The vertex ID. */
  int getID();

  /** @return Vertex attributes. */
  Attributes getAttributes();
}
