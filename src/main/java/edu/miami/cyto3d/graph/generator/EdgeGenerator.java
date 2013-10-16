package edu.miami.cyto3d.graph.generator;

import edu.miami.cyto3d.graph.model.Graph;
import edu.miami.cyto3d.graph.model.Vertex;
import edu.miami.cyto3d.graph.view.GraphView;

/**
 * Creates a random set of edges from the set of all possible edges in a network. This will create
 * up to (by not necessarily exactly) the number of requested edges.
 * 
 * @author justin
 */
public class EdgeGenerator {

   public static void createRandomEdges(Graph graph, GraphView view, int numEdges) {
      Vertex[] v = new Vertex[graph.getNumVertices()];
      graph.getVertices().toArray(v);
      int possibleEdges = v.length * (v.length - 1) / 2;
      double edgeChance = (double) numEdges / possibleEdges;
      
      for (int i = 0; i < v.length; i++) {
         for (int j = i + 1; j < v.length; j++) {
            if (Math.random() < edgeChance) {
               graph.createEdge(v[i], v[j], false);
               if (graph.getNumEdges() == numEdges) return;
            }
         }
      }
   }
}
