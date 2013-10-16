package edu.miami.cyto3d.graph.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import edu.miami.cyto3d.graph.view.EdgeView;
import edu.miami.cyto3d.graph.view.GraphView;
import edu.miami.jgloo.view.Camera;
import edu.miami.math.geom.Plane;
import edu.miami.math.geom.Ray;
import edu.miami.math.vector.RVec3;
import edu.miami.math.vector.Vec3;
import edu.miami.math.vector.Vec4;

public class EdgePicker extends Picker<EdgeView> {

   GraphView graphView;

   public EdgePicker(Camera camera, GraphView graphView) {
      super(camera);
      this.graphView = graphView;
      addPropertyChangeListener(P_HOVERED, new PropertyChangeListener() {
         public void propertyChange(PropertyChangeEvent e) {
            if (e.getOldValue() != null) {
               ((EdgeView) e.getOldValue()).restoreColor();
            }
            if (hovered != null) {
               hovered.pushColor(new Vec4(1, 1, 0.5f, 1));
            }
         }
      });
   }

   public void setGraphView(GraphView graphView) {
      this.graphView = graphView;
   }

   /** Returns distance to intersection of mouse ray and edge or a negative value if no intersection */
   private float intersects(Ray r, EdgeView e) {
      RVec3 a = e.getSourceView().getPosition();
      RVec3 b = e.getTargetView().getPosition();
      Vec3 ab = b.minus(a);
      Vec3 u = ab.normalize();

      // calculate edge plane
      Vec3 pTangent = u.cross(e.calcCenter().minus(camera.getEye())).normalize();
      Plane p = new Plane(a, u.cross(pTangent));

      // calculate ray / plane intersection
      Vec3 x = p.intersect(r);
      if (x == null) return -1;

      // make sure x is right of a and left of b
      if ((x.minus(a)).dot(ab) < 0 || x.minus(b).dot(a.minus(b)) < 0) return -1;

      Vec3 y = a.plus(u.times(x.minus(a).dot(u)));

      if (x.minus(y).length() > e.getWidth() / 2) {
         return -1;
      }
      return y.minus(camera.getEye()).length();
   }

   @Override
   protected void grabbedMoved(Ray ray) {
      // todo: could translate both vertices?
   }

   @Override
   protected EdgeView pickHovered(Ray ray) {
      EdgeView closest = null;
      float closestD = Float.POSITIVE_INFINITY;
      for (EdgeView view : graphView.getEdgeViews()) {
         float d = intersects(ray, view);
         if (d > 0 && d < closestD) {
            closest = view;
         }
      }
      return closest;
   }
}
