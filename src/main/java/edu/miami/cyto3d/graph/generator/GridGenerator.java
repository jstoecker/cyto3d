package edu.miami.cyto3d.graph.generator;

import edu.miami.cyto3d.graph.model.Graph;
import edu.miami.cyto3d.graph.model.Vertex;
import edu.miami.cyto3d.graph.view.GraphView;
import edu.miami.math.vector.Vec3;

public class GridGenerator implements GraphGenerator {

   int              numEdges      = 0;
   int              numNodes      = 50;
   int              width         = 10;
   int              length        = 10;
   int              spacing       = 1;
   float            minEdgeLength = 4;
   float            maxEdgeLength = 4;

   public void setNumEdges(int numEdges) {
      this.numEdges = numEdges;
   }

   public void setNumNodes(int numNodes) {
      this.numNodes = numNodes;
   }

   public void setSpacing(int spacing) {
      this.spacing = spacing;
   }

   public void setWidth(int width) {
      this.width = width;
   }

   public void setLength(int length) {
      this.length = length;
   }
   
   public void setMinEdgeLength(float minEdgeLength) {
      this.minEdgeLength = minEdgeLength;
   }
   
   public void setMaxEdgeLength(float maxEdgeLength) {
      this.maxEdgeLength = maxEdgeLength;
   }

   public int getNumEdges() {
      return numEdges;
   }

   public int getNumNodes() {
      return numNodes;
   }

   public int getWidth() {
      return width;
   }

   public int getLength() {
      return length;
   }

   public int getSpacing() {
      return spacing;
   }
   
   public float getMinEdgeLength() {
      return minEdgeLength;
   }
   
   public float getMaxEdgeLength() {
      return maxEdgeLength;
   }

   @Override
   public void generate(Graph graph, GraphView view) {
      int wl = width * length;
      Vec3 offset = new Vec3(-width / 2.0f, 0, -length / 2.0f);

      for (int i = 0; i < numNodes; i++) {
         int x = i % width;
         int y = i / wl;
         int z = (i / width) % length;
         Vec3 p = new Vec3(x, y, z).plus(offset).times(spacing);
         Vertex vertex = graph.createVertex();
         view.get(vertex).setPosition(p);
      }
      
      EdgeGenerator.createRandomEdges(graph, view, numEdges);
   }
}
