package edu.miami.cyto3d.graph.view;

import edu.miami.cyto3d.graph.model.Edge;
import edu.miami.math.vector.RVec4;
import edu.miami.math.vector.Vec3;
import edu.miami.math.vector.Vec4;

/**
 * Basic implementation of EdgeView interface.
 * 
 * @author justin
 */
public class BasicEdgeView implements EdgeView {

   final GraphView graphView;
   final Edge      edge;
   Style           style        = Style.SOLID;
   float           width        = 0.2f;
   Vec4            primaryColor = new Vec4(0.5f, 0.5f, 0.5f, 1.0f);
   Vec4            visibleColor = primaryColor.clone();
   boolean         pushedColor  = false;

   public BasicEdgeView(GraphView graphView, Edge edge) {
      this.graphView = graphView;
      this.edge = edge;
   }

   @Override
   public VertexView getSourceView() {
      return graphView.get(edge.getSource());
   }

   @Override
   public VertexView getTargetView() {
      return graphView.get(edge.getTarget());
   }

   @Override
   public Style getStyle() {
      return style;
   }

   @Override
   public void setStyle(Style style) {
      this.style = style;
   }

   @Override
   public float getWidth() {
      return width;
   }

   @Override
   public void setWidth(float width) {
      this.width = width;
   }

   @Override
   public RVec4 getPrimaryColor() {
      return primaryColor;
   }

   @Override
   public void setPrimaryColor(RVec4 color) {
      this.primaryColor.set(color);
      if (!pushedColor) visibleColor.set(primaryColor);
   }

   @Override
   public RVec4 getVisibleColor() {
      return visibleColor;
   }

   @Override
   public void pushColor(RVec4 color) {
      this.visibleColor.set(color);
      pushedColor = true;
   }

   @Override
   public void restoreColor() {
      this.visibleColor.set(primaryColor);
      pushedColor = false;
   }

   @Override
   public Vec3 calcCenter() {
      return getSourceView().getPosition().plus(calcVector().over(2));
   }

   @Override
   public float calcLength() {
      return calcVector().length();
   }

   @Override
   public Vec3 calcVector() {
      return getTargetView().getPosition().minus(getSourceView().getPosition());
   }

   @Override
   public EdgeView lerp(GraphView graphView, EdgeView other, float amount) {
      BasicEdgeView view = new BasicEdgeView(graphView, edge);

      if (other == null) {
         Vec4 color = this.primaryColor.clone();
         color.w *= (1 - amount);
         view.setPrimaryColor(color);
         view.setStyle(this.style);
         view.setWidth(this.width * (1 - amount));
      } else {
         view.setPrimaryColor(Vec4.lerp(this.primaryColor, other.getPrimaryColor(), amount));
         view.setStyle(this.style);
         view.setWidth(this.width + amount * (other.getWidth() - this.width));
      }

      return view;
   }
}
