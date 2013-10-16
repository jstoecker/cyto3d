package edu.miami.jgloo.view.controller.orbit;

import edu.miami.jgloo.view.OrbitCamera;
import edu.miami.jgloo.view.controller.CameraMotion;
import edu.miami.jgloo.view.controller.Target;
import edu.miami.math.vector.RVec3;

/**
 * A motion that rotates an OrbitCamera to look at a new center over time.
 * 
 * @author justin
 */
public class OrbitFocus extends CameraMotion {

  private final OrbitCameraSource camSrc;
  private final Target            target;
  private final boolean           fixEye;
  private final float             lag;

  /**
   * Creates a motion that focuses an OrbitCamera on a center target over time.
   * 
   * @param name
   * @param camSource
   * @param target
   * @param lag - value in (0,1] that determines the speed at which the camera centers the target.
   *          The higher the value the quicker the focus is gained.
   * @param maxSpeed
   * @param timeZeroToMaxSpeed
   * @param fixEye
   */
  public OrbitFocus(String name, OrbitCameraSource camSource, Target target, float lag,
      float maxSpeed, float timeZeroToMaxSpeed, boolean fixEye) {
    super(name, maxSpeed, timeZeroToMaxSpeed, timeZeroToMaxSpeed);
    this.camSrc = camSource;
    this.target = target;
    this.lag = lag;
    this.fixEye = fixEye;
    accelerating = true;
  }

  @Override
  public void setAccelerating(boolean accelerating) {
    // camera is always accelerating towards the focus point (clamped at max speed of course), so
    // acceleration must always be true.
  }

  @Override
  protected boolean applyMotion(float elapsedSec) {
    OrbitCamera camera = camSrc.getCamera();

    RVec3 c = camera.getCenter();
    RVec3 t = target.getPosition();
    float d = t.minus(c).length();
    float s = Math.min(1, speed * elapsedSec * d) * lag;
    camera.setCenter(c.lerp(t, s), fixEye);

    return true;
  }
}
