package edu.miami.jgloo.view.controller;

/**
 * Low-level camera motion that contains a speed, such as "rotate left 0.05 radians per second".
 * Higher-level actions like "rotate 90 degrees, pause, then move forward" may be constructed from
 * these low-level motions. The motion types and how they are applied depends on the camera type.
 * 
 * @author justin
 */
public abstract class CameraMotion {

  protected final String name;
  protected boolean      accelerating;
  protected float        speed;
  protected float        maxSpeed;
  protected float        timeZeroToMaxSpeed;
  protected float        timeMaxToZeroSpeed;
  protected float        acceleration;
  protected float        deceleration;

  public CameraMotion(String name, float maxSpeed, float timeZeroToMaxSpeed,
      float timeMaxToZeroSpeed) {
    this.name = name;
    this.timeZeroToMaxSpeed = timeZeroToMaxSpeed;
    this.timeMaxToZeroSpeed = timeMaxToZeroSpeed;
    setMaxSpeed(maxSpeed);
  }

  public void setAccelerating(boolean accelerating) {
    this.accelerating = accelerating;
  }

  public boolean isAccelerating() {
    return accelerating;
  }

  public float getSpeed() {
    return speed;
  }

  public String getName() {
    return name;
  }

  public void setMaxSpeed(float maxSpeed) {
    this.maxSpeed = maxSpeed;
    setTimeZeroToMaxSpeed(timeZeroToMaxSpeed);
    setTimeMaxToZeroSpeed(timeMaxToZeroSpeed);
  }

  public void setTimeZeroToMaxSpeed(float timeZeroToMaxSpeed) {
    this.timeZeroToMaxSpeed = timeZeroToMaxSpeed;
    acceleration = maxSpeed / timeZeroToMaxSpeed;
  }

  public void setTimeMaxToZeroSpeed(float timeMaxToZeroSpeed) {
    this.timeMaxToZeroSpeed = timeMaxToZeroSpeed;
    deceleration = maxSpeed / timeMaxToZeroSpeed;
  }

  /** @return The motion is still active. */
  protected boolean update(float elapsedSec) {
    if (accelerating) {
      speed = Math.min(maxSpeed, speed + acceleration * elapsedSec);
    } else {
      speed = Math.max(0, speed - deceleration * elapsedSec);
    }
    
    return applyMotion(elapsedSec);
  }
  
  /** @return The motion is still active. */
  protected abstract boolean applyMotion(float elapsedSec);

  @Override
  public String toString() {
    return name;
  }
}
