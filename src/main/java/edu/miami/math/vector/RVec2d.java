package edu.miami.math.vector;

/** Read-only Vec2 */
public interface RVec2d extends ReadOnlyVectorDouble<RVec2d, Vec2d> {

  /** @return The x coordinate. */
  double x();

  /** @return The y coordinate. */
  double y();

  /** @return The 2D cross product of (this x that). */
  double cross(RVec2d that);

  /**
   * Reflects the current vector across a surface with normal n:<br>
   * reflection = this - 2n(this dot n)
   */
  Vec2d reflect(RVec2d n);

  /** @return The vector rotated 90 degrees clockwise. */
  Vec2d rot90();
  
  /** Returns the product of two complex numbers: (a,b) * (c,d) = (ac - bd, ad + bc) */
  Vec2d times(RVec2d that);
  
  /** Converts to a single-precision vector */
  Vec2 toVec2();
}
