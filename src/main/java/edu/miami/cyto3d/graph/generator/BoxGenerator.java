package edu.miami.cyto3d.graph.generator;

import edu.miami.cyto3d.graph.model.Graph;
import edu.miami.cyto3d.graph.view.GraphView;
import edu.miami.math.vector.Vec3;

public class BoxGenerator implements GraphGenerator {

  int   numNodes      = 144;
  int   numEdges      = 100;
  int   numFrames     = 2;
  float width         = 30;
  float height        = 30;
  float length        = 30;
  float minEdgeLength = 1;
  float maxEdgeLength = 5;

  public void setNumFrames(int numFrames) {
    this.numFrames = numFrames;
  }

  public void setNumNodes(int numNodes) {
    this.numNodes = numNodes;
  }

  public void setNumEdges(int numEdges) {
    this.numEdges = numEdges;
  }

  public void setWidth(float width) {
    this.width = width;
  }

  public void setHeight(float height) {
    this.height = height;
  }

  public void setLength(float length) {
    this.length = length;
  }

  public void setMinEdgeLength(float minEdgeLength) {
    this.minEdgeLength = minEdgeLength;
  }

  public void setMaxEdgeLength(float maxEdgeLength) {
    this.maxEdgeLength = maxEdgeLength;
  }

  public int getNumFrames() {
    return numFrames;
  }

  public int getNumEdges() {
    return numEdges;
  }

  public int getNumNodes() {
    return numNodes;
  }

  public float getWidth() {
    return width;
  }

  public float getHeight() {
    return height;
  }

  public float getLength() {
    return length;
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
      float x = (float) (Math.random() * width) - width / 2.0f;
      float y = (float) (Math.random() * height) - height / 2.0f;
      float z = (float) (Math.random() * length) - length / 2.0f;
      view.get(graph.createVertex()).setPosition(new Vec3(x, y, z));
    }

    EdgeGenerator.createRandomEdges(graph, view, numEdges);
  }
}
