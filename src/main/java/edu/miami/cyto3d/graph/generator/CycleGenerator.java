package edu.miami.cyto3d.graph.generator;

import java.util.Iterator;
import java.util.Set;

import edu.miami.cyto3d.graph.model.Graph;
import edu.miami.cyto3d.graph.model.Vertex;
import edu.miami.cyto3d.graph.view.GraphView;
import edu.miami.math.vector.Vec3;

public class CycleGenerator implements GraphGenerator {

   int   numNodes      = 10;
   float radius        = 3;
   float variation     = 0;
   float minEdgeLength = 4;
   float maxEdgeLength = 4;

   public void setNumNodes(int numNodes) {
      this.numNodes = numNodes;
   }

   public void setRadius(float radius) {
      this.radius = radius;
   }

   public void setVariation(float variation) {
      this.variation = variation;
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

   public float getRadius() {
      return radius;
   }

   public float getVariation() {
      return variation;
   }

   public float getMinEdgeLength() {
      return minEdgeLength;
   }

   public float getMaxEdgeLength() {
      return maxEdgeLength;
   }

   @Override
   public String toString() {
      return "Cycle";
   }

   @Override
   public void generate(Graph graph, GraphView view) {
      float angleInc = (float) (Math.PI * 2.0 / numNodes);
      float angle = 0;

      for (int i = 0; i < numNodes; i++) {
         Vec3 p = new Vec3((float) Math.cos(angle), (float) Math.sin(angle), 0);
         p.mul(radius + (float) (Math.random() * variation));

         Vertex vertex = graph.createVertex();
         view.get(vertex).setPosition(p);

         angle += angleInc;
      }

      Set<Vertex> verts = graph.getVertices();
      Iterator<Vertex> iterator = verts.iterator();
      if (verts.size() > 1) {
         Vertex a = iterator.next();
         while (iterator.hasNext()) {
            Vertex b = iterator.next();
            graph.createEdge(a, b, false);
            a = b;
         }
         graph.createEdge(a, verts.iterator().next(), false);
      }
   }
}
