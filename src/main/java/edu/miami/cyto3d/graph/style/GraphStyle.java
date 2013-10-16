package edu.miami.cyto3d.graph.style;

import edu.miami.cyto3d.graph.model.Edge;
import edu.miami.cyto3d.graph.model.Graph;
import edu.miami.cyto3d.graph.model.Vertex;
import edu.miami.cyto3d.graph.view.GraphView;

/**
 * Any algorithm that, when applied, modifies the a graph view to fit some desired appearance.
 * 
 * @author justin
 */
public interface GraphStyle {

  /** Applies the style to an entire graph. */
  public void apply(Graph graph, GraphView view);

  /** Applies the style to a single edge. */
  public void apply(Graph graph, GraphView view, Edge edge);

  /** Applies the style to a single vertex. */
  public void apply(Graph graph, GraphView view, Vertex vertex);
}