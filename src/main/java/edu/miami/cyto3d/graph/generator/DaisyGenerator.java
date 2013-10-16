package edu.miami.cyto3d.graph.generator;

import edu.miami.cyto3d.graph.model.Graph;
import edu.miami.cyto3d.graph.model.Vertex;
import edu.miami.cyto3d.graph.view.GraphView;
import edu.miami.math.util.GMath;
import edu.miami.math.vector.Vec3;

public class DaisyGenerator implements GraphGenerator {

  private int   numNodes      = 50;
  private float minRadius     = 8;
  private float maxRadius     = 8;
  float         minEdgeLength = 4;
  float         maxEdgeLength = 4;

  public void setNumNodes(int numNodes) {
    this.numNodes = numNodes;
  }

  public void setMinRadius(float minRadius) {
    this.minRadius = minRadius;
  }

  public void setMaxRadius(float maxRadius) {
    this.maxRadius = maxRadius;
  }

  public void setMinEdgeLength(float minEdgeLength) {
    this.minEdgeLength = minEdgeLength;
  }

  public void setMaxEdgeLength(float maxEdgeLength) {
    this.maxEdgeLength = maxEdgeLength;
  }

  public int getNumNodes() {
    return numNodes;
  }

  public float getMinRadius() {
    return minRadius;
  }

  public float getMaxRadius() {
    return maxRadius;
  }

  public float getMinEdgeLength() {
    return minEdgeLength;
  }

  public float getMaxEdgeLength() {
    return maxEdgeLength;
  }

  @Override
  public String toString() {
    return "Daisy";
  }

  @Override
  public void generate(Graph graph, GraphView view) {
    Vertex center = graph.createVertex();

    for (int i = 0; i < numNodes; i++) {
      Vec3 p = GMath.rndVec3f(-1, 1).normalize();
      p.mul((float) (minRadius + Math.random() * (maxRadius - minRadius)));
      Vertex vertex = graph.createVertex();
      view.get(vertex).setPosition(p);
      graph.createEdge(center, vertex, false);
    }
  }
}
