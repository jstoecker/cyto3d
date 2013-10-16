package edu.miami.jgloo.view;

import edu.miami.math.util.GMath;
import edu.miami.math.vector.Mat4;
import edu.miami.math.vector.Vec3;

/**
 * First-person camera that defines a view based on an eye position, yaw angle, and pitch angle.
 * This may be seen as the inverse of the orbit camera: the focused point rotates around the eye
 * instead of the eye around a center.
 * 
 * @author justin
 */
public class FPCamera extends CameraBase {

  /**
   * Having the camera directly on the world up/down axes creates a special case for calculating the
   * local vectors. Instead of introducing more code, the pitch is limited to a value just under
   * PI/2 (or just above -PI/2); the offset is tiny so that visually there is no difference.
   */
  private static final float PITCH_EPSILON = 0.000001f;
  private static final float MIN_PITCH     = -(GMath.PI_OVER_2 - PITCH_EPSILON);
  private static final float MAX_PITCH     = GMath.PI_OVER_2 - PITCH_EPSILON;

  protected float            yaw;

  /** rotation around the right axis of world space */
  protected float            pitch;

  /** rotation around the forward axis of world space */
  protected double           roll;

  public FPCamera(Vec3 eye, float yaw, float pitch, boolean yUp) {
    super(yUp);
    this.eye = eye;
    setRotation(yaw, pitch);
  }

  public float getYaw() {
    return yaw;
  }

  public float getPitch() {
    return pitch;
  }

  public void setYaw(float yaw) {
    setRotation(yaw, pitch);
  }

  public void setPitch(float pitch) {
    setRotation(yaw, pitch);
  }

  public void setRotation(float yaw, float pitch) {
    this.yaw = yaw;
    this.pitch = GMath.clamp(pitch, MIN_PITCH, MAX_PITCH);

    forward = GMath.sphericalToCartesian(1, this.pitch, this.yaw, yUp);
    right = forward.cross(worldUp).normalize();
    up = right.cross(forward).normalize();

    updateView();
  }

  public void addRotation(float yaw, float pitch) {
    setRotation(this.yaw + yaw, this.pitch + pitch);
  }

  @Override
  protected void updateView() {
    Vec3 ctr = eye.plus(forward);
    view = Mat4.createLookAt(eye.x, eye.y, eye.z, ctr.x, ctr.y, ctr.z, up.x, up.y, up.z);
  }
}
