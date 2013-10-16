package edu.miami.jgloo.view;

import edu.miami.math.vector.Mat4;
import edu.miami.math.vector.RVec3;
import edu.miami.math.vector.Vec3;

/**
 * Creates an orthogonal projection used in OpenGL viewing.
 * 
 * @author justin
 */
public class OrthoFrustum implements Frustum {

  private Mat4 m;
  private float left, right, bottom, top, nearVal, farVal;

  public OrthoFrustum(float left, float right, float bottom, float top, float nearVal, float farVal) {
    this.left = left;
    this.right = right;
    this.bottom = bottom;
    this.top = top;
    this.nearVal = nearVal;
    this.farVal = farVal;
    m = Mat4.createOrtho(left, right, bottom, top, nearVal, farVal);
  }

  /** Sets up a two-dimensional orthographic viewing region (near = -1, far = 1). */
  public OrthoFrustum(float left, float right, float bottom, float top) {
    this(left, right, bottom, top, -1f, 1f);
  }

  @Override
  public Mat4 getMatrix() {
    return m;
  }

  public double getLeft() {
    return left;
  }

  public double getRight() {
    return right;
  }

  public double getBottom() {
    return bottom;
  }

  public double getTop() {
    return top;
  }

  public double getNearVal() {
    return nearVal;
  }

  public double getFarVal() {
    return farVal;
  }

  @Override
  public float getZNear() {
    return nearVal;
  }

  @Override
  public float getZFar() {
    return farVal;
  }

  @Override
  public Frustum withAspect(float aspect) {
    float h = top - bottom;
    float w = right - left;
    float pr = right / w;
    float pl = -left / pr;
    float newWidth = h * aspect;
    return new OrthoFrustum(-pl * newWidth, pr * newWidth, bottom, top);
  }

  @Override
  public Vec3[] calcCorners(RVec3 eye, RVec3 forward, RVec3 right,
      RVec3 up) {

    Vec3 r = right.times(this.right);
    Vec3 l = right.times(this.left);
    Vec3 t = up.times(this.top);
    Vec3 b = up.times(this.bottom);
    Vec3 f = forward.times(farVal - nearVal);

    return new Vec3[] { eye.plus(r).plus(b), eye.plus(r).plus(t), eye.plus(l).plus(t),
        eye.plus(l).plus(b), eye.plus(r).plus(b).plus(f), eye.plus(r).plus(t).plus(f),
        eye.plus(l).plus(t).plus(f), eye.plus(l).plus(b).plus(f), };
  }
}
