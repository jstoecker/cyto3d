package edu.miami.cyto3d.graph.model;

/**
 * Implementation of a vertex. An instance of this can only be generated through a Graph by calling
 * graph.createVertex();
 * 
 * @author justin
 */
public class BasicVertex implements Vertex {

  private static int       ID_COUNTER = 0;

  private final Attributes attributes = new Attributes();
  private final int        id;

  protected BasicVertex() {
    id = ID_COUNTER++;
  }

  @Override
  public Attributes getAttributes() {
    return attributes;
  }

  @Override
  public int getID() {
    return id;
  }

  @Override
  public String toString() {
    return "v" + id;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    BasicVertex other = (BasicVertex) obj;
    if (id != other.id) return false;
    return true;
  }
}
