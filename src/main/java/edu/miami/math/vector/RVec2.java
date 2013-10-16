package edu.miami.math.vector;

/** Read-only Vec2 */
public interface RVec2 extends ReadOnlyVector<RVec2, Vec2> {

  /** @return The x coordinate. */
  float x();

  /** @return The y coordinate. */
  float y();

  /** @return The 2D cross product of (this x that). */
  float cross(RVec2 that);

  /**
   * Reflects the current vector across a surface with normal n:<br>
   * reflection = this - 2n(this dot n)
   */
  Vec2 reflect(RVec2 n);

  /** @return The vector rotated 90 degrees clockwise. */
  Vec2 rot90();
  
  /** Returns the product of two complex numbers: (a,b) * (c,d) = (ac - bd, ad + bc) */
  Vec2 times(RVec2 that);
}
