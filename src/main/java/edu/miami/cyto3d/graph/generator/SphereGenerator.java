package edu.miami.cyto3d.graph.generator;

import edu.miami.cyto3d.graph.model.Graph;
import edu.miami.cyto3d.graph.model.Vertex;
import edu.miami.cyto3d.graph.view.GraphView;
import edu.miami.math.util.GMath;
import edu.miami.math.vector.Vec3;

public class SphereGenerator implements GraphGenerator {

  private int   numEdges      = 5;
  private int   numNodes      = 10;
  private float minDistance   = 0;
  private float maxDistance   = 10;
  private float minEdgeLength = 4;
  private float maxEdgeLength = 4;

  public void setNumNodes(int numNodes) {
    this.numNodes = numNodes;
  }

  public void setNumEdges(int numEdges) {
    this.numEdges = Math.min(numNodes * (numNodes - 1) / 2, numEdges);
  }

  public void setMinDistance(float minDistance) {
    this.minDistance = minDistance;
  }

  public void setMaxDistance(float max) {
    this.maxDistance = max;
  }

  public void setMinEdgeLength(float minEdgeLength) {
    this.minEdgeLength = minEdgeLength;
  }

  public void setMaxEdgeLength(float maxEdgeLnegth) {
    this.maxEdgeLength = maxEdgeLnegth;
  }

  public int getNumNodes() {
    return numNodes;
  }

  public int getNumEdges() {
    return numEdges;
  }

  public float getMaxDistance() {
    return maxDistance;
  }

  public float getMinDistance() {
    return minDistance;
  }

  public float getMinEdgeLength() {
    return minEdgeLength;
  }

  public float getMaxEdgeLength() {
    return maxEdgeLength;
  }

  @Override
  public void generate(Graph graph, GraphView view) {
    for (int i = 0; i < numNodes; i++) {
      Vec3 p = GMath.rndVec3f(-1, 1).normalize();
      p.mul((float) (minDistance + Math.random() * (maxDistance - minDistance)));
      Vertex vertex = graph.createVertex();
      view.get(vertex).setPosition(p);
    }

    EdgeGenerator.createRandomEdges(graph, view, numEdges);
  }
}
