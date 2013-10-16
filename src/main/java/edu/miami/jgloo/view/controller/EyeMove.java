package edu.miami.jgloo.view.controller;

import edu.miami.jgloo.view.Camera;
import edu.miami.math.vector.RVec3;
import edu.miami.math.vector.Vec3;

/**
 * Moves the camera eye towards a position each time it is applied.
 * 
 * @author justin
 */
public class EyeMove extends CameraMotion {

  private final CameraSource delegate;
  private final RVec3 position;
  private final float        slowDist;

  public EyeMove(String name, CameraSource delegate, RVec3 position, float maxSpeed,
      float timeZeroToMaxSpeed, float slowDist) {
    super(name, maxSpeed, timeZeroToMaxSpeed, timeZeroToMaxSpeed);
    this.delegate = delegate;
    this.position = position;
    this.slowDist = slowDist;
    accelerating = true;
  }

  @Override
  protected boolean applyMotion(float elapsedSec) {
    Camera camera = delegate.getCamera();
    Vec3 v = position.minus(camera.getEye());
    float d = v.length();
    v.div(d);

    accelerating = true;
    float adjustedSpeed = (d >= slowDist) ? speed : speed * (d / slowDist);

    System.out.printf("Dist: %.4f   Speed: %.4f\n", d, adjustedSpeed);

    camera.translateEye(v.times(adjustedSpeed * elapsedSec));

    return adjustedSpeed > 0.5f;
  }
}
