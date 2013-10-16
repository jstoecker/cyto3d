package edu.miami.cyto3d.graph.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import edu.miami.cyto3d.graph.model.Edge;
import edu.miami.cyto3d.graph.model.Graph;
import edu.miami.cyto3d.graph.model.Vertex;

/**
 * Maintains a view of a graph and all its vertices and edges.
 * 
 * @author justin
 */
public class BasicGraphView implements GraphView {

   private final HashMap<Vertex, VertexView>  vertexViews = new HashMap<Vertex, VertexView>();
   private final HashMap<Edge, EdgeView>      edgeViews   = new HashMap<Edge, EdgeView>();
   private final ArrayList<GraphViewListener> listeners   = new ArrayList<GraphViewListener>();

   public BasicGraphView(Graph graph) {
      for (Vertex v : graph.getVertices())
         vertexCreated(graph, v);
      for (Edge e : graph.getEdges())
         edgeCreated(graph, e);
   }

   @Override
   public void vertexCreated(Graph graph, Vertex vertex) {
      VertexView view = new BasicVertexView();
      vertexViews.put(vertex, view);
      for (GraphViewListener l : listeners)
         l.viewCreated(this, view);
   }

   @Override
   public void vertexRemoved(Graph graph, Vertex vertex) {
      VertexView view = vertexViews.get(vertex);
      vertexViews.remove(vertex);
      for (GraphViewListener l : listeners)
         l.viewRemoved(this, view);
   }

   @Override
   public void edgeCreated(Graph graph, Edge edge) {
      EdgeView view = new BasicEdgeView(this, edge);
      edgeViews.put(edge, view);
      for (GraphViewListener l : listeners)
         l.viewCreated(this, view);
   }

   @Override
   public void edgeRemoved(Graph graph, Edge edge) {
      EdgeView view = edgeViews.get(edge);
      edgeViews.remove(edge);
      for (GraphViewListener l : listeners)
         l.viewRemoved(this, view);
   }

   @Override
   public void graphCleared(Graph graph) {
      vertexViews.clear();
      edgeViews.clear();
      for (GraphViewListener l : listeners)
         l.viewCleared(this);
   }

   @Override
   public Collection<VertexView> getVertexViews() {
      return vertexViews.values();
   }

   @Override
   public Collection<EdgeView> getEdgeViews() {
      return edgeViews.values();
   }

   @Override
   public int getNumVertices() {
      return vertexViews.size();
   }

   @Override
   public int getNumEdges() {
      return edgeViews.size();
   }

   @Override
   public VertexView get(Vertex vertex) {
      return vertexViews.get(vertex);
   }

   @Override
   public EdgeView get(Edge edge) {
      return edgeViews.get(edge);
   }

   @Override
   public void setView(Vertex vertex, VertexView view) {
      vertexViews.put(vertex, view);
   }

   @Override
   public void setView(Edge edge, EdgeView view) {
      edgeViews.put(edge, view);
   }

   @Override
   public void addListener(GraphViewListener listener) {
      listeners.add(listener);
   }

   @Override
   public void removeListener(GraphViewListener listener) {
      listeners.remove(listener);
   }
}
