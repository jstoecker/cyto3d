package edu.miami.cyto3d.graph.layout.force;

import edu.miami.cyto3d.graph.view.EdgeView;
import edu.miami.math.vector.RVec3;
import edu.miami.math.vector.Vec3;

public class ForceEdge {

   final EdgeView    view;
   final ForceVertex source;
   final ForceVertex target;

   public ForceEdge(EdgeView view, ForceVertex source, ForceVertex target) {
      this.view = view;
      this.source = source;
      this.target = target;
   }

   public ForceVertex getSource() {
      return source;
   }

   public ForceVertex getTarget() {
      return target;
   }

   public void attract(float springiness, float equilibriumLength) {
      RVec3 a = source.view.getPosition();
      RVec3 b = target.view.getPosition();
      Vec3 ab = b.minus(a);

      float currentLength = ab.length();
      float desiredLength = equilibriumLength + (source.view.getSize() + target.view.getSize()) / 2;
      
      // if current length is shorter, the Coulomb repulsion will separate the nodes
      if (currentLength <= desiredLength) return;

      float displacementMagnitude = currentLength - desiredLength;
      ab.div(currentLength);
      ab.mul(displacementMagnitude * -springiness);

      source.addForce(ab.times(-1));
      target.addForce(ab);
   }
}
