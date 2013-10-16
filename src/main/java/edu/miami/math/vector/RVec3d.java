package edu.miami.math.vector;

/** Read-only Vec3d */
public interface RVec3d extends ReadOnlyVectorDouble<RVec3d, Vec3d> {

  /** @return The x coordinate. */
  double x();

  /** @return The y coordinate. */
  double y();

  /** @return The z coordinate. */
  double z();

  /** @return The 2D cross product of (this x that). */
  Vec3d cross(RVec3d that);

  Vec3d reflect(RVec3d n);
  
  /** Converts to a single-precision vector */
  Vec3 toVec3();
}
