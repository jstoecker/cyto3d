package edu.miami.jgloo.view.controller.orbit;

import edu.miami.jgloo.view.OrbitCamera;
import edu.miami.jgloo.view.controller.CameraMotion;
import edu.miami.math.util.GMath;

/**
 * Rotates the camera to a desired set of angles over time.
 * 
 * @author justin
 */
public class OrbitMouseRotation extends CameraMotion {

  private final OrbitCameraSource source;
  private float                   tgtAltitude;
  private float                   tgtAzimuth;

  public OrbitMouseRotation(String name, OrbitCameraSource source, float maxSpeed,
      float timeZeroToMaxSpeed, float timeMaxToZeroSpeed) {
    super(name, maxSpeed, timeZeroToMaxSpeed, timeMaxToZeroSpeed);
    this.source = source;
  }

  public void setTarget(float altitude, float azimuth) {
    this.tgtAltitude = altitude;
    this.tgtAzimuth = azimuth;
  }

  @Override
  public void setAccelerating(boolean accelerating) {
    boolean wasAccelerating = this.accelerating;
    super.setAccelerating(accelerating);
    if (!wasAccelerating && accelerating) {

      this.tgtAltitude = source.getCamera().getAltitude();
      this.tgtAzimuth = source.getCamera().getAzimuth();
    }
  }

  @Override
  protected boolean applyMotion(float elapsedSec) {
    OrbitCamera camera = source.getCamera();

    float curAltitude = camera.getAltitude() % GMath.PI_2;
    float curAzimuth = camera.getAzimuth() % GMath.PI_2;
    float deltaAltitude = (tgtAltitude - curAltitude);
    float deltaAzimuth = (tgtAzimuth - curAzimuth);
    if (Math.abs(deltaAzimuth) >= GMath.PI) {
      deltaAzimuth += (deltaAzimuth < 0) ? GMath.PI_2 : -GMath.PI_2;
    }

    float altitude = deltaAltitude * speed * elapsedSec;
    float azimuth = deltaAzimuth * speed * elapsedSec;

    camera.setEye(camera.getRadius(), curAltitude + altitude, curAzimuth + azimuth);

    return (accelerating || speed > 0);
  }
}
