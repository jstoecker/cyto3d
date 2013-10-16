package edu.miami.math.vector;

/** Read-only Vec4 */
public interface RVec4 extends ReadOnlyVector<RVec4, Vec4> {

  /** @return The x coordinate. */
  float x();

  /** @return The y coordinate. */
  float y();

  /** @return The x coordinate. */
  float z();

  /** @return The w coordinate. */
  float w();
}
