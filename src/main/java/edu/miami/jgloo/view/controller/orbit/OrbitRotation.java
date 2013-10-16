package edu.miami.jgloo.view.controller.orbit;

import edu.miami.jgloo.view.OrbitCamera;
import edu.miami.jgloo.view.controller.CameraMotion;

public class OrbitRotation extends CameraMotion {

  public enum Angle {
    POS_AZIMUTH,
    NEG_AZIMUTH,
    POS_ALTITUDE,
    NEG_ALTITUDE
  }

  private final OrbitCameraSource delegate;
  private final Angle             angle;
  private final boolean           decelerateAtMaxSpeed;
  private final boolean           scaleSpeed;

  /**
   * Creates a new orbit rotation.
   * 
   * @param name
   * @param delegate
   * @param angle
   * @param maxSpeed - max speed in 3D units per second (tangential speed).
   * @param timeZeroToMaxSpeed - time to accelerate from 0 to max speed.
   * @param timeMaxToZeroSpeed - time to decelerate from max speed to 0.
   */
  public OrbitRotation(String name, OrbitCameraSource delegate, Angle angle, float maxSpeed,
      float timeZeroToMaxSpeed, float timeMaxToZeroSpeed, boolean decelerateAtMaxSpeed,
      boolean scaleSpeed) {
    super(name, maxSpeed, timeZeroToMaxSpeed, timeMaxToZeroSpeed);
    this.delegate = delegate;
    this.angle = angle;
    this.decelerateAtMaxSpeed = decelerateAtMaxSpeed;
    this.scaleSpeed = scaleSpeed;
  }

  @Override
  protected boolean applyMotion(float elapsedSec) {
    OrbitCamera camera = delegate.getCamera();

    if (decelerateAtMaxSpeed && speed >= maxSpeed) accelerating = false;

    float angularSpeed = scaleSpeed ? speed / camera.getRadius() : speed;

    switch (angle) {
    case POS_ALTITUDE:
      camera.setAltitude(camera.getAltitude() + angularSpeed * elapsedSec);
      break;
    case NEG_ALTITUDE:
      camera.setAltitude(camera.getAltitude() - angularSpeed * elapsedSec);
      break;
    case POS_AZIMUTH:
      camera.setAzimuth(camera.getAzimuth() + angularSpeed * elapsedSec);
      break;
    case NEG_AZIMUTH:
      camera.setAzimuth(camera.getAzimuth() - angularSpeed * elapsedSec);
      break;
    }

    return speed > 0;
  }
}
