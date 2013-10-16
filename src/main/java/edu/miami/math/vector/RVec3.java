package edu.miami.math.vector;

/** Read-only Vec3 */
public interface RVec3 extends ReadOnlyVector<RVec3, Vec3> {

  /** @return The x coordinate. */
  float x();

  /** @return The y coordinate. */
  float y();

  /** @return The z coordinate. */
  float z();

  /** @return The 2D cross product of (this x that). */
  Vec3 cross(RVec3 that);

  Vec3 reflect(RVec3 n);
}
