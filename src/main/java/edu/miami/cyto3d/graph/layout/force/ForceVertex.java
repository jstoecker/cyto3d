package edu.miami.cyto3d.graph.layout.force;

import edu.miami.cyto3d.graph.view.VertexView;
import edu.miami.math.vector.Vec3;

public class ForceVertex {

  final VertexView view;
  private Vec3     netForce = new Vec3(0);
  private Vec3     velocity = new Vec3(0);
  private float    energy   = 0;

  // if this is too high then the repulsion will dominate the spring force and nodes will separate
  // more than desired length
  private float    k        = 0.01f;

  /** If a node is clamped it will not be affected by any forces */
  private boolean  clamped  = false;

  public void setClamped(boolean clamped) {
    this.clamped = clamped;
  }

  public float getEnergy() {
    return energy;
  }

  public ForceVertex(VertexView view) {
    this.view = view;
  }

  public void addForce(Vec3 force) {
    if (!clamped) {
      netForce.add(force);
    }
  }

  public void repel(ForceVertex other) {
    if (clamped) return;

    Vec3 f = view.getPosition().minus(other.view.getPosition());
    float d = f.length();
    if (d == 0) return;

    // 22.5 could be a parameter: larger = more separation between stuff and straighter branches
    if (d < 22.5f) {
      f.mul(k / (d * d));
      netForce.add(f);
    }
  }

  public float applyForce(float damping, boolean useVelocity, float maxSpeed) {
    if (!useVelocity) velocity.mul(0);

    velocity = velocity.plus(netForce).times(damping);

    // limits the speed the vertex can move to prevent instability
    if (velocity.length() > maxSpeed) {
      velocity = velocity.normalize().times(maxSpeed);
    }

    view.setPosition(view.getPosition().plus(velocity));

    netForce = new Vec3(0);

    energy = velocity.lengthSquared();

    return energy;
  }
}
