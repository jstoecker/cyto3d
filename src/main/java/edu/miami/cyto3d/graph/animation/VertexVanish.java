package edu.miami.cyto3d.graph.animation;

import java.util.ArrayList;

import edu.miami.cyto3d.graph.animation.EffectAnimator.Animation;
import edu.miami.cyto3d.graph.layout.force.ForceLayoutThread;
import edu.miami.cyto3d.graph.model.Edge;
import edu.miami.cyto3d.graph.model.Graph;
import edu.miami.cyto3d.graph.model.Vertex;
import edu.miami.cyto3d.graph.view.EdgeView;
import edu.miami.cyto3d.graph.view.GraphView;
import edu.miami.cyto3d.graph.view.VertexView;

public class VertexVanish extends Animation {

   final Vertex              vertex;
   final Graph               graph;
   final VertexView          vertView;
   final float               originalVertexSize;
   final ArrayList<EdgeView> edgeViews;
   final ArrayList<Float>    originalEdgeWidths;
   final ForceLayoutThread   forceLayoutThread;

   public VertexVanish(Vertex vertex, Graph graph, GraphView graphView,
         ForceLayoutThread forceLayoutThread, int duration) {
      super(duration);

      vertView = graphView.get(vertex);
      originalVertexSize = vertView.getSize();

      int degree = graph.getMixedDegree(vertex);

      edgeViews = new ArrayList<EdgeView>(degree);
      originalEdgeWidths = new ArrayList<Float>(degree);
      for (Edge edge : graph.getMixedEdges(vertex)) {
         EdgeView edgeView = graphView.get(edge);
         edgeViews.add(edgeView);
         originalEdgeWidths.add(edgeView.getWidth());
      }

      this.vertex = vertex;
      this.graph = graph;
      this.forceLayoutThread = forceLayoutThread;
   }

   @Override
   void updateAnimation(float progress) {
      vertView.setSize(originalVertexSize * (1 - progress));
      for (int i = 0; i < edgeViews.size(); i++)
         edgeViews.get(i).setWidth(originalEdgeWidths.get(i) * (1 - progress));
   }

   @Override
   protected void endAnimation() {
      if (forceLayoutThread != null) forceLayoutThread.stop();
      graph.remove(vertex);
      if (forceLayoutThread != null) forceLayoutThread.start();
   }
}
