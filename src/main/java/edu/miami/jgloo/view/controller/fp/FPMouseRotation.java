package edu.miami.jgloo.view.controller.fp;

import edu.miami.jgloo.view.FPCamera;
import edu.miami.jgloo.view.controller.CameraMotion;

public class FPMouseRotation extends CameraMotion {

  private final FPCameraSource source;
  private float                tgtYaw;
  private float                tgtPitch;

  public FPMouseRotation(String name, FPCameraSource source, float maxSpeed,
      float timeZeroToMaxSpeed, float timeMaxToZeroSpeed) {
    super(name, maxSpeed, timeZeroToMaxSpeed, timeMaxToZeroSpeed);
    this.source = source;
  }

  public void setTarget(float pitch, float yaw) {
    this.tgtPitch = pitch;
    this.tgtYaw = yaw;
  }

  @Override
  public void setAccelerating(boolean accelerating) {
    boolean wasAccelerating = this.accelerating;
    super.setAccelerating(accelerating);
    if (!wasAccelerating && accelerating) {
      this.tgtPitch = source.getCamera().getPitch();
      this.tgtYaw = source.getCamera().getYaw();
    }
  }

  @Override
  protected boolean applyMotion(float elapsedSec) {
    FPCamera camera = source.getCamera();

    float curPitch = camera.getPitch();
    float curYaw = camera.getYaw();
    float pitch = (tgtPitch - curPitch) * speed * elapsedSec;
    float yaw = (tgtYaw - curYaw) * speed * elapsedSec;
    camera.setRotation(curYaw + yaw, curPitch + pitch);

    return (accelerating || speed > 0);
  }
}
