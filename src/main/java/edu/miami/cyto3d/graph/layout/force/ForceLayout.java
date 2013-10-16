package edu.miami.cyto3d.graph.layout.force;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.miami.cyto3d.graph.layout.IterableGraphLayout;
import edu.miami.cyto3d.graph.model.Edge;
import edu.miami.cyto3d.graph.model.Graph;
import edu.miami.cyto3d.graph.model.GraphListener;
import edu.miami.cyto3d.graph.model.Vertex;
import edu.miami.cyto3d.graph.view.GraphView;

/**
 * Creates a force layout that operates on a graph's view. This layout is synchronized to changes in
 * the graph structure and is intended to be iterated over time. If the layout is no longer needed,
 * make sure to remove it from the graph's listeners.
 * 
 * @author justin
 */
public class ForceLayout implements IterableGraphLayout, GraphListener {

  private GraphView                    graphView;

  private List<ForceVertex>            vertices      = new ArrayList<ForceVertex>();
  private HashMap<Vertex, ForceVertex> vertexMap     = new HashMap<Vertex, ForceVertex>();
  private HashMap<Edge, ForceEdge>     edgeMap       = new HashMap<Edge, ForceEdge>();

  private float                        damping       = 0.9f;
  private float                        springiness   = 0.1f;
  private int                          maxIterations = 100;
  private float                        energy;
  private boolean                      useVelocity   = true;
  private float                        maxSpeed      = 1;

  public ForceLayout(Graph graph, GraphView graphView) {
    this.graphView = graphView;
    for (Vertex v : graph.getVertices())
      vertexCreated(graph, v);
    for (Edge e : graph.getEdges())
      edgeCreated(graph, e);
  }

  public void setUseVelocity(boolean useVelocity) {
    this.useVelocity = useVelocity;
  }

  public boolean isUseVelocity() {
    return useVelocity;
  }

  public void setDamping(float damping) {
    this.damping = damping;
  }

  public void setMaxSpeed(float maxSpeed) {
    this.maxSpeed = maxSpeed;
  }
  
  public void setSpringiness(float springiness) {
    this.springiness = springiness;
  }

  public ForceVertex getVertex(Vertex node) {
    return vertexMap.get(node);
  }

  public float getSpringiness() {
    return springiness;
  }

  public float getDamping() {
    return damping;
  }

  public float getEnergy() {
    return energy;
  }

  public void setMaxIterations(int maxIterations) {
    this.maxIterations = maxIterations;
  }

  @Override
  public void iterateLayout() {
    repelVertices();

    for (Map.Entry<Edge, ForceEdge> edgeMapping : edgeMap.entrySet()) {
      Edge edge = edgeMapping.getKey();
      ForceEdge forceEdge = edgeMapping.getValue();
      forceEdge.attract(springiness, edge.getAttributes().get("length", 5.0f));
    }

    updateEnergy();
  }

  @Override
  public void layoutGraph() {
    for (int i = 0; i < maxIterations; i++)
      iterateLayout();
  };

  private void repelVertices() {
    for (int i = 0; i < vertices.size(); i++) {
      ForceVertex a = vertices.get(i);
      for (int j = i + 1; j < vertices.size(); j++) {
        ForceVertex b = vertices.get(j);
        a.repel(b);
        b.repel(a);
      }
    }
  }

  private void updateEnergy() {
    energy = 0;
    for (Map.Entry<Vertex, ForceVertex> nodeMapping : vertexMap.entrySet())
      energy += nodeMapping.getValue().applyForce(damping, useVelocity, maxSpeed);
  }

  @Override
  public void vertexCreated(Graph graph, Vertex vertex) {
    ForceVertex v = new ForceVertex(graphView.get(vertex));
    vertexMap.put(vertex, v);
    vertices.add(v);
  }

  @Override
  public void vertexRemoved(Graph graph, Vertex vertex) {
    vertexMap.remove(vertex);
    vertices.remove(vertex);
  }

  @Override
  public void edgeCreated(Graph graph, Edge edge) {
    ForceVertex src = vertexMap.get(edge.getSource());
    ForceVertex tgt = vertexMap.get(edge.getTarget());
    ForceEdge e = new ForceEdge(graphView.get(edge), src, tgt);
    edgeMap.put(edge, e);
  }

  @Override
  public void edgeRemoved(Graph graph, Edge edge) {
    edgeMap.remove(edge);
  }

  @Override
  public void graphCleared(Graph graph) {
    edgeMap.clear();
    vertexMap.clear();
    vertices.clear();
  }
}
