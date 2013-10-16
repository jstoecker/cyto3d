package edu.miami.jgloo.view;

import edu.miami.math.util.GMath;
import edu.miami.math.vector.Mat4;
import edu.miami.math.vector.RVec3;
import edu.miami.math.vector.Vec3;

/**
 * @author justin
 */
public class PerspectiveFrustum implements Frustum {

  private final Mat4 m;
  private final float fovy, aspect, zNear, zFar;

  public PerspectiveFrustum(float fovy, float aspect, float zNear, float zFar) {
    this.fovy = fovy;
    this.aspect = aspect;
    this.zNear = zNear;
    this.zFar = zFar;
    m = Mat4.createPerspective(fovy, aspect, zNear, zFar);
  }

  @Override
  public Mat4 getMatrix() {
    return m;
  }

  @Override
  public float getZFar() {
    return zFar;
  }

  @Override
  public float getZNear() {
    return zNear;
  }

  public float getFovY() {
    return fovy;
  }

  public float getAspect() {
    return aspect;
  }

  @Override
  public Frustum withAspect(float aspect) {
    return new PerspectiveFrustum(fovy, aspect, zNear, zFar);
  }

  @Override
  public Vec3[] calcCorners(RVec3 eye, RVec3 forward, RVec3 right,
      RVec3 up) {
    float fovy = GMath.toRadians(this.fovy);
    float fovx = fovy * aspect;
    float xtan = GMath.tan(fovx / 2);
    float ytan = GMath.tan(fovy / 2);

    Vec3 nForward = forward.times(zNear);
    Vec3 nRight = right.times(zNear * xtan);
    Vec3 nUp = up.times(zNear * ytan);

    Vec3 fForward = forward.times(zFar);
    Vec3 fRight = right.times(zFar * xtan);
    Vec3 fUp = up.times(zFar * ytan);

    return new Vec3[] { eye.plus(nRight).minus(nUp).plus(nForward),
        eye.plus(nRight).plus(nUp).plus(nForward), eye.minus(nRight).plus(nUp).plus(nForward),
        eye.minus(nRight).minus(nUp).plus(nForward), eye.plus(fRight).minus(fUp).plus(fForward),
        eye.plus(fRight).plus(fUp).plus(fForward), eye.minus(fRight).plus(fUp).plus(fForward),
        eye.minus(fRight).minus(fUp).plus(fForward), };
  }

  /**
   * Splits the frustum into N subfrusta using a biased uniform/logarithmic split scheme. If the
   * bias is 0, the split will be uniform; if it is 1, it will be logarithmic. The value for bias
   * should be in [0,1].
   */
  public PerspectiveFrustum[] split(int n, float bias) {
    float[] z = new float[n + 1];
    for (int i = 0; i <= n; i++) {
      z[i] = (float) (bias * zNear * Math.pow(zFar / zNear, (float) i / n));
      z[i] += (1 - bias) * (zNear + ((float) i / n) * (zFar - zNear));
    }

    PerspectiveFrustum[] splits = new PerspectiveFrustum[n];
    for (int i = 0; i < n; i++)
      splits[i] = new PerspectiveFrustum(fovy, aspect, z[i], z[i + 1]);

    return splits;
  }
}
