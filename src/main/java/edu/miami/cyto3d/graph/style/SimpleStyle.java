package edu.miami.cyto3d.graph.style;

import edu.miami.cyto3d.graph.model.Edge;
import edu.miami.cyto3d.graph.model.Graph;
import edu.miami.cyto3d.graph.model.Vertex;
import edu.miami.cyto3d.graph.view.GraphView;
import edu.miami.cyto3d.graph.view.VertexView;
import edu.miami.cyto3d.util.AbstractModel;
import edu.miami.math.util.GMath;
import edu.miami.math.vector.RVec4;
import edu.miami.math.vector.Vec4;

/**
 * Visual style for a protein-protein interaction network.
 * 
 * @author justin
 */
public class SimpleStyle extends AbstractModel implements GraphStyle {

  public static final String PROP_SHRT_EDGE_COLOR = "shortEdgeColor";
  public static final String PROP_SML_VERT_COLOR  = "smallVertColor";
  public static final String PROP_LRG_VERT_COLOR  = "largeVertColor";
  public static final String PROP_EDGE_LENGTHS    = "edgeLengths";

  Vec4                       edgeColor            = new Vec4(1.0f, 0.6f, 0.6f, 1.0f);
  Vec4                       smallNodeColor       = new Vec4(0.6f, 0.7f, 0.6f, 1.0f);
  Vec4                       largeNodeColor       = new Vec4(0.2f, 1.0f, 0.4f, 1.0f);

  public void setShortEdgeColor(RVec4 shortEdgeColor) {
    Vec4 oldValue = this.edgeColor;
    this.edgeColor.set(shortEdgeColor);
    firePropertyChange(PROP_SHRT_EDGE_COLOR, oldValue, shortEdgeColor);
  }

  public void setSmallNodeColor(RVec4 smallNodeColor) {
    Vec4 oldValue = this.smallNodeColor;
    this.smallNodeColor.set(smallNodeColor);
    firePropertyChange(PROP_SML_VERT_COLOR, oldValue, smallNodeColor);
  }

  public void setLargeNodeColor(RVec4 largeNodeColor) {
    Vec4 oldValue = this.largeNodeColor;
    this.largeNodeColor.set(largeNodeColor);
    firePropertyChange(PROP_LRG_VERT_COLOR, oldValue, largeNodeColor);
  }

  public RVec4 getShortEdgeColor() {
    return edgeColor;
  }

  public RVec4 getSmallNodeColor() {
    return smallNodeColor;
  }

  public RVec4 getLargeNodeColor() {
    return largeNodeColor;
  }

  @Override
  public void apply(Graph graph, GraphView view) {
    for (Edge edge : graph.getEdges())
      apply(graph, view, edge);
    for (Vertex vertex : graph.getVertices())
      apply(graph, view, vertex);
  }

  @Override
  public void apply(Graph graph, GraphView view, Edge edge) {
    float minEL = graph.getAttributes().get("min_edge_length", 1f);
    float maxEL = graph.getAttributes().get("max_edge_length", 5f);
    float l = edge.getAttributes().get("length", 1f);
    float s = GMath.clamp(1 - (l - minEL) / (maxEL - minEL), 0.3f, 1);
    
    Vec4 c = edgeColor.clone();
    c.x *= s;
    c.y *= s;
    c.z *= s;
    
    view.get(edge).setPrimaryColor(c);
  }

  @Override
  public void apply(Graph graph, GraphView view, Vertex vertex) {
    VertexView vertexView = view.get(vertex);
    float minEL = graph.getAttributes().get("min_edge_length", 5f);
    float maxEL = graph.getAttributes().get("max_edge_length", 5f);
    int degree = graph.getMixedDegree(vertex);

    if (minEL == maxEL) {
      vertexView.setSize(GMath.clamp(0.5f + 0.25f * degree, 0.7f, 5));
      vertexView.setPrimaryColor(largeNodeColor);
    } else if (degree > 0) {
      float avgEdgeLength = 0;
      for (Edge edge : graph.getMixedEdges(vertex)) {
        avgEdgeLength += edge.getAttributes().get("length", maxEL);
      }
      avgEdgeLength /= degree;

      float p = 1 - (avgEdgeLength - minEL) / (maxEL - minEL);

      vertexView.setSize(GMath.clamp(0.55f + 0.25f * degree, 0.55f, 5));
      vertexView.setPrimaryColor(Vec4.lerp(smallNodeColor, largeNodeColor, p));
    } else {
      vertexView.setSize(0.5f);
      vertexView.setPrimaryColor(smallNodeColor);
    }
  }
}
