package edu.miami.jgloo.view;

import edu.miami.math.vector.Mat4;
import edu.miami.math.vector.RVec3;
import edu.miami.math.vector.Vec3;

public interface Frustum {

  public Mat4 getMatrix();

  public float getZNear();

  public float getZFar();

  /**
   * Returns an array of points at the corners of the frustum. The points are ordered as follows,
   * with directions relative to the forward direction of the camera:<br>
   * 0 : near bottom-right<br>
   * 1 : near top-right<br>
   * 2 : near top-left<br>
   * 3 : near bottom-left<br>
   * 4 : far bottom-right<br>
   * 5 : far top-right<br>
   * 6 : far top-left<br>
   * 7 : far bottom-left<br>
   */
  public Vec3[] calcCorners(RVec3 eye, RVec3 forward, RVec3 right, RVec3 up);

  /**
   * Returns a projection with the same values as this projection but with a given aspect ratio. If
   * stretched, the view will become widescreen and maintain the existing vertical dimensions.
   */
  public Frustum withAspect(float aspect);
}
