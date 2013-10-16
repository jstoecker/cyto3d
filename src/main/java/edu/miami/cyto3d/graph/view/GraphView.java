package edu.miami.cyto3d.graph.view;

import java.util.Collection;

import edu.miami.cyto3d.graph.model.Edge;
import edu.miami.cyto3d.graph.model.GraphListener;
import edu.miami.cyto3d.graph.model.Vertex;

/**
 * View of a graph model. Contains views of all vertices and edges in the graph.
 * 
 * @author justin
 */
public interface GraphView extends GraphListener {

   /** Returns the number of vertices in the graph view. */
   int getNumVertices();

   /** Returns the number of edges in the graph view. */
   int getNumEdges();

   /** Returns a read-only collection of all vertex views in the graph view. */
   Collection<VertexView> getVertexViews();

   /** Returns a read-only collection of all edge views in the graph view. */
   Collection<EdgeView> getEdgeViews();

   /** Returns the view of a given vertex. */
   VertexView get(Vertex vertex);

   /** Returns the view of a given edge. */
   EdgeView get(Edge edge);

   /** Manually sets the view of a vertex. */
   void setView(Vertex vertex, VertexView view);

   /** Manually sets the view of an edge. */
   void setView(Edge edge, EdgeView view);

   /** Adds a listener to this graph view. */
   void addListener(GraphViewListener listener);

   /** Removes a listener to this graph view. */
   void removeListener(GraphViewListener listener);
}
