package edu.miami.cyto3d.graph.morph;

import java.util.HashSet;
import java.util.Set;

import edu.miami.cyto3d.graph.model.BasicGraph;
import edu.miami.cyto3d.graph.model.Edge;
import edu.miami.cyto3d.graph.model.Graph;
import edu.miami.cyto3d.graph.model.GraphListener;
import edu.miami.cyto3d.graph.model.Vertex;
import edu.miami.cyto3d.graph.view.BasicEdgeView;
import edu.miami.cyto3d.graph.view.BasicGraphView;
import edu.miami.cyto3d.graph.view.BasicVertexView;
import edu.miami.cyto3d.graph.view.EdgeView;
import edu.miami.cyto3d.graph.view.GraphView;
import edu.miami.cyto3d.graph.view.VertexView;

/**
 * Calculates and provides a Graph and GraphView that is interpolated from two GraphFrame objects. A
 * GraphInterframe exists for each successive pair of GraphFrame objects in a GraphMorph (if there
 * are N GraphFrame objects in a GraphMorph, there are N-1 GraphInterframe objects).<br>
 * <br>
 * The graphs G_a and G_b do not need to have the same sets of vertices and edges (though they may).
 * If a vertex or edge is only present in one of the graphs, it fades in or out depending on how
 * close the interpolation is to that particular graph. When visualizing a morph, the user sees
 * neither G_a nor G_b; instead, they see a view of a graph that is the union of G_a and G_b.
 * 
 * @author justin
 */
public class GraphInterframe implements GraphListener {

   final GraphFrame frameA;
   final GraphFrame frameB;
   Graph            iGraph; // G_a (union) G_b
   GraphView        iView; // interpolated view of iGraph

   public GraphInterframe(GraphFrame start, GraphFrame end) {
      this.frameA = start;
      this.frameB = end;

      // union of G_a and G_b
      Set<Vertex> vSetAUB = new HashSet<Vertex>(frameA.graph.getVertices());
      vSetAUB.addAll(frameB.graph.getVertices());
      Set<Edge> eSetAUB = new HashSet<Edge>(frameA.graph.getEdges());
      eSetAUB.addAll(frameB.graph.getEdges());
      iGraph = new BasicGraph(vSetAUB, eSetAUB);
      iView = new BasicGraphView(iGraph);
   }

   public Graph getGraph() {
      return iGraph;
   }

   public GraphView getGraphView() {
      return iView;
   }

   public void lerp(float amount) {
      assert (amount >= 0.0f && amount <= 1.0f);

      // interpolate vertex views
      for (Vertex v : iGraph.getVertices()) {
         VertexView a = frameA.view.get(v);
         VertexView b = frameB.view.get(v);
         if (a != null) {
            if (b != null) {
               // V_a intersect V_b
               iView.setView(v, frameA.view.get(v).lerp(frameB.view.get(v), amount));
            } else {
               // V_a - V_b
               iView.setView(v, frameA.view.get(v).lerp(null, amount));
            }
         } else {
            // assume V_b - V_a: if b is also null v shouldn't be in iGraph!
            iView.setView(v, frameB.view.get(v).lerp(null, (1 - amount)));
         }
      }

      // interpolate edge views
      for (Edge e : iGraph.getEdges()) {
         EdgeView a = frameA.view.get(e);
         EdgeView b = frameB.view.get(e);
         if (a != null) {
            if (b != null) {
               iView.setView(e, frameA.view.get(e).lerp(iView, frameB.view.get(e), amount));
            } else {
               iView.setView(e, frameA.view.get(e).lerp(iView, null, amount));
            }
         } else {
            iView.setView(e, frameB.view.get(e).lerp(iView, null, 1 - amount));
         }
      }
   }

   @Override
   public void vertexCreated(Graph graph, Vertex vertex) {
      if (!iGraph.contains(vertex)) {
         iView.setView(vertex, new BasicVertexView());
         iGraph.add(vertex);
      }
   }

   @Override
   public void vertexRemoved(Graph graph, Vertex vertex) {
      if (frameA.graph.contains(vertex) || frameB.graph.contains(vertex)) return;
      iGraph.remove(vertex);
   }

   @Override
   public void edgeCreated(Graph graph, Edge edge) {
      if (!iGraph.contains(edge)) {
         iView.setView(edge, new BasicEdgeView(iView, edge));
         iGraph.add(edge);
      }
   }

   @Override
   public void edgeRemoved(Graph graph, Edge edge) {
      if (frameA.graph.contains(edge) || frameB.graph.contains(edge)) return;
      iGraph.remove(edge);
   }

   @Override
   public void graphCleared(Graph graph) {
      iGraph.clear();
      iGraph = (graph == frameA.graph) ? new BasicGraph(frameB.graph)
            : new BasicGraph(frameA.graph);
   }
}