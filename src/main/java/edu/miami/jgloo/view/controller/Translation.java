package edu.miami.jgloo.view.controller;

import edu.miami.jgloo.view.Camera;

/**
 * Translation along a camera vector.
 * 
 * @author justin
 */
public class Translation extends CameraMotion {

  private final CameraSource  delegate;
  private final Camera.Vector vector;
  private final boolean       accelUntilMax;

  public Translation(String name, CameraSource delegate, Camera.Vector vector, float maxSpeed,
      float timeZeroToMaxSpeed, float timeMaxToZeroSpeed, boolean accelUntilMax) {
    super(name, maxSpeed, timeZeroToMaxSpeed, timeMaxToZeroSpeed);
    this.delegate = delegate;
    this.vector = vector;
    this.accelUntilMax = accelUntilMax;
    accelerating = accelUntilMax;
  }

  @Override
  protected boolean applyMotion(float elapsedSec) {

    if (accelUntilMax && accelerating && speed >= maxSpeed) {
      accelerating = false;
    }

    Camera camera = delegate.getCamera();
    camera.translateEye(camera.getVector(vector).times(speed * elapsedSec));
    return speed > 0;
  }
}
